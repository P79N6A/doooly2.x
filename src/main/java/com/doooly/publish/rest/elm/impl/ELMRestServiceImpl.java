package com.doooly.publish.rest.elm.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.common.IPUtils;
import com.doooly.common.elm.ELMConstants;
import com.doooly.common.elm.ElmSignUtils;
import com.doooly.common.util.RandomUtil;
import com.doooly.publish.rest.elm.ELMRestServiceI;
import com.reach.redis.utils.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

/**
 * @Description: 饿了么
 * @author: qing.zhang
 * @date: 2019-01-03
 */
@Component
@Path("/doooly/elm")
public class ELMRestServiceImpl implements ELMRestServiceI {

    private static final Logger logger = LoggerFactory.getLogger(ELMRestServiceImpl.class);

    @Autowired
    private ELMServiceI elmServiceI;


    @POST
    @Path(value = "/orderAmountPush")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String orderAmountPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = elmServiceI.orderAmountPush(obj, httpServletRequest);
        return resultModel.toELMString();
    }

    @POST
    @Path(value = "/orderStatusPush")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String orderStatusPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = elmServiceI.orderStatusPush(obj, httpServletRequest);
        return resultModel.toELMString();
    }

    @GET
    @Path(value = "/createOrder")
    @Produces("application/json;charset=UTF-8")
    @Consumes("application/json;charset=UTF-8")
    @Override
    public String createElmOrderAndPay(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        JSONObject jsonObject = getJsonObjectFromRequest(request);
        logger.info("饿了么调用落单接口 createElmOrderAndPay：{}", GsonUtils.toString(jsonObject));
        boolean checkResult = checkParam(jsonObject);
        boolean signResult = checkSign(jsonObject);
        if (checkResult || !signResult) {
            JSONObject res = new JSONObject();
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnMsg", "请求参数为空");
            if (!signResult) {
                res.put("returnMsg", ELMConstants.CHECK_PARAM_SIGN_ERROR);
            }
            res.put("transactionId", "");
            res.put("payAmount", "");
            res.put("outTradeNo", "");
            res.put("payStatus", "");
            res.put("nonceStr", RandomUtil.getRandomStr(32));
            try {
                String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
                res.put("sign", signStr); //采用RSA2签名
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res.toJSONString();
        }
        jsonObject.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel resultModel = elmServiceI.createElmOrderAndPay(jsonObject);
        return resultModel.getData().toString();
    }

    @POST
    @Path(value = "/getPayInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String queryElmPayInfo(JSONObject obj, @Context HttpServletRequest request) {
        logger.info("饿了么调用支付查询接口 queryElmPayInfo：{}", GsonUtils.toString(obj));
        boolean flag = checkPayInfoParam(obj);
        boolean signResult = checkSign(obj);
        if (flag || !signResult) {
            JSONObject res = new JSONObject();
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnsg", "请求参数错误");
            if (!signResult) {
                res.put("returnMsg", ELMConstants.CHECK_PARAM_SIGN_ERROR);
            }
            res.put("appId", ELMConstants.ELM_APP_ID);
            res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
            res.put("erchantNo", "");              //三方分配的商户号
            res.put("transactionId", "");          //支付网关的订单号
            res.put("outTradeNo", "");             //第三方交易号，支付成功后必传。
            res.put("paAount", "");                //支付金额，单位：分
            res.put("paStatus", "");               //支付状态
            res.put("thirdUserId", "");            //S三方UserID，风控使用，支付成功后必传。
            res.put("thirdPaAccount", "");          //S三方收款账户，风控使用，支付成功后必传。
            res.put("nonceStr", RandomUtil.getRandomStr(32)); //随机串（长度32）
            String signStr = null;
            try {
                signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            res.put("sign", signStr);
            return res.toJSONString();
        }
        obj.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel resultModel = elmServiceI.queryElmPayInfo(obj, request);
        return resultModel.getData().toString();
    }


    @POST
    @Path(value = "/refund")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String elmRefund(JSONObject obj, @Context HttpServletRequest request) {
        logger.info("饿了么调用退款接口 createElmOrderAndPay：{}", GsonUtils.toString(obj));
        Integer payAmount = obj.getInteger("payAmount");
        Integer refundAmount = obj.getInteger("refundAmount");
        obj.put("payAmount", String.valueOf(payAmount));
        obj.put("refundAmount", String.valueOf(refundAmount));
        boolean signResult = checkSign(obj);
        if (!signResult) {
            try {
                JSONObject res = new JSONObject();
                res.put("appId", ELMConstants.ELM_APP_ID);
                res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
                res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
                res.put("returnMsg", ELMConstants.CHECK_PARAM_SIGN_ERROR);
                res.put("outTradeNo", "");
                res.put("outRefundNo", "");
                res.put("refundStatus", "");
                res.put("transactionId", "");
                res.put("refundNo", "");
                res.put("payAmount", "");
                res.put("refundAmount", "");
                res.put("nonceStr", RandomUtil.getRandomStr(32));
                String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
                res.put("sign", signStr);
                return res.toJSONString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        obj.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel resultModel = elmServiceI.elmRefund(obj);
        return resultModel.getData().toString();
    }


    @POST
    @Path(value = "/getRefundInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String queryElmRefundInfo(JSONObject obj, HttpServletRequest httpServletRequest) {
        logger.info("饿了么调用退款查询接口 queryElmRefundInfo：{}", GsonUtils.toString(obj));
        boolean signResult = checkSign(obj);
        if (!signResult) {
            try {
                JSONObject res = new JSONObject();
                res.put("appId", ELMConstants.ELM_APP_ID);
                res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
                res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
                res.put("returnMsg", ELMConstants.CHECK_PARAM_SIGN_ERROR);
                res.put("outTradeNo", "");
                res.put("outRefundNo", "");
                res.put("transactionId", "");
                res.put("refundNo", "");
                res.put("payAmount", "");
                res.put("refundAmount", "");
                res.put("refundStatus", "");
                res.put("nonceStr", RandomUtil.getRandomStr(32));
                String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
                res.put("sign", signStr);
                return res.toJSONString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ResultModel resultModel = elmServiceI.queryElmRefundInfo(obj);
        return resultModel.getData().toString();
    }

    private boolean checkSign(JSONObject obj) {
        boolean flag = false;
        try {
            String sign = obj.getString("sign");
            obj.remove("sign");
            flag = ElmSignUtils.rsaCheck(ElmSignUtils.ELM_GIAVE_PUBLIC_KEY, obj, sign);
            if (!flag) {
                logger.info("验证签名失败，参数：{}，饿了么签名：{}", GsonUtils.toString(obj), sign);
                String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, obj);
                logger.info("----------------生成可用的签名：" + signStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean checkPayInfoParam(JSONObject obj) {
        if (StringUtils.isBlank(obj.getString("appId")) || StringUtils.isBlank(obj.getString("merchantNo"))
                || StringUtils.isBlank(obj.getString("transactionId")) || StringUtils.isBlank(obj.getString("nonceStr"))
                || StringUtils.isBlank(obj.getString("sign"))) {
            return true;
        }
        return false;
    }

    private boolean checkParam(JSONObject req) {
        if (StringUtils.isBlank(req.getString("appId")) || StringUtils.isBlank(req.getString("merchantNo"))
                || StringUtils.isBlank(req.getString("subject")) || StringUtils.isBlank(req.getString("body"))
                || StringUtils.isBlank(req.getString("transactionId")) || StringUtils.isBlank(req.getString("payAmount"))
                || StringUtils.isBlank(req.getString("timestamp")) || StringUtils.isBlank(req.getString("timeExpire"))
                || StringUtils.isBlank(req.getString("redirectUrl")) || StringUtils.isBlank(req.getString("notifyUrl"))
                || StringUtils.isBlank(req.getString("nonceStr"))
                || StringUtils.isBlank(req.getString("sign"))) {
            return true;
        }
        return false;
    }


    public static JSONObject getJsonObjectFromRequest(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String sign = request.getHeader("sign");
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = request.getParameter(key);
            try {
                value = new String(value.getBytes("iso-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            jsonObject.put(key, value);
        }
        jsonObject.put("sign", sign);
        return jsonObject;
    }

}
