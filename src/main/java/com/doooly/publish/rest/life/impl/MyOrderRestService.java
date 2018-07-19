package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myorder.dto.OrderResult;
import com.doooly.business.myorder.service.MyOrderServiceI;
import com.doooly.publish.rest.life.MyOrderRestServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Description: 我的订单
 * @author: qing.zhang
 * @date: 2018-07-11
 */
@Path("/myorder")
@Component
public class MyOrderRestService implements MyOrderRestServiceI {

    private static final Logger logger = LoggerFactory.getLogger(MyOrderRestService.class);

    @Autowired
    private MyOrderServiceI myOrderServiceI;

    @POST
    @Path("/getOrders")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getOrders(JSONObject json) {
        OrderResult orderResult = new OrderResult();
        try {
            orderResult = myOrderServiceI.getOrders(json.toJSONString());
            logger.info("查询所有我的订单列表信息返回数据"+orderResult.toJsonString());
        } catch (Exception e) {
            logger.error("获得我的订单列表信息出错", e);
            orderResult.error(orderResult);
        }
        return orderResult.toJsonString();
    }


    @POST
    @Path("/getOrderDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getOrderDetail(JSONObject json) {
        OrderResult orderResult = new OrderResult();
        try {
            orderResult = myOrderServiceI.getOrderDetail(json.toJSONString());
            logger.info("查询所有我的订单详情信息返回数据"+orderResult.toJsonString());
        } catch (Exception e) {
            logger.error("获得我的订单详情信息出错", e);
            orderResult.error(orderResult);
        }
        return orderResult.toJsonString();
    }

}
