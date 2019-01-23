package com.doooly.publish.rest.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.common.IPUtils;
import com.doooly.common.constants.VersionConstants;
import com.doooly.publish.rest.payment.PaymentRestServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-08-09
 */
@Component
@Path("/doooly/payment")
public class PaymentRestService implements PaymentRestServiceI{

    private static final Logger logger = LoggerFactory.getLogger(PaymentRestService.class);

    @Autowired
    private NewPaymentServiceI paymentService;

    @Override
    @POST
    @Path(value = "/authorize")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String authorize(JSONObject obj) {
        String businessId = obj.getString("businessId");
        ResultModel authorize = paymentService.authorize(businessId);
        return authorize.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/getPayForm")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPayForm(JSONObject json) {
        logger.info("payment getPayForm() json = {}", json);
        ResultModel payForm = paymentService.getPayForm(json);
        return payForm.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/getPayForm/"+ VersionConstants.INTERFACE_VERSION_V2)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPayFormV2(JSONObject json) {
        logger.info("payment getPayFormV2() json = {}", json);
        ResultModel payForm = paymentService.getPayFormV2(json);
        return payForm.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/unifiedorder")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String unifiedorder(JSONObject json,@Context HttpServletRequest request) {
        json.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel unifiedorder = paymentService.unifiedorder(json);
        return unifiedorder.toJsonString();
    }
    @Override
    @POST
    @Path(value = "/unifiedorder/"+ VersionConstants.INTERFACE_VERSION_V2)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String unifiedorderV2(JSONObject json,@Context HttpServletRequest request) {
        json.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel unifiedorder = paymentService.unifiedorderV2(json);
        return unifiedorder.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/getTradeInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getTradeInfo(JSONObject json, HttpServletRequest httpServletRequest) {
        logger.info("payment getPayForm() json = {}", json);
        ResultModel tradeInfo = paymentService.getTradeInfo(json);
        return tradeInfo.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/integralPay")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String integralPay(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.integralPay(json);
        return resultModel.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/integralPay/"+ VersionConstants.INTERFACE_VERSION_V2)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String integralPayV2(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.integralPayV2(json);
        return resultModel.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/dooolyPayCallback")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String dooolyPayCallback(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.dooolyPayCallback(json);
        return resultModel.toJsonString();
    }
    
    @Override
    @POST
    @Path(value = "/dooolyPayCallback/"+ VersionConstants.INTERFACE_VERSION_V2)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String dooolyPayCallbackV2(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.dooolyPayCallbackV2(json);
        return resultModel.toJsonString();
    }


    @Override
    @POST
    @Path(value = "/getPayResult/"+ VersionConstants.INTERFACE_VERSION_V2)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPayResultV2(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.getPayResultV2(json);
        return resultModel.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/getPayResult")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getPayResult(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.getPayResult(json);
        return resultModel.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/dooolyRefundCallback")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String dooolyRefundCallback(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.dooolyRefundCallback(json);
        return resultModel.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/refund")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String refund(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.refund(json);
        return resultModel.toJsonString();
    }

    @Override
    @POST
    @Path(value = "/applyRefund")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String applyRefund(JSONObject json, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = paymentService.applyRefund(json);
        return resultModel.toJsonString();
    }
}
