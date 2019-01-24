package com.doooly.business.ele.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.ele.ELMServiceI;
import com.doooly.business.order.service.AdOrderReportServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.constants.PayConstants;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.business.payment.utils.SignUtil;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PaymentConstants;
import com.doooly.common.elm.*;
import com.doooly.common.elm.ElmSignUtils;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.RandomUtil;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdOrderReport;
import com.doooly.entity.reachad.AdReturnFlow;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 饿了么实现
 * @author: qing.zhang
 * @date: 2019-01-03
 */
@Service
public class ELMServiceImpl implements ELMServiceI {

    private Logger logger = LoggerFactory.getLogger(ELMServiceImpl.class);

    @Autowired
    private ConfigDictServiceI configDictServiceI;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AdOrderReportServiceI adOrderReportServiceI;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private NewPaymentServiceI newPaymentServiceI;
    @Autowired
    private AdOrderReportDao adOrderReportDao;
    @Autowired
    private AdPayFlowDao adPayFlowDao;
    @Autowired
    private RefundService refundService;
    @Autowired
    private AdReturnFlowDao adReturnFlowDao;
    @Autowired
    private AdUserDao adUserDao;

    /**
     * 推送信息
     * {
     * "bNo": "业务编码",
     * "uNo": "用户编码",
     * "orderNo": "订单号",
     * "orderNo98": "订单号(98开头)",
     * "totalFee": "总费用",
     * "status": "订单状态"
     * }
     */
    @Override
    public ResultModel orderAmountPush(JSONObject obj, HttpServletRequest httpServletRequest) {
        String consumerNo = httpServletRequest.getHeader("consumerNo");
        String orderNo = obj.getString("orderNo");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE, ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        Boolean flag = ElmSignUtils.validParam(consumerNo, timeStamp, sign, obj, eleConfig);
        if (!flag) {
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        } else {
            //验证成功将订单信息放入缓存
            stringRedisTemplate.opsForValue().set(String.format(ELMConstants.ELM_ORDER_PREFIX, orderNo), obj.toJSONString());
            return ResultModel.success_ok("获取订单信息成功");
        }
    }

