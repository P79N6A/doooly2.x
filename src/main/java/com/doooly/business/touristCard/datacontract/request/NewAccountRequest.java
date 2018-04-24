package com.doooly.business.touristCard.datacontract.request;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.touristCard.datacontract.base.BaseRequest;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.constants.PropertiesHolder;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 王晨宇 on 2018/1/15.
 */
public class NewAccountRequest extends BaseRequest {
	/** 用户表的id编号 **/
	private String userId;
	/** 商户表的id编号 **/
	private String businessId;
	/** 业务类型：sctcd-account(用户旅游卡开户信息) **/
	private String businessType;
	/** 用户在前端输入的旅游卡卡号，例如：U22727841937 **/
	private String orginialCardNo;
	/** 纯数字卡号(11位卡面号) **/
	private String cardno;
	/** 手机号(11位手机号) **/
	private String phoneno;
	/** 卡密码(6位数字密码) **/
	private String pwd;
	/** 姓名 **/
	private String userName;
	/** 身份证号 **/
	private String idCard;
	/** 地址 **/
	private String userAddress;
	/** 有效期格式：YYYYMM **/
	private String validate;
	/** 发证机关 **/
	private String issuers;

	public NewAccountRequest(JSONObject json) {
		super();
		this.userId = json.getString("userId");
		this.businessId = PropertiesConstants.sctcdBundle.getString("SCTCD_MERCHANT_ID");
		this.businessType = json.getString("businessType") == null ? "sctcd-account" : json.getString("businessType");
		this.requestType = PropertiesHolder.getProperty("NEW_ACCOUNT_SERVICE");
		this.orginialCardNo = json.getString("cardno");
		if ( !StringUtils.isEmpty(orginialCardNo) && orginialCardNo.length() == 12) {
			this.cardno = orginialCardNo.substring(1, orginialCardNo.length());
		} else {
			this.cardno = orginialCardNo;
		}
		this.phoneno = json.getString("phoneno");
		this.pwd = json.getString("pwd");
		this.userName = json.getString("userName");
		this.idCard = json.getString("idCard");
		this.userAddress = json.getString("userAddress") == null ? "未知" : json.getString("userAddress");
		this.validate = json.getString("validate") == null ? "000000" : json.getString("validate");
		this.issuers = json.getString("issuers") == null ? "未知" : json.getString("issuers");
	}

	@Override
	public boolean paramsNotEmpty() {
		if (StringUtils.isEmpty(cardno)
			|| StringUtils.isEmpty(phoneno)
			|| StringUtils.isEmpty(pwd)
			|| StringUtils.isEmpty(userName)
			|| StringUtils.isEmpty(idCard)
			|| StringUtils.isEmpty(userAddress)
			|| StringUtils.isEmpty(validate)
			|| StringUtils.isEmpty(issuers)
		) {
			return false;
		}
		return true;
	}

	@Override
	public String getParamsString() throws UnsupportedEncodingException {
		return "requestType=" + requestType +
				"&cardno=" + cardno +
				"&phoneno=" + phoneno +
				"&pwd=" + pwd +
				"&userName=" + URLEncoder.encode(userName, Charsets.UTF_8.name()) +
				"&userID=" + idCard +
				"&userAddress=" + URLEncoder.encode(userAddress, Charsets.UTF_8.name()) +
				"&validate=" + validate +
				"&issuers=" + URLEncoder.encode(issuers, Charsets.UTF_8.name()) +
				"&merchantNum=" + merchantNum +
				"&requestDate=" + requestDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOrginialCardNo() {
		return orginialCardNo;
	}

	public void setOrginialCardNo(String orginialCardNo) {
		this.orginialCardNo = orginialCardNo;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getIssuers() {
		return issuers;
	}

	public void setIssuers(String issuers) {
		this.issuers = issuers;
	}
}
