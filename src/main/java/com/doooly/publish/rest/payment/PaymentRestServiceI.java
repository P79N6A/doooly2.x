package com.doooly.publish.rest.payment;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-08-09
 */
public interface PaymentRestServiceI {

    // 获取授权信息
    String authorize(JSONObject obj);

    // 下单
    String unifiedorder(JSONObject obj, HttpServletRequest httpServletRequest);
    // 下单 v2
    String unifiedorderV2(JSONObject obj, HttpServletRequest httpServletRequest);

    // 获取支付参数
    String getPayForm(JSONObject obj);

    // 获取支付参数
    String getPayFormV2(JSONObject obj);

    // 获取支付信息
    String getTradeInfo(JSONObject obj, HttpServletRequest httpServletRequest);

    // 获取支付信息
    String integralPay(JSONObject obj, HttpServletRequest httpServletRequest);
    // 获取支付信息
    String integralPayV2(JSONObject obj, HttpServletRequest httpServletRequest);

    // 积分支付回调url
    String dooolyPayCallback(JSONObject obj, HttpServletRequest httpServletRequest);

    String dooolyPayCallbackV2(JSONObject obj, HttpServletRequest httpServletRequest);

    // 兜礼支付查询
    String getPayResult(JSONObject obj, HttpServletRequest httpServletRequest);
    // 兜礼支付查询
    String getPayResultV2(JSONObject obj, HttpServletRequest httpServletRequest);

    // 兜礼退款回调
    String dooolyRefundCallback(JSONObject obj, HttpServletRequest httpServletRequest);

    // 兜礼退款
    String refund(JSONObject obj, HttpServletRequest httpServletRequest);
    // 兜礼申请退款
    String applyRefund(JSONObject obj, HttpServletRequest httpServletRequest);
}
