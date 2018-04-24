package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信商城获取数据
 * 
 * @author 杨汶蔚
 * @date 2017年2月9日
 * @version 1.0
 */
public interface MallServiceI {

	/** 获取商城一级菜单 */
	public String getCategoryList();

	/** 获取二级菜单 商品信息 以及商品分类信息 */
	public String getPageData(JSONObject json);

	/** 获取二级商城 商品信息 */
	public String toSecondMall(JSONObject json);

	/** 获取商品信息预计返利 */
	public String getRebate(JSONObject json);

	/** 获取商品导购信息 */
	public String getGuideDetail(JSONObject json);

	/** 获取线下商家详情 */
	public String getBusinessInfo(JSONObject json);

	/** 获取线下品牌详情 */
	public String getBrandInfo(JSONObject json);

	/** 获取线下商家分店详情 */
	public String getBusinessStoreList(JSONObject json);

	/** 获取热门商家详情 */
	public String gethotBusiness(JSONObject json);
}
