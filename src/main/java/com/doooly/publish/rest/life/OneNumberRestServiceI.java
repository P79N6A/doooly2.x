package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;


/**
 * @Description: 兜礼1号通
 * @author: qing.zhang
 * @date: 2017-08-28
 */
public interface OneNumberRestServiceI {
    // 获取跳转链接
    String getTargetUrl(JSONObject obj);

    // 上线福特项目导致跳转京东的链接不成功，url后面多了token 和userId,代码回滚， add by pual 2019/1/25
    // String getTargetUrl(JSONObject obj, @Context HttpServletRequest request);
}
