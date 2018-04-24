package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: 报名活动接口
 * @author: qing.zhang
 * @date: 2017-04-25
 */
public interface SignupActivityRestServiceI {

	// 查询活动详情
	String getActivityDetail(JSONObject json);

	// 查询评论详情
	String getAllComment(JSONObject json);

	// 获取报名表单
	String getSignupForm(JSONObject json);

	// 报名
	String signupActivity(JSONObject json);

	// 参赛选手
	String getAllJoinUser(JSONObject json);

	// 点赞
	String clickLike(JSONObject json);

	// 上传评论
	String saveActivityComment(JSONObject json);

    //浏览次数
    String browserCount(JSONObject json);

    //浏览次数
    String getMap(JSONObject json);
}
