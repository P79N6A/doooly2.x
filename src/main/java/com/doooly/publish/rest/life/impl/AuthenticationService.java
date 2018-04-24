package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doooly.business.user.service.UserServiceI;
import com.doooly.dto.user.CheckVerifyCodeReq;
import com.doooly.dto.user.CheckVerifyCodeRes;
import com.doooly.dto.user.GetVerifyCodeReq;
import com.doooly.dto.user.GetVerifyCodeRes;
import com.doooly.publish.rest.life.AuthenticationServiceI;

/**
 * restful接口类(通用功能,请求验证码)
 * @author Albert
 * @date 2016-07-11
 * @version 1.0
 */
@Component
@Path("/code")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationService implements AuthenticationServiceI{

	@Autowired
	private UserServiceI userService;
	
	@POST
	@Path(value="/getVerifyCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String demandVerifyCode(GetVerifyCodeReq req) {
		System.out.println(req.toString());
		GetVerifyCodeRes response = userService.getVerifyCode(req);
		System.out.println(response.toJsonString());
		return response.toJsonString();
	}

	@POST
	@Path(value="/checkVerifyCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String checkVerifyCode(CheckVerifyCodeReq req) {
		System.out.println(req.toString());
		CheckVerifyCodeRes response = userService.checkVerifyCode(req);
		System.out.println(response.toJsonString());
		return response.toJsonString();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
