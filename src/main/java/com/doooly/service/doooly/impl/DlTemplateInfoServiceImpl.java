package com.doooly.service.doooly.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.freeCoupon.service.MyCouponsBusinessServiceI;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.common.constants.CstInfoConstants;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.dao.doooly.DlTemplateFloorDao;
import com.doooly.dao.doooly.DlTemplateFloorItemDao;
import com.doooly.dao.doooly.DlTemplateGroupDao;
import com.doooly.dao.doooly.DlTemplateInfoDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.doooly.DlTemplateFloor;
import com.doooly.entity.doooly.DlTemplateFloorItem;
import com.doooly.entity.doooly.DlTemplateGroup;
import com.doooly.entity.doooly.DlTemplateInfo;
import com.doooly.service.doooly.DlTemplateInfoServiceI;
import com.doooly.service.doooly.TemplateCacheServiceI;
import com.reach.redis.annotation.EnableCaching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模版服务接口实现
 * @Author: Mr.Wu
 * @Date: 2019/3/13
 */
@Service
@EnableCaching
public class DlTemplateInfoServiceImpl implements DlTemplateInfoServiceI {
   private static Logger log = LoggerFactory.getLogger(DlTemplateInfoServiceImpl.class);
    private String BASE_URL = PropertiesConstants.commonBundle.getString("BASE_URL");
    private String BASE_BUSINESS_INFO_URL = BASE_URL + "/businessinfo/";
    private String BASE_CARDBUYDETAIL_URL = BASE_URL + "/cardBuyDetail/";

    @Autowired
    private DlTemplateInfoDao dlTemplateInfoDao;
    @Autowired
    private DlTemplateGroupDao dlTemplateGroupDao;
    @Autowired
    private DlTemplateFloorDao dlTemplateFloorDao;
    @Autowired
    private DlTemplateFloorItemDao dlTemplateFloorItemDao;
    @Autowired
    private MyCouponsBusinessServiceI myCouponsBusinessServiceI;
    @Autowired
    private TemplateCacheServiceI templateCacheService;


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

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("floorId", floor.getId());

                List<DlTemplateFloorItem> items = null;
                switch (floor.getType()) {
                    case CstInfoConstants.TEMP_HOME_TYPE_TWO:
                        // 兜礼礼包
                        Map<String, Object> giftMap = templateCacheService.getDooolyGiftBagItemsByGroup(paramMap);
                        floorEntry.setHasMore((Boolean) giftMap.get("hasMore"));
                        items = (List<DlTemplateFloorItem>) giftMap.get("list");
                        break;
                    case CstInfoConstants.TEMP_HOME_TYPE_THREE:
                        // 广告位
                        paramMap.put("groupId", groupId);
                        paramMap.put("version", 3);
                        paramMap.put("type", 12);
                        items = templateCacheService.getAdAdItemsByTypeAndGroup(paramMap);
                        break;
                    case CstInfoConstants.TEMP_HOME_TYPE_FOUR:
                        paramMap.put("address", address);
                        paramMap.put("groupId", groupId);
                        paramMap.put("userId", userId);
                        items = templateCacheService.getHotBusinessFloor(paramMap);
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
                                paramMap.put("skuId", item.getRelationId());
                                AdSelfProductSku sku = templateCacheService.getSelfProductSku(paramMap);

                                if (sku == null) {
                                    continue;
                                }

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
                        // 标准楼层item列表获取
                        items = templateCacheService.getStandardFloorItem(paramMap);
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
