package com.doooly.business.myorder.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myorder.dto.OrderResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description: 我的订单单元测试
 * @author: qing.zhang
 * @date: 2018-07-11
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
public class MyOrderServiceTest {

    @Autowired
    private MyOrderService myOrderService;

    @Test
    public void getOrders() throws Exception {
        JSONObject params = new JSONObject();

        JSONObject paramBody = new JSONObject();
        JSONObject paramBodyParam = new JSONObject();
        JSONObject paramBodyPage = new JSONObject();

        // 查询条件
        paramBodyParam.put("userId", "926507");
        paramBodyParam.put("startDate", "");
        paramBodyParam.put("endDate", "");
        paramBodyParam.put("sn", "");
        paramBodyParam.put("type", "");
        paramBodyParam.put("state", "");

        // 分页
        paramBodyPage.put("gotoPage", 1);
        paramBodyPage.put("pageSize", 10);

        paramBody.put("data", paramBodyParam);
        //查询每月总记录不分页
        paramBody.put("ipage", paramBodyPage);
        params.put("body", paramBody);

        OrderResult orders = myOrderService.getOrders(params.toJSONString());
        System.out.println(orders.toJsonString());
    }
    @Test
    public void getOrderDetail() throws Exception {
        JSONObject params = new JSONObject();

        JSONObject paramBody = new JSONObject();
        JSONObject paramBodyParam = new JSONObject();

        // 查询条件
        paramBodyParam.put("userId", "926507");
        paramBodyParam.put("orderReportId", "199206735");

        paramBody.put("data", paramBodyParam);
        params.put("body", paramBody);

        OrderResult orders = myOrderService.getOrderDetail(params.toJSONString());
        System.out.println(orders.toJsonString());
    }

}