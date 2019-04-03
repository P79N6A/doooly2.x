package com.doooly.service.doooly.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.constants.RedisConstants;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.doooly.DlTemplateFloorItemDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdSelfProductSkuDao;
import com.doooly.dao.reachad.AdadDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.doooly.DlTemplateFloorItem;
import com.doooly.entity.reachad.AdAd;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.service.doooly.TemplateCacheServiceI;
import com.reach.redis.annotation.Cacheable;
import com.reach.redis.annotation.EnableCaching;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 模版缓存服务类
 * @Author: Mr.Wu
 * @Date: 2019/3/21
 */
@Service
@EnableCaching
public class TemplateCacheServiceImpl implements TemplateCacheServiceI {
    private static final String PROJECT_ACTIVITY_URL = PropertiesConstants.dooolyBundle.getString("project.activity.url");
    private static Logger log = LoggerFactory.getLogger(TemplateCacheServiceImpl.class);
    private String BASE_URL = PropertiesConstants.commonBundle.getString("BASE_URL");
    private String BASE_BUSINESS_INFO_URL = BASE_URL + "/businessinfo/";
    private static int DEAL_TYPE_ONLINE = 0;
    private static int DEAL_TYPE_OFFLINE = 1;

    @Autowired
    private DlTemplateFloorItemDao dlTemplateFloorItemDao;
    @Autowired
    private AdBusinessDao adBusinessDao;
    @Autowired
    private AdadDao adadDao;
    @Autowired
    private AdSelfProductSkuDao adSelfProductSkuDao;


    /**
     * 获得标准楼层items
     * @param map
     *      floor: 楼层id
     * @return
     */
    @Cacheable(module = "TEMPLATE", event = "getStandardFloorItem", key = "floorId",
            expiresKey = "expires", required = true)
    public List<DlTemplateFloorItem> getStandardFloorItem(Map<String, Object> map) {
        String floorId = (String) map.get("floorId");
        List<DlTemplateFloorItem> list = dlTemplateFloorItemDao.getAllByFloorId(floorId);

        if (!CollectionUtils.isEmpty(list)) {
            List<DlTemplateFloorItem> items = new ArrayList<>();

            for (DlTemplateFloorItem item : list) {
                DlTemplateFloorItem itemEntry = new DlTemplateFloorItem();
                itemEntry.setTitle(item.getTitle());
                itemEntry.setSubTitle(item.getSubTitle());
                if (StringUtils.isNotBlank(item.getLinkUrl()) && item.getLinkUrl().indexOf("#") > -1) {
                    itemEntry.setSubUrl(item.getLinkUrl().substring(item.getLinkUrl().indexOf("#") + 1, item.getLinkUrl().length()));
                } else {
                    itemEntry.setSubUrl(item.getLinkUrl());
                }
                itemEntry.setLinkUrl(item.getLinkUrl());
                itemEntry.setIconUrl(item.getIconUrl());
                itemEntry.setId(item.getId());
                itemEntry.setCornerMarkText(item.getCornerMarkText());
                items.add(itemEntry);
            }
            map.put("expires", RedisConstants.REDIS_CACHE_EXPIRATION_DATE);
            return items;
        } else {
            map.put("expires", -1);
            return null;
        }
    }

    /**
     * 获得自营商品信息
     * @param map
     *      skuId: 自营商品SKU Id
     * @return
     */
    @Cacheable(module = "SELF_PRODUCT", event = "getSelfProductSku", key = "skuId",
            expiresKey = "expires", required = true)
    public AdSelfProductSku getSelfProductSku(Map<String, Object> map) {
        String skuId = (String) map.get("skuId");
        AdSelfProductSku sku = adSelfProductSkuDao.get(skuId);

        if (sku != null) {
            // 获得系统当前时间
            long now = System.currentTimeMillis();
            // 获得下架时间的毫秒数
            long buyEndDate = sku.getBuyEndDate().getTime();
            // 计算剩余时间
            long expires = buyEndDate - now;
            map.put("expires", expires / 1000);
        } else {
            map.put("expires", -1);
        }
        return sku;
    }

