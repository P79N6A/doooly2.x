package com.doooly.entity.reachad;

import java.util.Date;

public class AdBusinessActivityRule {
	
	public enum RuleType {
		
		LESS_THAN, LESS_OR_EQUAL, EQUAL_TO, GREATER_THAN, GREATER_OR_EQUAL
		
	}
	
    private Integer id;

    private Integer activityId;

    private Integer type;

    private String value;

    private String ruleDesc;

    private Date createDate;
    
    private AdBusinessActivityInfo info;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public AdBusinessActivityInfo getInfo() {
		return info;
	}

	public void setInfo(AdBusinessActivityInfo info) {
		this.info = info;
	}
    
    
}