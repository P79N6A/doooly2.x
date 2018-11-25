package com.doooly.common.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ThirdPartySMSConstatns {
	private static ResourceBundle smsBundle = PropertiesConstants.smsBundle;
	/**
	 * 阿里大鱼短信配置
	* <p>Title: AlidayuConfig</p>
	* <p>Description: </p>
	* <p>Company: </p> 
	* @author hutao
	* @date 2016年8月25日 上午10:40:16
	 */
	public static class AlidayuConfig{
		public static String URL = smsBundle.getString("alidayu.url");
		public static String APP_KEYS = smsBundle.getString("alidayu.appKey");
		public static String APP_SECRETS = smsBundle.getString("alidayu.appSecret");
		public static String SMS_FREE_SIGN_NAME = "兜礼";
		public static String SMS_TEMPLATE_CODES = smsBundle.getString("alidayu.SmsTemplateCode");
		//支持多通道切换
		public static List<AlidayuSMSSwitch> alidayuSmsSwitchList = AlidayuSMSSwitch.init();;
		public static class AlidayuSMSSwitch{
			public String APP_KEY;
			public String APP_SECRET;
			public String SMS_TEMPLATE_CODE;
			
			/**
			 * 初始化短信通道配置列表
			 */
			static List<AlidayuSMSSwitch> init(){
				List<AlidayuSMSSwitch> smsSwitchList = new ArrayList<>();
				String[] appKeys = APP_KEYS.split(",");
				String[] appSecrets = APP_SECRETS.split(",");
				String[] appTemplateCodes = SMS_TEMPLATE_CODES.split(",");
				AlidayuSMSSwitch sms = null;
				for(int index=0;index<appKeys.length;index++){
					sms = new AlidayuSMSSwitch();
					sms.APP_KEY = appKeys[index];
					sms.APP_SECRET = appSecrets[index];
					sms.SMS_TEMPLATE_CODE = appTemplateCodes[index];
					smsSwitchList.add(sms);
				}
				return smsSwitchList;
			}
		}
	}
	/**
	 * 嘉戈短信通道配置
	 * 
	* <p>Title: JiayiConfig</p>
	* <p>Description: </p>
	* <p>Company: </p> 
	* @author hutao
	* @date 2016年8月25日 上午10:48:16
	 */
	public static class JiayiConfig{
		public static String URL = smsBundle.getString("jiayi.url");
		public static String USERID = smsBundle.getString("jiayi.userid");
		public static String ACCOUNT = smsBundle.getString("jiayi.account");
		public static String PASSWORD = smsBundle.getString("jiayi.password");
		public static String SMS_TEMPLATE = PropertiesConstants.string2Utf8(smsBundle.getString("jiayi.smsTemplete"));
	}
	
	public static class SMSSendConfig{
		public static Integer MOBILE_MAX_COUNT=200;//阿里大鱼默认一次批量发送200手机号
		
	}
	
	/** 短信内容参数配置 */
	public static class SMSInfoConfig{
		/** 罗森alidayuFlag */
		public static Boolean luosen_alidayuFlag = true;
		/** 罗森alidayuSmsCode */
		public static String luosen_alidayuSmsCode = smsBundle.getString("luosen.alidayuSmsCode");
		/** 罗森smsContent */
		public static String luosen_smsContent = smsBundle.getString("luosen.smsContent");
	}

	/** 短信模板参数配置 */
	public static class SMSTemplateConfig{
		/** 充值失败短信模板 */
		public static String recharge_fail_template_code = "SMS_145596520";
		/** 退款成功短信模板 */
		public static String refund_success_template_code = "SMS_145596516";
		/** 发送卡号卡密模板 */
		public static String send_card_template_code = "SMS_124330065";
		/** 发送活动报名成功短信模版 */
		public static String sign_up_success_template_code = "SMS_124330065";
	}
}
