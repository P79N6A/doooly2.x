package com.doooly.entity.reachad;

import java.util.Date;

/**
 * @Description: app 极光绑定信息
 * @author: qing.zhang
 * @date: 2017-07-31
 */
public class AdAppAuroraPushBind {

    private Integer id;//主键
    private String registrationId;//android/ios极光推送唯一标识
    private String uniqueIdentification;//android/ios设备唯一标识
    private String userId;//用户id
    private String type;//app类型,0:安卓;1:苹果
    private Date createDate;//创建时间
    private Date updateDate;//修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getUniqueIdentification() {
        return uniqueIdentification;
    }

    public void setUniqueIdentification(String uniqueIdentification) {
        this.uniqueIdentification = uniqueIdentification;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}




