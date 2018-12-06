package com.doooly.business.home.v2.servcie.impl;

import com.doooly.business.home.v2.servcie.IAdBasicTypeService;
import com.doooly.dao.reachad.AdBasicTypeDao;
import com.doooly.entity.reachad.AdBasicType;
import com.reach.redis.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模版常量配置服务类
 * @Author: Mr.Wu
 * @Date: 2018/12/6
 */
@Service
public class AdBasicTypeServiceImpl implements IAdBasicTypeService {

    @Autowired
    AdBasicTypeDao adBasicTypeDao;

    @Cacheable(module = "TEMPLATE", event = "getFloors", key = "userId, type, templateType")
    @Override
    public List<AdBasicType> getFloors(String userId, Integer type, Integer templateType) {
        return adBasicTypeDao.getFloors(userId, type, templateType);
    }
}
