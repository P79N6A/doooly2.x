package com.doooly.business.common.service;

import java.util.HashMap;

/**
 * 会员信息表xx_member服务接口
 * 
 * @author 赵清江
 * @date 2016年7月18日
 * @version 1.0
 */
public interface LifeMemberServiceI {

	/**
	 * A系统会员激活
	 * 
	 * @param member
	 * @return
	 */
	public boolean memberActivation(String cardNumber);

	/**
	 * A系统会员激活-登录
	 * 
	 * @param member
	 * @return
	 */
	public boolean memberActivationLogin(HashMap<String, Object> param);

}
