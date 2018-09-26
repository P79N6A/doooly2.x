package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * 
 * 用户访问token
 * 
 * @Title: TokenRestServiceI.java
 * @Package com.doooly.publish.rest.life
 * @author hutao
 * @date 2017年7月25日
 * @version V1.0
 */
public interface TokenRestServiceI {
	/**
	 * 
	 * Token认证
	 * 
	 * @Title: TokenRestServiceI.java
	 * @author linking
	 * @date 2017年7月27日
	 * @version V1.0
	 */
	JSONObject validUserToken(JSONObject json);

	/**
	 * 
	 * 获取用户Token
	 * 
	 * @Title: TokenRestServiceI.java
	 * @author hutao
	 * @date 2017年7月25日
	 * @version V1.0
	 */
	String getUserToken(JSONObject json);

	/**
	 * 
	 * 刷新用户token
	 * 
	 * @Title: TokenRestServiceI.java
	 * @author hutao
	 * @date 2017年7月25日
	 * @version V1.0
	 */
	String refreshToken(JSONObject json);
	
	/**
	 * 注销用户时清空该用户所有渠道已登录token
	 * 
	* @author  hutao 
	* @date 创建时间：2018年9月25日 下午5:59:54 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	MessageDataBean cancelToken(JSONObject json);

}
