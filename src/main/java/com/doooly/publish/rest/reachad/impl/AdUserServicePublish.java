package com.doooly.publish.rest.reachad.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.common.service.impl.AdGroupService;
import com.doooly.business.user.service.UserServiceI;
import com.doooly.common.constants.Constants;
import com.doooly.common.dto.BaseRes;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdUser;
import com.doooly.publish.rest.reachad.AdUserServicePublishI;

@Component
@Path("/member")
public class AdUserServicePublish implements AdUserServicePublishI {
	private static Logger log = LoggerFactory.getLogger(AdUserServicePublish.class);
	@Autowired
	private AdUserServiceI adUserService;
	@Autowired
	private UserServiceI userService;

	@Autowired
	private AdGroupService adGroupService;

	@POST
	@Path(value = "/batchSendSms")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject batchSendSms(JSONObject paramJSON) {
		JSONObject result = new JSONObject();
		try {
			AdUser user = paramJSON.getObject("user", AdUser.class);
			boolean alidayuFlag = paramJSON.getBoolean("alidayuFlag");
			JSONObject paramSMSJson = paramJSON.getJSONObject("smsParam");
			String mobiles = paramJSON.getString("mobiles");
			String alidayuSmsCode = paramJSON.getString("alidayuSmsCode");
			String smsContent = paramJSON.getString("smsContent");
			result = adUserService.batchSendSms(user, paramSMSJson, mobiles, alidayuSmsCode, smsContent, alidayuFlag);
			result.put("code", Constants.ResponseEnum.SUCCESS.getCode());
			log.info("batchSendSms success=" + result.toJSONString());
		} catch (Exception e) {
			log.error("",e);
			result.put("code", Constants.ResponseEnum.ERROR.getCode());
			result.put("info", "batchSendSms=" + e.getMessage());
		}
		return result;
	}

