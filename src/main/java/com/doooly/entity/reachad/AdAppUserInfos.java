package com.doooly.entity.reachad;

import java.math.BigDecimal;

/**
 * @className: AdAppUserInfos
 * @description: 兜礼APP_v2.0,0，首页的用户信息
 * @author: wangchenyu
 * @date: 2018-02-23 16:22
 */
public class AdAppUserInfos {
	// 会员姓名
	private String memberName;
	// 企业名称
	private String enterpriseName;
	// 企业logo图片URL
	private String enterpriseLogoURL;
	// 会员头像的URL
	private String memberHeadImgURL;
	// 会员可用积分
	private BigDecimal availablePoints;
	// 会员认证标识，(0-非认证会员,1-认证会员)
	private Integer authFlag;
	// 兜礼APP_v2.0.0，权益新手引导，是否完成，(null-未完成，0-未完成，1-已完成)
	private Integer noviceGuideFinished;
	// 兜礼APP_v2.0.0，积分新手引导，是否完成，(null-未完成，0-未完成，1-已完成)
	private Integer integralGuideFinished;
	// 兜礼APP_v2.0.0，企业员工专属特权的json字符串，{"app-index-privilege":{"business":0,"friends":0,"integral":0,"coupon":0,"birthday":0}}
	private String appIndexPrivilege;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseLogoURL() {
		return enterpriseLogoURL;
	}

	public void setEnterpriseLogoURL(String enterpriseLogoURL) {
		this.enterpriseLogoURL = enterpriseLogoURL;
	}

	public String getMemberHeadImgURL() {
		return memberHeadImgURL;
	}

	public void setMemberHeadImgURL(String memberHeadImgURL) {
		this.memberHeadImgURL = memberHeadImgURL;
	}

	public BigDecimal getAvailablePoints() {
		return availablePoints;
	}

	public void setAvailablePoints(BigDecimal availablePoints) {
		this.availablePoints = availablePoints;
	}

	public Integer getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(Integer authFlag) {
		this.authFlag = authFlag;
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

	public String getAppIndexPrivilege() {
		return appIndexPrivilege;
	}

	public void setAppIndexPrivilege(String appIndexPrivilege) {
		this.appIndexPrivilege = appIndexPrivilege;
	}
}
