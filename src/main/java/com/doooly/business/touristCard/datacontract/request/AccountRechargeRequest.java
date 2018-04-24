package com.doooly.business.touristCard.datacontract.request;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.touristCard.datacontract.base.BaseRequest;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.constants.PropertiesHolder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 王晨宇 on 2018/1/15.
 */
public class AccountRechargeRequest extends BaseRequest {
	/** 卡号(11位卡面号) **/
	private String cardno;
	/** 商户POS号 **/
	private String merchantPosNum;
	/** 商户订单号，长度20，大写字母 + 数字 **/
	private String merchantOrderNum;
	/** 商户订单金额：单位元 **/
	private String merchantOrderFee;
	/** 商户订单时间：yyyyMMddHHmmss格式 **/
	private String merchantOrderDate;
	/** 支付渠道：0001-支付宝支付，0002-XXX银行网银支付，0003-银联支付，0004-微信支付，0005-积分支付 **/
	private String payType;
	/** 支付结果：01-成功，02-失败 **/
	private String payResult;
	/** 数据签名 **/
	private String sign;
	/** 数据签名明文 **/
	private String signContent;

	public AccountRechargeRequest(OrderVo orderVo) {
		super();
		this.requestType = PropertiesHolder.getProperty("ACCOUNT_RECHARGE_SERVICE");
		String orginialCardNo = orderVo.getRemarks();
		if ( !StringUtils.isEmpty(orginialCardNo) && orginialCardNo.length() == 12) {
			this.cardno = orginialCardNo.substring(1, orginialCardNo.length());
		} else {
			this.cardno = orginialCardNo;
		}
		this.merchantPosNum = PropertiesHolder.getProperty("SCTCD_MERCHANT_POS_NUM");
		this.merchantOrderNum = orderVo.getOrderNumber();
		this.merchantOrderFee = orderVo.getTotalMount().setScale(0, BigDecimal.ROUND_DOWN).toPlainString();
		Date createDate = orderVo.getCreateDate() == null ? new Date() : orderVo.getCreateDate();
		this.merchantOrderDate = DateUtils.formatDate(createDate, "yyyyMMddHHmmss");
		this.payType = PropertiesConstants.sctcdBundle.getString("SCTCD_PAY_TYPE");
		this.payResult = "01";
		this.signContent = merchantOrderNum + merchantOrderFee + merchantOrderDate + System.currentTimeMillis();
	}

	@Override
	public boolean paramsNotEmpty() {
		if (StringUtils.isEmpty(cardno)
			|| StringUtils.isEmpty(merchantPosNum)
			|| StringUtils.isEmpty(merchantOrderNum)
			|| StringUtils.isEmpty(merchantOrderFee)
			|| StringUtils.isEmpty(merchantOrderDate)
			|| StringUtils.isEmpty(payType)
			|| StringUtils.isEmpty(payResult)
		) {
			return false;
		}
		return true;
	}

	@Override
	public String getParamsString() {
		return "requestType=" + requestType +
				"&cardno=" + cardno +
				"&merchantNum=" + merchantNum +
				"&merchantPosNum=" + merchantPosNum +
				"&merchantOrderNum=" + merchantOrderNum +
				"&merchantOrderFee=" + merchantOrderFee +
				"&merchantOrderDate=" + merchantOrderDate +
				"&payType=" + payType +
				"&payResult=" + payResult +
				"&sign=@sign" +
				"&signContent=" + signContent;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignContent() {
		return signContent;
	}

	public void setSignContent(String signContent) {
		this.signContent = signContent;
	}
}
