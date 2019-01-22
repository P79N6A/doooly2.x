package com.doooly.business.ele.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.business.AdBusinessServiceI;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.order.service.AdOrderReportServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.constants.PayConstants;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.business.payment.utils.SignUtil;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PaymentConstants;
import com.doooly.common.elm.*;
import com.doooly.common.elm.ElmSignUtils;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.RandomUtil;
import com.doooly.dao.reachad.AdBusinessExpandInfoDao;
import com.doooly.dao.reachad.AdOrderDetailDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.doooly.common.elm.ELMConstants.ELM_ORDER_PREFIX;
import static com.doooly.common.token.TokenUtil.TOKEN_EXPIRE;

/**
 * @Description: 饿了么实现
 * @author: qing.zhang
 * @date: 2019-01-03
 */
@Service
public class ELMServiceImpl implements ELMServiceI{

    private Logger logger = LoggerFactory.getLogger(ELMServiceImpl.class);

    @Autowired
    private ConfigDictServiceI configDictServiceI;
    @Autowired
    private AdBusinessServiceI adBusinessServiceI;
    @Autowired
    private NewPaymentServiceI paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AdOrderReportServiceI adOrderReportServiceI;
    @Autowired
    private AdOrderDetailDao detailDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AdUserDao adUserDao;
    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private NewPaymentServiceI newPaymentServiceI;

    /** 推送信息
     * {
     "bNo": "业务编码",
     "uNo": "用户编码",
     "orderNo": "订单号",
     "orderNo98": "订单号(98开头)",
     "totalFee": "总费用",
     "status": "订单状态"
     }
     */
    @Override
    public ResultModel orderAmountPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        String consumerNo = httpServletRequest.getHeader("consumerNo");
        String orderNo = obj.getString("orderNo");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE, ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        Boolean flag = ElmSignUtils.validParam(consumerNo,timeStamp,sign,obj,eleConfig);
        if(!flag){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }else {
            //验证成功将订单信息放入缓存
            stringRedisTemplate.opsForValue().set(String.format(ELMConstants.ELM_ORDER_PREFIX,orderNo), obj.toJSONString());
            return ResultModel.success_ok("获取订单信息成功");
        }
    }

    /**
     * 饿了么 状态推送
     * @param obj
     * @param httpServletRequest
     * @return
     */
    @Override
    public ResultModel orderStatusPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        String consumerNo = httpServletRequest.getHeader("consumerNo");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE, ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        String elmConsumerSecret = eleConfig.getString("consumerSecret");
        Boolean flag = ElmSignUtils.validParam(consumerNo,timeStamp,sign,obj,eleConfig);
        if(!flag){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }
        String orderNo = obj.getString("orderNo");
        Integer status = obj.getInteger("status");
        String remark = obj.getString("remark");
        //查询doooly订单
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNo);
        OrderVo o = adOrderReportServiceI.getOrderLimt(order);
        if(o==null){
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }
        //修改doooly订单状态
