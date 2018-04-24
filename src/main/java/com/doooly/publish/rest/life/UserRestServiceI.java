package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.user.CheckActiveCodeReq;
import com.doooly.dto.user.LoginReq;
import com.doooly.dto.user.LogoutReq;
import com.doooly.dto.user.ModifyMobileReq;
import com.doooly.dto.user.ModifyPwdReq;
import com.doooly.dto.user.UserActiveReq;

/**
 * 
 * @author Albert
 *
 */
public interface UserRestServiceI {

	/**
	 * 会员登录接口
	 * 
	 * @param req
	 * @return
	 */
	public String login(LoginReq req);

	/**
	 * 会员登出接口
	 * 
	 * @param req
	 * @return
	 */
	public String logOut(LogoutReq req);

	/**
	 * 验证激活码接口
	 * 
	 * @param req
	 * @return
	 */
	public String checkActiveCode(CheckActiveCodeReq req);

	/**
	 * 会员激活接口
	 * 
	 * @param req
	 * @return
	 */
	public String userActive(UserActiveReq req);

	/**
	 * 修改手机号接口
	 * 
	 * @param req
	 * @return
	 */
	public String userModifyMobile(ModifyMobileReq req);

	/**
	 * 修改密码接口
	 * 
	 * @param req
	 * @return
	 */
	public String userModifyPwd(ModifyPwdReq req);

	/**
	 * 用户登录操作
	 * 
	 * @param paramJson
	 * @return
	 */
	public JSONObject userLogin(JSONObject paramJson);

	/**
	 * APP用户登录验证,token操作
	 * 
	 * @param paramJson
	 * @return
	 */
	public JSONObject userValidateLogin(JSONObject paramJson);

	/**
	 * 用户登出操作
	 * 
	 * @param paramJson
	 * @return
	 */
	public String userLogout(JSONObject paramJson);

	JSONObject addActiveUser(JSONObject userJson);

	/**
	 * 微信端免登陆初始化用户信息
	 * 
	 * @param paramJson
	 * @return
	 */
	public String getWechatUserInitInfo(JSONObject paramJson);
}
