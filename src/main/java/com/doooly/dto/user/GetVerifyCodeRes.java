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
public class GetVerifyCodeRes extends BaseRes {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6742200940086149632L;
	/**
	 * 6位短信验证码
	 */
	private String verificationCode;
	/**
	 * 18位验证码
	 */
	private String verificationCode18;
	/**
	 * 18位条形码Uri
	 */
	private String barCodeUri;
	/**
	 * 18位二维码Uri
	 */
	private String dimCodeUri;

	
	
	public String getVerificationCode18() {
		return verificationCode18;
	}

	public void setVerificationCode18(String verificationCode18) {
		this.verificationCode18 = verificationCode18;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getBarCodeUri() {
		return barCodeUri;
	}

	public void setBarCodeUri(String barCodeUri) {
		this.barCodeUri = barCodeUri;
	}

	public String getDimCodeUri() {
		return dimCodeUri;
	}

	public void setDimCodeUri(String dimCodeUri) {
		this.dimCodeUri = dimCodeUri;
	}
	
	@Override
	public String toJsonString() {
		JSONObject data = new JSONObject();
		if (verificationCode != null) {
			data.put("verificationCode", verificationCode);
		} else {
			data.put("verificationCode", "");
		}
		if (verificationCode18 != null) {
			data.put("verificationCode18", verificationCode18);
		} else {
			data.put("verificationCode18", "");
		}
		if (barCodeUri != null) {
			data.put("barCodeUri", barCodeUri);
		} else {
			data.put("barCodeUri", "");
		}
		if (dimCodeUri != null) {
			data.put("dimCodeUri", dimCodeUri);
		} else {
			data.put("dimCodeUri", "");
		}
		JSONObject res = new JSONObject();
		res.put("code", this.getCode());
		res.put("msg", this.getMsg());
		res.put("data", data);
		return res.toJSONString();
	}
	
	@Override
	public String toString() {
		return "GetVerifyCodeRes [verificationCode=" + verificationCode + ", verificationCode18=" + verificationCode18
				+ ", barCodeUri=" + barCodeUri + ", dimCodeUri=" + dimCodeUri + ", getCode()=" + getCode()
				+ ", getMsg()=" + getMsg() + "]";
	}
}
