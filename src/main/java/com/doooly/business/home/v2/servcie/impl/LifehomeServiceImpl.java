package com.doooly.business.home.v2.servcie.impl;

import com.doooly.business.home.v2.servcie.LifehomeService;
import com.doooly.common.constants.CstInfoConstants;
import com.doooly.dao.doooly.DlTemplateFloorDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessGroupDao;
import com.doooly.dao.reachad.AdBusinessSceneMapper;
import com.doooly.entity.doooly.DlTemplateFloor;
import com.doooly.entity.home.AdBusinessScene;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessGroup;
import org.springframework.beans.factory.annotation.Autowired;

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
public class LifehomeServiceImpl implements LifehomeService{

    @Autowired
    private DlTemplateFloorDao templateFloorDao;

    @Autowired
    private AdBusinessSceneMapper adBusinessSceneMapper;

    @Autowired
    private AdBusinessGroupDao adBusinessGroupDao;

    @Autowired
    private AdBusinessDao adBusinessDao;


    @Override
    public Map<String, Object> getLifeFloors(String groupId) {
        List<DlTemplateFloor> dlTemplateFloorList = templateFloorDao.getTemplateFloorByGroup(groupId,"2");//1、首页模板，2、生活模板
        for (int i = 0; i < dlTemplateFloorList.size(); i++) {
            //生活场景
            if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_TWO) {
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
                    List<AdBusiness> businessList = adBusinessDao.getListByBusinessIds(businessIds);
                    List<Map<String,Object>> businessListMap = new ArrayList<>();
                    for (int k = 0; k < businessList.size(); k++) {
                        Map<String,Object> adBusinessMap = new HashMap<>();
                        adBusinessMap.put("mainTitle",businessList.get(k).getCompany());
                        adBusinessMap.put("linkUrl",businessList.get(k).getAppUrl());
                        adBusinessMap.put("iconUrl",businessList.get(k).getLogo());
                        adBusinessMap.put("serverEndTime",businessList.get(k).getServerEndTime());
                        businessListMap.add(adBusinessMap);
                    }
                    adBusinessSceneMap.put("subList",businessListMap);
                    adBusinessSceneListMap.add(adBusinessSceneMap);
                }
                lifeSceneMap.put("list",adBusinessSceneListMap);

            } else if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_THREE) {
                //导购管理
            }
        }
        return null;
    }
}
