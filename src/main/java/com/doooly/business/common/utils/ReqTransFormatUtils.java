package com.doooly.business.common.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


import com.doooly.common.constants.Constants.ResponseCode;
import com.doooly.common.util.ProjectRootPathUtils;
import com.doooly.dto.user.LoginReq;
import com.doooly.dto.user.LoginRes;
import com.doooly.dto.user.ModifyMobileReq;
import com.doooly.dto.user.ModifyMobileRes;
import com.doooly.dto.user.ModifyPwdReq;
import com.doooly.dto.user.ModifyPwdRes;
import com.doooly.dto.user.UserActiveReq;
import com.doooly.dto.user.UserActiveRes;
import com.doooly.dto.user.GetPayVerifyCodeReq;
import com.doooly.dto.user.GetPayVerifyCodeRes;
import com.doooly.dto.user.GetVerifyCodeReq;
import com.doooly.dto.user.GetVerifyCodeRes;
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
 * 调用Web Service接口格式转换工具类
 * @author Albert
 *
 */
public class ReqTransFormatUtils {
	
	public static final int SUCCESS = 0;
	public static final int Failed = 1001;
	public static final int BUSINESS_OR_STORE_ID_INVALID = 1002;
	public static final int FORBIDDEN = 1003;
	public static final int UNKOWN_ERROR = 1004;
	public static final int UPPER_LIMIT = 1005;
	public static final int VERIFY_CODE_ERROR = 1006;
	public static final int TRADE_CODE_ERROR = 1007;
	public static final int TRADE_REVERSAL = 1008;
	
	private static final String STOREID_ANDROID = "Android";
	
	private static final String STOREID_WECHAT = "WX_Reachlife";
	
	private static final String STOREID_IOS = "IOS";

	private static String HTTPS_URL;
	
	private static String DEFUALT_BUSINESSID;
	
	private static String DEFAULT_STOREID;
	
	private static String DEFAULT_USERNAME;
	
	private static String DEFAULT_PASSWORD;
	
	private static String SSL_TRUST_STORE = ProjectRootPathUtils.getRootPath() + "/SSL/client.truststore";
	
