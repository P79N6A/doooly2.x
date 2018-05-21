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
import com.doooly.business.activity.DoubleElevenActivityServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.DoubleElevenActivityRestServiceI;

/**
 *双十一活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月4日
 */
@Component
@Path("/doubleEleven")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoubleElevenActivityRestService implements DoubleElevenActivityRestServiceI {

	private static Logger logger = Logger.getLogger(DoubleElevenActivityRestService.class);
	@Autowired
	private DoubleElevenActivityServiceI doubleElevenActivityServiceI;

	@POST
	@Path(value = "/activitIndex")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String activitIndex(JSONObject obj) {
		String userId = obj.getString("superUserId");
		MessageDataBean messageDataBean = doubleElevenActivityServiceI.getActivityIndex(userId);
		logger.info("双十一活动个人页面返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/indexForJoiner")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String indexForJoiner(JSONObject obj) {
		String userId = obj.getString("userId");
		String superUserId = obj.getString("superUserId");
		MessageDataBean messageDataBean = doubleElevenActivityServiceI.getActivityIndexForJoiner(superUserId,userId);
		logger.info("双十一活动个人页面返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/help")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String help(JSONObject obj) {
		String userId = obj.getString("userId");
		String superUserId = obj.getString("superUserId");
		MessageDataBean messageDataBean = doubleElevenActivityServiceI.helpInitiator(superUserId,userId);
		logger.info("点击帮他加分返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/receiveGift")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String receiveGift(JSONObject json) {
		String userId = json.getString("userId");
		// 送清凉活动领取饮料
		MessageDataBean messageDataBean = doubleElevenActivityServiceI.receiveGift(userId);
		logger.info("点击立即领取 返回值:" + messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/isReceiveGift")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String isReceiveGift(JSONObject json) {
		String userId = json.getString("userId");
		// 进入领取页面返回值
		MessageDataBean messageDataBean = doubleElevenActivityServiceI.isReceiveGift(userId);
		logger.info("进入领取页面 返回值:" + messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	
}
