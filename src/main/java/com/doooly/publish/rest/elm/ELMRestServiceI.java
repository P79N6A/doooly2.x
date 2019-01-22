package com.doooly.publish.rest.elm;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2019-01-03
 */
public interface ELMRestServiceI {

    //订单金额信息推送 获取金额下未支付订单
    String orderAmountPush(JSONObject obj, HttpServletRequest httpServletRequest);

    //订单状态信息推送 根据状态修改
    String orderStatusPush(JSONObject obj, HttpServletRequest httpServletRequest);

    //落单并支付接口
    String createElmOrderAndPay(HttpServletRequest request,  HttpServletResponse response);

    //支付查询
    String queryElmPayInfo(JSONObject obj, HttpServletRequest httpServletRequest);

    //退款接口
    String elmRefund(JSONObject obj, HttpServletRequest httpServletRequest);

    //退款查询接口
    String queryElmRefundInfo(JSONObject obj, HttpServletRequest httpServletRequest);
}
