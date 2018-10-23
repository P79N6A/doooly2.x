package com.doooly.business.home.v2.servcie;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public interface IndexServiceI {
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
