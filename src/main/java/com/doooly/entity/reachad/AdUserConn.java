package com.doooly.entity.reachad;

/**
 * 个人资料
 * 
 * @author yuelou.zhang
 * @version 2017年7月19日
 */
public class AdUserConn {
	private long id;
	/** 会员id */
	private String userId;
	/** 企业LOGO链接地址 */
	private String logoUrl;
	/** 企业Mini LOGO链接地址 */
	private String miniLogoUrl;
	/** 企业宣传图片链接地址 */
	private String displayUrl;
	/** 企业背景图片链接地址(APP端家属完善资料时显示此图) */
	private String backgroundUrl;
	/** app会员头像地址 */
	private String appHeadImageUrl;
	/** 姓名 */
	private String name;
	/** 手机号 */
	private String telephone;
	/** 所属企业 */
	private String groupName;
	/** 企业表主键 */
	private String groupId;
	/** 企业编号 */
	private String groupNum;
	/** 企业部门 */
	private String departmentName;
	/** 工号 */
	private String workNumber;
	/** 性别 */
	private String sex;
	/** 生日 */
	private String birthday;
	/** 会员卡号 */
	private String cardNumber;
	/** 会员类型(0:员工 1:家属) */
	private String userType;
	/** 家属关系（1：夫妻 2：子女 3：父母 4：亲戚 5：朋友 6：其他） */
	private String familyRelation;
	/** 家属的邀请人姓名 */
	private String inviterName;
	/** 可用卡券张数 */
	private int unuseCouponsNum;
	/** 可用积分 */
	private String availableIntegral;
	/** 待返积分 */
	private String returnIntegral;
	/** 累计积分 */
	private String totalIntegral;
	/** 我的福利是否有可用的新券(false:无 true:有) */
	private boolean hasNewCoupon;
	/** 是否激活 */
	private String isActive;

	private String delFlag;
	/** 企业简称 */
	private String groupShortName;
	/** 认证标志,默认为认证会员(0-非认证会员,1-认证会员) */
	private String authFlag;
	/** 是否有“已下单”新订单 */
	private boolean newOrderFlag;
	/** 是否有“已完成”新订单 */
	private boolean newFinishFlag;
	/** 是否有“已取消”新订单 */
	private boolean newCancelFlag;
	/** “已下单”订单数 */
	private Integer orderTotal;
	/** “已完成”订单数 */
	private Integer finishTotal;
	/** “已取消”订单数 */
	private Integer cancelTotal;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getMiniLogoUrl() {
		return miniLogoUrl;
	}

	public void setMiniLogoUrl(String miniLogoUrl) {
		this.miniLogoUrl = miniLogoUrl;
	}

	public String getDisplayUrl() {
		return displayUrl;
	}

	public void setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl;
	}

	public String getBackgroundUrl() {
		return backgroundUrl;
	}

	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}

	public String getAppHeadImageUrl() {
		return appHeadImageUrl;
	}

	public void setAppHeadImageUrl(String appHeadImageUrl) {
		this.appHeadImageUrl = appHeadImageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFamilyRelation() {
		return familyRelation;
	}

	public void setFamilyRelation(String familyRelation) {
		this.familyRelation = familyRelation;
	}

	public String getInviterName() {
		return inviterName;
	}

	public void setInviterName(String inviterName) {
		this.inviterName = inviterName;
	}

	public int getUnuseCouponsNum() {
		return unuseCouponsNum;
	}

	public void setUnuseCouponsNum(int unuseCouponsNum) {
		this.unuseCouponsNum = unuseCouponsNum;
	}

	public String getAvailableIntegral() {
		return availableIntegral;
	}

	public void setAvailableIntegral(String availableIntegral) {
		this.availableIntegral = availableIntegral;
	}

	public String getReturnIntegral() {
		return returnIntegral;
	}

	public void setReturnIntegral(String returnIntegral) {
		this.returnIntegral = returnIntegral;
	}

	public String getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(String totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public boolean isHasNewCoupon() {
		return hasNewCoupon;
	}

	public void setHasNewCoupon(boolean hasNewCoupon) {
		this.hasNewCoupon = hasNewCoupon;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getGroupShortName() {
		return groupShortName;
	}

	public void setGroupShortName(String groupShortName) {
		this.groupShortName = groupShortName;
	}

	public String getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}

	public boolean isNewOrderFlag() {
		return newOrderFlag;
	}

	public void setNewOrderFlag(boolean newOrderFlag) {
		this.newOrderFlag = newOrderFlag;
	}

	public boolean isNewFinishFlag() {
		return newFinishFlag;
	}

	public void setNewFinishFlag(boolean newFinishFlag) {
		this.newFinishFlag = newFinishFlag;
	}

	public boolean isNewCancelFlag() {
		return newCancelFlag;
	}

	public void setNewCancelFlag(boolean newCancelFlag) {
		this.newCancelFlag = newCancelFlag;
	}

	public Integer getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Integer orderTotal) {
		this.orderTotal = orderTotal;
	}

	public Integer getFinishTotal() {
		return finishTotal;
	}

	public void setFinishTotal(Integer finishTotal) {
		this.finishTotal = finishTotal;
	}

	public Integer getCancelTotal() {
		return cancelTotal;
	}

	public void setCancelTotal(Integer cancelTotal) {
		this.cancelTotal = cancelTotal;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}