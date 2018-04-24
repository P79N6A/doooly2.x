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

}