    /**
     * 饿了么 状态推送
     *
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
        Boolean flag = ElmSignUtils.validParam(consumerNo, timeStamp, sign, obj, eleConfig);
        if (!flag) {
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        }
        String orderNo = obj.getString("orderNo");
        Integer status = obj.getInteger("status");
        String remark = obj.getString("remark");
        //查询doooly订单
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNo);
        OrderVo o = adOrderReportServiceI.getOrderLimt(order);
        if (o == null) {
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
        logger.info("update elm order status, item = {}", newItem);
        orderService.updateOrderItem(newItem);
        return ResultModel.success_ok("收到状态");
    }

    /**
     * 饿了么落单
     *
     * @param json
     * @return
     */
    @Override
    public ResultModel createElmOrderAndPay(JSONObject json) {
        ResultModel resultModel = new ResultModel();
        try {
            String transactionId = json.getString("transactionId");
            BigDecimal payAmount = json.getBigDecimal("payAmount");

            String redisTel = stringRedisTemplate.opsForValue().get(String.format(ELMConstants.ELM_ORDER_PREFIX, json.getString("transactionId")));
            if (StringUtils.isBlank(redisTel)) {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, "会员手机号或会员卡号为空",
                        PayStatusEnum.PayTypeNotPay.getCode(), "", transactionId, payAmount.toString());
                resultModel.setData(res);
                return resultModel;
            }
            JSONObject redisObj = (JSONObject) JSONObject.parse(redisTel);
            String telephone = redisObj.getString("bNo");
            //appId 是 client_secret, merchantNo 是ad_business 表的 business_id
            AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                    json.getString("merchantNo"), json.getString("appId"));
            if (null == adBusinessExpandInfo) {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, "无效的商户号",
                        "", "", transactionId, payAmount.toString());
                resultModel.setData(res);
                return resultModel;
            }
            JSONObject param = new JSONObject();
            param.put("businessId", json.getString("merchantNo"));
            param.put("cardNumber", telephone);
            param.put("storesId", "ELM001");
            param.put("merchantOrderNo", transactionId);
            param.put("price", payAmount);
            param.put("amount", payAmount);
            param.put("tradeType", "DOOOLY_JS");
            param.put("body", json.getString("subject"));
            param.put("isSource", 2);
            param.put("notifyUrl", json.getString("notifyUrl"));
            param.put("orderDate", DateUtils.formatDateTime(new Date()));
            param.put("clientIp", json.get("clientIp"));
            param.put("nonceStr", json.get("nonceStr"));

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

            String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY,
                    adBusinessExpandInfo.getClientId()));
            logger.info("饿了么下预付单参数=======accessToken========" + accessToken);
            if (accessToken == null) {
                ResultModel authorize = newPaymentServiceI.authorize(String.valueOf(adBusinessExpandInfo.getBusinessId()));
                if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                    Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                    accessToken = (String) data.get("access_token");
                } else {
                    JSONObject res = getCreateOrderResult(PayConstants.PAY_STATUS_2, "接口授权认证失败",
                            "", "", transactionId, payAmount.toString());
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
            logger.info("饿了么下单返回：{}", result);
            JSONObject jsonResult = JSONObject.parseObject(result);
            if (null == jsonResult) {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, "落单失败,请稍后在试!",
                        "", "", "", "");
                resultModel.setData(res);
                return resultModel;
            }

            if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
                //下单成功返回信息
                Map<Object, Object> data = (Map<Object, Object>) jsonResult.get("data");
                if (null == data) {
                    JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, jsonResult.getString("info"),
                            "", "", "", payAmount.toString());
                    resultModel.setData(res);
                    return resultModel;
                }
                String payId = String.valueOf(data.get("payId")); //生成预支付id
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_SUCCESS, jsonResult.getString("info"),
                        PayStatusEnum.PayTypeNotPay.getCode(), payId, transactionId, payAmount.toString());
                resultModel.setData(res);
            } else {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, jsonResult.getString("info"),
                        PayStatusEnum.PayTypeNotPay.getCode(), "", transactionId, payAmount.toString());
                resultModel.setData(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;
    }

    private JSONObject getCreateOrderResult(String returnCode, String returnMsg, String payStatus, String payId,
                                            String transactionId, String payAmount) {
        JSONObject res = new JSONObject();
        try {
            res.put("returnCode", returnCode);             //支付请求结果 1: 成功, 2: 失败
            res.put("returnMsg", returnMsg);               //支付请求结果或错误信息
            res.put("transactionId", transactionId);       //支付网关的订单号
            res.put("payAmount", payAmount);               //支付金额(单位:分)
            res.put("outTradeNo", payId);                  //三方交易号
            res.put("payStatus", payStatus);               //支付状态
            res.put("nonceStr", RandomUtil.getRandomStr(32)); //随机串（长度32）
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr);                   //采用RSA2签名
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public ResultModel queryElmPayInfo(JSONObject param, HttpServletRequest httpServletRequest) {
        ResultModel resultModel = new ResultModel();
        JSONObject reqParam = new JSONObject();
        String transactionId = param.getString("transactionId");
        String returnCode = ELMConstants.ELM_RESULT_FAIL;
        String returnsg = "";
        int paAount = 0;
        String outTradeNo = "";
        String paStatus = "";
        String thirdUserId = "";
        String thirdPaAccount = "";

        //appId 是 client_secret, merchantNo 是ad_business 表的 business_id
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                param.getString("merchantNo"), param.getString("appId"));
        if (null == adBusinessExpandInfo) {
            JSONObject ress = getQueryPayResult(returnCode, "无效的商户号", "", paAount,
                    outTradeNo, paStatus, thirdUserId, thirdPaAccount);
            resultModel.setData(ress);
            return resultModel;
        }
        reqParam.put("businessId", param.getString("merchantNo"));   //商户ID即ad_business 表的 business_id
        reqParam.put("merchantOrderNo", transactionId);                  //商户订单号码
        reqParam.put("nonceStr", param.getString("nonceStr"));     //随机字符串

        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY,
                clientId));
        logger.info("下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = newPaymentServiceI.authorize(String.valueOf(adBusinessExpandInfo.getBusinessId()));
            if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                accessToken = (String) data.get("access_token");
            } else {
                JSONObject ress = getQueryPayResult(returnCode, "接口授权认证失败", transactionId, paAount,
                        outTradeNo, paStatus, thirdUserId, thirdPaAccount);
                resultModel.setData(ress);
                return resultModel;
            }
        }
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", reqParam.toJSONString());
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());

        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", reqParam.toJSONString());
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_QUERY_URL);
        logger.info("支付查询返回参数===============" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (null == jsonObject) {
            JSONObject ress = getQueryPayResult(returnCode, "支付查询失败，请稍后在试", transactionId, paAount,
                    outTradeNo, paStatus, thirdUserId, thirdPaAccount);
            resultModel.setData(ress);
            return resultModel;
        }

        returnsg = jsonObject.getString("info");
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            returnCode = ELMConstants.ELM_RESULT_SUCCESS;
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            String payStatus = String.valueOf(data.get("payStatus"));   //支付状态 0 未支付 1支付成功 2支付失败
            outTradeNo = data.get("outTradeNo").toString();        //兜礼平台订单号
            paAount = Integer.valueOf(data.get("orderAmount").toString());
            if (PayConstants.PAY_STATUS_1.equals(payStatus)) {
                //说明支付成功处理结果
                paStatus = PayStatusEnum.PayTypeSuccess.getCode();
                // 根据订单号去第三方查询userId
                AdOrderReport queryParam = new AdOrderReport();
                queryParam.setOrderNumber(outTradeNo);
                Long userId = adOrderReportDao.getUserIdByOrderNum(queryParam);
                if (null != userId) {
                    thirdUserId = String.valueOf(userId);                   //S三方UserID，风控使用，支付成功后必传
                    thirdPaAccount = ELMConstants.DOOOLY_FINANCIAL_ACCOUNT; //S三方收款账户，风控使用，支付成功后必传。
                  /*  AdUser adUser = adUserDao.getById(userId.intValue());
                    if (null != adUser) {
                        thirdUserId = (null != adUser.getTelephone())  ? adUser.getTelephone() :adUser.getCardNumber();
                        thirdPaAccount = ELMConstants.DOOOLY_FINANCIAL_ACCOUNT;
                    }*/
                }
            } else {
                paStatus = PayStatusEnum.PayTypeNotPay.getCode();
            }
        } else {
            returnCode = ELMConstants.ELM_RESULT_SUCCESS;
            paStatus = PayStatusEnum.PayTypeNotPay.getCode();
        }
        JSONObject res = getQueryPayResult(returnCode, returnsg, transactionId, paAount,
                outTradeNo, paStatus, thirdUserId, thirdPaAccount);
        resultModel.setData(res);
        return resultModel;
    }

    private JSONObject getQueryPayResult(String returnCode, String returnsg, String transactionId, int paAount,
                                         String outTradeNo, String paStatus, String thirdUserId, String thirdPaAccount) {
        JSONObject res = new JSONObject();
        try {
            res.put("appId", ELMConstants.ELM_APP_ID);
            res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
            res.put("returnCode", returnCode);
            res.put("returnsg", returnsg);
            res.put("transactionId", transactionId);
            res.put("paAount", paAount);
            res.put("outTradeNo", outTradeNo);
            res.put("paStatus", paStatus);
            res.put("thirdUserId", thirdUserId);          //S三方UserID，风控使用，支付成功后必传。
            res.put("thirdPaAccount", thirdPaAccount);   //S三方收款账户，风控使用，支付成功后必传。
            res.put("nonceStr", RandomUtil.getRandomStr(32));
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public ResultModel elmRefund(JSONObject req) {
        ResultModel resultModel = new ResultModel();
        String returnCode = ELMConstants.ELM_RESULT_FAIL;           //支付请求结果 1: 成功, 2: 失败
        String transactionId = req.getString("transactionId"); //支付网关的订单号
        String outTradeNo = "";                                      //三方支付交易号
        String refundNo = req.getString("refundNo");            //支付网关退款单号
        String outRefundNo = "";                                     //	三方退款单号
        int payAmount = 0;                                           //支付金额(单位分)
        int refundAmount = 0;                                        //退款金额(单位分)
        String refundStatus = "";                                    //退款状态
        String nonceStr = req.getString("nonceStr");            //随机串（长度32）
        String orderNum = req.getString("transactionId");

        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                req.getString("merchantNo"), req.getString("appId"));
        if (null == adBusinessExpandInfo) {
            JSONObject res = getElmRefundResult(returnCode, "无效的商户号", transactionId, outTradeNo,
                    refundNo, outRefundNo, payAmount, refundAmount, refundStatus, nonceStr);
            resultModel.setData(res);
            return resultModel;
        }
        List<OrderVo> orderList = adOrderReportDao.getByOrderNum(orderNum);
        if (null == orderList || orderList.size() == 0) {
            JSONObject res = getElmRefundResult(returnCode, "无效的订单号", transactionId, outTradeNo,
                    refundNo, outRefundNo, payAmount, refundAmount, refundStatus, nonceStr);
            resultModel.setData(res);
            return resultModel;
        }
        OrderVo orderVo = orderList.get(0);
        PayMsg payMsg = refundService.autoRefund(orderVo.getUserId(), orderVo.getOrderNumber());
        if (null == payMsg) {
            JSONObject res = getElmRefundResult(returnCode, "退款失败,请稍后在试", transactionId,
                    outTradeNo, refundNo, outRefundNo, payAmount, refundAmount, refundStatus, nonceStr);
            resultModel.setData(res);
            return resultModel;
        }
        if (PayMsg.success_code.equals(payMsg.getCode())) {
            payAmount = req.getInteger("payAmount");       //支付金额(单位分)
            refundAmount = req.getInteger("refundAmount"); //	退款金额(单位分)
            refundStatus = RefundStatusEnum.RefundTypeSuccess.getCode();
            // 支付流水
            PayFlow payFlow = adPayFlowDao.getByOrderNum(orderNum, null, null);
            if (null != payFlow) {
                outTradeNo = String.valueOf(payFlow.getTransNo());
            }
            // 退款流水
            AdReturnFlow adReturnFlow = adReturnFlowDao.getByOrderId(orderVo.getOrderId(), null, null);
            if (null != adReturnFlow) {
                outRefundNo = String.valueOf(adReturnFlow.getReturnFlowNumber());
            }
        } else {
            returnCode = ELMConstants.ELM_RESULT_FAIL;
            refundStatus = RefundStatusEnum.RefundTypeProcessing.getCode();
        }
        String returnMsg = PayMsg.success_mess;
        JSONObject ress = getElmRefundResult(returnCode, returnMsg, transactionId, outTradeNo, refundNo, outRefundNo, payAmount,
                refundAmount, refundStatus, nonceStr);
        resultModel.setData(ress);
        return resultModel;
    }

    private JSONObject getElmRefundResult(String returnCode, String returnMsg, String transactionId,
                                          String outTradeNo, String refundNo, String outRefundNo, int payAmount,
                                          int refundAmount, String refundStatus, String nonceStr) {
        JSONObject res = new JSONObject();
        try {
            res.put("appId", ELMConstants.ELM_APP_ID);
            res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
            res.put("returnCode", returnCode);
            res.put("returnMsg", returnMsg);
            res.put("transactionId", transactionId);
            res.put("outTradeNo", outTradeNo);
            res.put("refundNo", refundNo);
            res.put("outRefundNo", outRefundNo);
            res.put("payAmount", payAmount);
            res.put("refundAmount", refundAmount);
            res.put("refundStatus", refundStatus);
            res.put("nonceStr", RandomUtil.getRandomStr(32));
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public ResultModel queryElmRefundInfo(JSONObject req) {
        ResultModel resultModel = new ResultModel();
        JSONObject res = new JSONObject();
        try {
            String orderNumber = req.getString("transactionId");
            String outTradeNo = "";
            String outRefundNo = "";
            AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                    req.getString("merchantNo"), req.getString("appId"));
            if (null == adBusinessExpandInfo) {
                res.put("returnCode", ELMConstants.ELM_RESULT_FAIL);
                res.put("returnMsg", "无效的商户号");
                res.put("appId", ELMConstants.ELM_APP_ID);
                res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
                res.put("transactionId", "");
                res.put("outTradeNo", "");
                res.put("refundNo", "");
                res.put("outRefundNo", "");
                res.put("payAmount", 0);
                res.put("refundAmount", 0);
                res.put("refundStatus", "");
                res.put("nonceStr", RandomUtil.getRandomStr(32));
                String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
                res.put("sign", signStr);
                resultModel.setData(res);
                return resultModel;
            }
            OrderVo order = new OrderVo();
            order.setType(5);
            order.setOrderNumber(orderNumber);
            res.put("refundStatus", RefundStatusEnum.RefundTypeProcessing.getCode());
            List<OrderVo> orderList = adOrderReportDao.getOrder(order);
            if (null != orderList && orderList.size() > 0) {
                res.put("refundStatus", RefundStatusEnum.RefundTypeSuccess.getCode());
                OrderVo orderVo = orderList.get(0);
                // 支付流水
                PayFlow payFlow = adPayFlowDao.getByOrderNum(orderVo.getOrderNumber(), null, null);
                if (null != payFlow) {
                    outTradeNo = String.valueOf(payFlow.getTransNo());
                }

                // 退款流水
                AdReturnFlow adReturnFlow = adReturnFlowDao.getByOrderId(orderVo.getOrderId(), null, null);
                if (null != adReturnFlow) {
                    outRefundNo = String.valueOf(adReturnFlow.getReturnFlowNumber());
                }
            }
            res.put("returnCode", ELMConstants.ELM_RESULT_SUCCESS);
            res.put("returnMsg", "处理成功");
            res.put("outRefundNo", outRefundNo);   // 三方退款单号
            res.put("outTradeNo", outTradeNo);      // 三方支付交易号
            res.put("appId", ELMConstants.ELM_APP_ID);
            res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
            res.put("transactionId", res.getString("transactionId"));
            res.put("refundNo", res.getString("refundNo"));
            res.put("payAmount", res.getIntValue("payAmount"));
            res.put("refundAmount", res.getString("refundAmount"));
            res.put("nonceStr", RandomUtil.getRandomStr(32));
            String signStr = ElmSignUtils.rsaSign(ElmSignUtils.ELM_PRIVATE_KEY, res);
            res.put("sign", signStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultModel.setData(res);
        return resultModel;
    }
}
