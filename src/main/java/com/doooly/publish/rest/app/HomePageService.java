package com.doooly.publish.rest.app;

import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.core.Response;

/**
 * Created by 王晨宇 on 2018/2/12.
 */
public interface HomePageService {
	/**
	 * 获取兜礼首页，会员概要信息
	 */
	Response getUserProfile(JSONObject json);
	Response getUserProfileV2_2(JSONObject json);
	/**
	 * 查询用户的新手引导完成进度
	 */
	Response getUserAppGuideFlow(JSONObject json);
	/**
	 * 后端数据库中保存，用户《权益，或积分》新手引导完成
	 */
	Response insertOrUpdateUserGuideFinish(JSONObject json);
	/**
	 * 获取企业闪屏（企业app开机启动图片）
	 */
	Response getSplashScreen(String groupId);
}
