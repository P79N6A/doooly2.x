package com.doooly.business.business.impl;

import com.doooly.business.business.AdBusinessServicePJI;
import com.doooly.dao.reachad.AdBusinessServicePJDao;
import com.doooly.entity.reachad.AdBusinessServicePJ;
import com.reach.redis.annotation.Cacheable;
import com.reach.redis.manager.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ad_business_service商户服务表服务实现类
 * @Author: Mr.Wu
 * @Date: 2018/12/6
 */
@Service
public class AdBusinessServicePJImpl implements AdBusinessServicePJI {

    @Autowired
    AdBusinessServicePJDao adBusinessServicePJDao;

    @Override
    @Cacheable(module = "ADBUSINESSSERVICEPJ", event = "getDataByUserId", key = "userId, serviceType")
    public List<AdBusinessServicePJ> getDataByUserId(Long userId, String serviceType) {
        AdBusinessServicePJDao adBusinessServicePJDao = (AdBusinessServicePJDao) SpringContextUtil.getBean("adBusinessServicePJDao");
        return adBusinessServicePJDao.getDataByUserId(userId, serviceType);
    }
}
