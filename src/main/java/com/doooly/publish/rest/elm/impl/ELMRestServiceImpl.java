package com.doooly.publish.rest.elm.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.common.IPUtils;
import com.doooly.common.elm.ELMConstants;
import com.doooly.common.elm.ElmSignUtils;
import com.doooly.common.util.RandomUtil;
import com.doooly.publish.rest.elm.ELMRestServiceI;
import com.reach.redis.utils.GsonUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String orderAmountPush(String s,@Context HttpServletRequest httpServletRequest) {
        String str = null;
        try {
            byte[] bytes = Hex.decodeHex(s.toCharArray());
            str = new String(bytes);
        } catch (DecoderException e) {
            logger.info("饿了么调用订单金额推送接口参数解密异常,异常原因",e);
        }
        JSONObject obj = JSONObject.parseObject(str);
        logger.info("饿了么调用订单金额推送接口, 参数s：{},转成json:{}",s,str);
        ResultModel resultModel = elmServiceI.orderAmountPush(s,obj, httpServletRequest);
        return resultModel.toELMString();
    }

    @POST
    @Path(value = "/orderStatusPush")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String orderStatusPush(String s,@Context  HttpServletRequest httpServletRequest) {
        String str = null;
        try {
            byte[] bytes = Hex.decodeHex(s.toCharArray());
            str = new String(bytes);
        } catch (DecoderException e) {
            logger.info("饿了么调用订单金额推送接口参数解密异常,异常原因",e);
        }
        JSONObject obj = JSONObject.parseObject(str);
        logger.info("饿了么调用订单金额推送接口, 参数s：{},转成json:{}",s,str);
        ResultModel resultModel = elmServiceI.orderStatusPush(s,obj, httpServletRequest);
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
        boolean validateParaRes = validateCrtOrderParam(jsonObject);
        if (validateParaRes) {
            JSONObject res = createOrderResult(GlobalResultStatusEnum.PARAM_VALID_ERROR.getInfo());
            return res.toJSONString();
        }
        boolean signResult = validateSign(jsonObject);
        if (!signResult) {
            JSONObject res = createOrderResult(ELMConstants.ELM_CHECK_PARAM_SIGN_ERROR);
            return res.toJSONString();
        }
        jsonObject.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel resultModel = elmServiceI.createElmOrderAndPay(jsonObject);
        if (null == resultModel.getData()) {
            JSONObject res = createOrderResult(ELMConstants.ELE_CREATE_ORDER_FAIL);
            return res.toJSONString();
        }
        return resultModel.getData().toString();
    }

    @POST
    @Path(value = "/getPayInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String queryElmPayInfo(JSONObject req, @Context HttpServletRequest request) {
        logger.info("饿了么调用支付查询接口 queryElmPayInfo：{}", GsonUtils.toString(req));
        boolean validateParamRes = validatePayInfoParam(req);
        if (validateParamRes) {
            JSONObject res = crtPayInfoResult(GlobalResultStatusEnum.PARAM_VALID_ERROR.getInfo());
            return res.toJSONString();
        }
        boolean signResult = validateSign(req);
        if (!signResult) {
            JSONObject res = crtPayInfoResult(ELMConstants.ELM_CHECK_PARAM_SIGN_ERROR);
            return res.toJSONString();
        }
        req.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel resultModel = elmServiceI.queryElmPayInfo(req, request);
        if (null == resultModel.getData()) {
            JSONObject res = crtPayInfoResult(ELMConstants.ELE_QUERY_PAY_FAIL);
            return res.toJSONString();
        }
        return resultModel.getData().toString();
    }

    @POST
    @Path(value = "/refund")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String elmRefund(JSONObject req, @Context HttpServletRequest request) {
        logger.info("饿了么调用退款接口 createElmOrderAndPay：{}", GsonUtils.toString(req));
        boolean validateParamRes = validateRefundParam(req);
        if (validateParamRes) {
            JSONObject res = crtRefundResult(GlobalResultStatusEnum.PARAM_VALID_ERROR.getInfo());
            return res.toJSONString();
        }
        boolean validatePayAmtRes = validatePayAmount(req);
        if (validatePayAmtRes) {
            JSONObject res = crtPayInfoResult(ELMConstants.ELE_REFUND_AMOUNT_ERROR);
            return res.toJSONString();
        }
        req.put("payAmount", req.getString("payAmount"));
        req.put("refundAmount", req.getString("refundAmount"));
        boolean signResult = validateSign(req);
        if (!signResult) {
            JSONObject res = crtRefundResult(ELMConstants.ELM_CHECK_PARAM_SIGN_ERROR);
            return res.toJSONString();
        }
        req.put("clientIp", IPUtils.getIpAddr(request));
        ResultModel resultModel = elmServiceI.elmRefund(req);
        if (null == resultModel) {
            JSONObject res = crtRefundResult(ELMConstants.ELE_REFUND_OPERATION_FAIL);
            return res.toJSONString();
        }
        return resultModel.getData().toString();
    }

    @POST
    @Path(value = "/getRefundInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String queryElmRefundInfo(JSONObject req, HttpServletRequest httpServletRequest) {
        logger.info("饿了么调用退款查询接口 queryElmRefundInfo：{}", GsonUtils.toString(req));
        boolean validateParamRes = validateRefundQueryParam(req);
        if (validateParamRes) {
            JSONObject res = crtRefundQueryResult(GlobalResultStatusEnum.PARAM_VALID_ERROR.getInfo());
            return res.toJSONString();
        }
        boolean signResult = validateSign(req);
        if (!signResult) {
            JSONObject res = crtRefundQueryResult(ELMConstants.ELM_CHECK_PARAM_SIGN_ERROR);
            return res.toJSONString();
        }
        ResultModel resultModel = elmServiceI.queryElmRefundInfo(req);
        if (null == resultModel.getData()) {
            JSONObject res = crtRefundQueryResult(GlobalResultStatusEnum.PARAM_VALID_ERROR.getInfo());
            return res.toJSONString();
        }
        return resultModel.getData().toString();
    }

    /**
     * 支付查询入参校验
     *
     * @param req
     * @return
     */
    private boolean validatePayInfoParam(JSONObject req) {
        if (StringUtils.isBlank(req.getString("appId")) || StringUtils.isBlank(req.getString("merchantNo"))
                || StringUtils.isBlank(req.getString("transactionId"))
                || StringUtils.isBlank(req.getString("nonceStr"))
                || StringUtils.isBlank(req.getString("sign"))) {
            return true;
        }
        return false;
    }

    /**
     * 从reqest 遍历入参, sign存header里面, 拼到url后面会失真
     *
     * @param request
     * @return
     */
    public static JSONObject getJsonObjectFromRequest(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        // String sign = request.getHeader("sign");
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
        // jsonObject.put("sign", sign);
        return jsonObject;
    }

    /**
     * 落单并支付入参校验
     *
     * @param req
     * @return
     */
    private boolean validateCrtOrderParam(JSONObject req) {
        if (StringUtils.isBlank(req.getString("appId")) || StringUtils.isBlank(req.getString("merchantNo"))
                || StringUtils.isBlank(req.getString("subject"))
                || StringUtils.isBlank(req.getString("body"))
                || StringUtils.isBlank(req.getString("transactionId"))
                || StringUtils.isBlank(req.getString("payAmount"))
                || StringUtils.isBlank(req.getString("timestamp"))
                || StringUtils.isBlank(req.getString("timeExpire"))
                || StringUtils.isBlank(req.getString("redirectUrl"))
                || StringUtils.isBlank(req.getString("notifyUrl"))
                || StringUtils.isBlank(req.getString("nonceStr"))
                || StringUtils.isBlank(req.getString("sign"))) {
            return true;
        }
        return false;
    }

    /**
     * 入参验签
     *
     * @param req
     * @return
     */
    private boolean validateSign(JSONObject req) {
        boolean flag = false;
        try {
            String sign = req.getString("sign");
            req.remove("sign");
            req.remove("ele_order_id");
            req.remove("uid");
            flag = ElmSignUtils.rsaCheck(ElmSignUtils.ELM_GIAVE_PUBLIC_KEY, req, sign); // PRD OPEN
            //flag = ElmSignUtils.rsaCheck(ElmSignUtils.ELM_PUBLIC_KEY, req, sign);     //LOCAL DEV OPEN, PRD DELETE
            if (!flag) {
                logger.info("验证签名失败，参数：{}，饿了么签名：{}", GsonUtils.toString(req), sign);
                //String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, req);   //LOCAL DEV OPEN, PRD DELETE
                //logger.info("----------------生成可用的签名：" + signStr);                  //LOCAL DEV OPEN, PRD DELETE
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 落单并支付返回参数封装
     *
     * @param returnMsg
     * @return
     */
    private JSONObject createOrderResult(String returnMsg) {
        JSONObject res = new JSONObject();
        try {
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnMsg", returnMsg);
            res.put("transactionId", "");
            res.put("payAmount", "");
            res.put("outTradeNo", "");
            res.put("payStatus", "");
            res.put("nonceStr", RandomUtil.getRandomStr(32));
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res); //采用RSA2签名
            res.put("sign", signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 支付查询返回参数
     *
     * @param returnsg
     * @return
     */
    private JSONObject crtPayInfoResult(String returnsg) {
        JSONObject res = new JSONObject();
        try {
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnsg", returnsg);
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
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 退款入参校验
     *
     * @param req
     * @return
     */
    private boolean validateRefundParam(JSONObject req) {
        if (StringUtils.isBlank(req.getString("appId")) || StringUtils.isBlank(req.getString("merchantNo"))
                || StringUtils.isBlank(req.getString("transactionId"))
                || StringUtils.isBlank(req.getString("refundNo"))
                || null == req.get("payAmount") || null == req.get("refundAmount")
                || StringUtils.isBlank(req.getString("nonceStr"))
                || StringUtils.isBlank(req.getString("notifyUrl"))
                || StringUtils.isBlank(req.getString("sign"))) {
            return true;
        }
        return false;
    }

    /**
     * 退款金额校验,保留小数点后两位且金额不能为0
     *
     * @param req
     * @return
     */
    private boolean validatePayAmount(JSONObject req) {
        String payAmount = req.getString("payAmount");
        String refundAmount = req.getString("refundAmount");
        if (!isNumber(payAmount) || !isNumber(refundAmount)) {
            return true;
        }
        BigDecimal zero = new BigDecimal("0");
        BigDecimal payAmt = new BigDecimal(payAmount);
        BigDecimal refundAmt = new BigDecimal(refundAmount);
        if (payAmt.compareTo(zero) == 0) {
            return true;
        }
        if (refundAmt.compareTo(zero) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断小数点后2位的数字的正则表达式
     *
     * @param amount
     * @return
     */
    private boolean isNumber(String amount) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match = pattern.matcher(amount);
        return match.matches();
    }

    /**
     * 退款返回参数
     *
     * @param returnMsg
     * @return
     */
    private JSONObject crtRefundResult(String returnMsg) {
        JSONObject res = new JSONObject();
        try {
            res.put("appId", ELMConstants.ELM_APP_ID);
            res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnMsg", returnMsg);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 退款查询入参校验
     *
     * @param req
     * @return
     */
    private boolean validateRefundQueryParam(JSONObject req) {
        if (StringUtils.isBlank(req.getString("appId")) || StringUtils.isBlank(req.getString("merchantNo"))
                || StringUtils.isBlank(req.getString("transactionId"))
                || StringUtils.isBlank(req.getString("refundNo"))
                || StringUtils.isBlank(req.getString("nonceStr"))
                || StringUtils.isBlank(req.getString("sign"))) {
            return true;
        }
        return false;
    }

    /**
     * 退款查询返回参数
     * @param returnMsg
     * @return
     */
    private JSONObject crtRefundQueryResult(String returnMsg) {
        JSONObject res = new JSONObject();
        try {
            res.put("appId", ELMConstants.ELM_APP_ID);
            res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnMsg", returnMsg);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}