package com.doooly.business.common.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 激活码服务接口
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
public interface AdActiveCodeServiceI {

	public static final int ACTIVE_CODE_NOT_EXIST = 90001;
	
	public static final int ACTIVE_CODE_USED = 90002;
	
	public static final int ACTIVE_CODE_NOT_USED = 90003;
	
	/**
	 * 该会员卡号对应的激活码是否有效
	 * @param cardNumber
	 * @param code
	 * @return
	 */
	public int isActiveCodeValid(String cardNumber,String code) throws Exception;
	/**
	 * 使用激活码
	 * @param cardNumber
	 * @return
	 * @throws Exception
	 */
	public int useActiveCode(String cardNumber) throws Exception;


	JSONObject validateFordUser(String code, String mobile, String staffNum, String email, String groupId,String verificationCode) throws Exception;
	
}
