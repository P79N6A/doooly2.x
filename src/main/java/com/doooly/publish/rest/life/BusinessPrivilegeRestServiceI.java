package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 商户特权开通Controller
 * @author: qing.zhang
 * @date: 2017-09-12
 */
public interface BusinessPrivilegeRestServiceI {
    // 查询活动详情
    String getActivityDetail(JSONObject json);
    // 活动报名详情
    String apply(JSONObject json);
}
