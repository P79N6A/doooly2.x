package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.user.service.UserServiceI;
import com.doooly.common.exception.GlobalException;
import com.doooly.common.token.TokenUtil;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.ConstantsLogin.Login;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.TokenRestServiceI;

/**
 * 
 * 用户访问token
 * 
 * @Title: TokenRestServiceI.java
 * @author hutao
 * @date 2017年7月25日
 * @version V1.0
 */
@Component
@Path("/token")
public class TokenRestService implements TokenRestServiceI {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserServiceI userService;
	@POST
	@Path(value = "/validateUserToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject validUserToken(JSONObject json) {
		Long start = System.currentTimeMillis();
		JSONObject dataJson = new JSONObject();
		try {
			// todo 验证渠道/密码 String channel, String pwd, String userId
			String userId = json.getString("userId");
			String userToken = json.getString(ConstantsLogin.TOKEN);
			String channel = json.getString(ConstantsLogin.CHANNEL);
			// 3.验证token有效性
			boolean validResult = TokenUtil.validUserToken(channel, userId, userToken);
			// 验证失败返回token信息
			if (!validResult) {
				dataJson.put(ConstantsLogin.CODE, GlobalException.TOKEN_CODE);
				dataJson.put(ConstantsLogin.MESS, "token验证失败或token不存在");
			} else {
				dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.SUCCESS.getCode());
				dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.SUCCESS.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			dataJson.put(ConstantsLogin.CODE, GlobalException.TOKEN_CODE);
			dataJson.put(ConstantsLogin.MESS, "token验证失败,系统错误");
		}
		log.info("validUserToken cost={}", System.currentTimeMillis() - start);
		return dataJson;
	}

	@POST
	@Path(value = "/getToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUserToken(JSONObject json) {
		// todo 验证渠道/密码
		// String channel, String pwd, String userId
		return TokenUtil.getUserToken(json.getString(ConstantsLogin.CHANNEL), json.getString("userId"));
	}

	@POST
	@Path(value = "/refreshToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String refreshToken(JSONObject json) {
		// todo 验证渠道/密码
		// String channel, String pwd, String userId
		return TokenUtil.refreshUserToken(json.getString(ConstantsLogin.CHANNEL), json.getString("userId"));
	}

	@POST
	@Path(value = "/cancelToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public MessageDataBean cancelToken(JSONObject json) {
		JSONArray phones = json.getJSONArray("phones");
		if (phones != null) {
			for (int index = 0; index < phones.size(); index++) {
				String phoneNo = phones.getString(index);
				userService.cancelUserByphoneNo(phoneNo);
			}
		}
		return new MessageDataBean(Login.SUCCESS.getCode(), Login.SUCCESS.getMsg());
	}
}
