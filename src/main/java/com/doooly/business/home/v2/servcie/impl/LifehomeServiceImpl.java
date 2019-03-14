package com.doooly.business.home.v2.servcie.impl;

import com.doooly.business.home.v2.servcie.LifehomeService;
import com.doooly.common.constants.CstInfoConstants;
import com.doooly.dao.doooly.DlTemplateFloorDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessSceneMapper;
import com.doooly.entity.doooly.DlTemplateFloor;
import com.doooly.entity.home.AdBusinessScene;
import org.springframework.beans.factory.annotation.Autowired;

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


    @Override
    public Map<String, Object> getLifeFloors(String groupId) {
        List<DlTemplateFloor> dlTemplateFloorList = templateFloorDao.getTemplateFloorByGroup(groupId,"2");//1、首页模板，2、生活模板
        for (int i = 0; i < dlTemplateFloorList.size(); i++) {
            //生活场景
            if (dlTemplateFloorList.get(i).getType() == CstInfoConstants.TEMP_LIFE_TYPE_TWO) {
                AdBusinessScene adBusinessScene = new AdBusinessScene();
                adBusinessScene.setState(1);
                List<AdBusinessScene> adBusinessSceneList = adBusinessSceneMapper.getListByCondition(adBusinessScene);
                for (int j = 0; j < adBusinessSceneList.size(); j++) {
                    Map<String,Object> adBusinessSceneMap = new HashMap<>();
                    //adBusinessSceneMap.put()
                    //adBusinessSceneList.get(i)
                }
            }
        }
        return null;
    }
}
