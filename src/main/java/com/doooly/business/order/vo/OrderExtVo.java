package com.doooly.business.order.vo;

import java.util.Date;
import java.util.List;

/**
 * 订单扩展信息
 * @author 2017-09-24 09:54:46 WANG
 *
 */
public class OrderExtVo {
	
	private int id;
	//订单主键
	private long orderReportId;
	//商品类型
	private int type;
	//办卡区域1
	private String applyCardArea1;
	//办卡门店2
	private String applyCardStore1;
	//办卡区域2
	private String applyCardArea2;
	//办卡门店2
	private String applyCardStore2;
	//虚拟卡券的接收人(办卡人姓名)
	private String deliveryName;
	//虚拟卡券的接收人手机号(办卡人号码, 充值手机号)
	private String deliveryTelephone;
	//身份证
	private String identityCard;
	//办卡人照片
	private String identityCardImage;
	//办卡人相片
	private String applyCardImage;
	//创建时间
	private Date createDate;
	//修改时间
	private Date updateDate;
	//工作证
	private String empCard;
	//订单详情数据
	private List<OrderItemVo> orderItems;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getOrderReportId() {
		return orderReportId;
	}
	public void setOrderReportId(long orderReportId) {
		this.orderReportId = orderReportId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getApplyCardArea1() {
		return applyCardArea1;
	}
	public void setApplyCardArea1(String applyCardArea1) {
		this.applyCardArea1 = applyCardArea1;
	}
	public String getApplyCardStore1() {
		return applyCardStore1;
	}
	public void setApplyCardStore1(String applyCardStore1) {
		this.applyCardStore1 = applyCardStore1;
	}
	public String getApplyCardArea2() {
		return applyCardArea2;
	}
	public void setApplyCardArea2(String applyCardArea2) {
		this.applyCardArea2 = applyCardArea2;
	}
	public String getApplyCardStore2() {
		return applyCardStore2;
	}
	public void setApplyCardStore2(String applyCardStore2) {
		this.applyCardStore2 = applyCardStore2;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public String getDeliveryTelephone() {
		return deliveryTelephone;
	}
	public void setDeliveryTelephone(String deliveryTelephone) {
		this.deliveryTelephone = deliveryTelephone;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getIdentityCardImage() {
		return identityCardImage;
	}
	public void setIdentityCardImage(String identityCardImage) {
		this.identityCardImage = identityCardImage;
	}
	public String getApplyCardImage() {
		return applyCardImage;
	}
	public void setApplyCardImage(String applyCardImage) {
		this.applyCardImage = applyCardImage;
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
	public List<OrderItemVo> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemVo> orderItems) {
		this.orderItems = orderItems;
	}

	public String getEmpCard() {
		return empCard;
	}

	public void setEmpCard(String empCard) {
		this.empCard = empCard;
	}
}
