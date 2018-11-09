package com.doooly.publish.rest.life.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.home.v2.servcie.IndexServiceI;
import com.doooly.common.constants.VersionConstants;

/**
 * 兜礼首页接口（微信端/app）
 * 接口版本管理
 */
@Component
@Path("/wechat/indexService")
public class IndexRestService {
	@Autowired
	private IndexServiceI indexService;

	/**
	 * 首页楼层查询
	 * 
	 * 接口v2.2:首页接口新增楼层
	 * 
	* @author  hutao 
	* @date 创建时间：2018年10月23日 下午4:13:36 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/index/v2_2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String indexV2_2(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByV2_2(params, request, VersionConstants.INTERFACE_VERSION_V2);
//		return "{\"msg\":\"success\",\"code\":\"1000\",\"data\":{\"floors\":[{\"mainTitle\":\"导航\",\"subTitle\":\"\",\"type\":\"4\",\"list\":[{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201810/99ec08f8-2fba-49f4-9890-a0e70cb02592.jpg\",\"linkUrl\":\"https://www.baidu.com\",\"mainTitle\":\"签到\",\"subTitle\":\"赢66积分\",\"cornerMark\":\"爆款\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201810/99ec08f8-2fba-49f4-9890-a0e70cb02592.jpg\",\"linkUrl\":\"https://www.baidu.com\",\"mainTitle\":\"信用卡\",\"subTitle\":\"95折还款\",\"cornerMark\":\"\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201810/99ec08f8-2fba-49f4-9890-a0e70cb02592.jpg\",\"linkUrl\":\"https://www.baidu.com\",\"mainTitle\":\"领券\",\"subTitle\":\"每日福利\",\"cornerMark\":\"火爆\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201810/99ec08f8-2fba-49f4-9890-a0e70cb02592.jpg\",\"linkUrl\":\"https://www.baidu.com\",\"mainTitle\":\"出游\",\"subTitle\":\"火车票\",\"cornerMark\":\"减10元\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201810/99ec08f8-2fba-49f4-9890-a0e70cb02592.jpg\",\"linkUrl\":\"https://www.baidu.com\",\"mainTitle\":\"话费\",\"subTitle\":\"9折话费\",\"cornerMark\":\"\"}]},{\"isOnline\":1,\"mainTitle\":\"每日特惠\",\"subTitle\":\"\",\"type\":\"3\",\"list\":[{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201810/99ec08f8-2fba-49f4-9890-a0e70cb02592.jpg\",\"linkUrl\":\"https://www.baidu.com\",\"mainTitle\":\"每日特惠\",\"subTitle\":\"每日特惠\"}]},{\"mainTitle\":\"兜礼会员购物特权\",\"subTitle\":\"可享受折扣与返利双重优惠\",\"type\":\"1\",\"list\":[{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/c4108d33-e717-4b5b-a728-5f27fac5ef06.jpg\",\"isSupportIntegral\":\"0\",\"linkUrl\":\"https://reach-life.com/pre_dist/dist/#/businessinfo/0/85\",\"mainTitle\":\"京东返利\",\"subTitle\":\"\",\"subUrl\":\"/businessinfo/0/85\",\"cornerMark\":\"返6.4%\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/d015cb19-21c4-4ac4-abfa-6c44ff31b37d.jpg\",\"isSupportIntegral\":\"0\",\"linkUrl\":\"https://reach-life.com/pre_dist/dist/#/businessinfo/0/82\",\"mainTitle\":\"网易考拉海购\",\"subTitle\":\"95折起\",\"subUrl\":\"/businessinfo/0/82\",\"cornerMark\":\"返6.4%\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/57ef09c3-25d4-450e-aa07-af5d658ad3e5.jpg\",\"isSupportIntegral\":\"1\",\"linkUrl\":\"https://reach-life.com/pre_dist/dist/#/businessinfo/0/26\",\"mainTitle\":\"滴滴出行\",\"subTitle\":\"85折起\",\"subUrl\":\"/businessinfo/0/26\",\"cornerMark\":\"返6.4%\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/e228127b-2d9d-4a22-8874-9ae710dd6a48.jpg\",\"isSupportIntegral\":\"1\",\"linkUrl\":\"https://reach-life.com/pre_dist/dist/#/businessinfo/0/43\",\"mainTitle\":\"唯品会\",\"subTitle\":\"95折起\",\"subUrl\":\"/businessinfo/0/43\",\"cornerMark\":\"返6.4%\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/24dd8511-4aa7-43c5-9d10-1aa7e003d83c.jpg\",\"isSupportIntegral\":\"1\",\"linkUrl\":\"https://reach-life.com/pre_dist/dist/#/businessinfo/0/61\",\"mainTitle\":\"网易严选\",\"subTitle\":\"98折起\",\"subUrl\":\"/businessinfo/0/61\",\"cornerMark\":\"返6.4%\"}]},{\"mainTitle\":\"特惠专区\",\"subTitle\":\"品牌特卖独家折扣\",\"type\":\"2\",\"list\":[{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/c02c7158-144a-47c2-b28d-d85b0571d040.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/openOneNnumber/106/https://mall.neigou.com/JingDong/JDIndex?intcmp=wx-sy-bk&ng_scene=MTY3ODMmMjIwMDU0OSZkb3VsaQ==\",\"mainTitle\":\"京东折扣区\",\"subTitle\":\"全场98折起\",\"subUrl\":\"/openOneNnumber/106/https://mall.neigou.com/JingDong/JDIndex?intcmp=wx-sy-bk&ng_scene=MTY3ODMmMjIwMDU0OSZkb3VsaQ==\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/77de06d1-f41e-4bbf-8a57-f8a2af29dedd.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/openOneNnumber/106/https://mall.neigou.com/Mall/index/id/39\",\"mainTitle\":\"严选折扣区\",\"subTitle\":\"全场85折起\",\"subUrl\":\"/openOneNnumber/106/https://mall.neigou.com/Mall/index/id/39\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/41f2d4b1-7690-40c4-9f27-9365756f3e0f.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/openOneNnumber/106/https://mall.neigou.com/Mall/index/id/49?intcmp=wx-sy-bk&ng_scene=MTY3ODMmMjIwMDY3MSZkb3VsaQ==\",\"mainTitle\":\"优鲜折扣区\",\"subTitle\":\"全场49包邮\",\"subUrl\":\"/openOneNnumber/106/https://mall.neigou.com/Mall/index/id/49?intcmp=wx-sy-bk&ng_scene=MTY3ODMmMjIwMDY3MSZkb3VsaQ==\"}]},{\"mainTitle\":\"今日热销品牌\",\"subTitle\":\"小编精选，当日最热\",\"type\":\"5\",\"list\":[{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201809/57c160a3-8d38-4857-9517-e986e5c4ea08.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/cardBuyDetail/52\",\"mainTitle\":\"\",\"subTitle\":\"\",\"subUrl\":\"\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201809/57c160a3-8d38-4857-9517-e986e5c4ea08.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/cardBuyDetail/52\",\"mainTitle\":\"最新单品\",\"subTitle\":\"98折起\",\"subUrl\":\"/cardBuyDetail/52\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201809/a2ed4060-07c1-47db-a561-6e349c69a033.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/cardBuyDetail/51\",\"mainTitle\":\"网红零食榜\",\"subTitle\":\"女生零食礼盒\",\"subUrl\":\"/cardBuyDetail/51\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201809/3fc21897-a069-4cdb-9f59-8b70341fd4b3.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/cardBuyDetail/50\",\"mainTitle\":\"复仇者联盟\",\"subTitle\":\"便浮现战车\",\"subUrl\":\"/cardBuyDetail/50\"}]},{\"mainTitle\":\"内部价\",\"subTitle\":\"员工专享特价\",\"type\":\"6\",\"list\":[{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/30a5207e-95b4-4577-b388-e8b706c1adbb.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/recharge/0/0\",\"title\":\"低压海盐热敷腰带\",\"marketPrice\":\"520.00\",\"dooolyPrice\":\"500.00\",\"rebate\":\"24.5\",\"expressage\":\"京东发货\",\"subUrl\":\"/recharge/0/0\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/30a5207e-95b4-4577-b388-e8b706c1adbb.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/recharge/0/0\",\"title\":\"低压海盐热敷腰带\",\"marketPrice\":\"520.00\",\"dooolyPrice\":\"500.00\",\"rebate\":\"24.5\",\"expressage\":\"京东发货\",\"subUrl\":\"/recharge/0/0\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/30a5207e-95b4-4577-b388-e8b706c1adbb.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/recharge/0/0\",\"title\":\"低压海盐热敷腰带\",\"marketPrice\":\"520.00\",\"dooolyPrice\":\"500.00\",\"rebate\":\"24.5\",\"expressage\":\"京东发货\",\"subUrl\":\"/recharge/0/0\"},{\"iconUrl\":\"http://pay.reach-life.com:23330/images/201808/30a5207e-95b4-4577-b388-e8b706c1adbb.png\",\"linkUrl\":\"https://reach-life.com/pro_dist/dist/#/recharge/0/0\",\"title\":\"低压海盐热敷腰带\",\"marketPrice\":\"520.00\",\"dooolyPrice\":\"500.00\",\"rebate\":\"24.5\",\"expressage\":\"京东发货\",\"subUrl\":\"/recharge/0/0\"}]}]}}";
	}
	
	/**
	 * 首页楼层查询
	 * 
	 * 接口v2：新增每日特惠
	 * 
	 * @author  hutao 
	 * @date 创建时间：2018年10月23日 下午4:13:36 
	 * @version 1.0 
	 * @parameter  
	 * @since  
	 * @return
	 */
	@POST
	@Path(value = "/index/v2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String indexV2(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByVersion(params, request, VersionConstants.INTERFACE_VERSION_V2);
	}

	/**
	 * 首页楼层查询
	 * 
	 * 接口v1
	 * 
	* @author  hutao 
	* @date 创建时间：2018年10月23日 下午4:15:10 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	@POST
	@Path(value = "/index")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String index(JSONObject params, @Context HttpServletRequest request) {
		return indexService.selectFloorsByVersion(params, request, VersionConstants.INTERFACE_VERSION_V1);
	}
}
