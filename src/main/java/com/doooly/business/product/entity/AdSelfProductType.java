/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.business.product.entity;


/**
 * 商品类型管理Entity
 * @author wenwei.yang
 * @version 2017-09-20
 */
public class AdSelfProductType  {
	
	private String id;
	private String name;		// 商品类型名称
	
	public AdSelfProductType() {
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AdSelfProductType [id=" + id + ", name=" + name + "]";
	}
	
	
}