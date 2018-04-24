package com.doooly.business.touristCard.datacontract.response;

import com.doooly.business.touristCard.datacontract.base.BaseResponse;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public class AccountRechargeResponse extends BaseResponse {
	private String cardno;
	private String merchantNum;
	private String merchantPosNum;
	private String merchantOrderNum;
	private String merchantOrderFee;
	private String merchantOrderDate;
	/** 支付渠道：0001-支付宝支付，0002-XXX银行网银支付，0003-银联支付，0004-微信支付，0005-积分支付 **/
	private String payType;
	/** 充值结果：01-成功，02-失败。注：账户充值成功重要标志 **/
	private String payResult;
	/** 账户充值交易号，旅游卡充值流水号 **/
	private String sctcdRechargeOrderNum;
	/** 账户充值时间，旅游卡充值时间，yyyyMMddHHmmss格式 **/
	private String sctcdRechargeOrderDate;
	private String sign;

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getMerchantNum() {
		return merchantNum;
	}

	public void setMerchantNum(String merchantNum) {
		this.merchantNum = merchantNum;
	}

	public String getMerchantPosNum() {
		return merchantPosNum;
	}

	public void setMerchantPosNum(String merchantPosNum) {
		this.merchantPosNum = merchantPosNum;
	}

	public String getMerchantOrderNum() {
		return merchantOrderNum;
	}

	public void setMerchantOrderNum(String merchantOrderNum) {
		this.merchantOrderNum = merchantOrderNum;
	}

	public String getMerchantOrderFee() {
		return merchantOrderFee;
	}

	public void setMerchantOrderFee(String merchantOrderFee) {
		this.merchantOrderFee = merchantOrderFee;
	}

	public String getMerchantOrderDate() {
		return merchantOrderDate;
	}

	public void setMerchantOrderDate(String merchantOrderDate) {
		this.merchantOrderDate = merchantOrderDate;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getSctcdRechargeOrderNum() {
		return sctcdRechargeOrderNum;
	}

	public void setSctcdRechargeOrderNum(String sctcdRechargeOrderNum) {
		this.sctcdRechargeOrderNum = sctcdRechargeOrderNum;
	}

	public String getSctcdRechargeOrderDate() {
		return sctcdRechargeOrderDate;
	}

	public void setSctcdRechargeOrderDate(String sctcdRechargeOrderDate) {
		this.sctcdRechargeOrderDate = sctcdRechargeOrderDate;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
