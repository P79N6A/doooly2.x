package com.doooly.publish.rest.reachad.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.impl.DailyCommonActivityService;
import com.doooly.business.activity.impl.XingFuJiaoHangActivityService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.reachad.DailyActivityServiceI;

@Component
@Path("/daily-activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DailyActivityServiceRest implements DailyActivityServiceI {

	@Autowired
	private DailyCommonActivityService dailyCommonService;
	@Autowired
	private XingFuJiaoHangActivityService jiaohangService;

	@POST
	@Path(value = "/v1/daily")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public MessageDataBean commonActivitySendCopuon(JSONObject jsonReq) {
		MessageDataBean result = dailyCommonService.send(jsonReq);
		return result;
	}

	@POST
	@Path(value = "/v1/jiaohang")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public MessageDataBean jiaoHangActivitySendCopuon(JSONObject jsonReq) {
		MessageDataBean result = jiaohangService.send(jsonReq);
		return result;
	}

}
