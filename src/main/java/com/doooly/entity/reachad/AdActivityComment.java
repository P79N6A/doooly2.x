package com.doooly.entity.reachad;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 评论Entity
 * @author qing.zhang
 * @version 2017-04-25
 */
public class AdActivityComment {

    private Long id;
	private Integer commentUserId;		// 评论人ID
	private String content;		// 评论内容
	private String pictrues;		// 图片（多个图片用逗号分隔）
	private Integer supportCount;		// 评论被点赞数
    private Date updateDate;	// 更新日期
    private Integer activityId;		// 活动ID
    private String telephone; //电话 获取微信id用
    private String cardNumber; //卡号 获取微信id用
    private String nickName; // 微信昵称
    private String headImgurl; // 头像图片

	@NotNull(message="评论人ID不能为空")
	public Integer getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(Integer commentUserId) {
		this.commentUserId = commentUserId;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getPictrues() {
		return pictrues;
	}

	public void setPictrues(String pictrues) {
		this.pictrues = pictrues;
	}
	
	@NotNull(message="评论被点赞数不能为空")
	public Integer getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(Integer supportCount) {
		this.supportCount = supportCount;
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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgurl() {
        return headImgurl;
    }

    public void setHeadImgurl(String headImgurl) {
        this.headImgurl = headImgurl;
    }
}