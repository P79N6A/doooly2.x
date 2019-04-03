package com.doooly.common.constants;

import com.google.common.collect.Lists;

import java.util.List;

public class Constants {

	/***
	 * 分享渠道
	 */
	public final static String WECHAT = "wechat";
	public final static String DOOOLY = "doooly";
	public final static String THIRDPARTYCHANNEL = "thirdPartyChannel";//第三方渠道来源

	/** 会员token，唯一标识，放入缓存 */
	public static String TOKEN_KEY = "token:%s";

	/** 会员token名称 */
	public static String TOKEN_NAME = "token";

	/**
	 * channel-终端渠道key 终端类型: wechat-微信端，app-移动app端，h5-浏览器
	 */
	public static final String CHANNEL = "channel";
	public static final String CHANNEL_WECHAT = "wechat";
	public static final String CHANNEL_APP = "app";
	public static final String CHANNEL_H5 = "h5";
	public static final List<String> VIP_VOP_EMPLOYEE_ERROR_LIST = Lists.newArrayList("task has be closed","employee mobile is muti binded","employee status is wrong");
	public static final String VIP_VOP_EMPLOYEE_CODE_SUCCESS="0";
	public static final String VIP_VOP_EMPLOYEE_CODE_FAILURE="1";
	//指定兜礼会员在唯品会的企业ID为16，如果有此异常，认定为升级成功；否则是在唯品会的其它企业，认定升级失败
	public static final String VIP_VOP_EMPLOYEE_SUCCESS_DESC="upgrade enterprise user exception企业用户id已经存在,id号:16";
	//员工已存在唯品会对应企业描述信息
	public static final String VIP_VOP_ENTERPRISE_EXIST_DESC="upgrade enterprise user exception企业用户id已经存在,id号:";
	
	
	/**
	 * 消息响应成功
	 */
	public static String MSG_CODE_SUCCESS = "0";
	/**
	 * 消息响应失败
	 */
	public static String MSG_CODE_ERROR = "101";
	
	public static class MerchantApiConstants{
		//商家接口base url
		public final static String MERCHANT_BASE_URL=PropertiesConstants.dooolyBundle.getString("ws.url");
		public final static String CHECK_VERIFICATION_CODE_URL = MERCHANT_BASE_URL+"services/rest/checkVerificationCode";
	}

	public static class OrderApiConstants{
		//订单接口base url
		public final static String ORDER_BASE_URL = PropertiesConstants.dooolyBundle.getString("project.order.url");
		public final static String SHOP_CART_URL = "shopCart/batch/v1/";
	}

	/**
	 * 消息响应处理结果响应码常量类
	 * <p>
	 * Title: ResponseEnum
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * <p>
	 * Company:
	 * </p>
	 * 
	 * @author hutao
	 * @date 2016年6月20日 下午3:12:01
	 */
	public enum ResponseEnum {
		SUCCESS(MSG_CODE_SUCCESS, "OK"), ERROR_SIGN("2001", "签名验证错误"), ERROR_MOBILE_NOT_EXIST("2002",
				"会员手机号不存在"), ERROR_NAME_NOT_EXIST("2005", "会员姓名不存在"), ERROR_GROUP_NOT_EXIST("2003",
						"企业编号不存在"), ERROR_SERIAL_NOT_EXIST("2004", "积分兑换流水号不存在"), ERROR_PARAM_BLANK("2000",
								"参数不能为空"), ERROR_ACTION_EXPIRED("2005", "登录时间已超过规定时间"), ERROR(MSG_CODE_ERROR, "error");

		private String code;
		private String msg;

