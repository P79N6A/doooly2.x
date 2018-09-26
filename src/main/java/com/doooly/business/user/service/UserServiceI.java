package com.doooly.business.user.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.user.CheckActiveCodeReq;
import com.doooly.dto.user.CheckActiveCodeRes;
import com.doooly.dto.user.CheckVerifyCodeReq;
import com.doooly.dto.user.CheckVerifyCodeRes;
import com.doooly.dto.user.GetVerifyCodeReq;
import com.doooly.dto.user.GetVerifyCodeRes;
import com.doooly.dto.user.LoginReq;
import com.doooly.dto.user.LoginRes;
import com.doooly.dto.user.LogoutReq;
import com.doooly.dto.user.LogoutRes;
import com.doooly.dto.user.ModifyMobileReq;
import com.doooly.dto.user.ModifyMobileRes;
import com.doooly.dto.user.ModifyPwdReq;
import com.doooly.dto.user.ModifyPwdRes;
import com.doooly.dto.user.UserActiveNewReq;
import com.doooly.dto.user.UserActiveReq;
import com.doooly.dto.user.UserActiveRes;
import com.doooly.entity.reachad.AdUser;

/**
 * 会员服务接口(主)
 * 
 * @author 赵清江
 * @date 2016-06-28
 * @version 1.0
 */
public interface UserServiceI {

	static final int ACCOUNT_LOGIN = 0;

	static final int MOBILE_LOGIN = 1;

	/**
	 * 处理会员登录
	 * 
	 * @param LoginReq
	 * @return UserIdentityRes
	 */
	public LoginRes manageLogin(LoginReq req);

	/**
	 * 处理会员登出(注销)
	 * 
	 * @param req
	 * @return
	 */
	public LogoutRes manageLogout(LogoutReq req);

	/**
	 * 获取验证码
	 * 
	 * @param GetVerifyCodeReq
	 *            req
	 * @return VerifyCodeRes
	 */
	public GetVerifyCodeRes getVerifyCode(GetVerifyCodeReq req);

	/**
	 * 验证验证码
	 * 
	 * @param req
	 * @return
	 */
	public CheckVerifyCodeRes checkVerifyCode(CheckVerifyCodeReq req);

	/**
	 * 验证激活码
	 * 
	 * @param req
	 * @return
	 */
	public CheckActiveCodeRes checkActiveCode(CheckActiveCodeReq req);

	/**
	 * 修改手机号
	 * 
	 * @param req
	 * @return
	 */
	public ModifyMobileRes modifyMobile(ModifyMobileReq req);

	/**
	 * 修改密码
	 * 
	 * @param req
	 * @return
	 */
	public ModifyPwdRes modifyPassword(ModifyPwdReq req);

	/**
	 * 会员激活
	 * 
	 * @param req
	 * @return
	 */
	public UserActiveRes manageUserActive(UserActiveReq req);

	/**
	 * 会员激活 （新兜礼）
	 * 
	 * @param req
	 * @return
	 */
	public BaseRes managerUserActiveNew(UserActiveNewReq req);

	/**
	 * 根据手机号/企业名称-新增会员信息
	 * 
	 * @author hutao
	 * @date 2017年7月25日
	 * @version V1.0
	 */
	AdUser addUser(AdUser user, String groupName);

	/**
	 * 会员登录操作 （新兜礼）
	 * 
	 * @param paramJson
	 * @return
	 */
	public JSONObject userLogin(JSONObject paramJson) throws Exception;

	/**
	 * APP会员登录验证,token操作 （新兜礼）
	 * 
	 * @param paramJson
	 * @return
	 */
	public JSONObject userValidateLogin(JSONObject paramJson) throws Exception;

	/**
	 * 会员登出操作 （新兜礼）
	 * 
	 * @param paramJson
	 * @return
	 */
	public MessageDataBean userLogout(JSONObject paramJson, HttpServletRequest request) throws Exception;

	/**
	 * 微信免登陆,初始化微信token
	 * 
	 * @param paramJson
	 * @return
	 */
	public BaseRes<JSONObject> getWechatUserInitInfo(JSONObject paramJson) throws Exception;

	/**
	 * 集团企业快捷登录兜礼安全性验证 （实现快捷登录）
	 * 
	 * @author hutao
	 * @date 创建时间：2017年10月19日 下午6:22:15
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	BaseRes<String> checkQuickLogin(String groupId, String mobile, String name, String groupName, Long actionTime,
			String sign);

	BaseRes<JSONObject> batchUpdateUser(JSONObject reqJson);

	/**
	 * 保存用户手机号修改记录
	 */
	BaseRes<JSONObject> saveTelephoneChange(JSONObject reqJson);
	/**
	 * 通过手机号注销用户
	 * 并返回该用户ID
	* @author  hutao 
	* @date 创建时间：2018年9月25日 下午6:07:15 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return 用户ID
	 */
	Long cancelUserByphoneNo(String phoneNo);

	// public void scheduleUpdateUser();
	//
	// public void initializeScheduleUpdateUser();
}
