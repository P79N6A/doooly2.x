package com.doooly.dto.user;

import com.doooly.common.dto.BaseReq;

/**
 * 验证验证码请求DTO
 * @author 赵清江
 * @date 2016年7月14日
 * @version 1.0
 */
public class CheckVerifyCodeReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4114688615985355759L;
	/**
	 * 商家门店或设备编号
	 */
	private String storesId;
	/**
	 * 会员手机号
	 */
	private String mobile;
	/**
	 * 验证码
	 */
	private String verifyCode;

	public String getStoresId() {
		return storesId;
	}

	public void setStoresId(String storesId) {
		this.storesId = storesId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	@Override
	public String toString() {
		return "CheckVerifyCodeReq [storesId=" + storesId + ", mobile=" + mobile + ", verifyCode=" + verifyCode + "]";
	}
}
