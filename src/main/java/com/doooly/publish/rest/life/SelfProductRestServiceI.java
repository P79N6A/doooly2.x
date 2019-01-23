package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 自营商品 REST Service
 * 
 * @author yuelou.zhang
 * @version 2017年9月25日
 */
public interface SelfProductRestServiceI {

	/** 获取自营商品列表 */
	String getSelfProductList(JSONObject obj);

	/** 获取自营商品详情 */
	String getSelfProductDetail(JSONObject obj);

	/** 根据活动名获取SKU商品*/
	String getSelfProductByName(JSONObject obj);

	String getActivityByType(JSONObject obj);

	/**
	 * 机场活动
	 * @param obj
	 * @return
	 */
	String getSelfProductAirport(JSONObject obj);
}
