package com.doooly.dto.common;

public class ConstantsLogin {
	/** APP用户认证token缓存key */
	public static final String TOKEN = "token";

	/**
	 * channel-终端渠道key 终端类型: wechat-微信端，app-移动app端，h5-浏览器
	 */
	public static final String CHANNEL = "channel";
	public static final String CHANNEL_WECHAT = "wechat";
	public static final String CHANNEL_APP = "app";
	public static final String CHANNEL_H5 = "h5";

	/** code名称 */
	public static final String CODE = "code";
	/** msg名称 */
	public static final String MSG = "msg";
	/** data名称 */
	public static final String DATA = "data";
	/** mess名称 */
	public static final String MESS = "mess";

	/** 登录相关参数 */
	public enum Login {

		SUCCESS("1000", "操作成功"), FAIL("1001", "系统错误"), PARAM_ERROR("1002", "参数或【token/channel】为空"), USER_NOT_EXIST(
				"1004", "用户信息不存在"), USER_DELETED("1005", "用户信息已删除"), USER_NOT_ACTIVED("1006",
						"用户未激活"), FREEZE_OR_CANCEL("1007", "用户已冻结或已注销"), PASSWORD_ERROR("1008", "密码错误"), USER_ID_DIFF(
								"1009", "A、B库用户ID不匹配"), IS_CHECKING("1010", "员工信息正在审核"), CHECK_NOT_PASS("1011",
										"审核未通过"), TELLOGIN_TYPE("1012", "切换到验证码登录，自动激活");
		// 成员变量
		private String code;
		private String msg;

		// 构造方法
		private Login(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public String getCode() {
			return this.code;
		}

		public String getMsg() {
			return this.msg;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.code + "_" + this.msg;
		}
	}

	/** 验证码相关参数 */
	public enum ValidCode {

		SEND_SUCCESS("1000", "发送成功"), VALID_SUCCESS("1000", "验证成功"), FAIL("1001", "系统错误"), PARAM_ERROR("1002",
				"参数或【token/channel】为空"), VALID_ERROR("1102", "验证失败"), MOBILE_BLANK("1103", "手机号为空");
		// 成员变量
		private String code;
		private String msg;

		// 构造方法
		private ValidCode(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public String getCode() {
			return this.code;
		}

		public String getMsg() {
			return this.msg;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.code + "_" + this.msg;
		}
	}

	/** 专属码激活相关参数 */
	public enum CodeActive {

		SUCCESS("1000", "操作成功"), FAIL("1001", "系统错误"), PARAM_ERROR("1002", "参数或【token/channel】为空"), CODE_NOT_EXIST(
				"1003", "激活码不存在"), IS_USED("1004", "激活码已使用"), CODE_STATE_ERROR("1005", "激活码状态有误"), IS_ACTIVED("1006",
						"激活码对应用户已激活"), WORKERNUM_CODE_NOT_EXIST("1007", "工号或激活码不存在"), USER_NOT_EXIST("1008", "用户信息不存在");
		// 成员变量
		private String code;
		private String msg;

		// 构造方法
		private CodeActive(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public String getCode() {
			return this.code;
		}

		public String getMsg() {
			return this.msg;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.code + "_" + this.msg;
		}
	}

	/** 专属码激活相关参数 */
	public enum CommandActive {

		SUCCESS("1000", "操作成功"), FAIL("1001", "系统错误"), GROUP_NO_DATA("1002",
				"未查询到企业数据"), COMMAND_GROUP_MATCH_ERROR("1003", "口令与所选企业不匹配"), MOBILE_EXIST("1004", "手机号已经存在");
		// 成员变量
		private String code;
		private String msg;

		// 构造方法
		private CommandActive(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public String getCode() {
			return this.code;
		}

		public String getMsg() {
			return this.msg;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.code + "_" + this.msg;
		}
	}
}
