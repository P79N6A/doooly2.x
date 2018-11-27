package com.doooly.entity.reachad;

/**
 * 活动类目，类型
 * 
 * @author zhaoyipeng
 *
 */
public class AdBasicType {
	//首页楼层类型标识
	public static final Integer INDEX_TYPE=3;
	//兜礼权益类型标识
	public static final Integer DOOOLY_RIGHTS_TYPE=4;
	
	private Long id;

	private int code;

	private String name;

	private String type;

	private String sort;

	private int floorId;

	private int templateId;
	
	private Integer floorType;
	//副标题
	private String subTitle;

	// 模版类型（0：花积分，1：首页新模版，2：首页模版（旧版））
	private String templateType;

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getFloorType() {
		return floorType;
	}

	public void setFloorType(Integer floorType) {
		this.floorType = floorType;
	}

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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
}