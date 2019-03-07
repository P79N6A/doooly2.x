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
    public ResultModel orderAmountPush(String s, JSONObject obj, HttpServletRequest httpServletRequest) {
        String consumerNo = httpServletRequest.getHeader("consumerNo");
        String orderNo = obj.getString("orderNo");
        String orderNo98 = obj.getString("orderNo98");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE,
                ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        Boolean flag = ElmSignUtils.validParam(consumerNo, timeStamp, sign, s, eleConfig);
        if (!flag) {
            return ResultModel.error(GlobalResultStatusEnum.PARAM_VALID_ERROR);
        } else {
            //验证成功将订单信息放入缓存
            stringRedisTemplate.opsForValue().set(String.format(ELMConstants.ELM_ORDER_PREFIX, orderNo98),
                    obj.toJSONString());
            logger.info("---------->> 验证成功订单缓存, key：{}", String.format(ELMConstants.ELM_ORDER_PREFIX, orderNo98));
            logger.info("---------->> 验证成功订单缓存, value：{}", obj.toJSONString());
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
    public ResultModel orderStatusPush(String s,JSONObject obj, HttpServletRequest httpServletRequest) {
        String consumerNo = httpServletRequest.getHeader("consumerNo");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String valueByTypeAndKey = configDictServiceI.getValueByTypeAndKey(ELMConstants.ELM_DICT_TYPE,
                ELMConstants.ELM_DICT_KEY);
        JSONObject eleConfig = JSONObject.parseObject(valueByTypeAndKey);
        String elmConsumerSecret = eleConfig.getString("consumerSecret");
        Boolean flag = ElmSignUtils.validParam(consumerNo, timeStamp, sign, s, eleConfig);
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
            String eleOrderId = json.getString("ele_order_id");
            String key = String.format(ELMConstants.ELM_ORDER_PREFIX, eleOrderId);
            String redisStr = stringRedisTemplate.opsForValue().get(key);
            logger.info("---------->>  ele_order_id:" + eleOrderId);
            logger.info("---------->> 验证成功订单缓存, key:"+ key);
            logger.info("---------->> 验证成功订单缓存, value:"+ redisStr);

            JSONObject redisData = JSONObject.parseObject(redisStr);
            String telephone = redisData.getString("bNo");
            logger.info("---------->> 验证成功订单缓存, telephone：{}"+ telephone);

            if (StringUtils.isBlank(telephone)) {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_MERCHANT_MOB_ERROR
                        , PayStatusEnum.PayTypeNotPay.getCode(), "", transactionId, payAmount.toString());
                resultModel.setData(res);
                return resultModel;
            }

            //appId 是 client_secret, merchantNo 是ad_business 表的 business_id
            AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                    json.getString("merchantNo"), json.getString("appId"));
            if (null == adBusinessExpandInfo) {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_MERCHANT_NO_ERROR,
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
            //logger.info("饿了么下单参数===== param ================================>>" + param);

            String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY
                    , adBusinessExpandInfo.getClientId()));
            logger.info("饿了么下预付单参数===== accessToken ================================>>:{}", accessToken);
            if (accessToken == null) {
                ResultModel authorize = newPaymentServiceI.authorize(String.valueOf(
                        adBusinessExpandInfo.getBusinessId()));
                if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                    Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                    accessToken = (String) data.get("access_token");
                } else {
                    JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL,
                            ELMConstants.ELE_MERCHANT_AUTHORIZE_FAIL, "", "", transactionId,
                            payAmount.toString());
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
            logger.info("饿了么下单发送http请求参数===== object ================================>>:{}", object.toJSONString());
            String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.UNIFIED_ORDER_URL);
            logger.info("饿了么下单发送http响应参数：===== result ================================>>:{}", result);
            if (null == result) {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_CREATE_ORDER_FAIL,
                        "", "", "", "");
                resultModel.setData(res);
                return resultModel;
            }
            JSONObject jsonResult = JSONObject.parseObject(result);
            if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
                //下单成功返回信息
                Map<Object, Object> data = (Map<Object, Object>) jsonResult.get("data");
                if (null == data) {
                    JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL,
                            jsonResult.getString("info"), "", "", "",
                            payAmount.toString());
                    resultModel.setData(res);
                    return resultModel;
                }
                String payId = String.valueOf(data.get("payId")); //生成预支付id
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_SUCCESS,
                        jsonResult.getString("info"), PayStatusEnum.PayTypeNotPay.getCode(), payId,
                        transactionId, payAmount.toString());
                resultModel.setData(res);
            } else {
                JSONObject res = getCreateOrderResult(ELMConstants.ELM_RESULT_FAIL, jsonResult.getString("info"),
                        PayStatusEnum.PayTypeNotPay.getCode(), "", transactionId, payAmount.toString());
                resultModel.setData(res);
            }
        } catch (Exception e) {
            logger.info("createElmOrderAndPay ================================>>饿了么落单并支付失败");
            e.printStackTrace();
        }
        return resultModel;
    }

    /**
     * 落单支付返回结果
     *
     * @param returnCode
     * @param returnMsg
     * @param payStatus
     * @param payId
     * @param transactionId
     * @param payAmount
     * @return
     */
    private JSONObject getCreateOrderResult(int returnCode, String returnMsg, String payStatus, String payId,
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
        int paAount = 0;
        String outTradeNo = "";
        String paStatus = "";
        String thirdUserId = "";
        String thirdPaAccount = "";

        //appId 是 client_secret, merchantNo 是ad_business 表的 business_id
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                param.getString("merchantNo"), param.getString("appId"));
        if (null == adBusinessExpandInfo) {
            JSONObject ress = getQueryPayResult(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_MERCHANT_NO_ERROR,
                    "", 0, "", paStatus, thirdUserId, thirdPaAccount);
            resultModel.setData(ress);
            return resultModel;
        }

        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY,
                clientId));
        logger.info("下预付单参数===== accessToken ================================>>" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = newPaymentServiceI.authorize(String.valueOf(adBusinessExpandInfo.getBusinessId()));
            if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                accessToken = (String) data.get("access_token");
            } else {
                JSONObject ress = getQueryPayResult(ELMConstants.ELM_RESULT_FAIL,
                        ELMConstants.ELE_MERCHANT_AUTHORIZE_FAIL, transactionId, paAount, "", paStatus,
                        thirdUserId, thirdPaAccount);
                resultModel.setData(ress);
                return resultModel;
            }
        }
        reqParam.put("businessId", param.getString("merchantNo"));   //商户ID即ad_business 表的 business_id
        reqParam.put("merchantOrderNo", transactionId);                  //商户订单号码
        reqParam.put("nonceStr", param.getString("nonceStr"));     //随机字符串

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
        logger.info("支付查询返回参数 ================================>>" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (null == jsonObject) {
            JSONObject ress = getQueryPayResult(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_QUERY_PAY_FAIL,
                    transactionId, paAount, "", paStatus, thirdUserId, thirdPaAccount);
            resultModel.setData(ress);
            return resultModel;
        }

        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
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
            paStatus = PayStatusEnum.PayTypeNotPay.getCode();
        }
        JSONObject res = getQueryPayResult(ELMConstants.ELM_RESULT_SUCCESS, jsonObject.getString("info"),
                transactionId, paAount, outTradeNo, paStatus, thirdUserId, thirdPaAccount);
        resultModel.setData(res);
        return resultModel;
    }

    private JSONObject getQueryPayResult(int returnCode, String returnsg, String transactionId, int paAount,
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
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                req.getString("merchantNo"), req.getString("appId"));
        if (null == adBusinessExpandInfo) {
            JSONObject res = getElmRefundResult(ELMConstants.ELM_RESULT_FAIL,
                    ELMConstants.ELE_MERCHANT_NO_ERROR, "", "",
                    "", "", 0, 0, "");
            resultModel.setData(res);
            return resultModel;
        }
        List<OrderVo> orderList = adOrderReportDao.getByOrderNum(req.getString("transactionId"));
        if (null == orderList || orderList.size() == 0) {
            JSONObject res = getElmRefundResult(ELMConstants.ELM_RESULT_FAIL,
                    ELMConstants.ELE_REFUND_ORDER_ERROR, "", "", "",
                    "", 0, 0, "");
            resultModel.setData(res);
            return resultModel;
        }
        OrderVo orderVo = orderList.get(0);
        PayMsg payMsg = refundService.autoRefund(orderVo.getUserId(), orderVo.getOrderNumber());
        if (null == payMsg) {
            JSONObject res = getElmRefundResult(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_REFUND_OPERATION_FAIL,
                    "", "", "", "", 0,
                    0, "");
            resultModel.setData(res);
            return resultModel;
        }
        if (PayMsg.success_code.equals(payMsg.getCode())) {
            String outTradeNo = "";
            String outRefundNo = "";
            // 支付流水
            PayFlow payFlow = getPayFlow(req.getString("transactionId"));
            if (null != payFlow) {
                outTradeNo = String.valueOf(payFlow.getTransNo());
            }
            // 退款流水
            AdReturnFlow adReturnFlow = getAdReturnFlow(orderVo.getOrderId());
            if (null != adReturnFlow) {
                outRefundNo = String.valueOf(adReturnFlow.getReturnFlowNumber());
            }
            JSONObject result = getElmRefundResult(ELMConstants.ELM_RESULT_SUCCESS, PayMsg.success_mess,
                    req.getString("transactionId"), outTradeNo,
                    req.getString("refundNo"), outRefundNo, req.getInteger("payAmount"),
                    req.getInteger("refundAmount"), RefundStatusEnum.RefundTypeSuccess.getCode());
            resultModel.setData(result);
        } else {
            JSONObject result = getElmRefundResult(ELMConstants.ELM_RESULT_FAIL, PayMsg.success_mess,
                    req.getString("transactionId"), "",
                    req.getString("refundNo"), "", 0,
                    0, RefundStatusEnum.RefundTypeProcessing.getCode());
            resultModel.setData(result);
        }
        return resultModel;
    }

    @Override
    public ResultModel queryElmRefundInfo(JSONObject req) {
        ResultModel resultModel = new ResultModel();
        try {
            String outTradeNo = "";
            String outRefundNo = "";
            int payAmount = 0;
            int refundAmount = 0;
            String refundStatus = RefundStatusEnum.RefundTypeProcessing.getCode();
            AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getBusinessAndExpandInfo(
                    req.getString("merchantNo"), req.getString("appId"));
            if (null == adBusinessExpandInfo) {
                JSONObject res = crtRefundQueryRes(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_MERCHANT_NO_ERROR,
                        "", "", "", "", 0,
                        0, "");
                resultModel.setData(res);
                return resultModel;
            }
            OrderVo order = new OrderVo();
            order.setType(5);
            order.setOrderNumber(req.getString("transactionId"));
            List<OrderVo> orderList = adOrderReportDao.getOrder(order);
            if (null == orderList || orderList.size() == 0) {
                JSONObject res = crtRefundQueryRes(ELMConstants.ELM_RESULT_FAIL, ELMConstants.ELE_REFUND_ORDER_ERROR,
                        "", "", "", "", 0,
                        0, "");
                resultModel.setData(res);
                return resultModel;
            }
            OrderVo orderVo = orderList.get(0);

            if (null != orderVo.getTotalMount()) {
                payAmount = orderVo.getTotalMount().intValue();
            }
            // 支付流水
            PayFlow payFlow = getPayFlow(orderVo.getOrderNumber());
            if (null != payFlow) {
                outTradeNo = String.valueOf(payFlow.getTransNo());
                if (null != payFlow.getAmount()) {
                    refundAmount = payFlow.getAmount().intValue();
                }
                // 交易状态（i、交易中，s、交易成功，f、交易失败， d、重复支付， r、已退款）
                String payStatus = payFlow.getPayStatus();
                if (StringUtils.equalsIgnoreCase("s", payStatus)) {
                    refundStatus = RefundStatusEnum.RefundTypeSuccess.getCode();
                } else if (StringUtils.equalsIgnoreCase("f", payStatus)) {
                    refundStatus = RefundStatusEnum.RefundTypeFail.getCode();
                }
            }

            // 退款流水
            AdReturnFlow adReturnFlow = getAdReturnFlow(orderVo.getOrderId());
            if (null != adReturnFlow) {
                outRefundNo = String.valueOf(adReturnFlow.getReturnFlowNumber());
            }

            JSONObject res = crtRefundQueryRes(ELMConstants.ELM_RESULT_SUCCESS,
                    GlobalResultStatusEnum.SUCCESS_OK.getInfo(), req.getString("transactionId"), outTradeNo,
                    req.getString("refundNo"), outRefundNo, payAmount, refundAmount, refundStatus);
            resultModel.setData(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;
    }

    /**
     * 封装退款查询接口
     *
     * @param returnCode
     * @param returnMsg
     * @param transactionId
     * @param outTradeNo
     * @param refundNo
     * @param outRefundNo
     * @param payAmount
     * @param refundAmount
     * @param refundStatus
     * @return
     */
    private JSONObject crtRefundQueryRes(int returnCode, String returnMsg, String transactionId, String outTradeNo,
                                         String refundNo, String outRefundNo, int payAmount, int refundAmount, String refundStatus) {
        JSONObject res = new JSONObject();
        try {
            res.put("returnCode", returnCode);
            res.put("returnMsg", returnMsg);
            res.put("appId", ELMConstants.ELM_APP_ID);
            res.put("merchantNo", ELMConstants.ELM_MERCHANT_NO);
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

    /**
     * 查询退款流水
     *
     * @param orderId
     * @return
     */
    private AdReturnFlow getAdReturnFlow(long orderId) {
        return adReturnFlowDao.getByOrderId(orderId, null, null);
    }

    /**
     * 查询支付流水
     *
     * @param orderNum
     * @return
     */
    private PayFlow getPayFlow(String orderNum) {
        return adPayFlowDao.getByOrderNum(orderNum, null, null);
    }

    /**
     * 退款接口返回参数
     *
     * @param returnCode
     * @param returnMsg
     * @param transactionId
     * @param outTradeNo
     * @param refundNo
     * @param outRefundNo
     * @param payAmount
     * @param refundAmount
     * @param refundStatus
     * @return
     */
    private JSONObject getElmRefundResult(int returnCode, String returnMsg, String transactionId, String outTradeNo,
                                          String refundNo, String outRefundNo, int payAmount, int refundAmount, String refundStatus) {
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
}