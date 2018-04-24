package com.doooly.entity.reachad;

import java.util.Date;


/**
 * 活动类目，类型
 * @author zhaoyipeng
 *
 */
 public class AdBasicType {
	
	private Long id;
	
	private String code;
	
	private String name;
	
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}