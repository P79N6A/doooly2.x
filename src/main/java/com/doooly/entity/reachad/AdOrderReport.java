/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.entity.reachad;

import com.doooly.business.pay.bean.AdOrderFlow;
import com.doooly.business.utils.DateUtils;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 睿仕账户统计（订单报表）Entity
 *
 * @author yanghaoyuan
 * @version 2015-12-09
 */
public class AdOrderReport {

	private static final long serialVersionUID = 1L;
    private long id;//主键
	private Long orderId; // 订单编号
	private AdBusiness adBusiness; // 商户编号
	private AdUser adUser; // 会员编号
	private String orderNumber; // 商户订单号
	private String storesId; // 商户门店或设备编号
	private BigDecimal totalMount; // 实付总金额
	private BigDecimal totalPrice; // 应付总金额
	private Date orderDate; // 订单发生时间
	private String state; // 订单完成状态
	private Integer type; // 交易阶段
	private String isUserRebate; // 是否返利
	private BigDecimal userRebate; // 预积分返还数量
	private BigDecimal userReturnAmount; // 返还积分数量
	private String isBusinessRebate; // 是否返佣
	private BigDecimal businessRebateAmount; // 返佣金额
	private String billingState; // 对账状态（0：未对账(进行中)；1：已对账（返利中）2：已到账（返利到账））
	private List<AdOrderDetail> adOrderDetailList = Lists.newArrayList(); // 下属的订单明细
	private List<AdOrderFlow> adOrderFlowList = Lists.newArrayList(); // 下属的支付流水明细
	private List<AdReturnDetail> adReturnDetailList = Lists.newArrayList(); // 下属的订单退货明细
	private Date beginOrderDate; // 开始 订单发生时间
	private Date endOrderDate; // 结束 订单发生时间
	private String delFlagUser; // A系统用户自己控制的删除标记
	private Long[] businessIds;// 检索用商户编号列表
	private BigDecimal points;// 积分
	private BigDecimal cash;// 现金
	private Integer payType;
	// 是否机票首单 1:是 2：分销商 0：非首单
	private Integer isFirst;
	// 首单优惠人数
	private Integer firstCount;
	// 机票结算金额
	private BigDecimal airSettleAccounts;
	// 订单来源
	private String orderChannel;

	private String cashDeskSource;	// 订单收银台来源 d:兜礼收银台 m:商户收银台

	private Integer isSource;// 订单来源 0:机票个人 1：机票分销商 2：合作商家 3：睿渠平台

	private String airlinesn; // 吉祥订单编号

	private String payTypeStr;//支付方式

	private BigDecimal savePrice;//节省金额
	// 商品类型(1.实体卡券 2.电子卡券 3.话费充值 4.流量充值   具体参考:ad_self_product_type)
	private int productType;
	// 商品名称
	private String goods;
	// 规格
	private String specification;
	// 欧飞话费流量充值手机号码
	private String consigneeMobile;
	// 收货人姓名
	private String consigneeName;
	// 收货人地址
	private String consigneeAddr;
	// 办卡区域1
	private String applyCardArea1;
	// 办卡门店1
	private String applyCardStore1;
	// 办卡区域2
	private String applyCardArea2;
	// 办卡门店2
	private String applyCardStore2;
	// 办卡人或电子券填的姓名
	private String deliveryName;
	// 办卡人或电子券填的手机
	private String deliveryTelephone;
	// 身份证号
	private String identityCard;
	// 商品主图链接
	private String productImg;
	// 飞行范围
	private String inflightRange;
	// 手续费
	private String serviceCharge;
	// 优惠券(抵扣券)金额
	private BigDecimal voucher;
	// 旅游卡卡号
	private String touristCardNo;
	// 旅游卡绑定手机号
	private String touristPhone;
	// 旅游卡绑定身份证
	private String touristIdCard;
	// 认证会员并且开通了返利开关
	private boolean openRebateSwitch;
    private Integer startIndex;
    private Integer pageSize;
    private String remarks;	// 备注

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getAirlinesn() {
		return airlinesn;
	}

	public void setAirlinesn(String airlinesn) {
		this.airlinesn = airlinesn;
	}

	public Integer getIsSource() {
		return isSource;
	}

