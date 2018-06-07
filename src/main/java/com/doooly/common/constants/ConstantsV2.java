package com.doooly.common.constants;

public class ConstantsV2 {
	
	/**
	 * 系统全局异常
	 * 
	    * @ClassName: SystemCode  
	    * @author hutao  
	    * @date 2018年2月11日  
	    *
	 */
	public enum SystemCode {
		SUCCESS(1000, "成功"),
		SUCCESS_NULL(10003, "成功"),
		SYSTEM_ERROR(30000, "系统异常");
		
		
		private Integer code;
		private String msg;
		
		private SystemCode(Integer code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public Integer getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}

	}
	
	/**
	 * 
	    * @ClassName: MemberCode  
	    * @Description: 会员模块异常码  
	    * @author hutao  
	    * @date 2018年2月11日  
	    *
	 */
	public enum MemberCode{
		NOT_EXIST(10001,"会员不存在"),
		PASSWORD_ERROR(10002,"会员密码错误");
		
		private Integer code;
		private String msg;
		
		private MemberCode(Integer code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public Integer getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	/**
	 * 
	    * @ClassName: IntegralCode  
	    * @Description: 积分模块异常码
	    * @author hutao  
	    * @date 2018年2月11日  
	    *
	 */
	public enum IntegralCode{
		QUERY_ERROR(13001,"会员密码错误"),
		NOT_EXIT(13002,"卡密不存在"),
		INCONFORMITY_USER(13003,"充值人与激活人不符"),
		CAN_NOT_USE(13004,"卡密未激活"),
		IS_USED(13005,"卡密已使用"),
		MAX_FAIL_COUNT(13006,"超过最大失败次数"),
		NOT_ACTIVATE(13007,"未激活"),
		WRONG_TIME(13008,"未在使用期限内"),
		IS_FREEZE(13009,"已冻结");
		private Integer code;
		private String msg;
		
		private IntegralCode(Integer code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public Integer getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	/**
	 * 
	    * @ClassName: OrderCode  
	    * @Description: 订单异常码  
	    * @author hutao  
	    * @date 2018年2月11日  
	    *
	 */
	public enum OrderCode{
		SELL_OUT(13001,"商品已售罄");
		
		private Integer code;
		private String msg;
		
		private OrderCode(Integer code, String msg) {
			this.code = code;
			this.msg = msg;
		}

		public Integer getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	/**
	 * 
	    * @ClassName: ActivityCode  
	    * @Description: 活动异常码定义
	    * @author hutao  
	    * @date 2018年2月11日  
	    *
	 */
	public enum ActivityCode{
		NOT_STARTED(11001,"活动未开始"),
		DONE(11002,"活动已结束"),
		HAD_ALREADY(11003,"已参加过活动"),
		HAD_NONE(11004,"库存不足")
		;
		
		private Integer code;
		private String msg;
		
		private ActivityCode(Integer code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public Integer getCode() {
			return code;
		}
		
		public String getMsg() {
			return msg;
		}
	}
}
