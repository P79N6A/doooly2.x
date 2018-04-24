package com.doooly.entity.reachad;

/**
 * 题库选项Entity
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public class AdQuestionOption {

	private Long id;
	/** 题库ID */
	private Long questionId;
	/** 选项类型 */
	private String type;
	/** 选项名称 */
	private String description;
	/** 所属题目 */
	private AdQuestionBank topic;
	/** 题目序号 */
	private String questionOrder;
	/** 优惠券礼品ID(针对五一答题活动) */
	private String couponId;
	/** 选择此选项增加的年龄(针对五一答题活动) */
	private int age;
	/** 活动ID(针对五一答题活动) */
	private String activityId;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AdQuestionBank getTopic() {
		return topic;
	}

	public void setTopic(AdQuestionBank topic) {
		this.topic = topic;
	}

	public String getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(String questionOrder) {
		this.questionOrder = questionOrder;
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

}
