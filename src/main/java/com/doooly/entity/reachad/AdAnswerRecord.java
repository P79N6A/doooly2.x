package com.doooly.entity.reachad;

/**
 * 答题记录Entity
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public class AdAnswerRecord {

	private Long id;
	/** 题库ID */
	private Long questionId;
	/** 选项类型 */
	private String optionType;
	/** 答题人 */
	private Long answerUser;
	/** 答题时间 */
	private String answerTime;
	/** 答题结果(T:"答对" F:"答错" N:"没答") */
	private String answerResult;
	/** 题目分值 */
	private String points;
	/** 连续答题截止标志(周期截止后标记为1) */
	private String continueFlag;
	/** 连续答对截止标志(周期截止后标记为1) */
	private String continueRightFlag;
	/** 连续答题天数 */
	private int continueDays;
	/** 连续答对天数 */
	private int continueRightDays;
	/** 优惠券礼品ID(针对五一答题活动) */
	private String couponId;
	/** 选择此选项增加的年龄(针对五一答题活动) */
	private int age;
	/** 活动ID(针对五一答题活动) */
	private String activityId;
	/** 题目序号 */
	private String orderNum;
	/** 五一答题获取到的券名称 */
	private String productName;
	/** 五一答题获取到的券编号 */
	private String productSn;
	/** 五一答题获取到的券用户手机号 */
	private String telephone;

	/* 需要发券的数量 */
	private int couponCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public Long getAnswerUser() {
		return answerUser;
	}

	public void setAnswerUser(Long answerUser) {
		this.answerUser = answerUser;
	}

	public String getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}

	public String getAnswerResult() {
		return answerResult;
	}

	public void setAnswerResult(String answerResult) {
		this.answerResult = answerResult;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getContinueFlag() {
		return continueFlag;
	}

	public void setContinueFlag(String continueFlag) {
		this.continueFlag = continueFlag;
	}

	public String getContinueRightFlag() {
		return continueRightFlag;
	}

	public void setContinueRightFlag(String continueRightFlag) {
		this.continueRightFlag = continueRightFlag;
	}

	public int getContinueDays() {
		return continueDays;
	}

	public void setContinueDays(int continueDays) {
		this.continueDays = continueDays;
	}

	public int getContinueRightDays() {
		return continueRightDays;
	}

	public void setContinueRightDays(int continueRightDays) {
		this.continueRightDays = continueRightDays;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

}
