package com.doooly.entity.reachad;

import java.util.Date;


/**
 * 活动类目，类型
 * @author zhaoyipeng
 *
 */
 public class AdBasicType {
	
	private Long id;
	
	private int code;
	
	private String name;
	
	private String type;

	private String sort;

	private int floorId;


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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getFloorId() {
		return floorId;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}
}