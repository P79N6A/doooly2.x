package com.doooly.publish.rest.life.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.doooly.business.myorder.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.myorder.po.OrderPoReq;
import com.doooly.business.myorder.po.OrderPoResp;
import com.doooly.business.myorder.service.MyOrderServiceI;
import com.doooly.business.myorder.service.OrderService;
import com.doooly.business.myorder.vo.OrderVoResp;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.Pagelab;
import com.doooly.common.dto.BaseRes;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.MyOrderRestServiceI;
import com.google.gson.Gson;

import cn.jiguang.common.utils.StringUtils;

/**
 * @Description: 我的订单
 * @author: qing.zhang
 * @date: 2018-07-11
 */
@Path("/myorder")
@Component
public class MyOrderRestService implements MyOrderRestServiceI {

	private static final Logger logger = LoggerFactory.getLogger(MyOrderRestService.class);

	@Autowired
	private MyOrderServiceI myOrderServiceI;
	
	@Autowired
	private OrderService orderservice;
	
	 @Autowired
	 private ConfigDictServiceI configDictServiceI;

	@POST
	@Path("/getOrders")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getOrders(JSONObject json) {
		OrderResult orderResult = new OrderResult();
		try {
			orderResult = myOrderServiceI.getOrders(json.toJSONString());
			logger.info("查询所有我的订单列表信息返回数据" + orderResult.toJsonString());
		} catch (Exception e) {
			logger.error("获得我的订单列表信息出错", e);
			orderResult.error(orderResult);
		}
		return orderResult.toJsonString();
	}

	@POST
	@Path("/getOrderDetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getOrderDetail(JSONObject json) {
		OrderResult orderResult = new OrderResult();
		try {
			orderResult = myOrderServiceI.getOrderDetail(json.toJSONString());
			logger.info("查询所有我的订单详情信息返回数据" + orderResult.toJsonString());
		} catch (Exception e) {
			logger.error("获得我的订单详情信息出错", e);
			orderResult.error(orderResult);
		}
		return orderResult.toJsonString();
	}

