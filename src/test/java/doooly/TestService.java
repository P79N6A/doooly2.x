package doooly;

//package com.redis;package com.redis.test;

import com.business.common.util.HttpClientUtil;
import com.doooly.business.meituan.MeituanOrderService;
import com.doooly.business.meituan.MeituanService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.pay.processor.refundprocessor.RefundSyncOrderProcessor;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.meituan.EncryptUtil;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.MeituanProductTypeEnum;
import com.doooly.common.meituan.OrderStatusEnum;
import com.doooly.dao.reachad.*;
import com.doooly.entity.meituan.Order;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdUser;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.reach.redis.utils.GsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

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

        /*RSAPrivateKey rsaPrivateKey = RsaUtil.loadPrivateKey(MeituanConstants.private_key);
        String signature = RsaUtil.sign("a".getBytes(),rsaPrivateKey);*/
        //signature = URLEncoder.encode(signature,"utf-8");
       /* System.out.println(signature);*/

    }

    //https://m-sqt.meituan.com/open/commonaccess/access
    // ?appKey=slt
    // &entId=15795&entToken=ehvihi
    // &nounce=wqfmwei15l694cwjqh5pd7z5lv0p49w7dfus
    // &productType=mt_waimai&requestTime=20181128151427
    // &staffNo=ljxtest1121&staffPhoneNo=18842612302
    // &signature=h0OitEyE97Puw02U3MT2NWNhEA7IS3%2FkoWq4IK2B%2BiDmLqZIIgl3jLvkydNS3%2B9TWP6j%2BEjljhoFfSbjOMnBTiLj6xMgjYqg2pP%2FUBERRm7VhNggR6Wt1Oxg2NyZf2V05ZH71J%2FhSJylJUO5w6aVSFNrRhk1DH5XLjSLDZ91paI%3D

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AdBusinessDao adBusinessDao;

    @Autowired
    private AdUserDao adUserDao;

    @Test
    public void test3() {

        /*PageHelper.startPage(2,6);
        AdUser adUser = new AdUser();
        long x = adUserDao.getIdByPhoneOrCard("");*/

        AdBusiness adBusiness = adBusinessDao.getByBusinessId(MeituanConstants.meituan_bussinesss_serial);
        if (adBusiness != null && !"0".equals(adBusiness.getDataSynchronization())) {
            System.out.println(11);
        } else {
            System.out.println(22);
        }

    }

    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;

    @Test
    public void test4() {
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId("9486");
        System.out.println(new Gson().toJson(adBusinessExpandInfo));
    }


    /**
     * {
         "sign": "hWonoCrJj4xf4UftS1OuqQ==",
         "ts": 1547611539,
         "method": "trade.third.pay.callback",
         "tradeNo": 393354663735396,
         "sqtOrderId": 393354663256156,
         "serialNum": "3VFKMYDWM4",
         "thirdPayOrderId": "393354663256156_3VFKMYDWM4",
         "payAmount": "1",
         "payTime": "2019-01-16 12:05:39"
         }
     */

    @Autowired
    private MeituanOrderService meituanOrderService;


    @Test
    public void test1() {
        Map<String,Object> params = new HashMap<>();
        params.put("token", "hWonoCrJj4xf4UftS1OuqQ==");
        params.put("version",MeituanConstants.version);
        Map<String,Object> contentParams = new HashMap<>();
        contentParams.put("orderSN","3Q2NCPFHPS40GNTXIL5Y");
        contentParams.put("amount","0.01");
        contentParams.put("sign", "hWonoCrJj4xf4UftS1OuqQ==");
        contentParams.put("ts",new Date().getTime()/1000);
        String content = "";
        try {
            content = EncryptUtil.aesEncrypt(GsonUtils.toString(contentParams),"30Barz8IDtwtBekmhV5AvA==");
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("content", content);
        System.out.println(GsonUtils.son.toJson(params));
        String ret = HttpClientUtil.doPost("http://api-sqt.meituan.com/trade/third/pay/callback",GsonUtils.toString(params));
        System.out.println(ret);

        /*Map<String,Object> params = new HashMap<>();
        params.put("sign","hWonoCrJj4xf4UftS1OuqQ==");
        params.put("ts",new Date().getTime()/1000);
        params.put("trade.third.pay.callback","trade.third.pay.callback");
        params.put("tradeNo","2019022514270224671866435434");
        params.put("sqtOrderId",)*/
    }



    @Test
    public void test5() {
        try {
            Order order = meituanOrderService.queryOrderByOrderSN("40LFS2QY08");
            System.out.println(GsonUtils.son.toJson(order));

           /* Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,-2);
            *//*String s = DateUtils.formatDate(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");
            System.out.println(s);*//*
            long fromTime = calendar.getTimeInMillis();
            long toTime = new Date().getTime();
            List<Order> orderList = meituanOrderService.queryOrderByTimeRange(fromTime,toTime, OrderStatusEnum.FULLY_REFUNDED);
            System.out.println(new Gson().toJson(orderList));*/
        } catch (Exception e) {
            e.printStackTrace();
        }


        String ret = "AoNtlH3V1kkRa8vQyc9Tm84ffYGpL1buPLfXWGyh7xsqkfOF7QHdH-WqhHANMBgH-CrVzc0Pd6-Pi2gjNEROK3_uxUPbjOdVJlU5RuRqWx97_ys0S5UtsNFrBqsTtH-MnZeV8ebBOwqM984uH6m_oKKlKyEWsYkjaiDUwKw72PnQIX75kYOcT2OR7gFe-40NmOs_7OdP02Dh_7lGR90n7ccf9vv7Datxk6iGgZuYNvFTnaqAR5ozhQYFg4tcGIIMw8bWkxXLQv0UkoMGBMCJb4My939lkemv-zU-3vQLjpQ";
        String s = "";
        try {
            s = EncryptUtil.aesDecrypt(ret,MeituanConstants.aesKey_prod);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

}


