package com.doooly.entity.reachad;

import java.util.Date;

public class AdSystemNotice {
    //通知ID
    private String id;
    //通知消息标题
    private String title;
    //通知消息内容
    private String content;
    //通知目标系统（0-企业端，1-微信端, 2-app端）注：添加类型
    private String target;
    //通知对应的用户id
    private String receiveUser;
    //通知创建时间
    private Date createDate;
    //通知对应的消息类别（0-系统通知，1-订单通知，2-积分通知，3-共享积分开关消息）
    private String noticeType;
    private String targetUrl;//跳转链接
    private String newTargetUrl;//新跳转链接

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getNewTargetUrl() {
        return newTargetUrl;
    }

    public void setNewTargetUrl(String newTargetUrl) {
        this.newTargetUrl = newTargetUrl;
    }
}
