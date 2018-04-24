package com.doooly.entity.doooly;

import java.util.Date;

/**
 * @Description: app 版本信息
 * @author: qing.zhang
 * @date: 2017-07-31
 */
public class AdAppVersionUrl {
    private Integer id;//主键
    private String downloadUrl;//android/ios下载地址
    private String versionCode;//app当前版本号
    private String type;//app类型,0:安卓;1:苹果
    private String domainName;//项目域名地址
    private Date createDate;//创建时间
    private Date updateDate;//修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
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

	@Override
	public String toString() {
		return "AdAppVersionUrl [id=" + id + ", downloadUrl=" + downloadUrl
				+ ", versionCode=" + versionCode + ", type=" + type
				+ ", domainName=" + domainName + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + "]";
	}
    
}




