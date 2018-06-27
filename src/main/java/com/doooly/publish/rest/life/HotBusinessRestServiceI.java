package com.doooly.publish.rest.life;

import javax.servlet.http.HttpServletRequest;
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

	/**
	 * 可用积分服务进入商户详情
	 * 
	 * @author linkDr
	 * @param channel
	 *            - 渠道
	 * @param dealType
	 *            - 商户类型(0-线上，1-线下，2-线上及线下)
	 * @return productId - 自营商品ID(0-无,1-有)
	 * @return adBusiness
	 * @return channel - 渠道(0为微信或者pc请求,1为app请求)
	 */
	String businessInfoService(JSONObject params, HttpServletRequest request);
}
