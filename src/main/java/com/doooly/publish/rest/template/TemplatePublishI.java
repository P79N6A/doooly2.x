package com.doooly.publish.rest.template;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * 模版管理接口
 * @Author: Mr.Wu
 * @Date: 2019/3/8
 */
public interface TemplatePublishI {
     String getTemplateByType(JSONObject json, @Context HttpServletRequest request);
}
