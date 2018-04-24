package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseReq;

/**
 * 
 */
public interface HotBusinessRestServiceI {
	/**
	 * app首页页面
	 * 
	 */
	String index(JSONObject obj);
	/**
	 * 热门商户页面
	 * 
	 */
	String hotMerchat(JSONObject obj);
	
	/**
	 * 获取商户数据
	 * 
	 */
	String hotDatas(JSONObject obj);
	/**
	 * 获取列表数据
	 * 
	 */
	String dictDatas(JSONObject obj);
	/**
	 * 获取商家服务数据
	 * 
	 */
	String businessServiceData(BaseReq<JSONObject> obj);
}
