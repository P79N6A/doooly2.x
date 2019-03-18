package com.doooly.publish.rest.common;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * 一些公共接口
 * @Author: Mr.Wu
 * @Date: 2019/3/18
 */
public interface CommonPublishI {

    /**
     * 清除用户提示
     * @param json
     * @return
     */
    public String cannelUserFlag(JSONObject json, @Context HttpServletRequest request);
}