    /**
     * 获得热门商户楼层并缓存
     * @param map
     *      userId
     *      address
     *      groupId
     * @return
     */
    @Cacheable(module = "HOT_BUSINESS", event = "getHotBusinessFloor", key = "groupId, address",
            expiresKey = "expires", required = true)
    public List<DlTemplateFloorItem> getHotBusinessFloor(Map<String, Object> map) {
        Integer userId = Integer.valueOf((String) map.get("userId"));
        String address = (String) map.get("address");

        // 热门商户
        List<AdBusiness> merchants = adBusinessDao.findHotMerchantsByDealType(userId, null, address,
                Arrays.asList(DEAL_TYPE_OFFLINE, DEAL_TYPE_ONLINE));

        if (!CollectionUtils.isEmpty(merchants)) {
            Long serverEndTime = null;
            List<DlTemplateFloorItem> items = new ArrayList<>();
            int row = 0;

            for (AdBusiness merchant : merchants) {
                if (row >= 10) {
                    break;
                }

                // 如果当前循环商户的服务结束时间小于上一次循环的商户时间
                if (serverEndTime == null || serverEndTime > merchant.getServerEndTime().getTime()) {
                    serverEndTime = merchant.getServerEndTime().getTime();
                }

                row++;
                DlTemplateFloorItem item = new DlTemplateFloorItem();
                item.setTitle(merchant.getCompany());
                item.setServerEndTime(merchant.getServerEndTime());

                // 前折信息
                if (merchant.getDiscount() != null && merchant.getDiscount() > 0) {
                    item.setSubTitle(merchant.getDiscount() + "折起");
                }

                // 返利信息
                if (!StringUtils.isEmpty(merchant.getMaxUserRebate())
                        && new BigDecimal(merchant.getMaxUserRebate()).compareTo(BigDecimal.ZERO) == 1) {
                    item.setCornerMarkText("返" + merchant.getMaxUserRebate() + "%");
                }

                item.setTitle(merchant.getSubTitle());
                item.setIconUrl(merchant.getLogo());
                item.setLinkUrl(BASE_BUSINESS_INFO_URL + merchant.getDealType() + "/" + merchant.getId());
                item.setSubUrl(item.getLinkUrl().substring(BASE_BUSINESS_INFO_URL.indexOf("#") + 1, item.getLinkUrl().length()));
                item.setIsSupportIntegral(merchant.getIsSupportIntegral());
                items.add(item);
            }

            if (serverEndTime != null) {
                map.put("expires", (serverEndTime - System.currentTimeMillis()) / 1000);
            } else {
                map.put("expires", -1);
            }

            return items;
        } else {
            map.put("expires", -1);
            return null;
        }
    }

    /**
     * 获得广告位楼层items
     * @param map
     *      groupId: 企业id
     *      type: 广告类型
     *      version: 广告显示版本
     * @return
     */
    @Cacheable(module = "TEMPLATE", event = "getAdAdItemsByTypeAndGroup", key = "groupId, type, version",
            expiresKey = "expires", required = true)
    public List<DlTemplateFloorItem> getAdAdItemsByTypeAndGroup(Map<String, Object> map) {
        String groupId = (String) map.get("groupId");
        Integer version = (Integer) map.get("version");
        Integer type = (Integer) map.get("type");

        // 广告位
        List<AdAd> ads = adadDao.getByTypeAndGroup(type, groupId, version);

        if (!CollectionUtils.isEmpty(ads)) {
            Long endDate = null;
            List<DlTemplateFloorItem> items = new ArrayList<>();

            for (AdAd ad : ads) {
                DlTemplateFloorItem item = new DlTemplateFloorItem();
                item.setLinkUrl(ad.getImageLinkUrl());
                item.setImageUrl(ad.getImagePath());
                item.setTitle(ad.getTitle());
                item.setLinkType(ad.getLinkType());
                items.add(item);

                if (endDate == null || endDate > ad.getEndDate().getTime()) {
                    endDate = ad.getEndDate().getTime();
                }
            }
            if (endDate != null) {
                map.put("expires", (endDate - System.currentTimeMillis()) / 1000);
            } else {
                map.put("expires", -1);
            }
            return items;
        } else {
            map.put("expires", -1);
            return null;
        }
    }

    /**
     * 获得礼包楼层items
     *
     * @param map
     *      groupId
     * @return
     */
    @Cacheable(module = "TEMPLATE", event = "getDooolyGiftBagItemsByGroup", key = "groupId",
            expiresKey = "expires", required = true)
    public Map<String, Object> getDooolyGiftBagItemsByGroup(Map<String, Object> map) {
        String groupId = (String) map.get("groupId");
        JSONObject json = new JSONObject();
        json.put("groupId", groupId);
        json.put("pageNo", 1);
        json.put("pageSize", 6);
        JSONObject resultJson = HttpClientUtil.httpPost(PROJECT_ACTIVITY_URL + "gift/bag/getDooolyGiftBagListByGroup", json);
        log.info("获得企业兜礼礼包：" + resultJson.toJSONString());

        if (MessageDataBean.success_code.equals(resultJson.getString("code"))) {
            JSONObject date = (JSONObject) JSONObject.parse(resultJson.getString("data"));
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("hasMore", date.getBoolean("hasMore"));
            JSONArray list = date.getJSONArray("list");
            Long endDate = null;

            if (list != null && list.size() > 0) {
                List<DlTemplateFloorItem> items = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    JSONObject entry = (JSONObject) list.get(i);
                    DlTemplateFloorItem item = new DlTemplateFloorItem();
                    item.setIconUrl(entry.getString("subImage"));
                    item.setId(entry.getString("id"));
                    item.setTitle(entry.getString("giftBagName"));
                    items.add(item);

                    if (endDate == null || endDate > entry.getDate("endDate").getTime()) {
                        endDate = entry.getDate("endDate").getTime();
                    }
                }
                resultMap.put("list", items);
            }

            if (endDate != null) {
                map.put("expires", (endDate - System.currentTimeMillis()) / 1000);
            } else {
                map.put("expires", -1);
            }
            return resultMap;
        } else {
            map.put("expires", -1);
            return null;
        }
    }
}
