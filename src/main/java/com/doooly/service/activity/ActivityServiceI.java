package com.doooly.service.activity;

import com.alibaba.fastjson.JSONObject;

/**
 * 活动服务接口
 * @Author: Mr.Wu
 * @Date: 2018/12/30
 */
public interface ActivityServiceI {
    /**
     * 生成活动的兑换码||抽奖码
     * @param activityId: 活动唯一标识
     * @param keyNumber: 生成数量
     * @return
     */
    String generateKey(String activityId, Integer keyNumber);

    /**
     * 通过手机号码参加活动
     * @return
     */
    String campaignByPhone(JSONObject obj);

    /**
     * 获得活动中奖纪录
     * @param activityId
     * @param rowNum
     * @return
     */
    String getWinningRecord(String activityId, Integer rowNum);
}
