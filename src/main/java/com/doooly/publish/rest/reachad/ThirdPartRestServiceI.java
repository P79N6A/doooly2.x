package com.doooly.publish.rest.reachad;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 第三方接口通用
 * @author: qing.zhang
 * @date: 2019-01-15
 */
public interface ThirdPartRestServiceI {

    String getGroupInfo(JSONObject json, HttpServletRequest request);

    String thirdLogin(JSONObject json, HttpServletRequest request);

}
