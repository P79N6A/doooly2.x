package doooly;

//package com.redis;package com.redis.test;

import com.doooly.business.meituan.MeituanService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessor;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessorFactory;
import com.doooly.business.pay.processor.refundprocessor.RefundSyncOrderProcessor;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.RsaUtil;
import com.doooly.common.util.BeanMapUtil;
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
import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void test2() throws Exception{
        /*Map<String,Object> map = new HashMap<>();
        map.put("entId",15795);
        map.put("appKey","slt");
        map.put("entToken","ehvihi");
        map.put("nounce","wqfmwei15l694cwjqh5pd7z5lv0p49w7dfus");
        map.put("productType","mt_waimai");
        map.put("requestTime","20181128151427");
        map.put("staffNo","ljxtest1121");
        map.put("staffPhoneNo","18842612302");
        map =  BeanMapUtil.sortMapByKey(map);
        String sign = meituanService.getSiginature(map);
        System.out.println("sign = " + sign);*/

        RSAPrivateKey rsaPrivateKey = RsaUtil.loadPrivateKey(MeituanConstants.private_key);
        String signature = RsaUtil.sign("a".getBytes(),rsaPrivateKey);
        //signature = URLEncoder.encode(signature,"utf-8");
        System.out.println(signature);
    }

    //https://m-sqt.meituan.com/open/commonaccess/access
    // ?appKey=slt
    // &entId=15795&entToken=ehvihi
    // &nounce=wqfmwei15l694cwjqh5pd7z5lv0p49w7dfus
    // &productType=mt_waimai&requestTime=20181128151427
    // &staffNo=ljxtest1121&staffPhoneNo=18842612302
    // &signature=h0OitEyE97Puw02U3MT2NWNhEA7IS3%2FkoWq4IK2B%2BiDmLqZIIgl3jLvkydNS3%2B9TWP6j%2BEjljhoFfSbjOMnBTiLj6xMgjYqg2pP%2FUBERRm7VhNggR6Wt1Oxg2NyZf2V05ZH71J%2FhSJylJUO5w6aVSFNrRhk1DH5XLjSLDZ91paI%3D




}