	private static String SSL_KEY_STORE =  ProjectRootPathUtils.getRootPath() + "/SSL/liketry.p12";
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("prop/pointpay");
		HTTPS_URL = bundle.getString("pay.https");
		DEFUALT_BUSINESSID = bundle.getString("pay.businessId");
		DEFAULT_STOREID = bundle.getString("pay.storesId");
		DEFAULT_USERNAME = bundle.getString("pay.username");
		DEFAULT_PASSWORD = bundle.getString("pay.password");
	}
	
	/**
	 * 获取短信验证码请求转换
	 * @param req
	 * @return
	 */
	public static GetVerificationCodeReq toGetVerificationCodeReq(GetVerifyCodeReq req){
		GetVerificationCodeReq wsReq = new GetVerificationCodeReq();
		wsReq.setBusinessId(DEFUALT_BUSINESSID);
		wsReq.setHttpsUrl(HTTPS_URL);
		wsReq.setClientStorePath(SSL_TRUST_STORE);
		wsReq.setLiketryp12Path(SSL_KEY_STORE);
		
		if (req.getStoresId() == null || req.getStoresId().equals("")) {
			wsReq.setStoresId(DEFAULT_STOREID);
		} else {
			wsReq.setStoresId(req.getStoresId());
		}
		
		wsReq.setCardNumber(req.getAccount());
		wsReq.setIsCheck(req.getNeedCheck());
		
		return wsReq;
	}
	/**
	 * 获取短信验证码回应转换
	 * @param res
	 * @return
	 */
	public static GetVerifyCodeRes toVerifyCodeRes(GetVerificationCodeRes res){
		GetVerifyCodeRes response = new GetVerifyCodeRes();
		if (res.getCode() == SUCCESS) {
			response.setCode(ResponseCode.SUCCESS.getCode());
			response.setMsg(ResponseCode.SUCCESS.getDesc());
		} else {
			response.setCode(ResponseCode.ACCOUNT_GET_VERIFY_CODE_FAILURE.getCode());
			response.setMsg(ResponseCode.ACCOUNT_GET_VERIFY_CODE_FAILURE.getDesc());
		}
		if (res.getVerificationCode() != null) {
			response.setVerificationCode(res.getVerificationCode());
		}
		if (res.getVerificationCode18() != null) {
			response.setVerificationCode18(res.getVerificationCode18());
		}
		return response;
	}
	/**
	 * 验证验证码请求转换
	 * @param storesId
	 * @param account
	 * @return
	 */
	public static CheckVerificationCodeReq toCheckVerificationCodeReq(String storesId,String account,String verifyCode){
		CheckVerificationCodeReq request = new CheckVerificationCodeReq();
		if (storesId == null || storesId.equals("")) {
			request.setStoresId(DEFAULT_STOREID);
		} else {
			request.setStoresId(storesId);
		}
		request.setCardNumber(account);
		request.setVerificationCode(verifyCode);
		request.setBusinessId(DEFUALT_BUSINESSID);
		request.setHttpsUrl(HTTPS_URL);
		request.setClientStorePath(SSL_TRUST_STORE);
		request.setLiketryp12Path(SSL_KEY_STORE);
		return request;
	}
	
	
	/**
	 * 用户身份验证请求转换
	 * @param req
	 * @return
	 */
	public static CheckUserIdentityReq toCheckUserIdentityReq(LoginReq req){
		CheckUserIdentityReq request = new CheckUserIdentityReq();
		request.setBusinessId(DEFUALT_BUSINESSID);
		request.setStoresId(DEFAULT_STOREID);
		request.setHttpsUrl(HTTPS_URL);
		request.setClientStorePath(SSL_TRUST_STORE);
		request.setLiketryp12Path(SSL_KEY_STORE);
		request.setCardNumber(req.getAccount());
		request.setPassword(req.getPassword());
		request.setIsCheck(0);
		return request;
	}
	/**
	 * 用户身份验证回应转换
	 * @param res
	 * @return
	 */
	public static LoginRes toUserIdentityRes(CheckUserIdentityRes res){
		LoginRes response = new LoginRes();
		String code = null;
		String msg = null;
		switch (res.getCode()) {
		case SUCCESS:
			code = ResponseCode.SUCCESS.getCode();
			msg = ResponseCode.SUCCESS.getDesc();
			break;
		case Failed:
			code = ResponseCode.ACCOUNT_NOT_ACTIVE_FAILURE.getCode();
			msg = ResponseCode.ACCOUNT_NOT_ACTIVE_FAILURE.getDesc();
			break;
		case FORBIDDEN:
			code = ResponseCode.ACCOUNT_FORBIDDEN_FAILURE.getCode();
			msg = ResponseCode.ACCOUNT_FORBIDDEN_FAILURE.getDesc();
			break;
		case UNKOWN_ERROR:
			code = ResponseCode.SERVER_UNKOWN_ERROR.getCode();
			msg = ResponseCode.SERVER_UNKOWN_ERROR.getDesc();
		default:
			System.out.println("用户身份验证信息: " + res.getInfo());
			return null;
		}
		System.out.println("用户身份验证信息: " + res.getInfo());
		response.setCode(code);
		response.setMsg(msg);
		response.setCardNumber(res.getCardNumber());
		response.setMobile(res.getTelephone());
		return response;
	}
	
	/**
	 * 用户手机号修改请求转换
	 * @param req
	 * @return
	 */
	public static CheckUserPhoneAuthorizationReq toCheckUserPhoneAuthorizationReq(ModifyMobileReq req){
		CheckUserPhoneAuthorizationReq request = new CheckUserPhoneAuthorizationReq();
		request.setBusinessId(DEFUALT_BUSINESSID);
		request.setStoresId(DEFAULT_STOREID);
		request.setHttpsUrl(HTTPS_URL);
		request.setClientStorePath(SSL_TRUST_STORE);
		request.setLiketryp12Path(SSL_KEY_STORE);
		request.setCardNumber(req.getCardNumber());
		request.setTelephone(req.getNewMobile());
		request.setVerificationCode(req.getVerifyCode());
		return request;
	}
	/**
	 * 用户手机号修改响应转换
	 * @param res
	 * @return
	 */
	public static ModifyMobileRes toModifyMobileRes(BasePayRes res){
		ModifyMobileRes response = new ModifyMobileRes();
		switch (res.getCode()) {
		case SUCCESS:
			response.setSuccessBaseResponse();
			break;
		case Failed:
			response.setCode(ResponseCode.SERVER_INTERNAL_ERROR.getCode());
			response.setMsg(ResponseCode.SERVER_INTERNAL_ERROR.getDesc());
			break;
		case FORBIDDEN:
			response.setCode(ResponseCode.ACCOUNT_FORBIDDEN_FAILURE.getCode());
			response.setMsg(ResponseCode.ACCOUNT_FORBIDDEN_FAILURE.getDesc());
			break;
		case VERIFY_CODE_ERROR:
			response.setCode(ResponseCode.ACCOUNT_VERIFY_CODE_FAILURE.getCode());
			response.setMsg(ResponseCode.ACCOUNT_VERIFY_CODE_FAILURE.getDesc());
			break;
		default:
			response.setCode(ResponseCode.SERVER_UNKOWN_ERROR.getCode());
			response.setMsg(ResponseCode.SERVER_UNKOWN_ERROR.getDesc());
			break;
		}
		return response;
	}
	/**
	 * 修改用户信息(用户手机和登录密码)请求转换
	 * @param req
	 * @return
	 */
	public static ModifyUserInfoReq toModifyUserInfoReq(UserActiveReq req){
		ModifyUserInfoReq request = new ModifyUserInfoReq();
		request.setBusinessId(DEFUALT_BUSINESSID);
		request.setStoresId(DEFAULT_STOREID);
		request.setHttpsUrl(HTTPS_URL);
		request.setClientStorePath(SSL_TRUST_STORE);
		request.setLiketryp12Path(SSL_KEY_STORE);
		request.setUsername(DEFAULT_USERNAME);
		request.setPassword(DEFAULT_PASSWORD);
		request.setCardNumber(req.getCardNumber());
		request.setTelephone(req.getNewMobile());
		request.setLoginPassword(req.getNewPwd());
		request.setPayPassword("");
		request.setLineCredit("");
		request.setIsPayPassword(2);
		return request;
	}
	/**
	 * 修改用户信息(用户手机和登录密码)响应转换
	 * @param res
	 * @return
	 */
	public static UserActiveRes toUserActiveRes(BasePayRes res){
		UserActiveRes response = new UserActiveRes();
		switch (res.getCode()) {
		case SUCCESS:
			response.setCode(ResponseCode.SUCCESS.getCode());
			response.setMsg(ResponseCode.SUCCESS.getDesc());
			break;
		case Failed:
			response.setCode(ResponseCode.ACCOUNT_MOBILE_ALREADY_BINDED.getCode());
			response.setMsg(ResponseCode.ACCOUNT_MOBILE_ALREADY_BINDED.getDesc());
			break;
		default:
			response.setServerUnkownErrorResponse();
			break;
		}
		return response;
	}
	/**
	 * 修改用户信息(登录密码)请求转换
	 * @param req
	 * @return
	 */
	public static ModifyUserInfoReq toModifyUserInfoReq(ModifyPwdReq req){
		ModifyUserInfoReq request = new ModifyUserInfoReq();
		request.setBusinessId(DEFUALT_BUSINESSID);
		request.setStoresId(DEFAULT_STOREID);
		request.setHttpsUrl(HTTPS_URL);
		request.setClientStorePath(SSL_TRUST_STORE);
		request.setLiketryp12Path(SSL_KEY_STORE);
		request.setUsername(DEFAULT_USERNAME);
		request.setPassword(DEFAULT_PASSWORD);
		request.setCardNumber(req.getMobile());
		request.setTelephone("");
		request.setLoginPassword(req.getNewPwd());
		request.setPayPassword("");
		request.setLineCredit("");
		request.setIsPayPassword(2);
		return request;
	}
	/**
	 * 修改用户信息(登录密码)响应转换
	 * @param res
	 * @return
	 */
	public static ModifyPwdRes toModifyPwdRes(BasePayRes res){
		ModifyPwdRes response = new ModifyPwdRes();
		System.out.println(res.getCode() + "    " + res.getInfo());
		switch (res.getCode()) {
		case SUCCESS:
			response.setCode(ResponseCode.SUCCESS.getCode());
			response.setMsg(ResponseCode.SUCCESS.getDesc());
			break;
		default:
			response.setServerUnkownErrorResponse();
			break;
		}
		return response;
	}
	
	/**
	 * 获取积分支付验证码请求转换
	 * @param req
	 * @return
	 */
	public static GetPayUserIntegralVerificationCodeReq toGetPayUserIntegralVerificationCodeReq(GetPayVerifyCodeReq req){
		GetPayUserIntegralVerificationCodeReq request = new GetPayUserIntegralVerificationCodeReq();
		request.setBusinessId(DEFUALT_BUSINESSID);
		request.setHttpsUrl(HTTPS_URL);
		request.setClientStorePath(SSL_TRUST_STORE);
		request.setLiketryp12Path(SSL_KEY_STORE);
		
		if (req.getStoresId() == null || req.getStoresId() == "") {
			request.setStoresId(DEFAULT_STOREID);
		} else {
			request.setStoresId(req.getStoresId());
		}
		request.setCardNumber(req.getCardNumber());
		request.setAmount("0");
		request.setOrderNumber("000000");
		request.setSerialNumber("000000");
		request.setOrderDate((new SimpleDateFormat("yyyyMMdd HH:mm:ss")).format(new Date()));
		request.setOrderDetail(null);
		request.setPrice("0");
		return request;
	}
	
	public static GetPayVerifyCodeRes toGetPayVerifyCodeRes(GetPayUserIntegralVerificationCodeRes res){
		GetPayVerifyCodeRes response = new GetPayVerifyCodeRes();
		System.out.println(res.getCode() + "          " +res.getInfo());
		switch (res.getCode()) {
		case SUCCESS:
			response.setCode(ResponseCode.SUCCESS.getCode());
			response.setMsg(ResponseCode.SUCCESS.getDesc());
			response.setVerifyCode18(res.getPayVerificationCode18());
			response.setIntegral(res.getIntegral());
			break;
		case Failed:
			response.setCode(ResponseCode.ACCOUNT_MOBILE_ALREADY_BINDED.getCode());
			response.setMsg(ResponseCode.ACCOUNT_MOBILE_ALREADY_BINDED.getDesc());
			break;
		default:
			response.setServerUnkownErrorResponse();
			break;
		}
		return response;
	}
	
	
	
	
	
	
	
}
