package com.doooly.publish.rest.template;

import com.alibaba.fastjson.JSONObject;

/**
 * 模版管理接口
 * @Author: Mr.Wu
 * @Date: 2019/3/8
 */
public interface TemplatePublishI {
     String getTemplateByType(JSONObject json);
}
