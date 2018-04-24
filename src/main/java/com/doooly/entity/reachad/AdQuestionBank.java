package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 题库Entity
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public class AdQuestionBank {

	private Long id;
	/** 题目 */
	private String name;
	/** 题目分值 */
	private Integer points;
	/** 正确答案类型(例：A...) */
	private String answerType;
	/** 提问时间 */
	private Date questionDate;
	/** 创建时间 */
	private Date createDate;
	/** 创建人 */
	private Long createUser;
	/** 更新时间 */
	private Date updateDate;
	/** 更新人 */
	private Long updateUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

	public Date getQuestionDate() {
		return questionDate;
	}

	public void setQuestionDate(Date questionDate) {
		this.questionDate = questionDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

}
