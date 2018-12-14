package doooly;

//package com.redis;package com.redis.test;

import com.doooly.business.meituan.MeituanService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessor;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessorFactory;
import com.doooly.business.pay.processor.refundprocessor.RefundSyncOrderProcessor;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdReturnPointsDao;
import com.doooly.dao.reachad.AdReturnPointsLogDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.entity.reachad.AdReturnPoints;
import com.doooly.entity.reachad.AdReturnPointsLog;
import com.doooly.entity.reachad.Order;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private OrderService  orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RefundSyncOrderProcessor refundSyncOrderProcessor;

    @Autowired
    private MeituanService meituanService;

    @Test
    public void test() throws Exception{
        String ret = meituanService.easyLogin("8e348852107ccc2b299648cd832dafdc","29352788037","15711667875");
        System.out.println("ret1 = " + ret);
    }




}


