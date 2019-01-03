/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 商户信息Entity
 * 
 * @author yangwenwei
 * @version 2018-2-11
 */
public class AdBusinessServicePJ  {
	private String id;
	private String serviceName;
	private String serviceUrl;
	private String serviceLogo;
	private String businessId;
	private String sort;
	private String logo;
	private String dealType;
	private String serviceType;
	private Date serverEndTime; 	// 商户服务结束时间

	public Date getServerEndTime() {
		return serverEndTime;
	}

	public void setServerEndTime(Date serverEndTime) {
		this.serverEndTime = serverEndTime;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getServiceLogo() {
		return serviceLogo;
	}
	public void setServiceLogo(String serviceLogo) {
		this.serviceLogo = serviceLogo;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}