package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.impl.MURechargeActivityService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.MURechargeRestI;
@Component
@Path("/enterprises")
public class MURechargeRest implements MURechargeRestI{
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MURechargeActivityService muRechargeService;
	
	@POST
	@Path(value = "/activity/send-coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MessageDataBean sendActivityCoupon(JSONObject req) {
		log.info("东航推文拉新活动请求参数：{}", req);
		Integer userId = req.getInteger("userId");
		Integer activityId = req.getInteger("activityId");
		MessageDataBean msgBean = muRechargeService.sendActivityCoupon(userId, activityId);
		
		return msgBean;
	}

}
