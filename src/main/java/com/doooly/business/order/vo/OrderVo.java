package com.doooly.business.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/***
 * 订单
 * @author 2017-09-23 18:59:12 WANG
 */
public class OrderVo {

	private long id;
	// 订单编号
	private long orderId;
	// 商户编号
	private long bussinessId;
	// 会员ID
	private long userId;
	// 企业ID
	private String groupId;
	// 商户订单号
	private String orderNumber;
	// 商户门店或设备编号
	private String storesId;
	// 实付总金额
	private BigDecimal totalMount;
	// 应付总金额
	private BigDecimal totalPrice;
	// 订单时间
	private Date orderDate;
	// 支付状态
	private int state;
	// 交易阶段 1：完成订单  5：退货订单, 自营订单状态(10:待付款,20:待发货,30已发货,40已收货,50确认收货,99交易取消, 交易完成和退款使用原来状态1,5)
	private int type;
	//商品类型
	private int productType;
	// 是否返利
	private char isUserRebate;
	// 预积分返还数量
	private int userRebate;
	// 返还积分数量
	private BigDecimal userReturnAmount;
	// 是否返佣
	private char isBusinessRebate;
	// 预返佣金额
	private BigDecimal businessRebateAmount;
	// 对账状态
	private char billingState;
	// A系统用户控制删除按钮
	private char delFlagUser;
	// 创建者
	private String createBy;
	// 删除标记
	private char delFlag;
	// 是否首单
	private char isFirst;
	// 订单来源
	private int isSource;
	// 首单优惠人数
	private int firstCount;
	// 机票结算金额
	private BigDecimal airSettleAccounts;
	// 备注信息
	private String remarks;
	// 更新时间
	private Date updateDate;
	// 更新者
	private String updateBy;
	// 创建时间
	private Date createDate;
	//接收人信息
	private String consigneeName;
	private String consigneeMobile;
	private String consigneeAddr;
	//活动类型
	private String actType;
	//手机充值的手续费
	private BigDecimal serviceCharge;
	//优惠券ID
	private String couponId;
	//抵扣券金额
	private BigDecimal voucher;
	//支持支付方式 (1.微信;2.积分;3.支付宝;多个以逗号分割)
	private String supportPayType;
	// 订单扩展信息
	private OrderExtVo orderExt;
	// 商家商品
	private List<MerchantProdcutVo> merchantProduct;
	//商品详情
	private List<OrderItemVo> items;

	public OrderVo() {
		super();
	}

	public OrderVo(String orderNumber) {
		super();
		this.orderNumber = orderNumber;
	}

	public OrderVo(OrderExtVo orderExt) {
		super();
		this.orderExt = orderExt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(long bussinessId) {
		this.bussinessId = bussinessId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStoresId() {
		return storesId;
	}

	public void setStoresId(String storesId) {
		this.storesId = storesId;
	}

	public BigDecimal getTotalMount() {
		return totalMount;
	}

	public void setTotalMount(BigDecimal totalMount) {
		this.totalMount = totalMount;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public char getIsUserRebate() {
		return isUserRebate;
	}

	public void setIsUserRebate(char isUserRebate) {
		this.isUserRebate = isUserRebate;
	}

	public int getUserRebate() {
		return userRebate;
	}

	public void setUserRebate(int userRebate) {
		this.userRebate = userRebate;
	}

	public BigDecimal getUserReturnAmount() {
		return userReturnAmount;
	}

	public void setUserReturnAmount(BigDecimal userReturnAmount) {
		this.userReturnAmount = userReturnAmount;
	}

	public char getIsBusinessRebate() {
		return isBusinessRebate;
	}

	public void setIsBusinessRebate(char isBusinessRebate) {
		this.isBusinessRebate = isBusinessRebate;
	}

	public BigDecimal getBusinessRebateAmount() {
		return businessRebateAmount;
	}

	public void setBusinessRebateAmount(BigDecimal businessRebateAmount) {
		this.businessRebateAmount = businessRebateAmount;
	}

	public char getBillingState() {
		return billingState;
	}

	public void setBillingState(char billingState) {
		this.billingState = billingState;
	}

	public char getDelFlagUser() {
		return delFlagUser;
	}

	public void setDelFlagUser(char delFlagUser) {
		this.delFlagUser = delFlagUser;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

	public char getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(char isFirst) {
		this.isFirst = isFirst;
	}

	public int getIsSource() {
		return isSource;
	}

	public void setIsSource(int isSource) {
		this.isSource = isSource;
	}

	public int getFirstCount() {
		return firstCount;
	}

	public void setFirstCount(int firstCount) {
		this.firstCount = firstCount;
	}

	public BigDecimal getAirSettleAccounts() {
		return airSettleAccounts;
	}

	public void setAirSettleAccounts(BigDecimal airSettleAccounts) {
		this.airSettleAccounts = airSettleAccounts;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public OrderExtVo getOrderExt() {
		return orderExt;
	}

	public void setOrderExt(OrderExtVo orderExt) {
		this.orderExt = orderExt;
	}

	public List<MerchantProdcutVo> getMerchantProduct() {
		return merchantProduct;
	}

	public void setMerchantProduct(List<MerchantProdcutVo> merchantProduct) {
		this.merchantProduct = merchantProduct;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
	}

	public List<OrderItemVo> getItems() {
		return items;
	}

	public void setItems(List<OrderItemVo> items) {
		this.items = items;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public BigDecimal getVoucher() {
		return voucher;
	}

	public void setVoucher(BigDecimal voucher) {
		this.voucher = voucher;
	}

	public String getSupportPayType() {
		return supportPayType;
	}

	public void setSupportPayType(String supportPayType) {
		this.supportPayType = supportPayType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
