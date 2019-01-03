package com.doooly.entity.activity;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动码记录明细表(ActActivityCodeRecord)实体类
 *
 * @author Mr_Wu
 * @since 2018-12-30 11:29:09
 */
public class ActActivityCodeRecord implements Serializable {
    private static final long serialVersionUID = 603990843173206386L;
    
    private Long id;
    //运营活动ID，act_activity_record表ID
    private Long activityId;
    //用户ID
    private Long userId;
    //活动码
    private String actCode;
    //0:未分配 1：未使用 2：已使用 3：过期
    private Integer state;
    //使用时间
    private Date usedTime;
    //更新时间
    private Date updateDate;
    
    private Date createDate;
    // keys
    private String[] keys;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}