package com.doooly.publish.rest.reachad.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.ofpay.service.OfPayService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.processor.productprocessor.ProductProcessor;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.utils.DateUtils;
import com.doooly.dto.common.PayMsg;

/**
 * 欧飞接口
 * 
 * 2017-11-03 15:21:18 WANG
 *
 */
@Component
@Path("/ofPayService")
public class OfPayServicePublish {
	protected Logger logger = LoggerFactory.getLogger(OfPayServicePublish.class);
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OfPayService ofPayService;
	@Autowired
	private RefundService refundService;
	
	/***
	 * 手机号是否可以充值话费
	 * @return
	 */
	@POST
	@Path(value = "/telCheck")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String telCheck(JSONObject json) {
		logger.info("telCheck() json = {}", json);
		String phoneNo = json.getString("phoneNo");
		String price = json.getString("price");
		return ofPayService.telCheck(phoneNo, price).toJsonString();
	}
	/***
	 * 手机号是否可以充值流量
	 * @return
	 */
	@POST
	@Path(value = "/flowCheck")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String flowCheck(JSONObject json) {
		logger.info("flowCheck() json = {}", json);
		String phoneNo = json.getString("phoneNo");
		String price = json.getString("price");
		return ofPayService.flowCheck(phoneNo, price).toJsonString();
	}

	/***
	 * 查看卡密库存
	 * @return
	 */
	@POST
	@Path(value = "/queryLeftcardNum")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String queryLeftcardNum(JSONObject json) {
		logger.info("queryLeftcardNum() json = {}", json);
		String cardid = json.getString("cardid");
		return ofPayService.queryLeftcardNum(cardid).toJsonString();
	}
	
	
	/***
	 * 话费充值回调接口
	 * @return
	 */
	@POST
	@Path(value = "/mobileCallback")
	public String mobileCallback(@FormParam("ret_code") String ret_code, @FormParam("sporder_id") String sporder_id,
			@FormParam("ordersuccesstime") String ordersuccesstime, @FormParam("err_msg") String err_msg) {
		logger.info("mobileCallback() start.22222");
		try {
			logger.info("mobileCallback() ret_code={},sporder_id={},ordersuccesstime={},msg={}", ret_code,sporder_id,ordersuccesstime,err_msg);
			String errMsg = err_msg;
			if(StringUtils.isNotBlank(ret_code)){
				if (ProductProcessor.RECHARGE_STATE_1.equals(ret_code)) {
					errMsg = "充值成功.";
				} 
				//保存充值结果
				OrderVo order = orderService.getByOrderNum(sporder_id);
				int rows = updateOrderItem(ret_code, order, ordersuccesstime, errMsg, "mobileCallback");
				logger.info("mobileCallback() rows={}", rows);
				//失败调用退款接口
				if(ProductProcessor.RECHARGE_STATE_9.equals(ret_code)){
					refundService.autoRefund(order.getUserId(),order.getOrderNumber());
				}
			}
		} catch (Exception e) {
			logger.error("mobileCallback() e = {}", e);
		}
		return new PayMsg(PayMsg.success_code, PayMsg.success_mess).toJsonString();
	}

	/***
	 * 流量充值回调接口
	 * @return
	 */
	@POST
	@Path(value = "/flowCallback")
	public String flowCallback(@FormParam("ret_code") String ret_code, @FormParam("sporder_id") String sporder_id,
			@FormParam("ordersuccesstime") String ordersuccesstime, @FormParam("err_msg") String err_msg) {
		logger.info("flowCallback() start.");
		try {
			logger.info("flowCallback() ret_code={},sporder_id={},ordersuccesstime={},msg={}", ret_code,sporder_id,ordersuccesstime,err_msg);
			String errMsg = err_msg;
			if(StringUtils.isNotBlank(ret_code)){
				if (ProductProcessor.RECHARGE_STATE_1.equals(ret_code)) {
					errMsg = "充值成功.";
				}
				//保存充值结果
				OrderVo order = orderService.getByOrderNum(sporder_id);
				int rows = updateOrderItem(ret_code, order, ordersuccesstime, errMsg, "flowCallback");
				logger.info("flowCallback() rows={}", rows);
				//失败调用退款接口
				if(ProductProcessor.RECHARGE_STATE_9.equals(ret_code)){
					refundService.autoRefund(order.getUserId(), order.getOrderNumber());
				}
			}
		} catch (Exception e) {
			logger.error("flowCallback() e={}", e);
		}
		return new PayMsg(PayMsg.success_code, PayMsg.success_mess).toJsonString();
	}
	
	private int updateOrderItem(String ret_code, OrderVo order, String ordersuccesstime, String msg, String updateBy) {
		OrderItemVo oldItem = order.getItems().get(0);
		OrderItemVo newItem = new OrderItemVo();
		newItem.setId(oldItem.getId());
		newItem.setRetCode(ret_code);
		newItem.setRetMsg(msg);
		newItem.setRetState(ret_code);
		newItem.setUpdateBy(updateBy);
		newItem.setUpdateDate(DateUtils.parse(ordersuccesstime,"yyyyMMddHHmmss"));
		return orderService.updateOrderItem(newItem);
	}
	
}
