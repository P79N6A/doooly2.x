package com.doooly.entity.reachad;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 点赞Entity
 * @author qing.zhang
 * @version 2017-04-25
 */
public class AdSupport{

    private Long id;
	private Integer supportUser;		// 点赞用户ID
	private Integer commentId;		// 评论记录ID
	private Integer activityId;		// 活动ID
	private Integer joinRecordId;		// 报名记录ID
    private Date updateDate;	// 更新日期
    private Integer type;//点赞类型方便为对应点赞数加1 (0,活动点赞 1，评论点赞 2，报名项目点赞)

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

    @NotNull(message="点赞用户ID不能为空")
	public Integer getSupportUser() {
		return supportUser;
	}

	public void setSupportUser(Integer supportUser) {
		this.supportUser = supportUser;
	}
	
	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	public Integer getJoinRecordId() {
		return joinRecordId;
	}

	public void setJoinRecordId(Integer joinRecordId) {
		this.joinRecordId = joinRecordId;
	}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}