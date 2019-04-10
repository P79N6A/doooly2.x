package com.doooly.business.oneNumber.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

import java.io.UnsupportedEncodingException;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2017-08-28
 */
public interface OneNumberServiceI {
    MessageDataBean getTargetUrl(String userId, String businessId,String targetUrl,String token) throws UnsupportedEncodingException;

    MessageDataBean authorization(JSONObject json);
}
