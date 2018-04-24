package com.doooly.dto.user;

import com.doooly.common.dto.BaseReq;

/**
 * 会员激活请求dto
 * @author 赵清江
 * @date 2016年7月13日
 * @version 1.0
 */
public class UserActiveReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3769762639224922681L;
	/**
	 * 会员卡号
	 */
	private String cardNumber;
	/**
	 * 推荐人会员卡号
	 */
	private String referrerCardNumber;
	/**
	 * 新手机号
	 */
	private String newMobile;
	/**
	 * 新密码
	 */
	private String newPwd;
	/**
	 * 手机验证码
	 */
	private String verifyCode;
	/**
	 * (通过第一步验证激活码后系统反馈的码)准备码
	 */
	private String readyCode;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getReferrerCardNumber() {
		return referrerCardNumber;
	}

	public void setReferrerCardNumber(String referrerCardNumber) {
		this.referrerCardNumber = referrerCardNumber;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getReadyCode() {
		return readyCode;
	}

	public void setReadyCode(String readyCode) {
		this.readyCode = readyCode;
	}

	@Override
	public String toString() {
		return "UserActiveReq [cardNumber=" + cardNumber + ", referrerCardNumber=" + referrerCardNumber + ", newMobile="
				+ newMobile + ", newPwd=" + newPwd + ", verifyCode=" + verifyCode + ", readyCode=" + readyCode + "]";
	}
}
