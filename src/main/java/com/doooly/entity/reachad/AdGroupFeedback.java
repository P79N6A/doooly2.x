package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 企业反馈Entity
 * 
 * @author yuelou.zhang
 * @version 2017年3月6日
 */
public class AdGroupFeedback {

	private Long id;
	/** acount_type：0：ad_user主键；1：企业管理员主键 */
	private Integer accountId;
	/** 账号类型：0：员工账号；1：企业管理员账号 */
	private Integer accountType;
	/** 反馈标题 */
	private String title;
	/** 反馈内容 */
	private String content;
	/** 是否匿名(默认匿名)，0:不匿名；1：匿名 */
	private String isAnonymous;
	/** 反馈时间 */
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(String isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
