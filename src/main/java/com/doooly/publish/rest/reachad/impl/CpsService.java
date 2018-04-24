package com.doooly.publish.rest.reachad.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.service.CpsSummaryServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.reachad.CpsServiceI;

@Component
@Path("/cps")
public class CpsService implements CpsServiceI {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private CpsSummaryServiceI cpsSummaryService;

	@POST
	@Path(value = "/updateCps")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject updateCps(String cpsJson) {
		JSONObject result = new JSONObject();
		if (cpsJson == null || cpsJson.length() == 0) {
			result.put("code", MessageDataBean.failure_code);
			return result;
		}
		try {
			cpsSummaryService.updateCpsFee(cpsJson);
			result.put("code", MessageDataBean.success_code);
		} catch (Exception e) {
			logger.error(e);
			result.put("code", MessageDataBean.failure_code);
		}
		return result;
	}

	@POST
	@Path(value = "/updateCps/batch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject batchUpdateCps(JSONArray cpsJsonArray) {
		JSONObject result = new JSONObject();
		if (cpsJsonArray == null || cpsJsonArray.size() == 0) {
			result.put("code", MessageDataBean.failure_code);
			return result;
		}
		try {
			int size = cpsJsonArray.size();
			for (int i = 0; i < size; i++) {
				String cpsJson = cpsJsonArray.getJSONObject(i).toJSONString();
				cpsSummaryService.updateCpsFee(cpsJson);
				logger.info("正在更新第" + i + "条");
			}
			result.put("code", MessageDataBean.success_code);
		} catch (Exception e) {
			logger.error(e);
			result.put("code", MessageDataBean.failure_code);
		}
		return result;
	}

}
