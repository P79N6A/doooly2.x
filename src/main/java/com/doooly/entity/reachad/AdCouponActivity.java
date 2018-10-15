package com.doooly.entity.reachad;

import java.util.Date;

/**
 * 卡券活动表Entity
 * 
 * @author lxl
 * @version 2016-12-14
 */
public class AdCouponActivity {
	public static Integer ACTIVITY_OPEN = 1;
	public static Integer ACTIVITY_CLOSE = 0;

	private int id;
	private String name;// 活动名称
	private String state;// 活动状态，默认为0 （0-关闭，1-开启）
	private int create_user;
	private Date create_date;
	private int update_user;
	private Date update_date;
	private Date beginDate; // 生效开始时间
	private Date endDate; // 生效结束时间
	private int optionCount;// 候选项数量（默认为0）
	private int voteCount;// 活动总投票数（默认为0）
	private int pvCount;// 网页访问次数（默认为0）
	private String ruleImage;// 规则图片
	private String endDateStr;// 结束时间格式化
	private String receiveType;// 领取类型，默认为0（0-可直接领取，1-报名领取）
	private String activityType;// 活动类型(如：新手开卡，0元抢券，签到答题...对应表-)
	private String plateformType;// 适用平台(0-微信端)
	private String businessId;// 商家编号
	private String listImageUrl;// 活动列表页图片地址
	private String detailImageUrl;// 活动详情页图片地址
	private String introduction;// 活动介绍
	private String activityLinkUrl;// 活动链接
	private int sort;// 排序号
	private String activityCode;// 活动编号
	private String deleteFlag;// 删除标记（0-正常，1-删除）
	private String category;// N活动券显示对应一级分类
	private String description;// 活动描述
	private Integer userCount;
	private String idFlag;
	private int couponCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getCreate_user() {
		return create_user;
	}

	public void setCreate_user(int create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public int getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(int update_user) {
		this.update_user = update_user;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(int optionCount) {
		this.optionCount = optionCount;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}

	public int getPvCount() {
		return pvCount;
	}

	public void setPvCount(int pvCount) {
		this.pvCount = pvCount;
	}

	public String getRuleImage() {
		return ruleImage;
	}

	public void setRuleImage(String ruleImage) {
		this.ruleImage = ruleImage;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getPlateformType() {
		return plateformType;
	}

	public void setPlateformType(String plateformType) {
		this.plateformType = plateformType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getListImageUrl() {
		return listImageUrl;
	}

	public void setListImageUrl(String listImageUrl) {
		this.listImageUrl = listImageUrl;
	}

	public String getDetailImageUrl() {
		return detailImageUrl;
	}

	public void setDetailImageUrl(String detailImageUrl) {
		this.detailImageUrl = detailImageUrl;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getActivityLinkUrl() {
		return activityLinkUrl;
	}

	public void setActivityLinkUrl(String activityLinkUrl) {
		this.activityLinkUrl = activityLinkUrl;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public String getIdFlag() {
		return idFlag;
	}

	public void setIdFlag(String idFlag) {
		this.idFlag = idFlag;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

}