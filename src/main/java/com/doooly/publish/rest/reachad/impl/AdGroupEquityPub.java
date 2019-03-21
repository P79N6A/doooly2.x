package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.groupEquity.AdGroupEquityService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.reachad.AdGroupEquityPubService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 
 * @author sfc
 * @date 2019.3.18
 */
@Component
@Path("/groupEquity")
public class AdGroupEquityPub implements AdGroupEquityPubService {

	private static Logger logger = Logger.getLogger(AdGroupEquityPub.class);

	@Autowired
	private AdGroupEquityService adGroupEquityService;

	@POST
	@Path(value="/getGroupEquity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getGroupEquityList(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try{
			String groupId = request.getHeader("groupId");
			logger.info("<<<调用接口参数adGroupId："+groupId);
			result.put("data", JSONArray.parseArray(adGroupEquityService.adGroupEquityLevelList(groupId)));
			result.put("code", MessageDataBean.success_code);
			result.put("msg", MessageDataBean.success_mess);
		}catch (Exception e){
			logger.info("%%%getGroupEquityist error is " + e.getMessage());
			result.put("code", MessageDataBean.failure_code);
			result.put("msg", MessageDataBean.failure_mess);
			result.put("data", "查询异常！");
			return result.toString();
		}
		return result.toString();
	}

	@POST
	@Path(value="/getEquityByEquityId")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getEquityByEquityId(JSONObject paramJSON) {
		JSONObject result = new JSONObject();
		try{
			String equityId = paramJSON.getString("equityId").toString();
			logger.info("<<<equityId："+equityId);
			result.put("data", JSONObject.parseObject(adGroupEquityService.adEquityByEquityId(equityId)));
			result.put("code", MessageDataBean.success_code);
			result.put("msg", MessageDataBean.success_mess);
		}catch (Exception e){
			logger.info("%%%getEquityByEquityId error is " + e.getMessage());
			result.put("code", MessageDataBean.failure_code);
			result.put("msg", MessageDataBean.failure_mess);
			result.put("data", "查询异常！");
			return result.toString();
		}
		return result.toString();
	}
}