	@POST
	@Path(value = "/validateUserInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject validateUserInfo(JSONObject param) {
		log.info("====验证用户信息参数-param：" + param.toJSONString());
		JSONObject jsonResult = new JSONObject();
		try {
			jsonResult = adUserService.validateUserInfo(param);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.FAIL.getCode());
			jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.FAIL.getMsg());
		}
		log.info("====验证用户信息返回数据-jsonResult：" + jsonResult.toJSONString());
		return jsonResult;
	}

	@POST
	@Path(value = "/validateAndActive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject validateAndActive(JSONObject param) {
		JSONObject resultData = new JSONObject();
		try {
			resultData = adUserService.validateAndActive(param);
		} catch (Exception e) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.FAIL.getMsg());
			log.info("====【validateAndActive】系统异常=====");
			e.printStackTrace();
		}
		return resultData;
	}

	@POST
	@Path(value = "/verifyCodeToActive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String verifyCodeAndActivation(JSONObject param,@Context HttpServletRequest request) {
		String groupId = request.getHeader("groupId");
		String channel = request.getHeader(Constants.CHANNEL);
		log.info("verifyCodeToActive请求参数：{}，{},{}",param,groupId,channel);
		if (StringUtils.isNotBlank(groupId)) {
			param.put("groupId",groupId);
		}
		if (StringUtils.isNotBlank(channel)) {
			param.put(Constants.CHANNEL,channel);
		}
		JSONObject resultData = new JSONObject();
		try {
			resultData = adUserService.verifyCodeAndActivation(param);
		} catch (Exception e) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.FAIL.getMsg());
			log.info("====【validateAndActive】系统异常=====");
			e.printStackTrace();
		}
		return resultData.toJSONString();
	}

	@POST
	@Path(value = "/getGroupCommandInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getGroupCommandInfo(JSONObject param) {
		MessageDataBean dataBean = new MessageDataBean();

		try {
			dataBean = adGroupService.getGroupCommandInfo(param);
		} catch (Exception e) {
			dataBean = new MessageDataBean(ConstantsLogin.CommandActive.FAIL.getCode(),
					ConstantsLogin.CommandActive.FAIL.getMsg());
			log.info("====【AdUserServicePublish】系统异常" + e.getMessage() + "=====");
			e.printStackTrace();
		}

		return dataBean.toJsonString();
	}

	@POST
	@Path(value = "/getGroupByCommand")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getGroupByCommand(JSONObject param) {
		MessageDataBean dataBean = new MessageDataBean();
		log.info("====【AdUserServicePublish】-通过口令获取企业集合-传入参数：" + param.toJSONString());
		try {
			dataBean = adGroupService.getGroupByCommand(param);
		} catch (Exception e) {
			dataBean = new MessageDataBean(ConstantsLogin.CommandActive.FAIL.getCode(),
					ConstantsLogin.CommandActive.FAIL.getMsg());
			log.info("====【AdUserServicePublish】系统异常" + e.getMessage() + "=====");
			e.printStackTrace();
		}

		return dataBean.toJsonString();
	}

	@POST
	@Path(value = "/groupCommandActive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String groupCommandActive(JSONObject param) {
		MessageDataBean dataBean = new MessageDataBean();
		log.info("====【groupCommandActive】-企业口令激活-传入参数：" + param.toJSONString());
		try {
			dataBean = adUserService.groupCommandActive(param);
		} catch (Exception e) {
			dataBean = new MessageDataBean(ConstantsLogin.CommandActive.FAIL.getCode(),
					ConstantsLogin.CommandActive.FAIL.getMsg());
			log.info("====【groupCommandActive】系统异常" + e.getMessage() + "=====");
			e.printStackTrace();
		}

		return dataBean.toJsonString();
	}

	@POST
	@Path(value = "/checkQuickLogin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public BaseRes<Object> checkQuickLogin(JSONObject req) {
		long start = System.currentTimeMillis();
		String groupId = req.getString("groupId");
		String mobile = req.getString("mobile");
		String name = req.getString("name");
		String groupName = req.getString("groupName");
		Long actionTime = req.getLong("actionTime");
		String sign = req.getString("sign");
		BaseRes result = userService.checkQuickLogin(groupId, mobile, name, groupName, actionTime, sign);
		log.info(String.format("企业平台跳转到兜礼快捷登录结果=%s, 耗时=%s", result.toJsonString(),
				(System.currentTimeMillis() - start)));
		return result;
	}

	@POST
	@Path(value = "/updates")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public BaseRes<JSONObject> batchUpdateUser(JSONObject req) {
		long start = System.currentTimeMillis();
		BaseRes<JSONObject> result = userService.batchUpdateUser(req);
		log.info(String.format("企业平台批量更新结果：groupId=%s,result=%s, 耗时=%s", req.getString("groupId"),
				result.toJsonString(), (System.currentTimeMillis() - start)));
		return result;
	}

	@POST
	@Path(value = "/saveTelephoneChange")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public BaseRes<JSONObject> saveTelephoneChange(JSONObject req) {
		long start = System.currentTimeMillis();
		BaseRes<JSONObject> result = userService.saveTelephoneChange(req);
		log.info(String.format("====>>保存手机号修改记录：jsonResult=%s, 耗时=%s", result.toJsonString(),
				(System.currentTimeMillis() - start)));
		return result;
	}

	// @POST
	// @Path(value = "/schedule")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public void scheduleUpdateUser(JSONObject req) {
	// long start = System.currentTimeMillis();
	// userService.scheduleUpdateUser();
	// //log.info(String.format("企业平台批量更新结果：groupId=%s,result=%s, 耗时=%s",
	// req.getString("groupId"),
	// result.toJsonString(),(System.currentTimeMillis()-start)));
	// }
	// @POST
	// @Path(value = "/initializeSchedule")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public void initializeSchedulecheduleUpdateUser(JSONObject req) {
	// long start = System.currentTimeMillis();
	// userService.initializeScheduleUpdateUser();
	// //log.info(String.format("企业平台批量更新结果：groupId=%s,result=%s, 耗时=%s",
	// req.getString("groupId"),
	// result.toJsonString(),(System.currentTimeMillis()-start)));
	// }
}
