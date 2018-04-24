package com.doooly.publish.rest.reachad.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdSendGiftServiceI;
import com.doooly.common.constants.Constants;
import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.entity.reachad.AdBusinessActivityOrder;
import com.doooly.publish.rest.reachad.AdBusinessActivityServiceI;

@Component
@Path("/businessActivity")
public class AdBusinessActivityService implements AdBusinessActivityServiceI {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AdSendGiftServiceI adSendGiftServiceI;

	@POST
	@Path(value = "/sendGift")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject senGift(String jsonStr) {
		JSONObject result = new JSONObject();
		try {
			AdBusinessActivityOrder adBusinessActivityOrder = JSONObject.parseObject(JSON.toJSONString(jsonStr), AdBusinessActivityOrder.class);
			adSendGiftServiceI.sendGift(adBusinessActivityOrder);
			logger.info("senGift success=" + result.toJSONString());
		} catch (Exception e) {
			logger.error(e);
			result.put("code", Constants.ResponseEnum.ERROR.getCode());
			result.put("msg", "senGift=" + e.getMessage());
		}
		return result;
	}
}
