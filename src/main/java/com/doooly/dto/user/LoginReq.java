package com.doooly.dto.user;

import javax.xml.bind.annotation.XmlRootElement;

import com.doooly.common.dto.BaseReq;
import com.doooly.entity.reachad.AppClient;

/**
 * 
 * @author Albert
 * 
 */
@XmlRootElement
public class LoginReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8765743213337051491L;
	/**
	 * 登录类型(帐号登录或手机登录) 
	 */
	private int loginType;
	/**
	 * 账号或手机号
	 */
	private String account;
	/**
	 * 密码(MD5)
	 */
	private String password;
	/**
	 * 客服端信息
	 */
	private AppClient client;

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AppClient getClient() {
		return client;
	}

	public void setClient(AppClient client) {
		this.client = client;
	}
	
	@Override
	public String toString() {
		return "LoginReq [loginType=" + loginType + ", account=" + account + ", password=" + password + ", client="
				+ client + "]";
	}
	
	

}
