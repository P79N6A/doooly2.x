package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 我的订单重构
 * @author: qing.zhang
 * @date: 2018-07-11
 */
public interface MyOrderRestServiceI {

    // 获得某个用户的所有订单列表信息
    String getOrders(JSONObject obj);

    // 获得某个用户的所有订单详情信息
    String getOrderDetail(JSONObject obj);
    
    /**
     * 根据订单号获取orderReportId
     * 
     * @param obj
     * @return
     */
    String getOrderReportIdByOrderNum(JSONObject obj);
}
