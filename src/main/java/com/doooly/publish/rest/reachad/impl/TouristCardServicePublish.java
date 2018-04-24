package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.touristCard.datacontract.base.BaseResponse;
import com.doooly.business.touristCard.datacontract.base.ResponseStatus;
import com.doooly.business.touristCard.datacontract.entity.SctcdAccount;
import com.doooly.business.touristCard.datacontract.entity.SctcdRechargeDetail;
import com.doooly.business.touristCard.datacontract.request.*;
import com.doooly.business.touristCard.datacontract.response.*;
import com.doooly.business.touristCard.service.TouristCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by 王晨宇 on 2018/1/16.
 * 都市旅游卡接口
 */
@Component
@Path("/touristCardService")
public class TouristCardServicePublish {
	private static Logger logger = LoggerFactory.getLogger(TouristCardServicePublish.class);

	@Autowired
	private TouristCardService touristCardService;

	/**
	 * 查询用户旅游卡充值记录
	 */
	@POST
	@Path(value = "/findRechargeHistory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findRechargeHistory(JSONObject json) {
		logger.info("findRechargeHistory() json = {}", json);
		FindRechargeHistoryRequest request = new FindRechargeHistoryRequest(json);
		FindRechargeHistoryResponse<SctcdRechargeDetail> response = new FindRechargeHistoryResponse<SctcdRechargeDetail>();
		List<SctcdRechargeDetail> rechargeHistoryList = null;
		try {
			rechargeHistoryList = touristCardService.findRechargeHistory(request);
			response.setStatus(ResponseStatus.SUCCESS);
			response.setRechargeHistoryList(rechargeHistoryList);
		} catch (Exception e) {
			logger.error("findRechargeHistory() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 统计用户旅游卡充值次数
	 */
	@POST
	@Path(value = "/countRechargeNum")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response countRechargeNum(JSONObject json) {
		logger.info("countRechargeNum() json = {}", json);
		CountRechargeNumRequest request = new CountRechargeNumRequest(json);
		CountRechargeNumResponse response = new CountRechargeNumResponse();
		try {
			int num = touristCardService.countRechargeNum(request);
			response.setStatus(ResponseStatus.SUCCESS);
			response.setTotalNum(num);
		} catch (Exception e) {
			logger.error("countRechargeNum() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 查询用户旅游卡绑定信息记录，一个用户可能绑定多张旅游卡
	 */
	@POST
	@Path(value = "/findSctcdAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findSctcdAccount(JSONObject json) {
		logger.info("findSctcdAccount() json = {}", json);
		FindSctcdAccountRequest request = new FindSctcdAccountRequest(json);
		FindSctcdAccountResponse response = new FindSctcdAccountResponse();
		try {
			List<SctcdAccount> sctcdAccountList = touristCardService.findSctcdAccount(request);
			response.setSctcdAccountList(sctcdAccountList);
			response.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("findSctcdAccount() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 查询用户旅游卡绑定信息记录，用户之前可能逻辑删除此卡
	 */
	@POST
	@Path(value = "/findOneSctcdAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findOneSctcdAccount(JSONObject json) {
		logger.info("findOneSctcdAccount() json = {}", json);
		FindSctcdAccountRequest request = new FindSctcdAccountRequest(json);
		FindSctcdAccountResponse response = new FindSctcdAccountResponse();
		try {
			List<SctcdAccount> sctcdAccountList = touristCardService.findOneSctcdAccount(request);
			response.setSctcdAccountList(sctcdAccountList);
			response.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("findOneSctcdAccount() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 用户逻辑删除，选中的旅游卡绑定信息
	 */
	@POST
	@Path(value = "/abandonedSctcdAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response abandonedSctcdAccount(JSONObject json){
		logger.info("abandonedSctcdAccount() json = {}", json);
		BaseResponse response = new BaseResponse();
		try {
			int id = (int) json.get("id");
			touristCardService.abandonedSctcdAccount(id);
			response.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("abandonedSctcdAccount() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 启用之前用户逻辑删除的旅游卡绑定信息
	 */
	@POST
	@Path(value = "/enableSctcdAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response enableSctcdAccount(JSONObject json){
		logger.info("enableSctcdAccount() json = {}", json);
		BaseResponse response = new BaseResponse();
		try {
			int id = (int) json.get("id");
			touristCardService.enableSctcdAccount(id);
			response.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			logger.error("enableSctcdAccount() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 验证开户
	 */
	@POST
	@Path(value = "/verifyAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response verifyAccount(JSONObject json) {
		logger.info("verifyAccount() json = {}", json);
		VerifyAccountRequest request = new VerifyAccountRequest(json);
		VerifyAccountResponse response = new VerifyAccountResponse();
		try {
			if ( !request.paramsNotEmpty() ) {
				return Response.ok(response.setStatus(ResponseStatus.BADPARAM)).build();
			}
			response = touristCardService.verifyAccount(request);
		} catch (Exception e) {
			logger.error("verifyAccount() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 新开户
	 */
	@POST
	@Path(value = "/newAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newAccount(JSONObject json) {
		logger.info("newAccount() json = {}", json);
		NewAccountRequest request = new NewAccountRequest(json);
		NewAccountResponse response = new NewAccountResponse();
		try {
			if ( !request.paramsNotEmpty() ) {
				return Response.ok(response.setStatus(ResponseStatus.BADPARAM)).build();
			}
			response = touristCardService.newAccount(request);
		} catch (Exception e) {
			logger.error("newAccount() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 账户余额查询
	 */
	@POST
	@Path(value = "/queryAccountBalance")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryAccountBalance(JSONObject json){
		logger.info("queryAccountBalance() json = {}", json);
		QueryAccountBalanceRequest request = new QueryAccountBalanceRequest(json);
		QueryAccountBalanceResponse response = new QueryAccountBalanceResponse();
		try {
			if ( !request.paramsNotEmpty() ) {
				return Response.ok(response.setStatus(ResponseStatus.BADPARAM)).build();
			}
			response = touristCardService.queryAccountBalance(request);
		} catch (Exception e) {
			logger.error("queryAccountBalance() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}

	/**
	 * 卡片余额查询
	 */
	@POST
	@Path(value = "/queryCardBalance")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryCardBalance(JSONObject json){
		logger.info("queryCardBalance() json = {}", json);
		QueryCardBalanceRequest request = new QueryCardBalanceRequest(json);
		QueryCardBalanceResponse response = new QueryCardBalanceResponse();
		try {
			if ( !request.paramsNotEmpty() ) {
				return Response.ok(response.setStatus(ResponseStatus.BADPARAM)).build();
			}
			response = touristCardService.queryCardBalance(request);
		} catch (Exception e) {
			logger.error("queryCardBalance() Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return Response.ok(response).build();
	}
}
