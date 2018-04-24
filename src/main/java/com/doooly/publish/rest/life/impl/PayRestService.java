package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doooly.business.pay.service.PayServiceI;
import com.doooly.dto.user.GetPayVerifyCodeReq;
import com.doooly.dto.user.GetPayVerifyCodeRes;
import com.doooly.publish.rest.life.PayRestServiceI;

/**
 * 支付功能接口
 * @author 赵清江
 * @date 2016年7月22日
 * @version 1.0
 */
@Component
@Path("/pay")
public class PayRestService implements PayRestServiceI{

	@Autowired
	private PayServiceI payService;
	
	@POST
	@Path(value="/getPayCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getPayCode(GetPayVerifyCodeReq req) {
		System.out.println(req.toString());
		GetPayVerifyCodeRes response = payService.getPayVerifyCode(req);
		System.out.println(response.toJsonString());
		return response.toJsonString();
	}

}
