package com.doooly.publish.rest.life.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.doooly.business.common.service.AdUserServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.user.service.UserServiceI;
import com.doooly.common.constants.Constants.ResponseCode;
import com.doooly.common.dto.BaseRes;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.user.CheckActiveCodeReq;
import com.doooly.dto.user.CheckActiveCodeRes;
import com.doooly.dto.user.CheckVerifyCodeReq;
import com.doooly.dto.user.CheckVerifyCodeRes;
import com.doooly.dto.user.LoginReq;
import com.doooly.dto.user.LoginRes;
import com.doooly.dto.user.LogoutReq;
import com.doooly.dto.user.LogoutRes;
import com.doooly.dto.user.ModifyMobileReq;
import com.doooly.dto.user.ModifyPwdReq;
import com.doooly.dto.user.ModifyPwdRes;
import com.doooly.dto.user.UserActiveNewReq;
import com.doooly.dto.user.UserActiveReq;
import com.doooly.dto.user.UserActiveRes;
import com.doooly.entity.reachad.AdUser;
import com.doooly.publish.rest.life.UserRestServiceI;

/**
 * 对外会员服务接口类
 * 
 * @author 赵清江
 * @date 2016-06-28
 * @version 1.0
 */
@Component
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestService implements UserRestServiceI {

	private static Logger logger = Logger.getLogger(UserRestService.class);

	@Autowired
	private UserServiceI userService;
    @Autowired
    private AdUserServiceI adUserServiceI;

	@POST
	@Path(value = "/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(LoginReq req) {
		String json = null;
		LoginRes response = null;
		System.out.println(req.toString());
		response = userService.manageLogin(req);
		json = response.toJsonString();
		System.out.println(json);
		return json;
	}

	@POST
	@Path(value = "/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String logOut(LogoutReq req) {
		String response = null;
		System.out.println(req.toString());
		LogoutRes res = userService.manageLogout(req);
		response = res.toJsonString();
		System.out.println(response);
		return response;
	}

	@POST
	@Path(value = "/checkActiveCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String checkActiveCode(CheckActiveCodeReq req) {
		System.out.println(req.toString());
		CheckActiveCodeRes response = userService.checkActiveCode(req);
		System.out.println(response.toJsonString());
		return response.toJsonString();
	}

	@POST
	@Path(value = "/active")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String userActive(UserActiveReq req) {
		System.out.println(req.toString());
		// 1:验证手机验证码
		CheckVerifyCodeReq checkVerifyCodeReq = new CheckVerifyCodeReq();
		checkVerifyCodeReq.setMobile(req.getNewMobile());
		checkVerifyCodeReq.setStoresId(null);
		checkVerifyCodeReq.setVerifyCode(req.getVerifyCode());
		CheckVerifyCodeRes checkVerifyCodeRes = userService.checkVerifyCode(checkVerifyCodeReq);
		if (!checkVerifyCodeRes.getCode().equals(ResponseCode.SUCCESS.getCode())) {
			System.out.println(checkVerifyCodeRes.toJsonString());
			return checkVerifyCodeRes.toJsonString();
		}
		// 2:进行激活操作
		UserActiveRes response = userService.manageUserActive(req);
		System.out.println(response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 新兜礼激活（只进行激活操作，不验证）
	 * 
	 * @param userId
	 * @return
	 */
	@POST
	@Path(value = "/activeNew")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String userActiveNew(UserActiveNewReq req) {
		BaseRes response = userService.managerUserActiveNew(req);
		return response.toJsonString();
	}

	@POST
	@Path(value = "/modifyPwd")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String userModifyPwd(ModifyPwdReq req) {
		System.out.println(req.toString());
		ModifyPwdRes res = userService.modifyPassword(req);
		System.out.println(res.toJsonString());
		return res.toJsonString();
	}

	@Override
	public String userModifyMobile(ModifyMobileReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@POST
	@Path(value = "/userLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject userLogin(JSONObject paramJson) {
		JSONObject dataJson = new JSONObject();
		try {
			dataJson = userService.userLogin(paramJson);
		} catch (Exception e) {
			dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.FAIL.getCode());
			dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.FAIL.getMsg());
			logger.info("========rest系统异常=====");
			e.printStackTrace();
		}
		return dataJson;
	}

	@POST
	@Path(value = "/userValidateLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject userValidateLogin(JSONObject paramJson) {
		JSONObject dataJson = new JSONObject();
		try {
			dataJson = userService.userValidateLogin(paramJson);
		} catch (Exception e) {
			dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.FAIL.getCode());
			dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.FAIL.getMsg());
			logger.info("========rest系统异常=====");
			e.printStackTrace();
		}
		return dataJson;
	}

	@POST
	@Path(value = "/userLogout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String userLogout(JSONObject paramJson, @Context HttpServletRequest request) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {

			messageDataBean = userService.userLogout(paramJson, request);
		} catch (Exception e) {
			messageDataBean.setCode(ConstantsLogin.Login.FAIL.getCode());
			messageDataBean.setMess(ConstantsLogin.Login.FAIL.getMsg());
			logger.info("========rest系统异常=====");
			e.printStackTrace();
		}
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/addActiveUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject addActiveUser(JSONObject userJson) {
		JSONObject result = new JSONObject();
		String mobile = userJson.getString("mobile");
		String groupName = userJson.getString("groupName");
		try {
			AdUser user = new AdUser();
			user.setTelephone(mobile);
			userService.addUser(user, groupName);
		} catch (Exception e) {
			result.put("code", -1);
			result.put("msg", "error" + e.getMessage());
			return result;
		}
		result.put("code", 0);
		result.put("msg", "OK");
		return result;
	}

	@POST
	@Path(value = "/getWechatUserInitInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getWechatUserInitInfo(JSONObject json) {
		BaseRes<JSONObject> baseRes = new BaseRes<JSONObject>();
		try {
			// username-卡号,channel-微信终端
			json.put("channel", "wechat");
			baseRes = userService.getWechatUserInitInfo(json);
		} catch (Exception e) {
			e.printStackTrace();
			baseRes = new BaseRes<>(MessageDataBean.failure_code, MessageDataBean.failure_mess);
		}
		return baseRes.toJsonString();
	}

    @POST
    @Path(value = "/userBind")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String userBind(JSONObject paramJson, @Context HttpServletRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            messageDataBean = adUserServiceI.userBind(paramJson);
        } catch (Exception e) {
            messageDataBean.setCode(ConstantsLogin.Login.FAIL.getCode());
            messageDataBean.setMess(ConstantsLogin.Login.FAIL.getMsg());
            logger.info("========rest系统异常=====",e);
        }
        return messageDataBean.toJsonString();
    }
}
