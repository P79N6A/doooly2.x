package com.doooly.dto.user;

import com.doooly.common.dto.BaseReq;

/**
 * 绑定手机请求DTO
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
public class ModifyMobileReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6674598604316693178L;
	/**
	 * 会员卡号
	 */
	private String cardNumber;
	/**
	 * 新手机号
	 */
	private String newMobile;
	/**
	 * 验证码
	 */
	private String verifyCode;
	
	

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	@Override
	public String toString() {
		return "ModifyMobileReq [cardNumber=" + cardNumber + ", newMobile=" + newMobile + ", verifyCode=" + verifyCode
				+ "]";
	}
}
