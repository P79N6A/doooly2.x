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
import com.doooly.business.order.service.UserDeliveryService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.UserDeliveryRestServiceI;

/**
 * 用户收货地址 REST Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年9月27日
 */
@Component
@Path("/userDelivery")
public class UserDeliveryRestService implements UserDeliveryRestServiceI {

	private static Logger logger = Logger.getLogger(UserDeliveryRestService.class);

	@Autowired
	private UserDeliveryService userDeliveryService;

	@POST
	@Path(value = "/list")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getUserDeliveryList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");
			HashMap<String, Object> map = userDeliveryService.getUserDeliveryList(userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取用户收货地址列表异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/insert")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String insertUserDelivery(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Integer result = userDeliveryService.insertUserDelivery(obj);
			messageDataBean.setCode(result == 1 ? MessageDataBean.success_code : MessageDataBean.failure_code);
		} catch (Exception e) {
			logger.error("新增收货地址异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String updateUserDelivery(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Integer result = userDeliveryService.updateUserDelivery(obj);
			messageDataBean.setCode(result == 1 ? MessageDataBean.success_code : MessageDataBean.failure_code);
		} catch (Exception e) {
			logger.error("更新收货地址异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String deleteUserDelivery(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String deliveryId = obj.getString("deliveryId");
			Integer result = userDeliveryService.deleteUserDelivery(deliveryId);
			String code = null;
			if(result == 1){
				code = MessageDataBean.success_code;
			}else if(result == 2){
				code = MessageDataBean.no_data_code;
			}else{
				code = MessageDataBean.failure_code;
			}
			messageDataBean.setCode(code);
		} catch (Exception e) {
			logger.error("删除收货地址异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/get")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getUserDelivery(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String deliveryId = obj.getString("id");
			HashMap<String, Object> map = userDeliveryService.getUserDeliveryById(deliveryId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取收货地址异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/setDefault")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String setDefaultDelivery(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");
			String deliveryId = obj.getString("deliveryId");
			Integer result = userDeliveryService.setDefaultDeliveryById(userId,deliveryId);
			messageDataBean.setCode(result == 1 ? MessageDataBean.success_code : MessageDataBean.failure_code);
		} catch (Exception e) {
			logger.error("设置默认收货地址异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
