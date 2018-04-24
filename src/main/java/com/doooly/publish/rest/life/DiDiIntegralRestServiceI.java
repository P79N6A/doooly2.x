package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 滴滴积分service
 * @author: qing.zhang
 * @date: 2018-03-06
 */
public interface DiDiIntegralRestServiceI {

    String getDiDiIntegral(JSONObject json);//获取积分信息

    String exchangeIntegral(JSONObject json);//积分兑换接口

    String toExchangeIntegral(JSONObject json);//进入积分兑换页面

    String getCode(JSONObject json);//获取验证码接口

}
