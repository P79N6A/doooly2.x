package com.doooly.business.home.v2.servcie;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public interface IndexServiceI {

	/**
	 * 兜礼权益接口查询楼层v2.2
	 * 
	* @author  hutao 
	* @date 创建时间：2018年11月1日 下午3:35:48 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	String selectFloorsByV2_2(JSONObject params, HttpServletRequest request, String version);

	/**
	 * 根据接口版本查询首页楼层信息
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月23日 下午3:36:00
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	String selectFloorsByVersion(JSONObject params, HttpServletRequest request, String version);
}
