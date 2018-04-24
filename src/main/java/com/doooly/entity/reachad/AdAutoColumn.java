package com.doooly.entity.reachad;


import java.util.Date;

/**
 * 报名选项Entity
 * @author qing.zhang
 * @version 2017-04-25
 */
public class AdAutoColumn{

    private Long id;
	private String name;		// 名称
	private String buttonName;		// 按钮名称
	private String dataType;		// 数据类型(0-表单填写字段，1-单选栏位数据，2-多选框栏位数据)
	private String isChecked;		// 是否必选(0,没选中，1选中，默认0)
	private Integer activityId;		// 活动ID
    private Date updateDate;	// 更新日期
    private Integer number;		// 序号，用以辨别固定6项的前台显示


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}