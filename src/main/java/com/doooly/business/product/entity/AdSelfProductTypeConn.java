/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.business.product.entity;


/**
 * 商品类型关联管理Entity
 * @author wenwei.yang
 * @version 2017-09-20
 */
public class AdSelfProductTypeConn  {
	
	private String id;
	private String selfProductId;		// 自营商品IDv
	private String selfProductTypeId;		// 自营商品类型ID
	
	public AdSelfProductTypeConn() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSelfProductId() {
		return selfProductId;
	}

	public void setSelfProductId(String selfProductId) {
		this.selfProductId = selfProductId;
	}
	
	public String getSelfProductTypeId() {
		return selfProductTypeId;
	}

	public void setSelfProductTypeId(String selfProductTypeId) {
		this.selfProductTypeId = selfProductTypeId;
	}

	@Override
	public String toString() {
		return "AdSelfProductTypeConn [id=" + id + ", selfProductId="
				+ selfProductId + ", selfProductTypeId=" + selfProductTypeId
				+ "]";
	}
	
	
}