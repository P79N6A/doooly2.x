package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 我的家属接口
 * @author: qing.zhang
 * @date: 2017-07-25
 */
public interface MyFamilyRestServiceI {
    // 查询家庭关系
    String getMyFamily(JSONObject json);
    // 初始化家庭表数据
    String initMyFamily(JSONObject json);
    // 获取家庭列表信息
    String getMyFamilyList(JSONObject json);
    // 确认同意开启积分共享
    String confirmSharePoint(JSONObject json);
}
