package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预计积分Entity
 * @author XueLing
 * @version 2015-12-14
 */
public class AdReturnPoints {
	
	private static final long serialVersionUID = 1L;
    private String id ;
    private String userId;//adUserId
    private Long orderId;   //订单编号
    private String reportId ;
	private BigDecimal amount;		// 预计返利
	private String type;		// 积分交易类型
	private String status;		// 状态标识
	private Date createDate;	// 时间
    private String company;  //商户名称
    private String storeName;  //门店名称
    private String logo;  //门店logo
    private String createDateStr;  //积分日志字符串格式
    private String receiveStuts;

	/** 预计积分表 积分交易类型：消费 */
	public static final String TYPE_CONSUME = "1";
	
	/** 预计积分表 积分交易类型：返利 */
	public static final String TYPE_REBATE = "2";
	
	/** 预计积分表 积分交易类型：睿仕充值 */
	public static final String TYPE_RECHARGE_BY_REACH = "3";
	
	/** 预计积分表 积分交易类型：积分赠送 */
	public static final String TYPE_PRESENT  = "4";
	
	/** 预计积分表 积分交易类型：积分互换 */
	public static final String TYPE_INTERCHANGE = "5";
	
	/** 预计积分表 积分交易类型：积分申诉 */
	public static final String TYPE_REBATE_COMPLAINT = "6";
	
	/** 预计积分表 积分交易类型：积分充值 */
	public static final String TYPE_RECHARGE_BY_SELF = "7";
	
	/** 预计积分表 状态标识：已获得 */
	public static final String STATUS_OBTAINED = "1";
	
	/** 预计积分表 状态标识：预计获得 */
	public static final String STATUS_EXPECTED = "2";
	
	/** 预计积分表 状态标识：失效 */
	public static final String STATUS_INVALID = "3";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

	public String getReceiveStuts() {
		return receiveStuts;
	}

	public void setReceiveStuts(String receiveStuts) {
		this.receiveStuts = receiveStuts;
	}
}