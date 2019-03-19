package com.doooly.business.home.v2.servcie.impl;

import com.doooly.business.home.v2.servcie.LifehomeService;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.CstInfoConstants;
import com.doooly.dao.doooly.DlTemplateFloorDao;
import com.doooly.dao.reachad.*;
import com.doooly.entity.doooly.DlTemplateFloor;
import com.doooly.entity.doooly.DlTemplateFloorItem;
import com.doooly.entity.home.AdBusinessScene;
import com.doooly.entity.reachad.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wanghai
 * @Date:2019/3/14 15:45
 * @Copyright:reach-life
 * @Description:
 */
@Service
public class LifehomeServiceImpl implements LifehomeService{

    @Autowired
    private DlTemplateFloorDao templateFloorDao;

    @Autowired
    private AdBusinessSceneMapper adBusinessSceneMapper;

    @Autowired
    private AdBusinessGroupDao adBusinessGroupDao;

    @Autowired
    private AdBusinessDao adBusinessDao;

    @Autowired
    private AdGuideCategoryDao adGuideCategoryDao;

    @Autowired
    private AdProductDao adProductDao;

    @Autowired
    private AdadDao adadDao;


    @Override
    public List<Map<String, Object>> getLifeFloors(String groupId,int pageNum,int pageSize,String channel) {
        List<Map<String,Object>> floorsItemMap = new ArrayList<>();
        List<DlTemplateFloor> dlTemplateFloorList = templateFloorDao.getTemplateFloorByGroup(groupId,"2");//1、首页模板，2、生活模板
        for (int i = 0; i < dlTemplateFloorList.size(); i++) {
            if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_ONE){
                // 广告位
                Map<String,Object> adMap = new HashMap<>();
                adMap.put("mainTitle","生活广告");
                adMap.put("type",dlTemplateFloorList.get(i).getType());
                List<Map<String,Object>> adMapItemList = new ArrayList<>();
                List<AdAd> ads = adadDao.getByTypeAndGroup(13, groupId, 3);
                if (!CollectionUtils.isEmpty(ads)) {
                    for (AdAd ad : ads) {
                        Map<String,Object> adMapItem = new HashMap<>();
                        adMapItem.put("linkUrl",ad.getImageLinkUrl());
                        adMapItem.put("iconUrl",ad.getImagePath());
                        adMapItemList.add(adMapItem);
                    }
                }
                adMap.put("list",adMapItemList);
                floorsItemMap.add(adMap);
            } else if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_TWO) {
                //生活场景
                Map<String,Object> lifeSceneMap = new HashMap<>();
                lifeSceneMap.put("mainTitle","生活场景");
                lifeSceneMap.put("type",dlTemplateFloorList.get(i).getType());

                List<Map<String,Object>> adBusinessSceneListMap = new ArrayList<>();
                AdBusinessScene adBusinessScene = new AdBusinessScene();
                adBusinessScene.setState(1);
                List<AdBusinessScene> adBusinessSceneList = adBusinessSceneMapper.getListByCondition(adBusinessScene);
                for (int j = 0; j < adBusinessSceneList.size(); j++) {
                    Map<String,Object> adBusinessSceneMap = new HashMap<>();
                    adBusinessSceneMap.put("subTitle",adBusinessSceneList.get(j).getName());
                    adBusinessSceneMap.put("iconUrl",adBusinessSceneList.get(j).getWxIcon());
                    //对应的商户
                    AdBusinessGroup adBusinessGroup = new AdBusinessGroup();
                    adBusinessGroup.setGroupId(groupId);
                    adBusinessGroup.setSceneId(adBusinessSceneList.get(j).getId());
                    List<AdBusinessGroup> adBusinessGroupList = adBusinessGroupDao.getListByCondition(adBusinessGroup);
                    List<String> businessIds = new ArrayList<>();
                    for (int k = 0; k < adBusinessGroupList.size(); k++) {
                        businessIds.add(adBusinessGroupList.get(k).getBusinessId());
                    }
                    List<AdBusiness> businessList = new ArrayList<>();
                    if (businessIds.size() > 0) {
                        businessList = adBusinessDao.getListByBusinessIds(businessIds);
                    }
                    List<Map<String,Object>> businessListMap = new ArrayList<>();
                    for (int k = 0; k < businessList.size(); k++) {
                        Map<String,Object> adBusinessMap = new HashMap<>();
                        adBusinessMap.put("mainTitle",businessList.get(k).getCompany());
                        if (Constants.CHANNEL_H5.equals(channel) || Constants.CHANNEL_WECHAT.equals(channel)) {
                            adBusinessMap.put("linkUrl",businessList.get(k).getUrl());
                        } else {
                            adBusinessMap.put("linkUrl",businessList.get(k).getAppUrl());
                        }
                        adBusinessMap.put("iconUrl",businessList.get(k).getLogo());
                        adBusinessMap.put("serverEndTime",businessList.get(k).getServerEndTime());
                        businessListMap.add(adBusinessMap);
                    }
                    if (businessListMap.size() > 0) {
                        adBusinessSceneMap.put("subList",businessListMap);
                        adBusinessSceneListMap.add(adBusinessSceneMap);
                    }
                }
                lifeSceneMap.put("list",adBusinessSceneListMap);
                floorsItemMap.add(lifeSceneMap);
            } else if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_THREE) {
                //导购管理
                Map<String,Object> adGuideCategoryData = new HashMap<>();
                adGuideCategoryData.put("mainTitle","导购管理");
                adGuideCategoryData.put("type",dlTemplateFloorList.get(i).getType());
                List<AdGuideCategory> adGuideCategoryList = adGuideCategoryDao.findList();
                List<Map<String,Object>> adGuideCategoryListMap = new ArrayList<>();
                for (int j = 0; j < adGuideCategoryList.size(); j++) {
                    Map<String,Object> adGuideCategoryMap = new HashMap<>();
                    AdGuideCategory adGuideCategory = adGuideCategoryList.get(j);
                    adGuideCategoryMap.put("subTitle",adGuideCategory.getCategoryName());
                    adGuideCategoryMap.put("iconUrl",adGuideCategory.getIconUrl());


                    AdProduct adProduct = new AdProduct();
                    adProduct.setGuideCategoryId(Integer.parseInt(adGuideCategory.getId()));
                    adProduct.setRecommendLife(1);//是否推荐到生活 0 不推荐，1 推荐
                    List<AdProduct> adProductList = adProductDao.getListByCondition(adProduct);
                    List<Map<String,Object>> adProductListMap = new ArrayList<>();
                    for (int k = 0; k < adProductList.size(); k++) {
                        AdProduct adProduct1 = adProductList.get(k);
                        Map<String,Object> adProductMap = new HashMap<>();
                        adProductMap.put("image",adProduct1.getImageWechat());
                        adProductMap.put("guideTag",adProduct1.getGuideTag());
                        adProductMap.put("marketPrice",adProduct1.getMarketPrice());
                        adProductMap.put("name",adProduct1.getName());
                        adProductMap.put("userRebate",adProduct1.getUserRebate());
                        adProductMap.put("id",adProduct1.getId());
                        adProductMap.put("sellPrice",adProduct1.getPrice());
                        adProductMap.put("businessName",adProduct1.getBusinessName());
                        adProductMap.put("linkUrlWechat",adProduct1.getLinkUrlWechat());
                        adProductListMap.add(adProductMap);
                    }


                    if (adProductListMap.size() > 0) {
                        adGuideCategoryMap.put("subList",adProductListMap);
                        adGuideCategoryListMap.add(adGuideCategoryMap);
                    }
                }
                adGuideCategoryData.put("list",adGuideCategoryListMap);
                floorsItemMap.add(adGuideCategoryData);
            }
        }
        return floorsItemMap;
    }
}
