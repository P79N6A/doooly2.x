package com.doooly.dto.user;

import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;

/**
 * 
 * @author Albert
 *
 */
@XmlRootElement
public class LoginRes extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 186901077262752639L;

	private String company;
	
	private String mobile;
	
	private String cardNumber;
	
	private String token;
	
	private String publicKey;
	
	private String privateKey;
	
	private Integer memberType;
	
	private String referrer;
	

	
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
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


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	@Override
	public String toJsonString() {
		JSONObject data = new JSONObject();
		if (company != null) {
			data.put("company", company);
		} else {
			data.put("company", "");
		}
		if (memberType != null) {
			data.put("memberType", memberType);
		} else {
			data.put("memberType", "");
		}
		if (token != null) {
			data.put("token", token);
		} else {
			data.put("token", "");
		}
		if (publicKey != null) {
			data.put("publicKey", publicKey);
		} else {
			data.put("publicKey", "");
		}
		if (privateKey != null) {
			data.put("privateKey", privateKey);
		} else {
			data.put("privateKey", "");
		}
		if (cardNumber != null) {
			data.put("cardNumber", cardNumber);
		} else {
			data.put("cardNumber", "");
		}
		if (mobile != null) {
			data.put("mobile", mobile);
		} else {
			data.put("mobile", "");
		}
		JSONObject res = new JSONObject();
		res.put("code", this.getCode());
		res.put("msg", this.getMsg());
		res.put("data", data);
		return res.toJSONString();
	}
}






















