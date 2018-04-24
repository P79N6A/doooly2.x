package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 中油投票活动
 * @author: qing.zhang
 * @date: 2017-05-31
 */
public interface VoteRestServiceI {
    // 查询活动详情
    String getActivityDetail(JSONObject json);

    // 查询首页选项详情
    String findVoteOptionList(JSONObject json);

    //浏览次数
    String browserCount(JSONObject json);

    //投票
    String clickVote(JSONObject json);

    //投票总数
    String findVoteCountByOpenId(JSONObject json);

    //投票总数
    String findVoteOption(JSONObject json);
}
