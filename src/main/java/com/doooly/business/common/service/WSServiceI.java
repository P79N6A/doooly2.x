package com.doooly.business.common.service;

import com.doooly.dto.user.LoginReq;
import com.doooly.dto.user.LoginRes;
import com.doooly.dto.user.ModifyMobileReq;
import com.doooly.dto.user.ModifyMobileRes;
import com.doooly.dto.user.ModifyPwdReq;
import com.doooly.dto.user.ModifyPwdRes;
import com.doooly.dto.user.UserActiveReq;
import com.doooly.dto.user.UserActiveRes;
import com.doooly.pay.connector.PointPaymentConnector;
import com.doooly.pay.dto.BasePayRes;
import com.doooly.pay.dto.ModifyUserInfoReq;
import com.doooly.common.dto.BaseReq;
import com.doooly.dto.user.GetPayVerifyCodeReq;
import com.doooly.dto.user.GetPayVerifyCodeRes;
import com.doooly.dto.user.GetVerifyCodeReq;
import com.doooly.dto.user.GetVerifyCodeRes;

/**
 * 公用Web Service接口封装
 * @author Albert
 *
 */
public interface WSServiceI {
	/**
	 * 获取连接器
	 * @return
	 */
	public PointPaymentConnector getConnector();
	
	/**
	 * 获取短信验证码或条形码和二维码
	 * @param req
	 * @return
	 */
	public GetVerifyCodeRes GetVerifyCode(GetVerifyCodeReq req);
	/**
	 * 验证验证码
	 * @param storesId
	 * @param account
	 * @return
	 */
	public BasePayRes CheckVerifyCode(String storesId,String account,String verifyCode);
	/**
	 * 用户身份验证
	 * @param req
	 * @return
	 */
	public LoginRes checkUserIdentity(LoginReq req);
	/**
	 * 修改用户手机号
	 * @param req
	 * @return
	 */
	public ModifyMobileRes modifyUserMobile(ModifyMobileReq req);
	/**
	 * 修改用户绑定手机号和登录密码
	 * @param req
	 * @return
	 */
	public UserActiveRes modifyUserInfo(UserActiveReq req);
	/**
	 * 修改用户登录密码
	 * @param req
	 * @return
	 */
	public ModifyPwdRes modifyUserInfo(ModifyPwdReq req);
	
	/**
	 * 获取积分支付验证码(生成18位验证码的二维码和条形码,即被扫码)
	 * @param req
	 * @return
	 */
	public GetPayVerifyCodeRes getIntegralPayVerifyCode(GetPayVerifyCodeReq req);
}
