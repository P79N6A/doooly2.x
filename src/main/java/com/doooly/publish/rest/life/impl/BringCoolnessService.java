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
import com.doooly.business.activity.AnswerQuestionBusinessServiceI;
import com.doooly.business.activity.BringCoolnessBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.AnswerQuestionRestServiceI;
import com.doooly.publish.rest.life.BringCoolnessServiceI;

/**
 * 答题活动 rest service实现
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
@Component
@Path("/activity/bring")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BringCoolnessService implements BringCoolnessServiceI {

	private static Logger logger = Logger.getLogger(BringCoolnessService.class);
	@Autowired
	private BringCoolnessBusinessServiceI bringCoolnessBusinessServiceI;

	@POST
	@Path(value = "/coolness")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String bringCoolness(JSONObject obj) {
		
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		Integer activityId = obj.getInteger("activityId");
		Integer type = obj.getInteger("type");
		MessageDataBean messageDataBean = bringCoolnessBusinessServiceI.sendCoolnessGift(userId,activityId,type);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/coolUsers")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String coolUsers(JSONObject obj) {
		// 获取用户id
		Integer activityId = obj.getInteger("activityId");
		MessageDataBean messageDataBean = bringCoolnessBusinessServiceI.getBringCoolnessUsers(activityId);
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
}
