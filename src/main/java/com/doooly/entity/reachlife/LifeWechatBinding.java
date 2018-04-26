/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachlife;

import java.util.Date;

/**
 * A系统wechatBinding
 * 
 * @author yangwenwei
 * @version 2018-4-23
 */
public class LifeWechatBinding {
	private Long id;
	private String openId;
	private String cardNo;
	private String password;
	private String status;
	private String nickName; // 微信昵称
	private byte[] nickName1; // 微信昵称
	private String headImgurl; // 头像图片
	private String lastOpenId; // 上一个登录用户openid
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public byte[] getNickName1() {
		return nickName1;
	}
	public void setNickName1(byte[] nickName1) {
		this.nickName1 = nickName1;
	}
	public String getHeadImgurl() {
		return headImgurl;
	}
	public void setHeadImgurl(String headImgurl) {
		this.headImgurl = headImgurl;
	}
	public String getLastOpenId() {
		return lastOpenId;
	}
	public void setLastOpenId(String lastOpenId) {
		this.lastOpenId = lastOpenId;
	}
    
}