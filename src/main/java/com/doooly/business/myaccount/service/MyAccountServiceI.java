package com.doooly.business.myaccount.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心业务Service
 * 
 * @author yuelou.zhang
 * @version 2017年3月6日
 */
public interface MyAccountServiceI {

	/**
	 * 获取用户个人信息
	 * 
	 * @param userId
	 *            会员id
	 */
	HashMap<String, Object> getAccountListById(String userId);

	/**
	 * 更新用户个人信息
	 * 
	 * @param obj
	 *            JSONObject
	 */
	void updatePersonalData(JSONObject obj);

	/**
	 * 上传图片并生成图片地址
	 * 
	 * @param request
	 * 
	 * @return 返回用户id && 头像地址
	 */
	JSONObject uploadImage(HttpServletRequest request);

	/**
	 * 个人中心页面获取个人资料(包括积分信息/可用卡券张数)
	 * 
	 * @param userId
	 *            会员id
	 * @param businessId
	 *            Global.businessid
	 * @param username
	 *            Global.username
	 * @param password
	 *            Global.password
	 */
	HashMap<String, Object> getUserInfoById(String userId, String businessId, String username,String password);

    HashMap<String,Object> getFamilyUserInfo(String userId);

    HashMap<String,Object> batchGetFamilyUserInfo(List<Map> invitationFamilyList);
}
