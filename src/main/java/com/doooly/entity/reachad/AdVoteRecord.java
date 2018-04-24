package com.doooly.entity.reachad;

import java.util.Date;

/**
 * @Description: 投票记录
 * @author: qing.zhang
 * @date: 2017-05-31
 */
public class AdVoteRecord {
    private int id;
    private String userWechatOpenId;// 微信用户id
    private int optionId;//投票选项id
    private Date createDate;// 创建时间
    private Integer activityId;
    private char state;
    private String mobile;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserWechatOpenId() {
        return userWechatOpenId;
    }

    public void setUserWechatOpenId(String userWechatOpenId) {
        this.userWechatOpenId = userWechatOpenId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
    
}
