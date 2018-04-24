package com.doooly.dto.user;

import com.doooly.common.dto.BaseReq;

/**
 * 积分支付验证码请求DTO
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
public class GetPayVerifyCodeReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3928197396236352760L;
	
	private String cardNumber;
	
	private String storesId;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getStoresId() {
		return storesId;
	}

	public void setStoresId(String storesId) {
		this.storesId = storesId;
	}

	@Override
	public String toString() {
		return "GetPayVerifyCodeReq [cardNumber=" + cardNumber + ", storesId=" + storesId + "]";
	}
	
	
	
}
