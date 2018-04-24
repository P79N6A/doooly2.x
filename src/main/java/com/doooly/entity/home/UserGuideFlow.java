package com.doooly.entity.home;

import com.alibaba.fastjson.JSONObject;
import com.doooly.entity.reachad.AdUserBusinessExpansion;
import org.apache.commons.lang3.StringUtils;

/**
 * @className: UserGuideFlow
 * @description: 用户新手引导完成进度对象
 * @author: wangchenyu
 * @date: 2018-03-06 20:24
 */
public class UserGuideFlow {
	/** 兜礼APP_v2.0.0，权益新手引导，是否完成，(null-未完成，0-未完成，1-已完成) **/
	private Integer noviceGuideFinished;
	/**  兜礼APP_v2.0.0，积分新手引导，是否完成，(null-未完成，0-未完成，1-已完成) **/
	private Integer integralGuideFinished;
	/** 兜礼APP_v2.0.0，企业员工专属特权的json字符串，{"business":0,"friends":0,"integral":0,"coupon":0,"birthday":0} **/
	private JSONObject appIndexPrivilege;

	public UserGuideFlow() {
	}

	public UserGuideFlow(AdUserBusinessExpansion adUserBusinessExpansion) {
		if (adUserBusinessExpansion == null) {
			this.noviceGuideFinished = 0;
			this.integralGuideFinished = 0;
			this.appIndexPrivilege = buildDefaultPrivilegeJSON();
		} else {
			this.noviceGuideFinished = StringUtils.isEmpty(adUserBusinessExpansion.getF1()) ? 0 : Integer.valueOf(adUserBusinessExpansion.getF1());
			this.integralGuideFinished = StringUtils.isEmpty(adUserBusinessExpansion.getF2()) ? 0 : Integer.valueOf(adUserBusinessExpansion.getF2());
			if ( StringUtils.isBlank(adUserBusinessExpansion.getF15()) ) {
				this.appIndexPrivilege = buildDefaultPrivilegeJSON();
			} else {
				JSONObject userPrivilegeJSON = JSONObject.parseObject(adUserBusinessExpansion.getF15()).getJSONObject("app-index-privilege");
				this.appIndexPrivilege = userPrivilegeJSON;
			}
		}
	}

	private JSONObject buildDefaultPrivilegeJSON() {
		JSONObject userPrivilegeJSON = new JSONObject(true);
		userPrivilegeJSON.put("business", 0);
		userPrivilegeJSON.put("friends", 0);
		userPrivilegeJSON.put("integral", 0);
		userPrivilegeJSON.put("coupon", 0);
		userPrivilegeJSON.put("birthday", 0);
		return userPrivilegeJSON;
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

	public JSONObject getAppIndexPrivilege() {
		return appIndexPrivilege;
	}

	public void setAppIndexPrivilege(JSONObject appIndexPrivilege) {
		this.appIndexPrivilege = appIndexPrivilege;
	}
}
