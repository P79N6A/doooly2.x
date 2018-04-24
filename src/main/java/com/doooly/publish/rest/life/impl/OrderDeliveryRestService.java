package com.doooly.publish.rest.life.impl;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.service.OrderDeliveryService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.OrderDeliveryRestServiceI;

/**
 * 确认下单 REST Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
@Component
@Path("/orderDelivery")
public class OrderDeliveryRestService implements OrderDeliveryRestServiceI {

	private static Logger logger = Logger.getLogger(OrderDeliveryRestService.class);

	@Autowired
	private OrderDeliveryService orderDeliveryService;

	@POST
	@Path(value = "/orderInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getOrderDeliveryInfo(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String productTypeId = obj.getString("productTypeId");// 卡券商品类型主键
			String deliveryId = obj.getString("deliveryId");// 所选收货地址id
			HashMap<String, Object> map = orderDeliveryService.getOrderDeliveryInfo(userId, productTypeId, deliveryId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取确认下单页数据异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/storeList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getStoreList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String businessId = obj.getString("businessId");
			String province = obj.getString("province");
			String city = obj.getString("city");
			String area = obj.getString("area");
			HashMap<String, Object> map = orderDeliveryService.getStoreList(businessId, province, city, area);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取门店列表异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
