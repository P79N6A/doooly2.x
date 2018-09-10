package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myaccount.service.MyAccountServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.MyAccountRestServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心
 * 
 * @author yuelou.zhang
 * @date 2017年7月18日
 * @version 1.0
 */
@Component
@Path("/myAccount")
public class MyAccountRestService implements MyAccountRestServiceI {

	private static Logger logger = Logger.getLogger(MyAccountRestService.class);
	@Autowired
	private MyAccountServiceI myAccountService;

	@POST
	@Path(value = "/accountList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAccountList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
		String userId = obj.getString("userId");
		HashMap<String,Object>  map = myAccountService.getAccountListById(userId);
		messageDataBean.setCode(MessageDataBean.success_code);
		messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取账户列表数据异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/updatePersonalData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updatePersonalData(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			myAccountService.updatePersonalData(obj);
			messageDataBean.setCode(MessageDataBean.success_code);
		} catch (Exception e) {
			logger.error("更新个人资料异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/updateAppHeadImageUrl")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String updateAppHeadImageUrl(@Context HttpServletRequest request) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 1.上传头像到图片服务器并生成图片地址
			JSONObject jsonObj = myAccountService.uploadImage(request);
			// 2.根据userId更新用户头像
			myAccountService.updatePersonalData(jsonObj);
			// 3.返回上传头像生成的图片地址
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("appHeadImageUrl", jsonObj.get("appHeadImageUrl"));
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} catch (Exception e) {
			logger.error("更新用户头像异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/getUserInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUserInfo(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");
			String businessId = obj.getString("businessId");
			String username = obj.getString("username");
			String password = obj.getString("password");
			HashMap<String, Object> map = myAccountService.getUserInfoById(userId, businessId, username, password);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取个人中心数据异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/getFamilyUserInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getFamilyUserInfo(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");
			HashMap<String, Object> map = myAccountService.getFamilyUserInfo(userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取个人中心数据异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/batchGetFamilyUserInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String batchGetFamilyUserInfo(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
            List<Map> invitationFamilyList = (List<Map>) obj.get("invitationFamilyList");
			HashMap<String, Object> map = myAccountService.batchGetFamilyUserInfo(invitationFamilyList);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取个人中心数据异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}


	@POST
	@Path(value = "/isSetPayPassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String isSetPayPassword(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
            String userId = obj.getString("userId");
			HashMap<String, Object> map = myAccountService.isSetPayPassword(userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取是否设置过支付密码数据异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/setPayPassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String setPayPassword(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
            String userId = obj.getString("userId");
            String payPassword = obj.getString("payPassword");
            String isPayPassword = obj.getString("isPayPassword");
            messageDataBean = myAccountService.setPayPassword(userId,payPassword,isPayPassword);
		} catch (Exception e) {
			logger.error("设置支付密码数据异常！",e);
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("设置支付密码数据异常");
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/validPayPassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String validPayPassword(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
            String userId = obj.getString("userId");
            String payPassword = obj.getString("payPassword");
            messageDataBean = myAccountService.validPayPassword(userId,payPassword);
		} catch (Exception e) {
			logger.error("设置支付密码数据异常！",e);
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("设置支付密码数据异常");
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/openPayPassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String openPayPassword(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
            String userId = obj.getString("userId");
            String isPayPassword = obj.getString("isPayPassword");
            messageDataBean = myAccountService.openPayPassword(userId,isPayPassword);
		} catch (Exception e) {
			logger.error("设置支付方式数据异常！",e);
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("设置支付方式数据异常");
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
