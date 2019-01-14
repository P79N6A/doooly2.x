package com.doooly.business.payment.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.bean.ResultModel;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-08-09
 */
public interface NewPaymentServiceI {

    ResultModel authorize(String businessId);//获取授权token

    ResultModel unifiedorder(JSONObject param);//下单

    ResultModel getTradeInfo(JSONObject param);//获取订单交易信息

    ResultModel integralPay(JSONObject param);//积分混合支付

    ResultModel getPayForm(JSONObject json);

    ResultModel dooolyPayCallback(JSONObject json);

    ResultModel getPayResult(JSONObject json);

    ResultModel queryNewPayResult(JSONObject json);

    ResultModel dooolyPayRefund(JSONObject json);

    ResultModel dooolyApplyPayRefund(JSONObject json);

    ResultModel dooolyRefundCallback(JSONObject json);

    ResultModel refund(JSONObject json);

    ResultModel applyRefund(JSONObject json);

    ResultModel unifiedorderV2(JSONObject json);

    ResultModel getPayFormV2(JSONObject json);
}
