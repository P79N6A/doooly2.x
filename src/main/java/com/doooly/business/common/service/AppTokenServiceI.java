package com.doooly.business.common.service;

import com.doooly.entity.reachad.AppToken;

/**
 * 
 * @author Albert
 *
 */
public interface AppTokenServiceI {
	/**
	 * 获取新token
	 * @return
	 */
	public String getNewToken();
	/**
	 * 会员卡号或手机号是否绑定
	 * @param token
	 * @return
	 */
	public boolean isBind(String account);
	/**
	 * 新增绑定token
	 * @param token
	 * @param deviceNumber
	 * @return
	 */
	public String newBind(AppToken token);
	/**
	 * 更新绑定token
	 * @param token
	 * @param deviceNumber
	 * @return
	 */
	public String renewBind(AppToken token);
	/**
	 * 解除绑定
	 * @param token
	 * @return
	 */
	public boolean unBind(AppToken token);
	
}
