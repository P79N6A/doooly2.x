package com.doooly.dto.gencode;

import javax.xml.bind.annotation.XmlRootElement;

import com.doooly.common.dto.BaseRes;
@XmlRootElement
public class GenerationCodeRes extends BaseRes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1158095396258939282L;
	//发送验证码手机号
	private String mobile;
	//用户可用积分
	private Double integral;
	//发送的身份认证手机验证码
	private String verficationCode;
	//条形码url
	private String barCodeURL;
	//二维码url
	private String qrCodeURL;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Double getIntegral() {
		return integral;
	}
	public void setIntegral(Double integral) {
		this.integral = integral;
	}
	public String getVerficationCode() {
		return verficationCode;
	}
	public void setVerficationCode(String verficationCode) {
		this.verficationCode = verficationCode;
	}
	public String getBarCodeURL() {
		return barCodeURL;
	}
	public void setBarCodeURL(String barCodeURL) {
		this.barCodeURL = barCodeURL;
	}
	public String getQrCodeURL() {
		return qrCodeURL;
	}
	public void setQrCodeURL(String qrCodeURL) {
		this.qrCodeURL = qrCodeURL;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
