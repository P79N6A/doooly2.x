package com.doooly.business.home.v2.servcie.impl;

import com.doooly.business.home.v2.servcie.AdConsumeRechargeServiceI;
import com.doooly.dao.reachad.AdConsumeRechargeDao;
import com.doooly.entity.reachad.AdConsumeRecharge;
import com.reach.redis.annotation.Cacheable;
import com.reach.redis.manager.SpringContextUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动类目，类型，楼层
 * @Author: Mr.Wu
 * @Date: 2018/12/6
 */
@Service
public class AdConsumeRechargeServiceImpl implements AdConsumeRechargeServiceI {
//    @Autowired
//    AdConsumeRechargeDao adConsumeRechargeDao;

    @Cacheable(module = "ADCONSUMERECHARGE", event = "getConsumeRecharges", key = "templateId, floorId")
    @Override
    public List<AdConsumeRecharge> getConsumeRecharges(int templateId, int floorId) {
        AdConsumeRechargeDao adConsumeRechargeDao = (AdConsumeRechargeDao) SpringContextUtil.getBean("adConsumeRechargeDao");
        return adConsumeRechargeDao.getConsumeRecharges(templateId, floorId);
    }
}
