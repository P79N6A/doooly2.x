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
import com.doooly.business.activity.CookGodVoteServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.CookGodVoteRestServiceI;
/**
 *宝钢化工厨神大赛投票活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月6日
 */
@Component
@Path("/cookGodVote")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CookGodVoteRestService implements CookGodVoteRestServiceI {

	private static Logger logger = Logger.getLogger(CookGodVoteRestService.class);
	@Autowired
	private CookGodVoteServiceI cookGodVoteServiceI;

	@POST
	@Path(value = "/getIndexData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getIndexData(JSONObject obj) {
		Integer activityId = obj.getInteger("activityId");
		String userId = obj.getString("userId");
		MessageDataBean messageDataBean = cookGodVoteServiceI.getIndexData(activityId,userId);
		logger.info("宝钢化工厨神大赛报名投票页面返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/vote")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String vote(JSONObject obj) {
		Integer activityId = obj.getInteger("activityId");
		String userId = obj.getString("userId");
		Integer optionId = obj.getInteger("optionId");
		MessageDataBean messageDataBean = cookGodVoteServiceI.vote(activityId,userId,optionId);
		logger.info("ajax宝钢化工厨神大赛报名投票动作返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/rank")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String rank(JSONObject obj) {
		Integer activityId = obj.getInteger("activityId");
		MessageDataBean messageDataBean = cookGodVoteServiceI.rank(activityId);
		logger.info("ajax宝钢化工厨神大赛报名排名返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/activityDetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String activityDetail(JSONObject obj) {
		Integer activityId = obj.getInteger("activityId");
		MessageDataBean messageDataBean = cookGodVoteServiceI.getActivityDetail(activityId);
		logger.info("ajax宝钢化工厨神大赛报名活动详情返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
}
