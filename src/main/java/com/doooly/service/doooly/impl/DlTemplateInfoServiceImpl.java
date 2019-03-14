package com.doooly.service.doooly.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.common.constants.CstInfoConstants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.constants.VersionConstants;
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
import com.doooly.entity.reachad.AdConsumeRecharge;
import com.doooly.service.doooly.DlTemplateInfoServiceI;
import com.reach.redis.annotation.EnableCaching;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String BASE_URL = PropertiesHolder.getProperty("BASE_URL") + "/businessinfo/";
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
                        result.put("date", floors);
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
                DlTemplateFloor entry = new DlTemplateFloor();
                entry.setTitle(floor.getTitle());
                entry.setSubTitle(floor.getSubTitle());

                List<DlTemplateFloorItem> items = null;
                switch (floor.getType()) {
                    case CstInfoConstants.TEMP_HOME_TYPE_TWO:
                        items = new ArrayList<>();
                        // 兜礼礼包

                        template.getFloors().add(floor);
                        break;
                    case CstInfoConstants.TEMP_HOME_TYPE_THREE:
                        // 广告位
                        List<AdAd> ads = adadDao.getByTypeAndGroup(12, groupId);

                        if (!CollectionUtils.isEmpty(ads)) {
                            items = new ArrayList<>();

                            for (AdAd ad : ads) {
                                DlTemplateFloorItem item = new DlTemplateFloorItem();
                                item.setLinkUrl(ad.getImageLinkUrl());
                                item.setImageUrl(ad.getImagePath());
                                item.setTitle(ad.getTitle());
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

                            for (AdBusiness merchant : merchants) {
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
                                item.setLinkUrl(BASE_URL + merchant.getDealType() + "/" + merchant.getId());
                                item.setSubUrl(BASE_URL.substring(BASE_URL.indexOf("#") + 1, BASE_URL.length()) + merchant.getDealType()
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
                            floor.setCouponCount("9");

                            for (DlTemplateFloorItem item : itemList) {
                                // 如果关联类型为自营商品name去查找自营商品相关信息
                                AdSelfProductSku sku = adSelfProductSkuDao.get(item.getRelationId());
                                item.setPrice(sku.getSellPrice());
                                item.setOriginalPrice(sku.getMarketPrice());
                                item.setTitle(sku.getTitle());
                                item.setSubTitle(sku.getIntroduction());
                                item.setIconUrl(sku.getImage());
                                item.setId(sku.getSelfProductId());
                                item.setLinkUrl("baidu.com");
                                item.setSubUrl("baidu.com");
                                items.add(item);
                            }
                        }
                        break;
                    default:
                        List<DlTemplateFloorItem> list = dlTemplateFloorItemDao.getAllByFloorId(floor.getId());
                        if (!CollectionUtils.isEmpty(list)) {
                            items = new ArrayList<>();

                            for (DlTemplateFloorItem item : list) {
                                item.setSubUrl(item.getLinkUrl());
                                items.add(item);
                            }
                        }
                        break;
                }
                floor.setItems(items);
            }
            template.setFloors(floorList);
        } else {
            log.error(String.format("模版没有可用楼层, 模版ID：%s，模版类型：%s", template.getId(), template.getType()));
            return false;
        }
        return true;
    }

    /**
     * 查询热门商户
     *
     * @author hutao
     * @date 创建时间：2018年11月2日 上午11:23:30
     * @version 1.0
     * @parameter
     * @since
     * @return
     */
    private List<AdConsumeRecharge> getBussiness(String userId, String address, List<Integer> dealTypeList,
                                                 String version) {
        List<AdBusiness> merchants = adBusinessDao.findHotMerchantsByDealType(Integer.valueOf(userId), null, address,
                Arrays.asList(DEAL_TYPE_OFFLINE, DEAL_TYPE_ONLINE));
        List<AdConsumeRecharge> beans = null;
        if (!CollectionUtils.isEmpty(merchants)) {
            beans = new ArrayList<AdConsumeRecharge>();
            for (AdBusiness merchant : merchants) {
                AdConsumeRecharge bean = new AdConsumeRecharge();
                bean.setMainTitle(merchant.getCompany());
                bean.setServerEndTime(merchant.getServerEndTime());
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
                if (VersionConstants.INTERFACE_VERSION_V2_2.equals(version)) {
                    bean.setCornerMark(rebateInfo);
                    bean.setMainTitle(merchant.getSubTitle());
                    if(StringUtils.isNotBlank(promotionInfo)){
                        bean.setSubTitle(promotionInfo.trim()+"起");
                    }
                } else {
                    bean.setSubTitle(promotionInfo+" "+rebateInfo);
                }
                bean.setIconUrl(merchant.getLogo());

                bean.setLinkUrl(BASE_URL + merchant.getDealType() + "/" + merchant.getId());
                bean.setSubUrl(BASE_URL.substring(BASE_URL.indexOf("#") + 1, BASE_URL.length()) + merchant.getDealType()
                        + "/" + merchant.getId());
                bean.setIsSupportIntegral(merchant.getIsSupportIntegral());
                beans.add(bean);
            }
        }
        return beans;
    }
}
