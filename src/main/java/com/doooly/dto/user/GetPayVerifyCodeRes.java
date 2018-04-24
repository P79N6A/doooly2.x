package com.doooly.dto.user;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;

/**
 * 
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
public class GetPayVerifyCodeRes extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5307231683875068470L;

	private String barCodeUri;
	
	private String QRCodeUri;
	
	private String verifyCode18;
	
	private String integral;

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getBarCodeUri() {
		return barCodeUri;
	}

	public void setBarCodeUri(String barCodeUri) {
		this.barCodeUri = barCodeUri;
	}

	public String getQRCodeUri() {
		return QRCodeUri;
	}

	public void setQRCodeUri(String qRCodeUri) {
		QRCodeUri = qRCodeUri;
	}

	public String getVerifyCode18() {
		return verifyCode18;
	}

	public void setVerifyCode18(String verifyCode18) {
		this.verifyCode18 = verifyCode18;
	}
	
	@Override
	public String toJsonString() {
		JSONObject data = new JSONObject();
		data.put("barCodeUri", this.barCodeUri == null ? "" : getBarCodeUri());
		data.put("QRCodeUri", this.QRCodeUri == null ? "" : getQRCodeUri());
		data.put("verifyCode18", this.verifyCode18 == null ? "" : getVerifyCode18());
		data.put("integral", this.integral == null ? "" : getIntegral()); 
		
		JSONObject json = new JSONObject();
		json.put("code", getCode());
		json.put("msg", getMsg());
		json.put("data", data);
		return json.toJSONString();
	}
	
}
