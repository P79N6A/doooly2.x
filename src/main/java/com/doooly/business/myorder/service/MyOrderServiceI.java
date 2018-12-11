package com.doooly.business.myorder.service;

import com.doooly.business.myorder.dto.OrderResult;

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
}
