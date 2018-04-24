package com.doooly.entity.reachad;

import java.util.Date;

import com.doooly.common.constants.Constants.AppTokenStatus;

/**
 * 表app_token_info映射POJO类
 * @author 赵清江
 * @date 2016-06-28
 * @version 1.0
 */
public class AppToken {
	/**
	 * 编号
	 */
	private int id;
	/**
	 * 该token对应的设备信息id
	 */
	private int appClientId;
	/**
	 * 会员卡好
	 */
	private String card;
	/**
	 * 会员手机号
	 */
	private String mobile;
	/**
	 * 会员登录唯一标识
	 */
	private String token;
	/**
	 * 创建日期
	 */
	private Date createDate;
	/**
	 * 更改日期
	 */
	private Date modifyDate;
	/**
	 * 该token是否在有效期
	 */
	private AppTokenStatus isValid;
	

	public int getAppClientId() {
		return appClientId;
	}

	public void setAppClientId(int appClientId) {
		this.appClientId = appClientId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public AppTokenStatus getIsValid() {
		return isValid;
	}

	public void setIsValid(AppTokenStatus isValid) {
		this.isValid = isValid;
	}
}
