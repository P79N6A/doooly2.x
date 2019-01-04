package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.impl.DailyCommonActivityService;
import com.doooly.business.activity.impl.XingFuJiaoHangActivityService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.reachad.DailyActivityServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 
    * @ClassName: DailyActivityServiceRest  
    * @Description: 兜礼日常运营活动发布接口类
    * @author hutao  
    * @date 2018年10月15日  
    *
 */
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
	public String commonActivitySendCopuon(JSONObject jsonReq) {
		MessageDataBean result = dailyCommonService.send(jsonReq);
		return result.toJsonString();
	}

	@POST
	@Path(value = "/v1/checkIfSendCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String checkIfSendCode(JSONObject jsonReq) {
		MessageDataBean result = dailyCommonService.checkIfSendCode(jsonReq);
		return result.toJsonString();
	}

	@POST
	@Path(value = "/v1/dailyQuery")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String commonActivityQueryCopuon(JSONObject jsonReq) {
		MessageDataBean result = dailyCommonService.query(jsonReq);
		return result.toJsonString();
	}

	@POST
	@Path(value = "/v1/jiaohang")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String jiaoHangActivitySendCopuon(JSONObject jsonReq) {
		MessageDataBean result = jiaohangService.send(jsonReq);
		return result.toJsonString();
	}

	@POST
	@Path(value = "/v1/jiaohang/lottery")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String jiaoHanLotteryActivitySendCopuon(JSONObject jsonReq) {
        jsonReq.put("remarks", "交行亲友" + jsonReq.getString("remarks"));
		MessageDataBean result = jiaohangService.sendLotteryCode(jsonReq);
		return result.toJsonString();
	}

}
