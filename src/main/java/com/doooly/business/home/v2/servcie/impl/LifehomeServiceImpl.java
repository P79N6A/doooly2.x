package com.doooly.business.home.v2.servcie.impl;

import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.business.home.v2.servcie.LifehomeService;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.CstInfoConstants;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.dao.doooly.DlTemplateFloorDao;
import com.doooly.dao.doooly.DlTemplateFloorItemDao;
import com.doooly.dao.reachad.*;
import com.doooly.dto.reachad.AdProductExtend;
import com.doooly.entity.doooly.DlTemplateFloor;
import com.doooly.entity.doooly.DlTemplateFloorItem;
import com.doooly.entity.home.AdBusinessScene;
import com.doooly.entity.reachad.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author: wanghai
 * @Date:2019/3/14 15:45
 * @Copyright:reach-life
 * @Description:
 */
@Service
public class LifehomeServiceImpl implements LifehomeService{


    private String BASE_URL = PropertiesHolder.getProperty("BASE_URL") + "/businessinfo/";

    @Autowired
    private DlTemplateFloorDao templateFloorDao;

    @Autowired
    private DlTemplateFloorItemDao dlTemplateFloorItemDao;

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

    @Autowired
    private AdArticleServiceI adArticleServiceI;


    @Override
    public List<Map<String, Object>> getLifeFloors(String groupId,int pageNum,int pageSize,String channel,String city) {
        List<Map<String,Object>> floorsItemMap = new ArrayList<>();
        List<DlTemplateFloor> dlTemplateFloorList = templateFloorDao.getTemplateFloorByGroup(groupId,"2");//1、首页模板，2、生活模板
        for (int i = 0; i < dlTemplateFloorList.size(); i++) {
            if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_ONE){
                // 广告位
                Map<String,Object> adMap = new HashMap<>();
                adMap.put("mainTitle",dlTemplateFloorList.get(i).getTitle());
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
                lifeSceneMap.put("mainTitle",dlTemplateFloorList.get(i).getTitle());
                lifeSceneMap.put("type",dlTemplateFloorList.get(i).getType());

                List<Map<String,Object>> adBusinessSceneListMap = new ArrayList<>();
                AdBusinessScene adBusinessScene = new AdBusinessScene();
                adBusinessScene.setState(1);
                List<AdBusinessScene> adBusinessSceneList = adBusinessSceneMapper.getListByCondition(adBusinessScene);
                for (int j = 0; j < adBusinessSceneList.size(); j++) {
                    Map<String,Object> adBusinessSceneMap = new HashMap<>();
                    adBusinessSceneMap.put("subTitle",adBusinessSceneList.get(j).getName());
                    if (Constants.CHANNEL_APP.equals(channel)) {
                        adBusinessSceneMap.put("iconUrl",adBusinessSceneList.get(j).getAppIcon());
                        adBusinessSceneMap.put("iconUrlActive",adBusinessSceneList.get(j).getAppIconActive());
                    } else {
                        adBusinessSceneMap.put("iconUrl",adBusinessSceneList.get(j).getWxIcon());
                        adBusinessSceneMap.put("iconUrlActive",adBusinessSceneList.get(j).getWxIconActive());
                    }

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
                        if (StringUtils.isNotBlank(city) && !AdArea.ALLCITY.equals(city)) {
                            List<String> businessIdsCity = adBusinessDao.getBusinessByCity(city);
                            for (Iterator<String> it = businessIds.iterator();it.hasNext();) {
                                String id = it.next();
                                if(!businessIdsCity.contains(id)) {
                                    it.remove();
                                }
                            }
                        }

                        if (businessIds.size() > 0) {
                            businessList = adBusinessDao.getListByBusinessIds(businessIds);
                        }
                    }
                    List<Map<String,Object>> businessListMap = new ArrayList<>();
                    for (int k = 0; k < businessList.size(); k++) {
                        Map<String,Object> adBusinessMap = new HashMap<>();
                        adBusinessMap.put("mainTitle",businessList.get(k).getCompany());
                        adBusinessMap.put("subUrl",BASE_URL.substring(BASE_URL.indexOf("#") + 1, BASE_URL.length()) + businessList.get(k).getDealType()
                                + "/" + businessList.get(k).getId());
                        adBusinessMap.put("linkUrl",BASE_URL + businessList.get(k).getDealType() + "/" + businessList.get(k).getId());
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
                adGuideCategoryData.put("mainTitle",dlTemplateFloorList.get(i).getTitle());
                adGuideCategoryData.put("type",dlTemplateFloorList.get(i).getType());
                floorsItemMap.add(adGuideCategoryData);
            }
        }
        return floorsItemMap;
    }


