package com.doooly.dto.user;

import com.doooly.common.dto.BaseReq;

/**
 * 验证激活码请求DTO
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
public class CheckActiveCodeReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8491113387452849304L;
	/**
	 * 会员卡号
	 */
	private String cardNumber;
	/**
	 * 激活码
	 */
	private String activeCode;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	@Override
	public String toString() {
		return "CheckActiveCodeReq [cardNumber=" + cardNumber + ", activeCode=" + activeCode + "]";
	}
}
