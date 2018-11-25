package doooly;

//package com.redis;package com.redis.test;

import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdReturnPointsDao;
import com.doooly.dao.reachad.AdReturnPointsLogDao;
import com.doooly.entity.reachad.AdReturnPoints;
import com.doooly.entity.reachad.AdReturnPointsLog;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

//package com.redis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:conf/spring-*.xml" })
public class TestService {

    @Autowired
    private RedisTemplate<String, String> redis;

    @Autowired
    private AdReturnPointsDao adReturnPointsDao;

    @Autowired
    private AdOrderReportDao adOrderReportDao;

    @Autowired
    private AdReturnPointsLogDao adReturnPointsLogDao;

    @Test
    public void test(){
       /* ValueOperations<String, String> ops = redis.opsForValue();
        System.out.println(ops.setIfAbsent("t1", "1"));
        System.out.println(ops.setIfAbsent("t1", "1"));
        System.out.println(redis.expire("t1", 1000, TimeUnit.SECONDS));*/
        /*AdReturnPoints adReturnPoints = new AdReturnPoints();
        adReturnPoints.setReportId("121");
       adReturnPoints = adReturnPointsDao.getByCondition(adReturnPoints);
        System.out.println(new Gson().toJson(adReturnPoints));*/
        //synAdReturnPointsLog("199295666","vipte00001223","TEST_14754724c5a9067880bafaa4d046be9e","1");


        //插入ad_return_points
        AdReturnPoints adReturnPoints = new AdReturnPoints();
        adReturnPoints.setReportId("199295818");
        AdReturnPoints adReturnPoints1 = adReturnPointsDao.get(adReturnPoints);
        adReturnPoints1 = adReturnPointsDao.getByCondition(adReturnPoints);
        System.out.println(new Gson().toJson(adReturnPoints1));
    }



    /**
     * 插入synAdReturnPointsLog
     * @param orderId
     * @param orderNumber
     * @param businessId
     */
    private void synAdReturnPointsLog(String orderId,String orderNumber,String businessId,String type) {


        //插入ad_return_points_log
        AdReturnPointsLog adReturnPointsLog = new AdReturnPointsLog();
        adReturnPointsLog.setOrderId(Long.parseLong(orderId));
        adReturnPointsLog.setType(type);
        AdReturnPointsLog adReturnPointsLog1 = adReturnPointsLogDao.getByCondition(adReturnPointsLog);
        if (adReturnPointsLog1 != null) {
            return;
        }
        adReturnPointsLog.setAdReturnPointsId(Long.parseLong("22"));
        adReturnPointsLog.setOperateAmount(BigDecimal.ZERO);
        if ("1".equals(type)) {
            adReturnPointsLog.setOperateType("1");
        } else if ("5".equals(type)) {
            adReturnPointsLog.setOperateType("2");
        }
        adReturnPointsLog.setDelFlag("0");
        adReturnPointsLog.setCreateDate(new Date());
        adReturnPointsLog.setUpdateDate(new Date());
        adReturnPointsLogDao.save(adReturnPointsLog);

    }
}


