package com.doooly.business.myorder.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myorder.dto.OrderResult;
import com.doooly.dto.common.MessageDataBean;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-07-11
 */
public interface MyOrderServiceI {

    OrderResult getOrders(String paramBody);

    OrderResult getOrderDetail(String paramBody);
    
    long getOrderReportIdByOrderNum(String orderNum);

    int orderBelongOneActivity(String activityName,String orderNum);

    MessageDataBean getLiftOrder(JSONObject s);
}
