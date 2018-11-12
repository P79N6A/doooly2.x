package com.doooly.business.myorder.constant;

public enum ProductType {

	//商品类型(1.实体卡券 2.电子卡券 3.话费充值 4.流量充值 5.旅游卡, 6.自营卡券, 7.兜礼话费特惠 参考:ad_self_product_type)
	TRAFFIC_TOP_UP(4,"流量充值"),
	SWISS_CARD(5,"旅游卡")
	;
	private Integer value;
	private String desc;
	private ProductType(Integer value,String desc) {
		this.value = value;
		this.desc = desc;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
