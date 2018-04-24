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
import com.doooly.business.activity.BringCoolnessBusinessServiceI;
import com.doooly.business.activity.CookGodGameServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.CookGodGameRestServiceI;

/**
 *宝钢化工厨神大赛报名活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月4日
 */
@Component
@Path("/cookGod")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CookGodGameRestService implements CookGodGameRestServiceI {

	private static Logger logger = Logger.getLogger(CookGodGameRestService.class);
	@Autowired
	private CookGodGameServiceI cookGodGameServiceI;

	@POST
	@Path(value = "/getActivityDetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getActivityDetail(JSONObject obj) {
		Integer activityId = obj.getInteger("activityId");
		String userId = obj.getString("userId");
		MessageDataBean messageDataBean = cookGodGameServiceI.getActivityDetail(activityId,userId);
		logger.info("宝钢化工厨神大赛报名活动页面返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/apply")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String apply(JSONObject obj) {
		Integer activityId = obj.getInteger("activityId");
		Integer userId = obj.getInteger("userId");
		MessageDataBean messageDataBean = cookGodGameServiceI.apply(activityId,userId);
		logger.info("宝钢化工厨神大赛报名活动页面返回值:"+messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
}
