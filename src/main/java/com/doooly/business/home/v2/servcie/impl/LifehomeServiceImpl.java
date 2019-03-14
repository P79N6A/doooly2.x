package com.doooly.business.home.v2.servcie.impl;

import com.doooly.business.home.v2.servcie.LifehomeService;
import com.doooly.dao.doooly.DlTemplateFloorDao;
import com.doooly.entity.doooly.DlTemplateFloor;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Override
    public Map<String, Object> getLifeFloors(String groupId) {
        List<DlTemplateFloor> dlTemplateFloorList = templateFloorDao.getTemplateFloorByGroup(groupId,"2");//1、首页模板，2、生活模板
        for (int i = 0; i < dlTemplateFloorList.size(); i++) {

        }
        return null;
    }
}
