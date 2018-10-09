package com.doooly.common.constants;

public class ActivityConstants {

	public final static String ACTIVITY_TYPE = "WECHAT_MSG";
	// 兜礼裂变活动版本1
	public final static String FISSION_V1_ACTIVITY = "fission_v1_activity";
	public final static String DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_NOTICE = "DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_NOTICE";
	public final static String DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_COMPLETION = "DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_COMPLETION";
	public final static String DOOOLY_FISSION_V1_ACTIVITY_INVITATION_NUMBER = "DOOOLY_FISSION_V1_ACTIVITY_INVITATION_NUMBER";
	public final static String DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_TEXT_MSG = "DOOOLY_FISSION_V1_ACTIVITY_TEMPLATE_TEXT_MSG";
	
	// 540充600话费活动
	public static final String RECHARGE_ACTIVITY = "recharge_activity";
	
	public static enum ActivityEnum{
		ACTIVITY_NOT_EXIST("2010", "活动不存在"),
		ACTIVITY_CLOSED("2011", "活动已关闭"),
		ACTIVITY_NOT_STARTED("2012", "活动尚未开始"),
		ACTIVITY_ENDED("2013", "活动已结束"),
		ACTIVITY_NOT_PARTICIPATING("2014", "企业未参与该活动"),
		ACTIVITY_RECEIVED("2015", "已领取"),
		ACTIVITY_VERIFICATION_CODE_ERROR("2016", "验证码验证失败"),
		;
		private String code;
		private String desc;
		ActivityEnum(String code, String desc){
			this.code = code;
			this.desc = desc;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
		
	}
}
