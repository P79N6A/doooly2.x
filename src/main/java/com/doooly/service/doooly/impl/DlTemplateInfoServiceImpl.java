package com.doooly.service.doooly.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.freeCoupon.service.MyCouponsBusinessServiceI;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.common.constants.CstInfoConstants;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dao.doooly.DlTemplateFloorDao;
import com.doooly.dao.doooly.DlTemplateFloorItemDao;
import com.doooly.dao.doooly.DlTemplateGroupDao;
import com.doooly.dao.doooly.DlTemplateInfoDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdSelfProductSkuDao;
import com.doooly.dao.reachad.AdadDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.doooly.DlTemplateFloor;
import com.doooly.entity.doooly.DlTemplateFloorItem;
import com.doooly.entity.doooly.DlTemplateGroup;
import com.doooly.entity.doooly.DlTemplateInfo;
import com.doooly.entity.reachad.AdAd;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.service.doooly.DlTemplateInfoServiceI;
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
 * 模版服务接口实现
 * @Author: Mr.Wu
 * @Date: 2019/3/13
 */
@Service
@EnableCaching
public class DlTemplateInfoServiceImpl implements DlTemplateInfoServiceI {
    private String BASE_URL = PropertiesConstants.commonBundle.getString("BASE_URL");
    private String BASE_BUSINESSINFO_URL = BASE_URL + "/businessinfo/";
    private String BASE_CARDBUYDETAIL_URL = BASE_URL + "/cardBuyDetail/";
    private static final String PROJECT_ACTIVITY_URL = PropertiesConstants.dooolyBundle.getString("project.activity.url");
    private static Logger log = LoggerFactory.getLogger(DlTemplateInfoServiceImpl.class);
    private static int DEAL_TYPE_ONLINE = 0;
    private static int DEAL_TYPE_OFFLINE = 1;

    @Autowired
    private DlTemplateInfoDao dlTemplateInfoDao;
    @Autowired
    private DlTemplateGroupDao dlTemplateGroupDao;
    @Autowired
    private DlTemplateFloorDao dlTemplateFloorDao;
    @Autowired
    private AdBusinessDao adBusinessDao;
    @Autowired
    private DlTemplateFloorItemDao dlTemplateFloorItemDao;
    @Autowired
    private AdadDao adadDao;
    @Autowired
    private AdSelfProductSkuDao adSelfProductSkuDao;
    @Autowired
    private MyCouponsBusinessServiceI myCouponsBusinessServiceI;


