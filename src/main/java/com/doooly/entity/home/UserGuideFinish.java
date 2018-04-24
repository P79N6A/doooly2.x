package com.doooly.entity.home;

/**
 * @className: UserGuideFinish
 * @description: 兜礼用户完成引导的对象
 * @author: wangchenyu
 * @date: 2018-03-01 15:58
 */
public class UserGuideFinish {
	private Integer id;
	private Integer userId;
	private String businessType;
	private Integer noviceGuideFinished;
	private Integer integralGuideFinished;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Integer getNoviceGuideFinished() {
		return noviceGuideFinished;
	}

	public void setNoviceGuideFinished(Integer noviceGuideFinished) {
		this.noviceGuideFinished = noviceGuideFinished;
	}

	public Integer getIntegralGuideFinished() {
		return integralGuideFinished;
	}

	public void setIntegralGuideFinished(Integer integralGuideFinished) {
		this.integralGuideFinished = integralGuideFinished;
	}
}
