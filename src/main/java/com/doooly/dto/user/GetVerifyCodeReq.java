package com.doooly.dto.user;

import javax.xml.bind.annotation.XmlRootElement;

import com.doooly.common.dto.BaseReq;

/**
 * 短信验证码或条形码和二维码请求dto
 * @author Albert
 * @date 2016-06-28
 * @version 1.0
 */
@XmlRootElement
public class GetVerifyCodeReq extends BaseReq{
	
	public static final int MOBILE_TEXT = 0;
	public static final int MOBILE_DIRECT = 1;
	public static final int DIMENSION = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 166611002595411890L;
	/**
	 * 会员卡号或会员手机号
	 */
	private String account;
	/**
	 * 商家门店或设备编号
	 */
	private String storesId;
	/**
	 * 0:验证会员;1:不验证会员
	 */
	private int needCheck;
	/**
	 * 0:发短信;1:6位验证码;2:18位验证码
	 */
	private int msgType;
	
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getStoresId() {
		return storesId;
	}
	public void setStoresId(String storesId) {
		this.storesId = storesId;
	}
	public int getNeedCheck() {
		return needCheck;
	}
	public void setNeedCheck(int needCheck) {
		this.needCheck = needCheck;
	}
	
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	@Override
	public String toString() {
		return "GetVerifyCodeReq [account=" + account + ", storesId=" + storesId + ", needCheck=" + needCheck
				+ ", msgType=" + msgType + "]";
	}
	
	
}
