package com.doooly.publish.rest.elm;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

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
}
