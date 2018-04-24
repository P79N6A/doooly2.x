package com.doooly.publish.rest.reachad.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSONArray;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.util.HTTPSClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.pay.bean.RetWxBean;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.pay.service.PaymentService;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.pay.service.impl.PaymentServiceFactory;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.PayMsg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 支付中心
 * 
 * @author 2017-10-09 16:25:03 WANG
 *
 */
@Component
@Path("/payService")
public class PayServicePublish {
	protected Logger logger = LoggerFactory.getLogger(PayServicePublish.class);

	private static final String RET_CODE_SUCCESS = "SUCCESS";
	private static final String RET_CODE_FAIL = "FAIL";
	
	@Autowired
	private RefundService refundService;

	/**
	 * 订单退款
	 * 
	 * @param json
	 * @return 退款
	 */
	@POST
	@Path(value = "/orderRefund")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String orderRefund(JSONObject json) {
		logger.info("orderRefund() json = {}", json);
		long userId = json.getLong("userId");
		String orderNum = json.getString("orderNum");
		return refundService.refund(userId, orderNum).toJsonString();
	}
	
	/**
	 * 预支付
	 * 
	 * @param json
	 * @return 微信支付参数
	 */
	@POST
	@Path(value = "/getPayForm")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getPayForm(JSONObject json) {
		logger.info("getPayForm() json = {}", json);
		String payType = json.getString("payType");
		PaymentService paymentService = PaymentServiceFactory.getPayService(payType);
		if (paymentService != null) {
			PayMsg payMsg = paymentService.prePay(json);
			logger.info("payMsg = {}", payMsg);
			return payMsg.toJsonString();
		}
		return new PayMsg(PayMsg.success_code, "invalied payType=" + payType).toJsonString();
	}
	
	/***
	 * 积分回调
	 * @param request
	 * @param response
	 * @return
	 */
	@POST
	@Path(value = "/dooolyCallback")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String dooolyCallback(JSONObject json) {
		return payCallback(PayFlowService.PAYTYPE_DOOOLY,json.toJSONString()).toJsonString();
	}
	

	/***
	 * 微信JSAPI回调
	 * @param request
	 * @param response
	 * @return
	 */
	@POST
	@Path(value = "/wxJsapiCallback")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String wxJsapiCallback(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		logger.info("wxJsapiCallback() start...");
		String resultXml = getResultXml(request);
		PayMsg msg = payCallback(PayFlowService.PAYTYPE_WEIXIN_JSAPI,resultXml);
		logger.info("wxJsapiCallback() msg = {}",msg);
		if(MessageDataBean.success_code.equals(msg.getCode())){
			return new RetWxBean(RET_CODE_SUCCESS, RET_CODE_SUCCESS).toXmlString();
		}else{
			return new RetWxBean(RET_CODE_FAIL, RET_CODE_FAIL).toXmlString();
		}
	}
	
	/***
	 * 微信APP回调
	 * @param request
	 * @param response
	 * @return
	 */
	@POST
	@Path(value = "/wxAppCallback")
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String wxAppCallback(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		logger.info("wxAppCallback() start...");
		String resultXml = getResultXml(request);
		PayMsg msg = payCallback(PayFlowService.PAYTYPE_WEIXIN,resultXml);
		logger.info("wxAppCallback() msg = {}",msg);
		if(MessageDataBean.success_code.equals(msg.getCode())){
			String retXml =  new RetWxBean(RET_CODE_SUCCESS, RET_CODE_SUCCESS).toXmlString();
			logger.info("retXml = {}", retXml);
			return retXml;
		}else{
			return new RetWxBean(RET_CODE_FAIL, RET_CODE_FAIL).toXmlString();
		}
	}
	
	@POST
	@Path(value = "/getPayResult")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getPayResult(JSONObject json) {
		String payType = json.getString("payType");
		logger.info("getPayResult() start...payType = {}",payType);
		try {
			String orderNum = json.getString("orderNum");
			logger.info("payType = {},orderNum={}", payType, orderNum);
			PaymentService paymentService = PaymentServiceFactory.getPayService(payType);
			if (paymentService != null) {
				PayMsg payMsg = paymentService.getPayResult(orderNum);
				logger.info("payMsg = {}", payMsg);
				return payMsg.toJsonString();
			}
		} catch (Exception e) {
			logger.info("getPayResult,e = {}", e);
		}
		return new PayMsg(PayMsg.success_code, "invalied payType=" + payType).toJsonString();
	}
	
	/***
	 * 处理回调结果
	 * @param payType
	 * @param retStr
	 * @return
	 */
	private PayMsg payCallback(String payType,String retStr){
		PaymentService paymentService = PaymentServiceFactory.getPayService(payType);
		if (paymentService != null) {
			return paymentService.handlePayResult(retStr);
		}
		return new PayMsg(PayMsg.failure_code, "invalied payType=" + payType);
	}
	
	/**
	 * 解析微信返回xml
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private String getResultXml(HttpServletRequest request) {
		BufferedReader br = null;
		InputStream in = null; 
        try {
			StringBuffer sb = new StringBuffer();  
			in = request.getInputStream();  
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
			String s = null;
			while ((s = br.readLine()) != null){  
			    sb.append(s);  
			} 
			return sb.toString();
		} catch (Exception e) {
			logger.error("getResultXml() e = {}", e);
		}finally{
			try {
				if(br != null){
					br.close();
				}
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}
	
}
