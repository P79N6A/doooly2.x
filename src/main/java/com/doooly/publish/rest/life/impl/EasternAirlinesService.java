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
import com.doooly.business.activity.EasternAirlinesBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.EasternAirlinesServiceI;

/**
 * 东航e家活动rest Service实现
 * 
 * @author zilei.sun
 * @version 2017年9月19日
 */
@Component
@Path("/activity/east")
public class EasternAirlinesService implements EasternAirlinesServiceI {
	
	private static Logger logger = Logger.getLogger(EasternAirlinesService.class);
	
	@Autowired
	private EasternAirlinesBusinessServiceI easternAirlinesBusinessServiceI;

	@POST
	@Path(value = "/airlines")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String easternAirlines(JSONObject obj) {
		
		Integer userId = obj.getInteger("userId");
		String activityId = obj.getString("activityId");
		MessageDataBean messageDataBean = easternAirlinesBusinessServiceI.eastAirlinesEfamily(userId,activityId);
		
		logger.info("返回结果：====" + messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	
	@POST
	@Path(value = "/airlines/coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String easternAirlinesCouponData(JSONObject obj) {
		
		String activityId = obj.getString("activityId");
		MessageDataBean messageDataBean = easternAirlinesBusinessServiceI.eastAirlinesEfamilyCouponCheck(activityId);
		
		logger.info("返回结果：====" + messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	
	@POST
	@Path(value = "/airlines/receiveCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String receiveCoupon(JSONObject obj) {
		
		Integer userId = obj.getInteger("userId");
		String activityId = obj.getString("activityId");
		String couponId = obj.getString("couponId");
		MessageDataBean messageDataBean = easternAirlinesBusinessServiceI.receiveCoupon(userId,activityId,couponId);
		
		logger.info("返回结果：====" + messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
