package com.doooly.publish.rest.activity;

import com.alibaba.fastjson.JSONObject;

/**
 * 运营活动接口
 * @Author: Mr.Wu
 * @Date: 2018/12/30
 */
public interface ActivityPublishI {

    /**
     * 生成活动的兑换码||抽奖码
     * @param obj
     * @return
     */
    String generateKey(JSONObject obj);

    /**
     * 参加活动
     * @param obj
     * @return
     */
    String campaignByPhone(JSONObject obj);

    /**
     * 获得中奖纪录
     * @param obj
     * @return
     */
    String getWinningRecord(JSONObject obj);
}
