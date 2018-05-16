package com.doooly.publish.rest.life.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myaccount.service.ComplaintBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.ComplaintServiceI;

/**
 * 联系客服
 * @author sunzilei
 * @date 2017年7月20日
 * @version 1.0
 */
@Component
@Path("/complaint")
public class ComplaintService implements ComplaintServiceI {
	
	private static Logger log = Logger.getLogger(ComplaintService.class);

	@Autowired
	private ComplaintBusinessServiceI complaintBusinessServiceI;
	
	@POST
	@Path(value = "/save")
	@Produces("text/plain;charset=utf-8")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public String complaintSave(@Context HttpServletRequest request) {
		log.info("保存申诉开始。。。。");
		JSONObject result = new JSONObject();

		try {
			complaintBusinessServiceI.complaintSave(request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("保存申述异常：" + e);
			result.put("code", 1002);
			result.put("msg", "保存申述信息失败！");
			return result.toJSONString();
		}

		result.put("code", 1000);
		result.put("msg", "保存申述信息成功！");
		return result.toJSONString();
	}

	@POST
	@Path(value = "/saveForAppTwo")
	@Produces("text/plain;charset=utf-8")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String complaintSaveForAppTwo(@Context HttpServletRequest request) {
		log.info("保存申诉app2.0开始。。。。");
		JSONObject result = new JSONObject();
		try {
		complaintBusinessServiceI.complaintSaveForAppTwo(request);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("保存申述app2.0异常：" + e);
			result.put("code", 1002);
			result.put("msg", "保存申述信息失败！");
			return result.toJSONString();
		}

		result.put("code", 1000);
		result.put("msg", "保存申述信息成功！");
		return result.toJSONString();
	}

	

}
