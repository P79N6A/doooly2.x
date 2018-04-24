package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

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

}