		private ResponseEnum(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		private ResponseEnum(String code) {
			this.code = code;
		}

		/**
		 * 根据code码获取出错信息
		 * 
		 * @param code
		 * @return
		 */
		public static String getName(String code) {
			for (ResponseEnum c : ResponseEnum.values()) {
				if (c.getCode() == code) {
					return c.msg;
				}
			}
			return null;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	/**
	 * 返回码枚举类(结果响应码)
	 * 
	 * @author 赵清江
	 * @date 2016年7月12日
	 * @version 1.0
	 */
	public enum ResponseCode {
		/**
		 * 成功
		 */
		SUCCESS("1000", "SUCCESS"),
		/**
		 * 2000 - 2999 失败(影响使用)
		 */
		CHANNEL_NAME_OR_PWD_ERROR_FAILURE("2000", "渠道用户名或密码错误"), ACCOUNT_NOT_EXSIT_FAILURE("2001", "会员账户不存在"), ACCOUNT_PWD_ERROR_FAILURE("2002", "会员密码错误"), ACCOUNT_NOT_ACTIVE_FAILURE("2003", "会员账户未激活"), ACCOUNT_FORBIDDEN_FAILURE("2004", "会员账号被禁用"), ACCOUNT_VERIFY_CODE_FAILURE("2005", "验证码验证错误"), ACCOUNT_MOBILE_REGISERED_FAILURE("2006", "该手机号码已被绑定"), ACCOUNT_ACTIVE_CODE_NOT_EXSIT_FAILURE("2007", "该激活码不存在(无效)"), ACCOUNT_ACTIVE_CODE_IS_USED("2008", "激活码已使用(该账户已激活)"), ACCOUNT_GET_VERIFY_CODE_FAILURE("2009", "验证码获取失败"), ACCOUNT_MOBILE_FORMAT_ERROR_FAILURE("2010", "手机号格式错误"), ACCOUNT_NOT_MATCH_THE_CODE("2011", "手机号与验证码不匹配"), ACCOUNT_MOBILE_ALREADY_BINDED("2012", "该手机已绑定会员卡"),

		SECRET_KEY_FAILURE("2015", "密钥失效"), APP_TOKEN_FAILURE("2016", "Token失效"),
		/**
		 * 3000 - 3999 异常(不影响使用)
		 */
		DUPLICATE_LOGIN("3000", "账号在其它手机设备登录"),
		/**
		 * 4000 - 4999 错误(服务器内部处理出现问题)
		 */
		SERVER_INTERNAL_ERROR("4000", "服务器内部错误"), SERVER_UNKOWN_ERROR("4001", "服务器未知错误")

		;

		private String code;
		private String desc;

		private ResponseCode(String code, String desc) {
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

	/**
	 * 服务器提供的响应信息
	 * 
	 * @author 赵清江
	 * @date 2016年7月14日
	 * @version 1.0
	 */
	public enum ResponseMsg {
		MOBILE_FORMAT_EEROR("手机号格式错误"), CARD_NUMBER_FORMAT_EEROR("会员号不存在"), ACTIVE_CODE_NOT_EXIST(
				"会员号与激活码不匹配,请检查后重新输入!"), ACTIVE_CODE_IS_USED("该账户已激活");

		private String msg;

		private ResponseMsg(String msg) {
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	public enum ResultMsg {

		RET_SUCCESS("1000", "SUCCESS"), RET_FAIL("1001", "FAIL");

		private ResultMsg(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		private String code;
		private String msg;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}

	/**
	 * 密钥状态码(0:有效;1:无效)
	 * 
	 * @author Albert(赵清江)
	 * @date 2016-7-8
	 * @version 1.0
	 */
	public enum SecretKeyStatus {
		VALID(0), INVALID(1);

		private int value;

		private SecretKeyStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * 密钥拥有者判定码(0:服务器;1:客服端) 一个密钥分公钥和私钥,公钥用于加密,私钥用于解密
	 * 服务器用一个密钥的公钥加密,客服端用该私钥解密,该密钥属于服务器 客服端用一个密钥的公钥加密,服务器用该私钥解密,该密钥属于客服端
	 * 
	 * @author Albert(赵清江)
	 * @date 2016-7-8
	 * @version 1.0
	 */
	public enum SecretKeyOwner {
		Server(0), Client(1);

		private int value;

		private SecretKeyOwner(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * 密钥对,一个密钥对包含公钥和私钥 该枚举类用于标记(选择)公钥和私钥
	 * 
	 * @author 清江
	 * @date 2016-07-10
	 * @version 1.0
	 */
	public enum SecretKeyPair {
		ServerPublicKey(0, "ServerPublicKey"), ServerPrivateKey(1, "ServerPrivateKey"), ClientPublicKey(2,
				"ClientPublicKey"), ClientPrivateKey(3, "ClientPrivateKey");

		private int value;
		private String desc;

		private SecretKeyPair(int value, String desc) {
			this.desc = desc;
			this.value = value;
		}

		public static String getDesc(int value) {
			for (SecretKeyPair keyPair : SecretKeyPair.values()) {
				if (keyPair.getValue() == value) {
					return keyPair.getDesc();
				}
			}
			return null;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	/**
	 * App token 状态码(有效,无效)
	 * 
	 * @author 赵清江
	 * @date 2016年7月12日
	 * @version 1.0
	 */
	public enum AppTokenStatus {
		Valid(0), Invalid(1);

		private int value;

		private AppTokenStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * 客服端设备状态码(说明该设备是否有会员账号正在使用)
	 * 
	 * @author 赵清江
	 * @date 2016年7月12日
	 * @version 1.0
	 */
	public enum AppClientStatus {
		Using(0), NotUsing(1);

		private int value;

		private AppClientStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * 客服端类型(安卓,ios,微信,pc网页)
	 * 
	 * @author 赵清江
	 * @date 2016年7月12日
	 * @version 1.0
	 */
	public enum AppClientType {
		Android(0), IOS(1), WX(2), PC(3);

		private int value;

		private AppClientType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * 会员类型(企业正式员工,员工亲属)
	 * 
	 * @author 赵清江
	 * @date 2016年7月14日
	 * @version 1.0
	 */
	public enum MemberType {
		Employee(0), Employee_Relation(1), Retired(2), Dimssion(3), Social(4), Reject_Push(5);

		private int value;

		private MemberType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

    public static final String PROJECT_ACTIVITY_URL = PropertiesConstants.dooolyBundle.getString("project.activity.url");

    public static final String GIFT_ORDER_TYPE = "gift_order";

    public static final String GIFT_ORDER_REDIS_MESS = "gift_order_message_";

}
