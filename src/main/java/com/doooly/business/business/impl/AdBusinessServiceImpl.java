package com.doooly.business.business.impl;

import com.doooly.business.business.AdBusinessServiceI;
import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessExpandInfoDao;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.reach.redis.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-12-27
 */
@Service
public class AdBusinessServiceImpl implements AdBusinessServiceI {

    @Autowired
    private AdBusinessDao adBusinessDao;
    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;

    @Override
    @Cacheable(module = "ADBUSINESSSERVICEIMPL", event = "getBusiness", key = "id",
            expires = RedisConstants.REDIS_CACHE_EXPIRATION_DATE)
    public AdBusiness getBusiness(AdBusiness adBusiness) {
        return adBusinessDao.getBusiness(adBusiness);
    }

    @Override
    @Cacheable(module = "ADBUSINESSSERVICEIMPL", event = "getBusinessExpandInfo", key = "businessId",
            expires = RedisConstants.REDIS_CACHE_EXPIRATION_DATE, required = true)
    public AdBusinessExpandInfo getBusinessExpandInfo(AdBusinessExpandInfo adBusinessExpandInfo) {
        return adBusinessExpandInfoDao.getBusinessExpandInfo(adBusinessExpandInfo);
    }
}