/*        if(OrderTypeEnum.OrderTypeEnum10.getCode()==status){
            //订单取消
            orderService.cancleOrder(o.getUserId(), orderNo);
        }else {
            //更新
        }*/
        OrderItemVo newItem = new OrderItemVo();
        newItem.setOrderReportId(order.getId());
        newItem.setRetCode(String.valueOf(status));
        newItem.setRetMsg(remark);
        newItem.setRetState(OrderTypeEnum.getOrderTypeByCode(status));
        logger.info("update elm order status, item = {}",newItem);
        orderService.updateOrderItem(newItem);
        return ResultModel.success_ok("收到状态");
    }

    @Override
    public ResultModel createElmOrderAndPay(JSONObject json) {
        ResultModel resultModel = new ResultModel();
        String telephone = json.getString("uid");
        // TODO 饿了么下单成功后推送订单信息至兜里，兜里把订单新缓存到redis，根据订单号从redis取bNo.
        try {
            if (StringUtils.isNotBlank(telephone)) { // 目前饿了么没有返回userId
                String phone = json.getString("uid");
                OrderMsg msg = new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess);
                if (StringUtils.isEmpty(phone)) {
                    JSONObject res = getResultData(PayConstants.PAY_STATUS_2, "用户标识为空",
                            PayStatusEnum.PayTypeNotPay.getCode());
                    resultModel.setData(res);
                    return resultModel;
                }
                AdUser adUser = new AdUser();
                adUser.setTelephone(phone);
                adUser = adUserDao.get(adUser);
                if (adUser == null) {
                    JSONObject res = getResultData(PayConstants.PAY_STATUS_2, "用户查询失败",
                            PayStatusEnum.PayTypeNotPay.getCode());
                    resultModel.setData(res);
                    return resultModel;
                }
            }
            if (StringUtils.isBlank(telephone)) {
                telephone = "18017712088";
            }

            BigDecimal payAmount = json.getBigDecimal("payAmount");
            JSONObject param = new JSONObject();
            param.put("businessId", ELMConstants.elm_bussinesss_serial);
            param.put("cardNumber", telephone);
            param.put("storesId", "ELM001");
            param.put("merchantOrderNo", json.getString("transactionId"));
            param.put("price", payAmount);
            param.put("amount", payAmount);
            param.put("tradeType", "DOOOLY_JS");
            param.put("body", json.getString("subject"));
            param.put("isSource", 2);
            param.put("notifyUrl", json.getString("notifyUrl"));
            param.put("orderDate", DateUtils.formatDateTime(new Date()));
            param.put("clientIp", json.get("clientIp"));
            param.put("nonceStr", json.get("nonceStr"));

            /*param.put("isPayPassword", adUser.getIsPayPassword());*/

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonDetail = new JSONObject();
            jsonDetail.put("code", "1001"); //饿了么没有传，目前写个假数据
            jsonDetail.put("goods", json.getString("subject"));
            jsonDetail.put("number", 1);
            jsonDetail.put("amount", payAmount);
            jsonDetail.put("category", "0000");
            jsonDetail.put("price", payAmount);
            jsonDetail.put("tax", 0);

            jsonArray.add(jsonDetail);
            param.put("orderDetail", jsonArray.toJSONString());
            logger.info("饿了么下单参数param=========" + param);

            AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(ELMConstants.ELM_BUSINESS_ID);
            String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY,
                    adBusinessExpandInfo.getClientId()));
            logger.info("饿了么下预付单参数=======accessToken========" + accessToken);
            if (accessToken == null) {
                ResultModel authorize = newPaymentServiceI.authorize(MeituanConstants.meituan_bussinesss_id);
                if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                    Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                    accessToken = (String) data.get("access_token");
                } else {
                    JSONObject res = getResultData(PayConstants.PAY_STATUS_2, "接口授权认证失败",
                            PayStatusEnum.PayTypeNotPay.getCode());
                    resultModel.setData(res);
                    return resultModel;
                }
            }

            long timestamp = System.currentTimeMillis() / 1000;
            SortedMap<Object, Object> parameters = new TreeMap<>();
            parameters.put("client_id", adBusinessExpandInfo.getClientId());
            parameters.put("timestamp", timestamp);
            parameters.put("access_token", accessToken);
            parameters.put("param", param.toJSONString());
            String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());

            JSONObject object = new JSONObject();
            object.put("client_id", adBusinessExpandInfo.getClientId());
            object.put("timestamp", timestamp);
            object.put("access_token", accessToken);
            object.put("param", param.toJSONString());
            object.put("sign", sign);
            String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.UNIFIED_ORDER_URL);
            JSONObject jsonResult = JSONObject.parseObject(result);
            logger.info("饿了么下单返回：{}", result);
            if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
                //下单成功返回信息
                JSONObject res = getResultData(PayConstants.PAY_STATUS_1, "下单成功",
                        PayStatusEnum.PayTypeNotPay.getCode());
                resultModel.setData(res);
                return resultModel;
            } else {
                JSONObject res = getResultData(PayConstants.PAY_STATUS_2, "下单失败",
                        PayStatusEnum.PayTypeNotPay.getCode());
                resultModel.setData(res);
                return resultModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;
    }

    private JSONObject getResultData(String returnCode, String returnMsg, String payStatus) {
        JSONObject res = new JSONObject();
        res.put("returnCode", returnCode);              //支付请求结果 1: 成功, 2: 失败
        res.put("returnMsg", returnMsg);               //支付请求结果或错误信息
        res.put("transactionId", "");                  //支付网关的订单号
        res.put("payAmount", "");                      //支付金额(单位:分)
        res.put("outTradeNo", "");                     //三方交易号
        res.put("payStatus", payStatus);                //支付状态
        res.put("nonceStr", RandomUtil.getRandomStr(32));   //随机串（长度32）
        res.put("returnMsg", payStatus);                //支付请求结果或错误信息
        try {
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr);                   //采用RSA2签名
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private JSONObject createReqObj(String appId, String merchantNo, String subject, String body, String transactionId,
                                    String payAmount, String timestamp, String timeExpire, String redirectUrl,
                                    String notifyUrl, String uid, String nonceStr, String sign) {
        JSONObject req = new JSONObject();
        req.put("appId", appId);
        req.put("merchantNo", merchantNo);
        req.put("subject", subject);
        req.put("body", body);
        req.put("transactionId", transactionId);
        req.put("payAmount", payAmount);
        req.put("timestamp", timestamp);
        req.put("timeExpire", timeExpire);
        req.put("redirectUrl", redirectUrl);
        req.put("notifyUrl", notifyUrl);
        req.put("uid", uid);
        req.put("nonceStr", nonceStr);
        req.put("sign", sign);
        return req;
    }


    @Override
    public ResultModel queryElmPayInfo(JSONObject param, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = new ResultModel();
        JSONObject res = new JSONObject();
        JSONObject reqParam = new JSONObject();

        String id = ELMConstants.ELM_BUSINESS_ID;
        // param.getString("merchantNo");   //商户表主键
        reqParam.put("businessId", id);         //商户ID
        reqParam.put("merchantOrderNo", param.getString("transactionId"));      //商户订单号码
        reqParam.put("nonceStr", param.getString("nonceStr"));             //随机字符串

        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(Long.valueOf(id));
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param.toJSONString());
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        logger.info("下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = this.authorize(String.valueOf(adBusinessExpandInfo.getBusinessId()));
            if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                accessToken = (String) data.get("access_token");
            } else {
                return new ResultModel(GlobalResultStatusEnum.FAIL, "接口授权认证失败");
            }
        }
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", reqParam.toJSONString());
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_QUERY_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            res.put("returnCode", ELMConstants.ELM_RESULT_SUCCESS);
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            String payStatus = String.valueOf(data.get("payStatus"));
            if (PayConstants.PAY_STATUS_1.equals(payStatus)) {
                //说明支付成功处理结果
                res.put("paStatus", PayStatusEnum.PayTypeSuccess.getCode());
                res.put("paAount", String.valueOf(data.get("orderAmount")));
                res.put("outTradeNo", String.valueOf(data.get("outTradeNo")));
            } else {
                res.put("paStatus", PayConstants.PAY_STATUS_0);
            }
        } else {
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
        }
        res.put("returnsg", "请求成功");            //支付请求结果错误信息

        res.put("appId", param.getString("appId"));                 //三方分配的应用APPID
        res.put("merchantNo", param.getString("merchantNo"));             //三方分配的商户号
        res.put("transactionId", param.getString("transactionId"));          //支付网关的订单号

        /*res.put("thirdUserId", );            //S三方UserID，风控使用，支付成功后必传。
        res.put("thirdPaAccount", );          //S三方收款账户，风控使用，支付成功后必传。*/
        res.put("nonceStr", RandomUtil.getRandomStr(32)); //随机串（长度32）
        try {
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr); //采用RSA2签名
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultModel.setData(res);
        return resultModel;
    }

    public ResultModel authorize(String businessId) {
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.AUTHORIZE_URL);
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonResult.get("data");
            return ResultModel.ok(data);
        } else {
            return ResultModel.error(GlobalResultStatusEnum.SIGN_VALID_ERROR);
        }
    }

    @Override
    public ResultModel elmRefund(JSONObject req) {
        ResultModel resultModel = new ResultModel();
        JSONObject param = new JSONObject();
        JSONObject res = new JSONObject();
        param.put("businessId", ELMConstants.elm_bussinesss_serial);
        param.put("storesId", "ELM001");
        param.put("cardNumber", "18017712088"); // TODO 饿了么发起订单实时推送，从缓存取值
        param.put("merchantOrderNo", req.getString("transactionId"));
        param.put("merchantRefundNo", req.get("refundNo")); //商户退款订单号码，商户内部退款订单唯一，位数12-32位内
        param.put("refundPrice", req.getString("payAmount"));
        param.put("refundAmount", req.getString("refundAmount"));
        param.put("notifyUrl", req.getString("notifyUrl"));
        param.put("clientIp", req.get("clientIp"));
        param.put("nonceStr", req.get("nonceStr"));

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonDetail = new JSONObject();
        jsonDetail.put("code", "1002"); //饿了么没有传，目前写个假数据
        jsonDetail.put("goods", "饿了么商品退款");
        jsonDetail.put("number", 1);
        jsonDetail.put("amount", req.getString("payAmount"));
        jsonDetail.put("category", "0000");
        jsonDetail.put("price", req.getString("payAmount"));
        jsonDetail.put("tax", 0);
        jsonArray.add(jsonDetail);
        param.put("orderDetail", jsonArray.toJSONString()); // 订单详情
        AdBusinessExpandInfo queryParam = new AdBusinessExpandInfo();
        queryParam.setClientId(ELMConstants.ELM_CLIENT_ID);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessExpandInfo(queryParam);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        if (accessToken == null) {
            ResultModel authorize = this.authorize(String.valueOf(adBusinessExpandInfo.getBusinessId()));
            if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                accessToken = (String) data.get("access_token");
            } else {
                return new ResultModel(GlobalResultStatusEnum.FAIL, "接口授权认证失败");
            }
        }
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param.toJSONString());
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param.toJSONString());
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_REFUND_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        logger.info("退款返回结果,{}", jsonObject.toJSONString());
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            String refundStatus = String.valueOf(data.get("refundStatus"));

            res.put("returnCode", ELMConstants.ELM_RESULT_SUCCESS);
            res.put("returnMsg", "处理成功");
            res.put("outTradeNo", data.get("outTradeNo").toString());
            res.put("outRefundNo", data.get("outRefundNo").toString());
            if (PayConstants.REFUND_STATUS_S.equals(refundStatus)) {
                //说明退款成功处理结果
                res.put("refundStatus", RefundStatusEnum.RefundTypeSuccess.getCode());
            } else if (PayConstants.REFUND_STATUS_F.equals(refundStatus)) {
                res.put("refundStatus", RefundStatusEnum.RefundTypeFail.getCode());
            } else {
                res.put("refundStatus", RefundStatusEnum.RefundTypeProcessing.getCode());
            }
        } else {
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnMsg", "退款失败，请稍后在试");
            res.put("refundStatus", RefundStatusEnum.RefundTypeFail.getCode());
        }
        res.put("appId", adBusinessExpandInfo.getShopId());
        res.put("merchantNo", adBusinessExpandInfo.getBusinessId());
        res.put("transactionId", res.getString("transactionId"));
        res.put("refundNo", res.getString("refundNo"));
        res.put("payAmount", res.getString("payAmount"));
        res.put("refundAmount", res.getString("refundAmount"));
        res.put("nonceStr", RandomUtil.getRandomStr(32));
        try {
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultModel.setData(res);
        return resultModel;
    }

    @Override
    public ResultModel queryElmRefundInfo(JSONObject req) {
        ResultModel resultModel = new ResultModel();
        JSONObject param = new JSONObject();
        JSONObject res = new JSONObject();
        param.put("businessId", ELMConstants.elm_bussinesss_serial);
        param.put("merchantRefundNo", req.getString("refundNo"));
        param.put("outRefundNo", req.getString("transactionId")); //兜礼退款订单号
        AdBusinessExpandInfo queryParam = new AdBusinessExpandInfo();
        queryParam.setClientId(ELMConstants.ELM_CLIENT_ID);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessExpandInfo(queryParam);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        if (accessToken == null) {
            ResultModel authorize = this.authorize(String.valueOf(adBusinessExpandInfo.getBusinessId()));
            if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                accessToken = (String) data.get("access_token");
            } else {
                return new ResultModel(GlobalResultStatusEnum.FAIL, "接口授权认证失败");
            }
        }
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param.toJSONString());
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param.toJSONString());
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_REFUND_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        logger.info("退款查询返回结果,{}", jsonObject.toJSONString());
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            String refundStatus = String.valueOf(data.get("refundStatus"));
            res.put("returnCode", ELMConstants.ELM_RESULT_SUCCESS);
            res.put("returnMsg", "处理成功");
            res.put("outTradeNo", data.get("outTradeNo").toString());
            res.put("outRefundNo", data.get("outRefundNo").toString());
            if (PayConstants.REFUND_STATUS_S.equals(refundStatus)) {
                //说明退款成功处理结果
                res.put("refundStatus", RefundStatusEnum.RefundTypeSuccess.getCode());
            } else if (PayConstants.REFUND_STATUS_F.equals(refundStatus)) {
                res.put("refundStatus", RefundStatusEnum.RefundTypeFail.getCode());
            } else {
                res.put("refundStatus", RefundStatusEnum.RefundTypeProcessing.getCode());
            }
        } else {
            res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
            res.put("returnMsg", "退款查询失败，请稍后在试");
        }
        res.put("appId", adBusinessExpandInfo.getShopId());
        res.put("merchantNo", adBusinessExpandInfo.getBusinessId());
        res.put("transactionId", res.getString("transactionId"));

        res.put("refundNo", res.getString("refundNo"));
        res.put("payAmount", res.getString("payAmount"));
        res.put("refundAmount", res.getString("refundAmount"));

        res.put("refundStatus", RefundStatusEnum.RefundTypeFail.getCode());
        res.put("nonceStr", RandomUtil.getRandomStr(32));
        resultModel.setData(res);
        return resultModel;
    }
}
