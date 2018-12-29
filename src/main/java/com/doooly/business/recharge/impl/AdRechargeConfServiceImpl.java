package com.doooly.business.recharge.impl;

import com.doooly.business.recharge.AdRechargeConfServiceI;
import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.reachad.AdRechargeConfDao;
import com.doooly.entity.reachad.AdRechargeConf;
import com.reach.redis.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-12-26
 */
@Service
public class AdRechargeConfServiceImpl implements AdRechargeConfServiceI{

    @Autowired
    private AdRechargeConfDao adRechargeConfDao;

    @Override
    @Cacheable(module = "ADRECHARGECONFSERVICEIMPL", event = "getRechargeConf", key = "groupId",
            expires = RedisConstants.REDIS_RECHARGE_CACHE_EXPIRATION_DATE, required = true)
    public AdRechargeConf getRechargeConf(Map<String, Object> paramMap) {
        String groupId = (String) paramMap.get("groupId");
        return adRechargeConfDao.getRechargeConf(groupId);
    }
}
