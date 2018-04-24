package com.doooly.publish.rest.reachad.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.doooly.business.activity.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.ActivityServiceI;
import com.doooly.common.queue.ArrayBlockQueue;
import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.dto.activity.ActivityOrderRes;
import com.doooly.publish.rest.reachad.ActivityServicePublishI;

/**
 * 
 * @author 赵清江
 * @date 2016年12月16日
 * @version 1.0
 */
@Component
@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActivityServicePublish implements ActivityServicePublishI {

	@Autowired
	private ActivityServiceI activityService;
	@Autowired
	private ArrayBlockQueue<ActivityOrderReq> activityQueue;
	@Autowired
	private EnrollService enrollService;
	
	@POST
	@Path(value="/order")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String activityOrderReceiver(ActivityOrderReq req) {
		ActivityOrderRes res = new ActivityOrderRes("1000", "请求已受理!");
//		try {
//			res = activityService.activityOrderService(req);
//		} catch (Exception e) {
//			e.printStackTrace();
//			res = new ActivityOrderRes("5000", "服务器内部错误!");
//		}
		activityQueue.add(req);
		return res.toJsonString();
	}
	
	
	@POST
	@Path(value = "/hotActivity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getHotActivity(JSONObject results) {
		return activityService.getHotActivity(results).toJsonString();
	}

	@POST
	@Path(value = "/getActivityInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getActivityInfo(JSONObject results) {
		return activityService.getActivityInfo(results).toJsonString();
	}
	
	@POST
	@Path(value = "/enrollService")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String enrollService(JSONObject json) {
		return enrollService.signUp(json).toJsonString();
	}

	@POST
	@Path(value = "/getActivityCategoryList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getActivityCategoryList(JSONObject results) {
		return activityService.getActivityCategoryList(results).toJsonString();
	}

}
