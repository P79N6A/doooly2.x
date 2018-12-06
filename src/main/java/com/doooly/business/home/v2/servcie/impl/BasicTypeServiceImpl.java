package com.doooly.business.home.v2.servcie.impl;


import com.doooly.business.home.v2.servcie.BasicTypeService;
import com.doooly.entity.reachad.AdBasicType;
import com.reach.redis.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component("basicTypeService")
public class BasicTypeServiceImpl implements BasicTypeService {


    @Cacheable(module = "TEMPLATE", event = "getFloors", key = "userId, type, templateType")
    @Override
    public List<AdBasicType> getFloors(String userId, Integer type, Integer templateType) {
        return null;
    }

    @Override
    @Cacheable(module = "TEMPLATE", event = "getAll", key = "Str")
    public void getAll(String str) {

    }
}