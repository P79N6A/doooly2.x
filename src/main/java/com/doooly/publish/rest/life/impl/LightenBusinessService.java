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
import com.doooly.business.lightenBusiness.AdLightenBusinessServiceI;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.LightenBusinessServiceI;

@Component
@Path("/lightenBusiness")
public class LightenBusinessService implements LightenBusinessServiceI {

	private static Logger logger = Logger.getLogger(LightenBusinessService.class);
	@Autowired
	private AdLightenBusinessServiceI adLightenBusinessService;
	
	@Autowired
	private AdBusinessDao adBusinessDao;
	
	@Override
	@POST
	@Path(value = "/getAllBusiness")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAllBusiness(JSONObject obj) {
		JSONObject res = new JSONObject();
		logger.info("获取所有商户的列表参数:" + obj.toJSONString());
		res = adLightenBusinessService.getAllBusiness(obj);
		// 获取所有商户的列表
		logger.info("获取所有商户的列表返回数据:" + res.toJSONString());
		String resString = JSONObject.toJSONString(res);
		return resString;
	}
	
	
	@Override
	@POST
	@Path(value = "/lightenBusiness")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String lightenBusiness(JSONObject obj) {
		JSONObject res = new JSONObject();
		logger.info("点亮商户参数:" + obj.toJSONString());
		res = adLightenBusinessService.lightenBusiness(obj);
		logger.info("点亮商户返回参数:" + res.toJSONString() + "==========");
		return JSONObject.toJSONString(res);
	}
	
	@Override
	@POST
	@Path(value = "/reservationLightenBusiness")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String reservationOrLightenBusiness(JSONObject obj) {
		JSONObject res = new JSONObject();
		logger.info("预约或点亮商户参数:" + obj.toJSONString());
		res = adLightenBusinessService.reservationOrLightenBusiness(obj);
		logger.info("预约或点亮商户返回参数:" + res.toJSONString() + "==========");
		return JSONObject.toJSONString(res);
	}
	
	@Override
	@POST
	@Path(value = "/getBusinessDeatil")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getBusinessDeatil(JSONObject obj) {
		JSONObject res = new JSONObject();
		logger.info("预约点亮商户参数:" + obj.toJSONString());
		res = adLightenBusinessService.getBusinessDeatil(obj);
		logger.info("预约点亮商户返回参数:" + res.toJSONString() + "==========");
		return JSONObject.toJSONString(res);
	}
	@Override
	@POST
	@Path(value = "/lightSearch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String lightSearch(JSONObject obj) {
		JSONObject res = new JSONObject();
		logger.info("查询预约点亮商户参数:" + obj.toJSONString());
		String businessId = obj.getString("businessId");
		String userId = obj.getString("userId");
		MessageDataBean messageDataBean = adLightenBusinessService.lightSearch(businessId,userId);
		logger.info("查询点亮商户返回参数:" + messageDataBean.toJsonString() + "==========");
		return messageDataBean.toJsonString();
	}
	@Override
	@POST
	@Path(value = "/getLightType")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getLightType(JSONObject obj) {
		JSONObject res = new JSONObject();
		logger.info("查询预约点亮商户参数:" + obj.toJSONString());
		String businessId = obj.getString("businessId");
		String userId = obj.getString("userId");
		Integer messageDataBean = adLightenBusinessService.lightenBusinessType(userId,businessId);
		logger.info("查询点亮商户返回参数:" + messageDataBean.toString() + "==========");
		return messageDataBean.toString();
	}
	
}
