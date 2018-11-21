package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账系统可用积分Entity
 * 
 * @author yhy
 * @version 2015-12-14
 */
public class AdAvailablePoints {

	private Integer id;
	private String userId;// adUserId
	private String rechargeId; // 充值明细编号
	private Long reportId; // 订单报表编号
	private String type; // 积分交易类型
	private String status; // 积分状态
	private BigDecimal businessRebateAmount; // 积分数量
	private Long orderId; // 订单编号
	private Date createDate; // 开始时间
	private String company; // 商户名称
	private String storeName; // 门店名称
	private String logo; // 门店logo
	private String createDateStr; // 积分日志字符串格式
	private String groupName; // 充值企业名称
	private String groupShortName; // 充值企业简称
	private String groupLogo; // 充值企业logo
	private String integralName;//积分活动积分池名称
	/** 可用积分表 积分交易类型：企业充值 对应type字段 */
	public static final String TYPE_RECHARGE_BY_COMPANY = "0";

	/** 可用积分表 积分交易类型：消费 */
	public static final String TYPE_CONSUME = "1";

	/** 可用积分表 积分交易类型：返利 */
	public static final String TYPE_REBATE = "2";

	/** 可用积分表 积分交易类型：睿仕充值 */
	public static final String TYPE_RECHARGE_BY_REACH = "3";

	/** 可用积分表 积分交易类型：积分赠送 */
	public static final String TYPE_PRESENT = "4";

	/** 可用积分表 积分交易类型：积分互换 */
	public static final String TYPE_INTERCHANGE = "5";

	/** 可用积分表 积分交易类型：积分申诉 */
	public static final String TYPE_REBATE_COMPLAINT = "6";

	/** 可用积分表 积分交易类型：积分充值 */
	public static final String TYPE_RECHARGE_BY_SELF = "7";

	/** 可用积分表 积分交易类型：退货积分 */
	public static final String TYPE_RETURN = "8";

	/** 可用积分表 积分交易类型：退货返利 */
	public static final String TYPE_RETRUN_REBATE = "9";
	
	/** 可用积分表 积分交易类型：积分活动 */
	public static final String TYPE_INTEGRAL_ACTIVITY = "10";
	/** 可用积分表 积分交易类型：积分签到 */
	public static final String TYPE_INTEGRAL_ATTEND = "11";

	/** 可用积分表 状态标识：已获得 */
	public static final String STATUS_OBTAINED = "1";

	/** 可用积分表 状态标识：预计获得 */
	public static final String STATUS_EXPECTED = "2";

	/** 可用积分表 状态标识：失效 */
	public static final String STATUS_INVALID = "3";

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(String rechargeId) {
		this.rechargeId = rechargeId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getBusinessRebateAmount() {
		return businessRebateAmount;
	}

	public void setBusinessRebateAmount(BigDecimal businessRebateAmount) {
		this.businessRebateAmount = businessRebateAmount;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupShortName() {
		return groupShortName;
	}

	public void setGroupShortName(String groupShortName) {
		this.groupShortName = groupShortName;
	}

	public String getGroupLogo() {
		return groupLogo;
	}

	public void setGroupLogo(String groupLogo) {
		this.groupLogo = groupLogo;
	}

	public String getIntegralName() {
		return integralName;
	}

	public void setIntegralName(String integralName) {
		this.integralName = integralName;
	}
	
}