	@POST
	@Path("/getOrderReportIdByOrderNum")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getOrderReportIdByOrderNum(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String orderNum = obj.getString("orderNum");
			String activityName = obj.getString("activityName");
			long orderReportId = myOrderServiceI.getOrderReportIdByOrderNum(orderNum);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setMess(MessageDataBean.success_mess);
			HashMap<String, Object> map = new HashMap<String, Object>();
			int showQr = myOrderServiceI.orderBelongOneActivity(activityName,orderNum);
			map.put("showQr",showQr > 0 ? 1 : 0);
			map.put("orderReportId", orderReportId);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取orderReportId失败", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}
	
	/**
	 * 我的订单-订单列表
	 * @param json
	 * @return
	 */
	@POST
	@Path("/list/v2/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String list(JSONObject json) {
		try {
			long start = System.currentTimeMillis();
			logger.info("order.list.param:{}",json.toJSONString());
			Gson gson = new Gson();
			OrderReq req = gson.fromJson(json.toJSONString(), OrderReq.class);
			String result =  list(req);
			if(req.getHintState() != null) {
				OrderHintReq orderHintReq = new OrderHintReq();
				orderHintReq.setHintState(req.getHintState());
				orderHintReq.setUserId(Long.parseLong(req.getUserId()));
				orderservice.cannelHint(orderHintReq);
			}
			logger.info("我的订单(/list)===> 接口耗时：{}", System.currentTimeMillis()-start);
			return result;
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return "{}";
	}
	
	/**
	 * 订单列表
	 * @return
	 */
	public String list(OrderReq req) {
		 Gson gson = new Gson();  
		List<OrderResp> orderList = new ArrayList<>();
		Pagelab pagelab = new Pagelab(req.getCurrentPage(), req.getPageSize());
		List<OrderPoResp>  orderResultList = orderservice.getOrderList(req);
		OrderResp resp = null;
		String orderDay = configDictServiceI.getValueByTypeAndKey("ORDER", "LATEST_ORDER_DAY");
		for(OrderPoResp  orderPoResp : orderResultList) {
			resp = new OrderResp();
			resp.setAmountPayable(orderPoResp.getTotalPrice());
			resp.setIsSource(orderPoResp.getIsSource());
			resp.setIsUserRebate(Integer.parseInt(orderPoResp.getIsUserRebate()));
			resp.setOrderDate(DateUtils.formatDate(orderPoResp.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
			resp.setOrderDateStr(DateUtils.formatDate(orderPoResp.getOrderDate(), "M-d HH:mm"));
			resp.setOrderId(orderPoResp.getId());
			resp.setOrderNumber(orderPoResp.getOrderNumber());
			resp.setPayAmount(orderPoResp.getTotalMount());
			resp.setState(String.valueOf(orderPoResp.getState()));
			resp.setType(String.valueOf(orderPoResp.getType()));
			resp.setStoreName(orderPoResp.getStoreName());
			resp.setUserId(orderPoResp.getUserId());
			resp.setUserRebate(orderPoResp.getUserRebate());
			resp.setProductType(orderPoResp.getProductType());
			resp.setLogo(orderPoResp.getLogo());
			resp.setBusinessId(orderPoResp.getBusinessId());
			resp.setSavePrice(orderPoResp.getSavePrice());
			resp.setCompany(orderPoResp.getCompany());
			resp.setGoods(orderPoResp.getGoods());
			resp.setSpecification(orderPoResp.getSpecification());
			resp.setProductImg(orderPoResp.getProductImg());
			Date intervalDayDate = DateUtils.addDays(orderPoResp.getOrderDate(), Integer.parseInt(orderDay));//推后30天的日期
			resp.setIntegrateReturnDate(com.doooly.business.utils.DateUtils.formatDate(intervalDayDate, "yyyy.MM.dd"));		
			resp.setCashDeskSource(orderPoResp.getCashDeskSource());
			orderList.add(resp);
		}
		Long countOrderNum = orderservice.countOrderNum(req);
		pagelab.setTotalNum(countOrderNum.intValue());
		OrderPoReq orderPoReq = new OrderPoReq();
		orderPoReq.setUserId(Long.parseLong(req.getUserId()));
		List<Map<String,String>> listMap = orderservice.getOrderdDetailSum(orderPoReq);

		OrderVoResp orderVoResp = new OrderVoResp();
		orderVoResp.setCurrentPage(pagelab.getCountPage());
		orderVoResp.setOrderDataList(listMap);
		orderVoResp.setPage(orderList);
		orderVoResp.setTotalNum(pagelab.getTotalNum());
		orderVoResp.setTotalPage(pagelab.getCountPage());
		
		
		BaseRes<OrderVoResp> baseRes = new BaseRes<>();
		baseRes.setCode("1000");
		baseRes.setData(orderVoResp);
		return gson.toJson(baseRes);
	}
	
	
	@POST
	@Path("/detail/v2/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String detail(JSONObject json) {
		try {
			long start = System.currentTimeMillis();
			BaseRes<OrderDetailResp> result = new BaseRes<>(); 
			logger.info("order.detail.param:{}",json.toJSONString());
			Gson gson = new Gson();
			OrderDetailReq req = gson.fromJson(json.toJSONString(), OrderDetailReq.class);
			if(StringUtils.isEmpty(req.getOrderId())) {
				result.setCode("1002");
				return gson.toJson(result);
			}
			
			OrderDetailResp resp =  orderservice.getOrderDetail(req);
			result.setCode("1000");
			result.setData(resp);
			
			logger.info("我的订单(/detail)===> 接口耗时：{}", System.currentTimeMillis()-start);
			return gson.toJson(result);
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return "{}";
	}

	
	@POST
	@Path("/hint/v2/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String hint(JSONObject json) {
		try {
			long start = System.currentTimeMillis();
			BaseRes<HintResp> result = new BaseRes<>(); 
			logger.info("order.detail.param:{}",json.toJSONString());
			Gson gson = new Gson();
			HintReq req = gson.fromJson(json.toJSONString(), HintReq.class);
			
			HintResp resp =  orderservice.getHint(req);
			result.setCode("1000");
			result.setData(resp);
			
			logger.info("我的订单(/hint)===> 接口耗时：{}", System.currentTimeMillis()-start);
			return gson.toJson(result);
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return "{}";
	}
	
	@POST
	@Path("/deleteOrder/v2/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteOrder(JSONObject json) {
		Gson gson = new Gson();
		BaseRes<Boolean> result = new BaseRes<>();
		try {
			long start = System.currentTimeMillis();
			logger.info("order.detail.param:{}",json.toJSONString());
			OrderDeleteReq  req = gson.fromJson(json.toJSONString(), OrderDeleteReq.class);
			
			boolean flag =  orderservice.deleteOrder(req);
			if(req.getHintState() != null) {
				OrderHintReq orderHintReq = new OrderHintReq();
				orderHintReq.setHintState(req.getHintState());
				orderHintReq.setUserId(req.getUserId());
				orderservice.cannelHint(orderHintReq);
			}
			result.setCode("1000");
			result.setData(flag);
			logger.info("我的订单(/delete)===> 接口耗时：{}", System.currentTimeMillis()-start);
			return gson.toJson(result);
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		result.setCode("1002");
		result.setData(false);
		return gson.toJson(result);
	}
}
