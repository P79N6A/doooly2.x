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
import com.doooly.business.vipWeal.VipWealNewBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.VipWealServiceI;

/**
 * 答题活动 rest service实现
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
@Component
@Path("/vipWeal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VipWealService implements VipWealServiceI {
	private static Logger logger = Logger.getLogger(VipWealService.class);
	@Autowired
	private VipWealNewBusinessServiceI vipWealBusinessServiceI;
	
	@POST
	@Path(value="/getBenefitsData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String benefitsData(JSONObject obj) {
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		String address = obj.getString("address");
		logger.info("专享福利的地域信息为============="+address);
		MessageDataBean messageDataBean = vipWealBusinessServiceI.getBenefitsData(userId,address);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}	
	@POST
	@Path(value="/getIntegralAndBusinessList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getIntegralAndBusinessList(JSONObject obj) {
		// 获取用户id
		Long userId = obj.getLong("userId");
		MessageDataBean messageDataBean = vipWealBusinessServiceI.getIntegralAndBusinessList(userId);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}	

	
		
}
