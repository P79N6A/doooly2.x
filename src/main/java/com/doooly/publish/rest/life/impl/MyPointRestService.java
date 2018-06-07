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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mypoint.service.MyPointServiceI;
import com.doooly.common.constants.ConstantsV2;
import com.doooly.common.dto.BaseReq;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.MyPointRestServiceI;

/**
 * @Description: 积分接口实现
 * @author: qing.zhang
 * @date: 2017-05-18
 */
@Component
@Path("/mypoint")
public class MyPointRestService implements MyPointRestServiceI {

	private static final Logger logger = LoggerFactory.getLogger(MyPointRestService.class);

	@Autowired
	private MyPointServiceI myPointServiceI;

	/**
	 * 通过家属邀请的所有id查询到返利的列表和积分的总和
	 * 
	 * @return
	 */
	@POST
	@Path(value = "/getFamilyRebateInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject getFamilyRebateInfo(JSONObject data) {
		JSONObject result = new JSONObject();
		try {
			result = myPointServiceI.getFamilyRebateInfo(data);
		} catch (Exception e) {
			result.put("code", "1");
			result.put("info", "服务器异常");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询可用积分和待返积分
	 */
	@POST
	@Path(value = "/queryUserIntegral")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String queryUserIntegral(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String businessId = json.getString("businessId");
			String username = json.getString("username");
			String password = json.getString("password");
			String userId = json.getString("userId");
			messageDataBean = myPointServiceI.queryUserIntegral(businessId, username, password, userId);
			logger.info("查询可用积分和待返积分返回数据"+messageDataBean.toJsonString());
		} catch (Exception e) {
			logger.error("查询可用积分和待返积分出错", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	/**
	 * 获得某个用户的所有可用积分列表信息
	 */
	@POST
	@Path(value = "/getAvailablePoints")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAvailablePoints(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String income = json.getString("income");// 收入支出标识 收入1 支出2
			String userId = json.getString("userId");
			Integer currentPage = json.getInteger("currentPage");
			Integer pageSize = json.getInteger("pageSize");
			messageDataBean = myPointServiceI.getAvailablePoints(income, userId, currentPage, pageSize);
			logger.info("查询所有可用积分列表信息返回数据"+messageDataBean.toJsonString());
		} catch (Exception e) {
			logger.error("获得某个用户的所有可用积分列表信息出错", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	/**
	 * 获得某个用户的所有待返积分列表信息
	 */
	@POST
	@Path(value = "/getReturnPoints")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getReturnPoints(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String income = json.getString("income");// 收入支出标识 收入1 支出2
			String userId = json.getString("userId");
			Integer currentPage = json.getInteger("currentPage");
			Integer pageSize = json.getInteger("pageSize");
			messageDataBean = myPointServiceI.getReturnPoints(income, userId, currentPage, pageSize);
			logger.info("查询所有待返积分列表信息返回数据"+messageDataBean.toJsonString());
		} catch (Exception e) {
			logger.error("获得某个用户的所有待返积分列表信息出错", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	/**
	 * 获得某个用户的所有可用积分详细信息
	 */
	@POST
	@Path(value = "/getAvailablePointDetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAvailablePointDetail(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String availablePointsId = json.getString("availablePointsId");// 可用积分表id
			String userId = json.getString("userId");
			messageDataBean = myPointServiceI.getAvailablePointDetail(availablePointsId, userId);
			logger.info("查询可用积分详情信息返回数据"+messageDataBean.toJsonString());
		} catch (Exception e) {
			logger.error("获得某个用户的可用积分详情信息出错", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	/**
	 * 获得某个用户的所有待返积分详细信息
	 */
	@POST
	@Path(value = "/getReturnPointDetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getReturnPointDetail(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String returnPointsId = json.getString("returnPointsId");// 可用积分表id
			String userId = json.getString("userId");
			messageDataBean = myPointServiceI.getReturnPointDetail(returnPointsId, userId);
			logger.info("查询待返积分详情信息返回数据"+messageDataBean.toJsonString());
		} catch (Exception e) {
			logger.error("获得某个用户的待返积分详情信息出错", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/integralRechargeList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String integralRechargeList(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
            logger.info(JSON.toJSONString(json));
//			JSONObject jsonObject  = json.getParams();
			Long userId = json.getLong("userId");
			Integer currentPage = json.getInteger("currentPage");
			Integer pageSize = json.getInteger("pageSize");
			messageDataBean = myPointServiceI.getIntegralRechargeListData(userId,currentPage,pageSize);
			logger.info(messageDataBean.toJsonString());
			logger.info("获取充值记录查询时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(ConstantsV2.SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(ConstantsV2.SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/integralRechargeDo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String integralRechargeDo(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
            logger.info(JSON.toJSONString(json));
//			JSONObject jsonObject  = json.getParams();
			Long userId = json.getLong("userId");
			String cardPassword = json.getString("cardPassword");
			messageDataBean = myPointServiceI.doIntegralRecharge(userId,cardPassword);
			logger.info(messageDataBean.toJsonString());
			logger.info("积分卡充值操作时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(ConstantsV2.SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(ConstantsV2.SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}
}
