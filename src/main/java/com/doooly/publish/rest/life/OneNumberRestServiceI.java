package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * @Description: 兜礼1号通
 * @author: qing.zhang
 * @date: 2017-08-28
 */
public interface OneNumberRestServiceI {
    // 获取跳转链接
    String getTargetUrl(JSONObject obj, @Context HttpServletRequest request);
}
