package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 个人中心
 * 
 * @author 赵一鹏
 * @date 2017年5月16日
 * @version 1.0
 */
public interface MyAccountRestServiceI {

	/**
	 * 获取个人信息(提供给IOS的接口,此接口只包含用户的基本信息,不包含积分信息&&可用卡券张数)
	 * 
	 */
	String getAccountList(JSONObject obj);

	/**
	 * 更新用户基本资料(头像、性别、生日、家属关系)
	 * 
	 */
	String updatePersonalData(JSONObject obj);

	/**
	 * 更新用户头像
	 * 
	 */
	String updateAppHeadImageUrl(HttpServletRequest request);

	/**
	 * 个人中心页面获取个人资料(包括积分信息/可用卡券张数)
	 * 
	 */
	String getUserInfo(JSONObject obj);

	/**
	 * 获取家属头像姓名
	 *
	 */
	String getFamilyUserInfo(JSONObject obj);
	/**
	 * 批量获取家属头像姓名
	 *
	 */
	String batchGetFamilyUserInfo(JSONObject obj);
}