    @Override
    public String getTemplateInfoByType(Map<String, Object> map) {
        JSONObject result = new JSONObject();
        String groupId = (String) map.get("groupId");
        String type =  map.get("type").toString();
        String userId = (String) map.get("userId");
        String address = (String) map.get("address");
        // 根据企业及模版类型获得模版
        DlTemplateGroup tempGroup = dlTemplateGroupDao.getByGroupIdAndType(groupId, type);

        if (tempGroup != null) {
            DlTemplateInfo template = dlTemplateInfoDao.get(tempGroup.getTemplateId());

            if (template != null) {
                if (template.getType() == CstInfoConstants.TEMP_TYPE_ONE) {
                    if (getHomeFloorByTempTypeOne(template, groupId, userId, address)) {
                        JSONObject floors = new JSONObject();
                        floors.put("floors", template.getFloors());
                        result.put("data", floors);
                        result.put("code", MessageDataBean.success_code);
                        result.put("msg", MessageDataBean.success_mess);
                    }
                }
            } else {
                log.error(String.format("企业关联模版不存在,企业ID：%s，模版ID：%s，模版类型：%s", groupId, tempGroup.getTemplateId(), type));
                return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess).toJsonString();
            }
        } else {
            log.error(String.format("企业未找到关联模版,企业ID：%s，模版类型：%s", groupId, type));
            return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess).toJsonString();
        }
        return result.toJSONString();
    }

    /**
     * 获得3.0首页模版楼层数据
     * @param template
     * @return
     */
    private boolean getHomeFloorByTempTypeOne(DlTemplateInfo template, String groupId, String userId, String address) {
        // 获得模版Floor
        List<DlTemplateFloor> floorList = dlTemplateFloorDao.getByTemplateId(template.getId());
        List<DlTemplateFloor> floors = null;

        if (floorList != null && floorList.size() > 0) {
            floors = new ArrayList<>();

            for (DlTemplateFloor floor : floorList) {
                DlTemplateFloor floorEntry = new DlTemplateFloor();
                floorEntry.setTitle(floor.getTitle());
                floorEntry.setSubTitle(floor.getSubTitle());
                floorEntry.setType(floor.getType());

                List<DlTemplateFloorItem> items = null;
                switch (floor.getType()) {
                    case CstInfoConstants.TEMP_HOME_TYPE_TWO:
                        // 兜礼礼包
                        // 未领取礼包数量
                        JSONObject json = new JSONObject();
                        json.put("groupId", groupId);
                        json.put("pageNo", 1);
                        json.put("pageSize", 6);
                        JSONObject resultJson = HttpClientUtil.httpPost(PROJECT_ACTIVITY_URL + "gift/bag/getDooolyGiftBagListByGroup", json);
                        log.info("获得企业兜礼礼包：" + resultJson.toJSONString());

                        if (MessageDataBean.success_code.equals(resultJson.getString("code"))) {
                            JSONObject date = (JSONObject) JSONObject.parse(resultJson.getString("data"));
                            floorEntry.setHasMore(date.getBoolean("hasMore"));
                            JSONArray list = date.getJSONArray("list");

                            if (list.size() > 0) {
                                items = new ArrayList<>();

                                for (int i = 0; i < list.size(); i++) {
                                    JSONObject entry = (JSONObject) list.get(0);
                                    DlTemplateFloorItem item = new DlTemplateFloorItem();
                                    item.setIconUrl(entry.getString("subImage"));
                                    item.setId(entry.getString("id"));
                                    item.setTitle(entry.getString("giftBagName"));
                                    items.add(item);
                                }
                            }
                        }
                        break;
                    case CstInfoConstants.TEMP_HOME_TYPE_THREE:
                        // 广告位
                        List<AdAd> ads = adadDao.getByTypeAndGroup(12, groupId, 3);

                        if (!CollectionUtils.isEmpty(ads)) {
                            items = new ArrayList<>();

                            for (AdAd ad : ads) {
                                DlTemplateFloorItem item = new DlTemplateFloorItem();
                                item.setLinkUrl(ad.getImageLinkUrl());
                                item.setImageUrl(ad.getImagePath());
                                item.setTitle(ad.getTitle());
                                item.setLinkType(ad.getLinkType());
                                items.add(item);
                            }
                        }
                        break;
                    case CstInfoConstants.TEMP_HOME_TYPE_FOUR:
                        // 热门商户
                        List<AdBusiness> merchants = adBusinessDao.findHotMerchantsByDealType(
                                Integer.valueOf(userId), null, address,
                                Arrays.asList(DEAL_TYPE_OFFLINE, DEAL_TYPE_ONLINE));

                        if (!CollectionUtils.isEmpty(merchants)) {
                            items = new ArrayList<>();
                            int row = 0;

                            for (AdBusiness merchant : merchants) {
                                if (row >= 10) {
                                    break;
                                }
                                row++;
                                DlTemplateFloorItem item = new DlTemplateFloorItem();
                                item.setTitle(merchant.getCompany());
                                item.setServerEndTime(merchant.getServerEndTime());
                                // 前折信息
                                String promotionInfo = "";
                                // 返利信息
                                String rebateInfo = "";

                                if (merchant.getDiscount() != null && merchant.getDiscount() > 0) {
                                    promotionInfo = merchant.getDiscount() + "折 ";
                                }

                                if (!StringUtils.isEmpty(merchant.getMaxUserRebate())
                                        && new BigDecimal(merchant.getMaxUserRebate()).compareTo(BigDecimal.ZERO) == 1) {
                                    rebateInfo = "返" + merchant.getMaxUserRebate() + "%";
                                }

                                item.setCornerMarkText(rebateInfo);
                                item.setTitle(merchant.getSubTitle());
                                item.setSubTitle(promotionInfo.trim()+"起");

                                if(StringUtils.isNotBlank(promotionInfo)){
                                    item.setSubTitle(promotionInfo.trim()+"起");
                                }

                                item.setIconUrl(merchant.getLogo());
                                item.setLinkUrl(BASE_BUSINESSINFO_URL + merchant.getDealType() + "/" + merchant.getId());
                                item.setSubUrl(BASE_BUSINESSINFO_URL.substring(BASE_BUSINESSINFO_URL.indexOf("#") + 1, BASE_BUSINESSINFO_URL.length()) + merchant.getDealType()
                                        + "/" + merchant.getId());
                                item.setIsSupportIntegral(merchant.getIsSupportIntegral());
                                items.add(item);
                            }

                        }
                        break;
                    case CstInfoConstants.TEMP_HOME_TYPE_FIVE:
                        // 卡券列表
                        List<DlTemplateFloorItem> itemList = dlTemplateFloorItemDao.getAllByFloorId(floor.getId());

                        if (!CollectionUtils.isEmpty(itemList)) {
                            items = new ArrayList<>();
                            HashMap<String, Object> map = myCouponsBusinessServiceI.getCouponListByType(userId, "unuse", "0");
                            floorEntry.setCouponCount(((ArrayList)map.get("actConnList")).size());
                            log.info(String.format("用户：%s，可使用卡券数量为：%s", userId, ((ArrayList)map.get("actConnList")).size()));

                            for (DlTemplateFloorItem item : itemList) {
                                // 如果关联类型为自营商品name去查找自营商品相关信息
                                AdSelfProductSku sku = adSelfProductSkuDao.get(item.getRelationId());
                                DlTemplateFloorItem itemEntry = new DlTemplateFloorItem();
                                itemEntry.setPrice(sku.getSellPrice());
                                itemEntry.setOriginalPrice(sku.getMarketPrice());
                                itemEntry.setTitle(sku.getTitle());
                                itemEntry.setSubTitle(sku.getIntroduction());
                                itemEntry.setIconUrl(sku.getImage());
                                itemEntry.setId(sku.getSelfProductId());
                                itemEntry.setLinkUrl(BASE_CARDBUYDETAIL_URL + sku.getSelfProductId());
                                itemEntry.setSubUrl(BASE_CARDBUYDETAIL_URL.substring(BASE_CARDBUYDETAIL_URL.indexOf("#") + 1, BASE_CARDBUYDETAIL_URL.length())
                                        + sku.getSelfProductId());
                                items.add(itemEntry);
                            }
                        }
                        break;
                    default:
                        List<DlTemplateFloorItem> list = dlTemplateFloorItemDao.getAllByFloorId(floor.getId());

                        if (!CollectionUtils.isEmpty(list)) {
                            items = new ArrayList<>();

                            for (DlTemplateFloorItem item : list) {
                                DlTemplateFloorItem itemEntry = new DlTemplateFloorItem();
                                itemEntry.setTitle(item.getTitle());
                                itemEntry.setSubTitle(item.getSubTitle());
                                if (StringUtils.isNotBlank(item.getLinkUrl()) && item.getLinkUrl().indexOf("#") > -1) {
                                    itemEntry.setSubUrl(item.getLinkUrl().substring(item.getLinkUrl().indexOf("#") + 1, item.getLinkUrl().length()));
                                }
                                itemEntry.setLinkUrl(item.getLinkUrl());
                                itemEntry.setIconUrl(item.getIconUrl());
                                itemEntry.setId(item.getId());
                                items.add(itemEntry);
                            }
                        }
                        break;
                }

                if (items != null && items.size() > 0) {
                    floorEntry.setItems(items);
                    floors.add(floorEntry);
                }
            }
            template.setFloors(floors);
        } else {
            log.error(String.format("模版没有可用楼层, 模版ID：%s，模版类型：%s", template.getId(), template.getType()));
            return false;
        }
        return true;
    }
}
