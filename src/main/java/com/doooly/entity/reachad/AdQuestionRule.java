package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 答题规则Entity
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public class AdQuestionRule {

	private Long id;
	/** 规则类型(例如：0-连续7天答题...) */
	private String type;
	/** 可变值1 */
	private String value1;
	/** 可变值2 */
	private String value2;
	/** 可变值3 */
	private String value3;
	/** 可变值4 */
	private String value4;
	/** 创建时间 */
	private Date createDate;
	/** 创建人 */
	private Long createUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
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

}
