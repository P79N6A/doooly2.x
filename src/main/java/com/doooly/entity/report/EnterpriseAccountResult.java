package com.doooly.entity.report;

import java.util.Date;

/**
 * 企业账户会员同步结果Entity
 * 
 * @author linking
 * @version 2018-08-10
 */
public class EnterpriseAccountResult {

	private Long id;
	private String returnCode; // 分页接口返回码
	private String returnMessage; // 分页接口返回信息
	private String phoneNo; // 手机号
	private String resultCode; // 同步结果code
	private String resultDesc; // 同步结果描述
	private Date createDate; // 创建时间
	private Date updateDate; // 修改时间
	private Date dataSyncTime; // 数据同步时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
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
	public Date getDataSyncTime() {
		return dataSyncTime;
	}
	public void setDataSyncTime(Date dataSyncTime) {
		this.dataSyncTime = dataSyncTime;
	}
}