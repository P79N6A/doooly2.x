package com.doooly.dto.user;

import com.doooly.common.dto.BaseReq;
import com.doooly.entity.reachad.AppClient;

/**
 * 会员登出(注销)请求dto
 * @author 赵清江
 * @date 2016年7月13日
 * @version 1.0
 */
public class LogoutReq extends BaseReq{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1195680208340091666L;
	/**
	 * 会员卡号 或 会员手机号
	 */
	private String account;
	/**
	 * 会员登录唯一标识
	 */
	private String token;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "LogoutReq [account=" + account + ", token=" + token + "]";
	}
}
