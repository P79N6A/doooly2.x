/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.forum.invitation;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 论坛邀请Entity
 * @author zhangyi.wu
 * @version 2018-11-20
 */
public class AdForumInvitationActivity {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;		// 姓名
	private String phone;		// 手机号码
	private String companyName;	// 公司名称
	private Date registerDate;	// 注册时间
	private String inviter;		// 邀请人
	private String remark;		// 备注
	private Date createDate;	// 创建日期


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getInviter() {
		return inviter;
	}

	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}