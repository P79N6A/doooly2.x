package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

public class AdRechargeConf {

	private int id;
	private String name;		// 配置名称
	private String groupId;//选择的企业
	private BigDecimal monthLimit;		// 每人每月充值话费流量金额限制
	private BigDecimal charges;		// 默认手机话费充值手续费

	private BigDecimal discountsMonthLimit;		// 优惠价格每人每月充值话费流量金额限制
	private BigDecimal cmccCharges;		// 中国移动默认手机话费充值手续费
	private BigDecimal chuCharges;		// 中国联通默认手机话费充值手续费
	private BigDecimal chaCharges;		// 中国电信默认手机话费充值手续费

	private Date discountsStartDate;		// 优惠开始时间
	private Date discountsEndDate;		// 优惠结束时间


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getMonthLimit() {
		return monthLimit;
	}

	public void setMonthLimit(BigDecimal monthLimit) {
		this.monthLimit = monthLimit;
	}

	public BigDecimal getCharges() {
		return charges;
	}

	public void setCharges(BigDecimal charges) {
		this.charges = charges;
	}

	public BigDecimal getDiscountsMonthLimit() {
		return discountsMonthLimit;
	}

	public void setDiscountsMonthLimit(BigDecimal discountsMonthLimit) {
		this.discountsMonthLimit = discountsMonthLimit;
	}

	public BigDecimal getCmccCharges() {
		return cmccCharges;
	}

	public void setCmccCharges(BigDecimal cmccCharges) {
		this.cmccCharges = cmccCharges;
	}

	public BigDecimal getChuCharges() {
		return chuCharges;
	}

	public void setChuCharges(BigDecimal chuCharges) {
		this.chuCharges = chuCharges;
	}

	public BigDecimal getChaCharges() {
		return chaCharges;
	}

	public void setChaCharges(BigDecimal chaCharges) {
		this.chaCharges = chaCharges;
	}

	public Date getDiscountsStartDate() {
		return discountsStartDate;
	}

	public void setDiscountsStartDate(Date discountsStartDate) {
		this.discountsStartDate = discountsStartDate;
	}

	public Date getDiscountsEndDate() {
		return discountsEndDate;
	}

	public void setDiscountsEndDate(Date discountsEndDate) {
		this.discountsEndDate = discountsEndDate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AdRechargeConf{" +
				"id=" + id +
				", name='" + name + '\'' +
				", groupId='" + groupId + '\'' +
				", monthLimit=" + monthLimit +
				", charges=" + charges +
				", discountsMonthLimit=" + discountsMonthLimit +
				", cmccCharges=" + cmccCharges +
				", chuCharges=" + chuCharges +
				", chaCharges=" + chaCharges +
				", discountsStartDate=" + discountsStartDate +
				", discountsEndDate=" + discountsEndDate +
				'}';
	}
}