	public void setIsSource(Integer isSource) {
		this.isSource = isSource;
	}

	public Integer getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Integer isFirst) {
		this.isFirst = isFirst;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public BigDecimal getPoints() {
		return points == null ? new BigDecimal("0.0") : points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public BigDecimal getCash() {
		return cash == null ? new BigDecimal("0.0") : cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}


	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public AdBusiness getAdBusiness() {
		return adBusiness;
	}

	public void setAdBusiness(AdBusiness adBusiness) {
		this.adBusiness = adBusiness;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIsUserRebate() {
		return isUserRebate;
	}

	public void setIsUserRebate(String isUserRebate) {
		this.isUserRebate = isUserRebate;
	}

	public BigDecimal getUserRebate() {
		return userRebate;
	}

	public void setUserRebate(BigDecimal userRebate) {
		this.userRebate = userRebate;
	}

	public BigDecimal getUserReturnAmount() {
		return userReturnAmount;
	}

	public void setUserReturnAmount(BigDecimal userReturnAmount) {
		this.userReturnAmount = userReturnAmount;
	}

	public String getIsBusinessRebate() {
		return isBusinessRebate;
	}

	public void setIsBusinessRebate(String isBusinessRebate) {
		this.isBusinessRebate = isBusinessRebate;
	}

	public BigDecimal getBusinessRebateAmount() {
		return businessRebateAmount;
	}

	public void setBusinessRebateAmount(BigDecimal businessRebateAmount) {
		this.businessRebateAmount = businessRebateAmount;
	}

	public AdUser getAdUser() {
		return adUser;
	}

	public void setAdUser(AdUser adUser) {
		this.adUser = adUser;
	}

	public AdUser getAdUser1() {
		return adUser;
	}

	public Date getBeginOrderDate() {
		return beginOrderDate;
	}

	public void setBeginOrderDate(Date beginOrderDate) {
		this.beginOrderDate = beginOrderDate;
	}

	public Date getEndOrderDate() {
		return endOrderDate;
	}

	public void setEndOrderDate(Date endOrderDate) {
		this.endOrderDate = endOrderDate;
	}

	public List<AdOrderDetail> getAdOrderDetailList() {
		return adOrderDetailList;
	}

	public void setAdOrderDetailList(List<AdOrderDetail> adOrderDetailList) {
		this.adOrderDetailList = adOrderDetailList;
	}

	public List<AdOrderFlow> getAdOrderFlowList() {
		return adOrderFlowList;
	}

	public void setAdOrderFlowList(List<AdOrderFlow> adOrderFlowList) {
		this.adOrderFlowList = adOrderFlowList;
	}

	public List<AdReturnDetail> getAdReturnDetailList() {
		return adReturnDetailList;
	}

	public void setAdReturnDetailList(List<AdReturnDetail> adReturnDetailList) {
		this.adReturnDetailList = adReturnDetailList;
	}

	public String getAdBusinessCompany() {
		if (adBusiness != null) {
			return adBusiness.getCompany();
		}
		return null;
	}

	public String getCashDeskSource() {
		return cashDeskSource;
	}

	public void setCashDeskSource(String cashDeskSource) {
		this.cashDeskSource = cashDeskSource;
	}

	public String getTotalPriceString() {
		return new StringBuffer("￥").append(totalPrice).toString();
	}

	public String getTotalMountString() {
		return new StringBuffer("￥").append(totalMount).toString();
	}

	public String getUserRebateString() {
		return new StringBuffer((userRebate == null ? "0.0" : userRebate.toString())).append(" 积分").toString();
	}

	public String getUserReturnAmountString() {
		return new StringBuffer(userReturnAmount.toString()).append(" 积分").toString();
	}

	public String getBusinessRebateAmountString() {
		return new StringBuffer("￥").append(businessRebateAmount).toString();
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	/**
	 * @return the delFlagUser
	 */
	public String getDelFlagUser() {
		return delFlagUser;
	}

	/**
	 * @return the businessIds
	 */
	public Long[] getBusinessIds() {
		return businessIds;
	}

	/**
	 * @param businessIds
	 *            the businessIds to set
	 */
	public void setBusinessIds(Long[] businessIds) {
		this.businessIds = businessIds;
	}

	/**
	 * @param delFlagUser
	 *            the delFlagUser to set
	 */
	public void setDelFlagUser(String delFlagUser) {
		this.delFlagUser = delFlagUser;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", getId());
		map.put("lifeSupplierAdId", getAdBusiness().getId());
		map.put("businessId", getAdBusiness().getBusinessId());
		map.put("company", getAdBusinessCompany());
		map.put("logo", getAdBusiness() != null ? getAdBusiness().getLogo() : null);
		map.put("storeName", getAdBusiness() != null ? getAdBusiness().getStoreName() : null);
		map.put("type", getType());
		map.put("state", getState());
		map.put("orderNumber", getOrderNumber());
		map.put("orderDate", DateUtils.formatDate(getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
		map.put("userRebate", getUserRebate() == null ? "0.0" : getUserRebate().toString());
		map.put("userReturnAmount", getUserReturnAmount().toString());
		map.put("billingState", getBillingState());
		map.put("totalPlatFormPoint", getTotalPlatformPointFromOrderFlow().toString()); // 总的积分消费数额
		map.put("isSource", getIsSource());
		map.put("productType", this.getProductType());
		map.put("goods", this.getGoods());
		map.put("specification", this.getSpecification());
		map.put("consigneeMobile", this.getConsigneeMobile());
		map.put("productImg", this.getProductImg());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (AdOrderDetail adOrderDetail : getAdOrderDetailList()) {
				list.add(adOrderDetail.toMap());
		}
		map.put("orderDetails", list);
		map.put("totalPrice", getTotalPrice());
		map.put("totalAmount", getTotalMount());
		map.put("isUserRebate", getIsUserRebate());
		map.put("payTypeStr", getPayTypeStr());
		map.put("savePrice", getSavePrice());
		map.put("serviceCharge", this.getServiceCharge());
		map.put("voucher", this.getVoucher());
		map.put("openRebateSwitch", this.isOpenRebateSwitch());
		return map;
	}

	/**
	 * 总的积分消费数额
	 *
	 * @return
	 */
	public BigDecimal getTotalPlatformPointFromOrderFlow() {
		BigDecimal total = new BigDecimal("0.00");
		for (AdOrderFlow adOrderFlow : getAdOrderFlowList()) {
			if (adOrderFlow.getPayType().intValue() == 0) {
				total = total.add(adOrderFlow.getAmount());
			}
		}
		return total;
	}


	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public Integer getFirstCount() {
		return firstCount;
	}

	public void setFirstCount(Integer firstCount) {
		this.firstCount = firstCount;
	}

	public BigDecimal getAirSettleAccounts() {
		return airSettleAccounts;
	}

	public void setAirSettleAccounts(BigDecimal airSettleAccounts) {
		this.airSettleAccounts = airSettleAccounts;
	}

	public String getPayTypeStr() {
		return payTypeStr;
	}

	public void setPayTypeStr(String payTypeStr) {
		this.payTypeStr = payTypeStr;
	}

	public BigDecimal getSavePrice() {
		return savePrice;
	}

	public void setSavePrice(BigDecimal savePrice) {
		this.savePrice = savePrice;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
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

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public BigDecimal getVoucher() {
		return voucher;
	}

	public void setVoucher(BigDecimal voucher) {
		this.voucher = voucher;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getInflightRange() {
		return inflightRange;
	}

	public void setInflightRange(String inflightRange) {
		this.inflightRange = inflightRange;
	}

	public String getTouristCardNo() {
		return touristCardNo;
	}

	public void setTouristCardNo(String touristCardNo) {
		this.touristCardNo = touristCardNo;
	}
	public String getTouristPhone() {
		return touristPhone;
	}

	public void setTouristPhone(String touristPhone) {
		this.touristPhone = touristPhone;
	}

	public String getTouristIdCard() {
		return touristIdCard;
	}

	public void setTouristIdCard(String touristIdCard) {
		this.touristIdCard = touristIdCard;
	}

	public boolean isOpenRebateSwitch() {
		return openRebateSwitch;
	}

	public void setOpenRebateSwitch(boolean openRebateSwitch) {
		this.openRebateSwitch = openRebateSwitch;
	}

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}