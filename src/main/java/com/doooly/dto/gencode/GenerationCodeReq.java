package com.doooly.dto.gencode;

import javax.xml.bind.annotation.XmlRootElement;

import com.doooly.common.dto.BaseReq;
@XmlRootElement
public class GenerationCodeReq extends BaseReq {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4082113743973759248L;
	//生成码类型（默认生成条形码、二维码、手机验证码）
	//AUTH:身份验证 POINTPAY:积分兑换
	private String codeType;
	//条形码长度（6/11/18位）
	private int barCodeLength;
	//生成码有效时间（单位：秒），默认60秒
	private int expireTime=60;
	//会员手机号
	private String mobile;
	//会员卡号
	private String cardNumber;
	//当前用户位置信息
	private String location;

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public int getBarCodeLength() {
		return barCodeLength;
	}

	public void setBarCodeLength(int barCodeLength) {
		this.barCodeLength = barCodeLength;
	}
}
