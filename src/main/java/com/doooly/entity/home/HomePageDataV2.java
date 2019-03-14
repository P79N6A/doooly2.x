package com.doooly.entity.home;

import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdGroupEquityLevel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className: HomePageDataV2
 * @description: 兜礼APP首页，数据对象
 * @author: wangchenyu
 * @date: 2018-02-13 11:57
 */
public class HomePageDataV2 {
	/** 首页显示的会员姓名 **/
	private String memberName;
	/** 首页显示会员所属的企业名称 **/
	private String memberCompanyName;
	/** 首页显示的会员头像，默认企业logo **/
	private String memberHeadImgUrl;
	/** 购物节省金额 **/
	private BigDecimal thriftAmount;
	/** 购物节省笔数 **/
	private Integer thriftTotal;
	/** 会员抵扣券张数 **/
	private Integer couponNum;
	/** 抵扣券，即将过期张数 **/
	private Integer couponExpireNum;
	/** 会员可用积分 (通用积分+定向积分)**/
	private BigDecimal availablePoints;
	//会员定向积分
	private BigDecimal dirIntegralPoints;
	//会员通用积分
	private BigDecimal generalIntegral;
	
	/** 会员认证标识，(0-非认证会员,1-认证会员) **/
	private Integer authFlag;

	private boolean newOrderFlag;
	private boolean newFinishFlag;
	private boolean newCancelFlag;

    private String isPayPassword;
    private String isSetPayPassword;
    //会员返利积分，精确2位小数
    private String returnPoints;

    private AdGroup adGroup;

	private List<AdGroupEquityLevel> groupEquitys;	// 企业权益
	private boolean hasMoreEquity;		// 是否还有更多权益
	private Integer groupLevel;			// 企业等级
	private Integer giftBagCount;		// 未领取企业礼包数量


//	/** 兜礼APP_v2.0.0，权益新手引导，是否完成，(null-未完成，0-未完成，1-已完成) **/
//	private Integer noviceGuideFinished;
//	/**  兜礼APP_v2.0.0，积分新手引导，是否完成，(null-未完成，0-未完成，1-已完成) **/
//	private Integer integralGuideFinished;
//	/** 兜礼APP_v2.0.0，企业员工专属特权的json字符串，{"app-index-privilege":{"business":0,"friends":0,"integral":0,"coupon":0,"birthday":0}} **/
//	private JSONObject appIndexPrivilege;

	public String getMemberName() {
		return memberName;
	}

	public BigDecimal getDirIntegralPoints() {
		return dirIntegralPoints;
	}

	public void setDirIntegralPoints(BigDecimal dirIntegralPoints) {
		this.dirIntegralPoints = dirIntegralPoints;
	}

	public BigDecimal getGeneralIntegral() {
		return generalIntegral;
	}

	public void setGeneralIntegral(BigDecimal generalIntegral) {
		this.generalIntegral = generalIntegral;
	}

	public String getReturnPoints() {
		return returnPoints;
	}

	public void setReturnPoints(String returnPoints) {
		this.returnPoints = returnPoints;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberCompanyName() {
		return memberCompanyName;
	}

	public void setMemberCompanyName(String memberCompanyName) {
		this.memberCompanyName = memberCompanyName;
	}

	public String getMemberHeadImgUrl() {
		return memberHeadImgUrl;
	}

	public void setMemberHeadImgUrl(String memberHeadImgUrl) {
		this.memberHeadImgUrl = memberHeadImgUrl;
	}

	public BigDecimal getThriftAmount() {
		return thriftAmount;
	}

	public void setThriftAmount(BigDecimal thriftAmount) {
		this.thriftAmount = thriftAmount;
	}

	public Integer getThriftTotal() {
		return thriftTotal;
	}

	public void setThriftTotal(Integer thriftTotal) {
		this.thriftTotal = thriftTotal;
	}

	public Integer getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	public Integer getCouponExpireNum() {
		return couponExpireNum;
	}

	public void setCouponExpireNum(Integer couponExpireNum) {
		this.couponExpireNum = couponExpireNum;
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

    public String getIsPayPassword() {
        return isPayPassword;
    }

    public void setIsPayPassword(String isPayPassword) {
        this.isPayPassword = isPayPassword;
    }

    public String getIsSetPayPassword() {
        return isSetPayPassword;
    }

    public void setIsSetPayPassword(String isSetPayPassword) {
        this.isSetPayPassword = isSetPayPassword;
    }

	public AdGroup getAdGroup() {
		return adGroup;
	}

	public void setAdGroup(AdGroup adGroup) {
		this.adGroup = adGroup;
	}

	//	public Integer getNoviceGuideFinished() {
//		return noviceGuideFinished;
//	}
//
//	public void setNoviceGuideFinished(Integer noviceGuideFinished) {
//		this.noviceGuideFinished = noviceGuideFinished;
//	}
//
//	public Integer getIntegralGuideFinished() {
//		return integralGuideFinished;
//	}
//
//	public void setIntegralGuideFinished(Integer integralGuideFinished) {
//		this.integralGuideFinished = integralGuideFinished;
//	}
//
//	public JSONObject getAppIndexPrivilege() {
//		return appIndexPrivilege;
//	}
//
//	public void setAppIndexPrivilege(JSONObject appIndexPrivilege) {
//		this.appIndexPrivilege = appIndexPrivilege;
//	}


	public List<AdGroupEquityLevel> getGroupEquitys() {
		return groupEquitys;
	}

	public void setGroupEquitys(List<AdGroupEquityLevel> groupEquitys) {
		this.groupEquitys = groupEquitys;
	}

	public boolean isHasMoreEquity() {
		return hasMoreEquity;
	}

	public void setHasMoreEquity(boolean hasMoreEquity) {
		this.hasMoreEquity = hasMoreEquity;
	}

	public Integer getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(Integer groupLevel) {
		this.groupLevel = groupLevel;
	}

	public Integer getGiftBagCount() {
		return giftBagCount;
	}

	public void setGiftBagCount(Integer giftBagCount) {
		this.giftBagCount = giftBagCount;
	}
}
