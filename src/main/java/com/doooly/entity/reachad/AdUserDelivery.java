package com.doooly.entity.reachad;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户收货地址Entity
 * 
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
public class AdUserDelivery {
	// 默认收货地址
	public static final String IS_DEFAULT = "1";
	// 非默认收货地址
	public static final String NOT_DEFAULT = "0";

	private Long id;
	/** 会员id */
	private String userId;
	/** 收件人姓名 */
	private String receiverName;
	/** 收件人联系电话 */
	private String receiverTelephone;
	/** 省 */
	private String province;
	/** 市 */
	private String city;
	/** 区 */
	private String area;
	/** 详细地址 */
	private String address;
	/** 是否为默认地址(0:非默认地址 1:默认地址) */
	private String isDefault;
	/** 创建时间 */
	private Date createDate;
	/** 更新时间 */
	private Date updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTelephone() {
		return receiverTelephone;
	}

	public void setReceiverTelephone(String receiverTelephone) {
		this.receiverTelephone = receiverTelephone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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
		return ToStringBuilder.reflectionToString(AdUserDelivery.class, ToStringStyle.MULTI_LINE_STYLE);
	}

}
