package com.doooly.entity.reachad;

public class AdSystemNoticeRead {
    //通知ID
    private String id;
    //通知消息表id
    private String noticeId;
    private Integer readType;// 消息是否读取，0 未读，1 已读

    //读取通知对应的用户id
    private String receiveUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public Integer getReadType() {
        return readType;
    }

    public void setReadType(Integer readType) {
        this.readType = readType;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }
}
