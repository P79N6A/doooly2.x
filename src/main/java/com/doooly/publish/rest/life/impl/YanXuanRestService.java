package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.YanXuanBusinessServiceI;
import com.doooly.business.activity.impl.YanXuanBusinessService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.YanXuanRestServiceI;

@Component
@Path("/YanXuan")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class YanXuanRestService implements YanXuanRestServiceI {

	private static Logger logger = Logger.getLogger(YanXuanRestService.class);
	@Autowired
	private YanXuanBusinessService yanXuanBusinessService;
	
	
	@POST
	@Path(value = "/getCouponInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getCouponInfo(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String telephone = obj.getString("telephone");// 手机号
			String type = obj.getString("type");// 会员类型
			String cardNumber = obj.getString("cardNumber");// 卡号
			String activityId = obj.getString("activityId");// 卡号
			
			messageDataBean = yanXuanBusinessService.getCounponInfo(userId, telephone,type,cardNumber,activityId);
		} catch (Exception e) {
			logger.error("网易严选活动|领取优惠券异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	
	
	@POST
	@Path(value = "/getCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getCoupon(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String telephone = obj.getString("telephone");// 手机号
			String type = obj.getString("type");// 会员类型
			String cardNumber = obj.getString("cardNumber");// 卡号
			String activityId = obj.getString("activityId");// 卡号
			
			messageDataBean = yanXuanBusinessService.getCounpon(userId, telephone,type,cardNumber,activityId);
		} catch (Exception e) {
			logger.error("网易严选活动|领取优惠券异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	@POST
	@Path(value = "/myCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String myCoupon(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String activityId = obj.getString("activityId");// 活动Id
			messageDataBean = yanXuanBusinessService.myCoupon(userId,activityId);
		} catch (Exception e) {
			logger.error("网易严选活动|我的福利异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	
	@POST
	@Path(value = "/sendWangYiCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String sendWangYiCoupon(String obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			messageDataBean = yanXuanBusinessService.sendWangYiCoupon(obj);
		} catch (Exception e) {
			logger.error("网易严选活动|领取优惠券异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	/*==========================================================严选活动2=========================================================*/

	@POST
	@Path(value = "/getCouponTwoInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getCouponTwoInfo(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String telephone = obj.getString("telephone");// 手机号
			String type = obj.getString("type");// 会员类型
			String cardNumber = obj.getString("cardNumber");// 卡号
			String idFlag = obj.getString("idFlag");// 卡号
			
			messageDataBean = yanXuanBusinessService.getCouponTwoInfo(userId, telephone,type,cardNumber,idFlag);
		} catch (Exception e) {
			logger.error("网易严选活动异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}


	@POST
	@Path(value = "/getPrivilege")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getPrivilege(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String telephone = obj.getString("telephone");// 手机号
			String type = obj.getString("type");// 会员类型
			String cardNumber = obj.getString("cardNumber");// 卡号
			String idFlag = obj.getString("idFlag");//后台id标识
			String sex = obj.getString("sex");//后台id标识
			
			messageDataBean = yanXuanBusinessService.getPrivilege(userId, telephone,type,cardNumber,idFlag,sex);
		} catch (Exception e) {
			logger.error("网易严选活动，执行我要特权异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}


	@POST
	@Path(value = "/toBind")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String toBind(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String telephone = obj.getString("telephone");// 手机号
			String type = obj.getString("type");// 会员类型
			String cardNumber = obj.getString("cardNumber");// 卡号
			String idFlag = obj.getString("idFlag");// 卡号
			messageDataBean = yanXuanBusinessService.toBind(userId, telephone,type,cardNumber,idFlag);
		} catch (Exception e) {
			logger.error("网易严选活动，执行我要特权异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}


	@POST
	@Path(value = "/sendCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String sendCoupons(String msg) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			messageDataBean = yanXuanBusinessService.sendWangYiTwoCoupons(msg);
		} catch (Exception e) {
			logger.error("网易严选活动发送卡券异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	
}
