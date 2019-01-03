package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdRechargeConfDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdRechargeConf;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 订单
 * @author 2017-10-09 16:25:17 WANG
 *
 */
@Component
@Path("/order")
public class OrderServicePublish {
	
	protected Logger logger = LoggerFactory.getLogger(OrderServicePublish.class);
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private MallBusinessService mallBusinessService;
	@Autowired
	private AdOrderReportDao adOrderReportDao;
	@Autowired
	private AdRechargeConfDao adRechargeConfDao;
	@Autowired
	protected AdUserDao adUserDao;

	@POST
	@Path(value = "/createOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String createOrder(JSONObject json, HttpServletRequest request){
		OrderMsg orderMsg = new OrderMsg();
		String groupId = json.getString("groupId");
		if (StringUtils.isBlank(groupId)) {
			String userId = json.getString("userId");
			if (StringUtils.isNotBlank(userId)) {
				AdUser adUser = adUserDao.getById(Integer.parseInt(userId));
				if (adUser != null) {
					json.put("groupId",adUser.getGroupNum());
				} else {
					orderMsg.setCode("1001");
					orderMsg.setMess("用户不存在");
					return orderMsg.toJsonString();
				}
			} else {
				orderMsg.setCode("1001");
				orderMsg.setMess("用户不存在");
				return orderMsg.toJsonString();
			}
		}
		return orderService.createOrder(json).toJsonString();
	}

	@POST
	@Path(value = "/getConsumptionAmount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getConsumptionAmount(JSONObject json){
		//获得用户手机充值消费金额
		long userId = json.getLong("userId");
		BigDecimal amount = orderService.getConsumptionAmount(userId);
		OrderMsg msg = new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess, new HashMap<String, Object>());
		msg.data.put("totalMount", amount == null ? "0" : amount.toString());
		return msg.toJsonString();
	}

	@POST
	@Path(value = "/getOrderInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getOrder(JSONObject json){
		OrderVo order = new OrderVo();
		order.setUserId(json.getLong("userId"));
		order.setOrderNumber(json.getString("orderNum"));
		List<OrderVo> ls = orderService.getOrder(order);
		if (ls != null && !ls.isEmpty()) {
			OrderVo o = ls.get(0);
			AdBusiness business = mallBusinessService.getById(String.valueOf(o.getBussinessId()));
			OrderMsg msg = new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess, new HashMap<String, Object>());
			msg.data.put("orderNumber", o.getOrderNumber());
			msg.data.put("orderId", o.getOrderId());
			msg.data.put("totalMount", o.getTotalMount());
			msg.data.put("businessNum", business.getBusinessId());
			msg.data.put("storeId", OrderService.STORESID);
			msg.data.put("productType", o.getProductType());
			msg.data.put("serviceCharge", o.getServiceCharge());
			return msg.toJsonString();
		}
		return new OrderMsg(OrderMsg.failure_code, OrderMsg.failure_mess).toJsonString();
	}
	
	@POST
	@Path(value = "/cancleOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String cancleOrder(JSONObject json){
		long userId = json.getLongValue("userId");
		String orderNum = json.getString("orderNum");
		return orderService.cancleOrder(userId, orderNum).toJsonString();
	}
	
	
	/**
	 * 获得商品简要
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/getOrderSummary")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getOrderSummary(JSONObject json){
		logger.info("getOrderSummary() json = {}",json);
		String orderNum = json.getString("orderNum");
		long userId = json.getLong("userId");
		OrderVo order = new OrderVo();
		order.setOrderNumber(orderNum);
		order.setUserId(userId);
		OrderVo o = orderService.getOrder(order).get(0);
		OrderItemVo item = o.getItems().get(0);
		String 	orderDesc = item.getGoods() + item.getSku() + "-" + orderNum;
		JSONObject retJson = new JSONObject();
		retJson.put("productType", o.getProductType());
		retJson.put("totalFree", o.getTotalMount().toString());
		retJson.put("orderDesc", orderDesc);
		retJson.put("orderId", o.getOrderId());
		retJson.put("productImg", item.getProductImg());
		retJson.put("supportPayType", o.getSupportPayType());
		if(o.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode() && o.getServiceCharge() != null)
		{
			retJson.put("serviceCharge", o.getServiceCharge().compareTo(BigDecimal.ZERO) == 0 ? null : o.getServiceCharge());
		}
		//话费充值需要校验积分消费金额,用到此参数
        AdUser user = adUserDao.getById(order.getUserId().intValue());
        if (o.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode()) {
            //用户消费金额
            BigDecimal consumptionAmount = adOrderReportDao.getConsumptionAmount(userId);
            retJson.put("consumptionAmount", consumptionAmount == null ? "0" : consumptionAmount);
			AdRechargeConf conf = adRechargeConfDao.getRechargeConf(user.getGroupNum()+"");
			retJson.put("monthLimit",( conf == null || conf.getMonthLimit() == null ) ? "0" : conf.getMonthLimit());
		}
        retJson.put("isPayPassword",user.getIsPayPassword());
		logger.info("retJon = {}", retJson);
		return retJson.toJSONString();
	}
}
