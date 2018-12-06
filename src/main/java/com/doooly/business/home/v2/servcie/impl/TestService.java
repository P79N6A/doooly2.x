package com.doooly.business.home.v2.servcie.impl;

import com.reach.redis.annotation.Cacheable;
import com.reach.redis.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class TestService {
    @Cacheable(module = "TEMPLATE", event = "getFloors", key = "userId, type, templateType")
    public void test(String userId){

    }
}
