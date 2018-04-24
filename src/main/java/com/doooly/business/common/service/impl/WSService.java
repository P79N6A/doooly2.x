package com.doooly.business.common.service.impl;


import org.springframework.stereotype.Service;

import com.doooly.business.common.service.WSServiceI;
import com.doooly.business.common.utils.ReqTransFormatUtils;
import com.doooly.dto.user.GetPayVerifyCodeReq;
import com.doooly.dto.user.GetPayVerifyCodeRes;
import com.doooly.dto.user.GetVerifyCodeReq;
import com.doooly.dto.user.GetVerifyCodeRes;
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
import com.doooly.pay.dto.CheckUserIdentityReq;
import com.doooly.pay.dto.CheckUserIdentityRes;
import com.doooly.pay.dto.CheckUserPhoneAuthorizationReq;
import com.doooly.pay.dto.CheckVerificationCodeReq;
import com.doooly.pay.dto.GetPayUserIntegralVerificationCodeReq;
import com.doooly.pay.dto.GetPayUserIntegralVerificationCodeRes;
import com.doooly.pay.dto.GetVerificationCodeReq;
import com.doooly.pay.dto.GetVerificationCodeRes;
import com.doooly.pay.dto.ModifyUserInfoReq;

/**
 * 
 * @author Albert
 *
 */
@Service
public class WSService implements WSServiceI {

	@Override
	public PointPaymentConnector getConnector() {
		return new PointPaymentConnector();
	}
	
	@Override
	public GetVerifyCodeRes GetVerifyCode(GetVerifyCodeReq req) {
		GetVerificationCodeReq request = ReqTransFormatUtils.toGetVerificationCodeReq(req);
		GetVerificationCodeRes response = getConnector().getVerificationCode(request);
		return ReqTransFormatUtils.toVerifyCodeRes(response);
	}
	
	@Override
	public BasePayRes CheckVerifyCode(String storesId, String account,String verifyCode) {
		CheckVerificationCodeReq request = ReqTransFormatUtils.toCheckVerificationCodeReq(storesId, account,verifyCode);
		BasePayRes response = getConnector().checkVerificationCode(request);
		return response;
	}

	@Override
	public LoginRes checkUserIdentity(LoginReq req) {
		CheckUserIdentityReq request = ReqTransFormatUtils.toCheckUserIdentityReq(req);
		CheckUserIdentityRes response = getConnector().checkUserIdentity(request);
		return ReqTransFormatUtils.toUserIdentityRes(response);
	}

	@Override
	public ModifyMobileRes modifyUserMobile(ModifyMobileReq req) {
		CheckUserPhoneAuthorizationReq request = ReqTransFormatUtils.toCheckUserPhoneAuthorizationReq(req);
		BasePayRes res = getConnector().checkUserPhoneAuthorization(request);
		return ReqTransFormatUtils.toModifyMobileRes(res);
	}

	@Override
	public UserActiveRes modifyUserInfo(UserActiveReq req) {
		ModifyUserInfoReq request = ReqTransFormatUtils.toModifyUserInfoReq(req);
		BasePayRes res = getConnector().ModifyUserInfo(request);
		return ReqTransFormatUtils.toUserActiveRes(res);
	}

	@Override
	public ModifyPwdRes modifyUserInfo(ModifyPwdReq req) {
		ModifyUserInfoReq request = ReqTransFormatUtils.toModifyUserInfoReq(req);
		BasePayRes res = getConnector().ModifyUserInfo(request);
		return ReqTransFormatUtils.toModifyPwdRes(res);
	}

	@Override
	public GetPayVerifyCodeRes getIntegralPayVerifyCode(GetPayVerifyCodeReq req) {
		GetPayUserIntegralVerificationCodeReq request = ReqTransFormatUtils.toGetPayUserIntegralVerificationCodeReq(req);
		GetPayUserIntegralVerificationCodeRes res = getConnector().GetPayUserIntegralVerificationCode(request);
		return ReqTransFormatUtils.toGetPayVerifyCodeRes(res);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
