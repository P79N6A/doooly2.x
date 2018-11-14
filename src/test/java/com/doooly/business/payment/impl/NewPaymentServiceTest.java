package com.doooly.business.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.service.NewPaymentServiceI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-11-13
 */
@RunWith(SpringJUnit4ClassRunner.class)
//配置了@ContextConfiguration注解并使用该注解的locations属性指明spring和配置文件之后，
@ContextConfiguration(locations = {"classpath:conf/mybatis-config.xml",
        "classpath:conf/spring-hessian.xml",
        "classpath:conf/spring-jersey.xml",
        "classpath:conf/spring-mybatis.xml",
        "classpath:conf/spring-mybatis-reachad.xml",
        "classpath:conf/spring-mybatis-reachlife.xml",
        "classpath:conf/spring-mybatis-report.xml",
        "classpath:conf/spring-mybatis-payment.xml",
        "classpath:conf/spring-redis.xml",
        "classpath:conf/spring-task.xml"})
public class NewPaymentServiceTest {

    @Autowired
    private NewPaymentServiceI paymentService;

    @Test
    public void dooolyRefundCallback() throws Exception {

        JSONObject json = new JSONObject();
        json.put("outRefundNo","2018111318035411282213368290");
        json.put("payType","0");
        json.put("createTime","2018-11-13 18:03:54");
        paymentService.dooolyRefundCallback(json);
    }

}