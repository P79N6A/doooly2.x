/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.business.product.entity;


/**
 * 商品图片管理Entity
 * @author wenwei.yang
 * @version 2017-09-20
 */
public class AdSelfProductImage  {
	
	private String id; 
	private String image;		// 自营商品主图
	private String detailImage;		// 自营商品详情图
	private String selfProductId;		// 自营商品ID
	
	public AdSelfProductImage() {
	}

	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getDetailImage() {
		return detailImage;
	}

	public void setDetailImage(String detailImage) {
		this.detailImage = detailImage;
	}
	
	public String getSelfProductId() {
		return selfProductId;
	}

	public void setSelfProductId(String selfProductId) {
		this.selfProductId = selfProductId;
	}



	@Override
	public String toString() {
		return "AdSelfProductImage [id=" + id + ", image=" + image
				+ ", detailImage=" + detailImage + ", selfProductId="
				+ selfProductId + "]";
	}
	
	
}