  public  List<Map<String,Object>> getGuideCategory(String groupId) {
        List<DlTemplateFloor> dlTemplateFloorList = templateFloorDao.getTemplateFloorByGroup(groupId,"2");//1、首页模板，2、生活模板
        List<Map<String,Object>> guideCategoryList = new ArrayList<>();
        for (int i = 0; i < dlTemplateFloorList.size(); i++) {
            if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_THREE) {
                List<DlTemplateFloorItem> dlTemplateFloorItemList = dlTemplateFloorItemDao.getAllByTempIdAndFloorId(dlTemplateFloorList.get(i).getTemplateId(),dlTemplateFloorList.get(i).getId());
                for (int j = 0; j < dlTemplateFloorItemList.size(); j++) {
                    DlTemplateFloorItem dlTemplateFloorItem = dlTemplateFloorItemList.get(j);
                    Map<String,Object> adGuideCategoryMap = new HashMap<>();
                    adGuideCategoryMap.put("id",dlTemplateFloorItem.getRelationId());
                    adGuideCategoryMap.put("subTitle",dlTemplateFloorItem.getTitle());
                    adGuideCategoryMap.put("iconUrl",dlTemplateFloorItem.getIconUrl());
                    guideCategoryList.add(adGuideCategoryMap);
                }
            }
        }
        for (Iterator<Map<String,Object>> iterator = guideCategoryList.iterator();iterator.hasNext();) {
            Map<String,Object> itemMap = iterator.next();
            String id = String.valueOf(itemMap.get("id"));
            int cnt = adProductDao.getGuideProductListCntv4(id,"1");
            if (cnt == 0) {
                iterator.remove();
            }
        }
        return guideCategoryList;
    }


    public List<Map<String,Object>> getGuideCategoryBusi(String guideCategoryId,int pageNum,int pageSize) {
        List<Map<String,Object>> adProductListMap = new ArrayList<>();
        if (StringUtils.isBlank(guideCategoryId)) {
            return adProductListMap;
        }
        int offset = (pageNum - 1) * pageSize;
        List<AdProductExtend> productExtends = adProductDao.getGuideProductListv4(guideCategoryId,offset,pageSize,"1");
        for (AdProductExtend adProduct : productExtends) {
            adArticleServiceI.calculateExtend(adProduct);
        }
        for (int k = 0; k < productExtends.size(); k++) {
            AdProductExtend adProduct1 = productExtends.get(k);
            Map<String,Object> adProductMap = new HashMap<>();
            adProductMap.put("image",adProduct1.getImageWechat());
            adProductMap.put("guideTag", StringUtils.isNotBlank(adProduct1.getGuideTag())
                    && adProduct1.getGuideTag().charAt(adProduct1.getGuideTag().length() - 1) == ',' ?
                    adProduct1.getGuideTag().substring(0,adProduct1.getGuideTag().length() - 1) : adProduct1.getGuideTag());
            adProductMap.put("marketPrice",adProduct1.getMarketPrice());
            adProductMap.put("name",adProduct1.getName());
            adProductMap.put("userRebate",adProduct1.getUserRebate());
            adProductMap.put("id",adProduct1.getId());
            adProductMap.put("isStar",adProduct1.getIsStar());
            adProductMap.put("dooolyPrice",adProduct1.getDooolyPrice());
            adProductMap.put("sellPrice",adProduct1.getPrice());
            adProductMap.put("businessName",adProduct1.getShippingMethod());
            adProductMap.put("linkUrlWechat",adProduct1.getLinkUrlWechat());
            adProductListMap.add(adProductMap);
        }
        return adProductListMap;
    }

}
