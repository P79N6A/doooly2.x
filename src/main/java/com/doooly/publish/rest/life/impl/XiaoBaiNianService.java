package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.XiaoBaiNianBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.XiaoBaiNianServiceI;

/**
 * 小拜年活动 rest Service实现
 * 
 * @author zilei.sun
 * @version 2017年8月29日
 */
@Component
@Path("/activity/xiaobainian")
public class XiaoBaiNianService implements XiaoBaiNianServiceI {

	private static Logger logger = Logger.getLogger(XiaoBaiNianService.class);
	@Autowired
	private XiaoBaiNianBusinessServiceI xiaoBaiNianBusinessServiceI;

	@POST
	@Path(value = "/happryYear")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String happryYear(JSONObject obj) {
		
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		Integer activityId = obj.getInteger("activityId");
		MessageDataBean messageDataBean = xiaoBaiNianBusinessServiceI.xiaoBaiNianCoupon(userId,activityId);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
}
