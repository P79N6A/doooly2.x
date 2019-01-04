package com.doooly.business.ele;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.bean.ResultModel;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2019-01-03
 */
public interface ELMServiceI {

    ResultModel orderAmountPush(JSONObject obj, HttpServletRequest httpServletRequest);

    ResultModel orderStatusPush(JSONObject obj, HttpServletRequest httpServletRequest);
}
