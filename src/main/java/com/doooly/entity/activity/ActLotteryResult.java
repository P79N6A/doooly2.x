package com.doooly.entity.activity;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽奖结果明细记录表(ActLotteryResult)实体类
 *
 * @author Mr_Wu
 * @since 2018-12-30 11:29:20
 */
public class ActLotteryResult implements Serializable {
    private static final long serialVersionUID = 668730806585062368L;
    
    private Long id;
    //抽奖码活动ID，act_activity_record表ID
    private Long activityId;
    //用户ID
    private Long userId;
    //用户手机号
    private String phoneNo;
    //微信昵称
    private String wechatNick;
    //微信头像url
    private String wechatImgUrl;
    //中奖码
    private String lotteryCode;
    //奖品名称
    private String lotteryName;
    //收件人
    private String receiverName;
    //收件人手机号
    private String receiverPhoneNo;
    //收件人地址
    private String receiverAddress;
    //创建人
    private String createBy;
    
    private Date createDate;


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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getWechatNick() {
        return wechatNick;
    }

    public void setWechatNick(String wechatNick) {
        this.wechatNick = wechatNick;
    }

    public String getWechatImgUrl() {
        return wechatImgUrl;
    }

    public void setWechatImgUrl(String wechatImgUrl) {
        this.wechatImgUrl = wechatImgUrl;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhoneNo() {
        return receiverPhoneNo;
    }

    public void setReceiverPhoneNo(String receiverPhoneNo) {
        this.receiverPhoneNo = receiverPhoneNo;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}