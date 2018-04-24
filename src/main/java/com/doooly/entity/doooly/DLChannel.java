package com.doooly.entity.doooly;

import java.util.Date;

public class DLChannel {
	private String channelName;
	private String channelPwd;
	private String ipWhiteLists;
	private Integer reqMaxNum;
	private Integer delFlag;
	private Date createDate;
	private Date modifyDate;
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getChannelPwd() {
		return channelPwd;
	}
	public void setChannelPwd(String channelPwd) {
		this.channelPwd = channelPwd;
	}
	public String getIpWhiteLists() {
		return ipWhiteLists;
	}
	public void setIpWhiteLists(String ipWhiteLists) {
		this.ipWhiteLists = ipWhiteLists;
	}
	public Integer getReqMaxNum() {
		return reqMaxNum;
	}
	public void setReqMaxNum(Integer reqMaxNum) {
		this.reqMaxNum = reqMaxNum;
	}
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
