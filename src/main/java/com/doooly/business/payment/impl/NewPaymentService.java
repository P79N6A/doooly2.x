package com.doooly.business.payment.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessor;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessorFactory;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.pay.service.PaymentService;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.pay.service.ReturnFlowService;
import com.doooly.business.pay.service.impl.PaymentServiceFactory;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.constants.PayConstants;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.business.payment.utils.SignUtil;
import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PaymentConstants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.RandomUtil;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessExpandInfoDao;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdPayFlowDao;
import com.doooly.dao.reachad.AdRechargeConfDao;
import com.doooly.dao.reachad.AdRechargeRecordDao;
import com.doooly.dao.reachad.AdRefundFlowDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.OrderMsg;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdRechargeConf;
import com.doooly.entity.reachad.AdRechargeRecord;
import com.doooly.entity.reachad.AdRefundFlow;
import com.doooly.entity.reachad.AdReturnFlow;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.doooly.business.pay.service.RefundService.REFUND_STATUS_I;
import static com.doooly.business.pay.service.RefundService.REFUND_STATUS_S;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-08-09
 */
@Service
public class NewPaymentService implements NewPaymentServiceI {

    protected Logger logger = LoggerFactory.getLogger(NewPaymentService.class);

    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    protected AdUserDao adUserDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AdOrderReportDao adOrderReportDao;
    @Autowired
    private AdRechargeConfDao adRechargeConfDao;
    @Autowired
    private AdBusinessDao adBusinessDao;
    @Autowired
    private AdPayFlowDao payFlowDao;
    @Autowired
    protected PayFlowService payFlowService;
    @Autowired
    protected AdRechargeRecordDao adRechargeRecordDao;
    @Autowired
    private AdRefundFlowDao adRefundFlowDao;
    @Autowired
    private MallBusinessService mallBusinessService;
    @Autowired
    private ReturnFlowService returnFlowService;
    @Autowired
    private RefundService refundService;

    @Override
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
    public ResultModel unifiedorder(JSONObject jsonObject) {
        JSONObject orderSummary = getOrderSummary(jsonObject);
        if(orderSummary==null){
            return new ResultModel(GlobalResultStatusEnum.FAIL,"登录用户和下单用户不匹配");
        }
        logger.info("订单参数=======orderSummary========" + orderSummary.toJSONString());
        JSONObject param = orderSummary.getJSONObject("param");
        JSONObject retJson = orderSummary.getJSONObject("retJson");//页面展示数据集合
        String businessId = orderSummary.getString("businessId");//从返回参数获取商户id来查询扩展信息
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        logger.info(accessToken);
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        logger.info(sign);
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param);
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.UNIFIED_ORDER_URL);
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonResult.get("data");
            String payId = (String) data.get("payId");
            String integralRebatePayAmount = (String) data.get("integralRebatePayAmount");
            retJson.put("payId", payId);
            if(StringUtils.isNotBlank(integralRebatePayAmount)){
                retJson.put("integralRebatePayAmount", integralRebatePayAmount);
            }
            logger.info("payment unifiedorder result data={}",data);
            return ResultModel.ok(retJson);
        } else {
            return ResultModel.error(GlobalResultStatusEnum.SIGN_VALID_ERROR);
        }
    }

    /**
     * 获取商品信息
     *
     * @param json
     * @return
     */
    private JSONObject getOrderSummary(JSONObject json) {
        logger.info("getOrderSummary() json = {}", json);
        JSONObject result = new JSONObject();
        String orderNum = json.getString("orderNum");
        long userId = json.getLong("userId");
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNum);
        order.setUserId(userId);
        List<OrderVo> orderVoList = orderService.getOrder(order);
        if(CollectionUtils.isEmpty(orderVoList)){
            return null;
        }
        OrderVo o = orderVoList.get(0);
        OrderItemVo item = o.getItems().get(0);
        String sku = item.getSku() != null?item.getSku():"";
        String orderDesc = item.getGoods() + sku;
        //String orderDesc = item.getGoods() + item.getSku() + "-" + orderNum;
        JSONObject retJson = new JSONObject();
        retJson.put("productType", o.getProductType());
        retJson.put("totalFree", o.getTotalMount().toString());
        retJson.put("orderDesc", orderDesc);
        retJson.put("orderId", o.getOrderId());
        retJson.put("isSource", o.getIsSource());
        retJson.put("productImg", item.getProductImg());
        retJson.put("supportPayType", o.getSupportPayType());
        retJson.put("orderNum", orderNum);
        if (o.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode() && o.getServiceCharge() != null) {
            retJson.put("serviceCharge", o.getServiceCharge().compareTo(BigDecimal.ZERO) == 0 ? null : o.getServiceCharge());
        }
        //话费充值需要校验积分消费金额,用到此参数
        if (o.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode()) {
            //用户消费金额
            BigDecimal consumptionAmount = adOrderReportDao.getConsumptionAmount(userId);
            retJson.put("consumptionAmount", consumptionAmount == null ? "0" : consumptionAmount);
            AdUser user = adUserDao.getById(order.getUserId().intValue());
            AdRechargeConf conf = adRechargeConfDao.getRechargeConf(user.getGroupNum() + "");
            retJson.put("monthLimit", (conf == null || conf.getMonthLimit() == null) ? "0" : conf.getMonthLimit());
        }
        //组建预支付订单参数
        AdUser user = adUserDao.getById(order.getUserId().intValue());
        AdBusiness business = adBusinessDao.getById(String.valueOf(o.getBussinessId()));
        String price = String.valueOf(o.getTotalPrice().setScale(2, BigDecimal.ROUND_DOWN));
        String amount = String.valueOf(o.getTotalMount().setScale(2, BigDecimal.ROUND_DOWN));
        JSONObject param = new JSONObject();
        param.put("businessId", business.getBusinessId());//商户编号
        param.put("cardNumber", user.getTelephone());
        param.put("merchantOrderNo", o.getOrderNumber());
        param.put("tradeType", "DOOOLY_JS");
        param.put("notifyUrl", PaymentConstants.PAYMENT_NOTIFY_URL);
        param.put("body", orderDesc);
        param.put("isSource",  o.getIsSource());
        param.put("orderDate",DateUtils.formatDateTime(o.getOrderDate()));
        param.put("storesId", "A001");
        param.put("price", price);
        param.put("amount", amount);
        param.put("clientIp", json.get("clientIp"));
        param.put("nonceStr", RandomUtil.getRandomStr(16));
        param.put("isPayPassword", user.getIsPayPassword());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonDetail = new JSONObject();
        jsonDetail.put("code", item.getCode());
        jsonDetail.put("goods", item.getGoods());
        jsonDetail.put("number", item.getNumber());
        jsonDetail.put("price",item.getPrice());
        jsonDetail.put("category",item.getCategoryId());
        jsonDetail.put("tax",item.getTax());
        jsonDetail.put("amount",item.getAmount());
        jsonArray.add(jsonDetail);
        param.put("orderDetail",jsonArray.toJSONString());
        logger.info("下单参数param=========" + param);
        result.put("param", param);
        retJson.put("company", business.getCompany());
        retJson.put("userIntegral", user.getIntegral());
        logger.info("retJson = {}", retJson);
        result.put("retJson", retJson);
        result.put("businessId", o.getBussinessId());//商户id
        return result;
    }


    @Override
    public ResultModel getPayForm(JSONObject params) {
        String orderNum = params.getString("orderNum");
        OrderVo order = orderService.getByOrderNum(orderNum);
        Integer isSource = order.getIsSource();
        //自营商品全是兜礼支付
        PayMsg payMsg;
        if (isSource == 3) {
            //说明是自营订单
            payMsg = prePay(params);
        } else {
            payMsg = prePayNew(params);
        }
        //为空或者校验失败直接返回错误信息
        if (payMsg != null && !payMsg.getCode().equals(OrderMsg.valid_pass_code)) {
            return new ResultModel(Integer.parseInt(payMsg.getCode()), payMsg.getMess());
        }
        JSONObject jsonData = payMsg.getJsonData();
        String businessId = jsonData.getString("businessId");
        String isPayPassword = jsonData.getString("isPayPassword");
        params.put("isPayPassword", isPayPassword);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", params);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", params);
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.GET_PAYFROM_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            return ResultModel.ok(data);
        } else {
            return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"));
        }
    }

    //自营都作为兜礼支付
    private PayMsg prePay(JSONObject json) {
        String payType = PayFlowService.PAYTYPE_CASHIER_DESK;
        logger.info("prePay()  payType={},json = {}", payType, json);
        String orderNum = json.getString("orderNum");
        String userId = json.getString("userId");
        String channel = json.getString("channel");
        // 校验订单
        List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
        if (CollectionUtils.isEmpty(orders)) {
            return new PayMsg(PayMsg.failure_code, "没有找到订单");
        }
        // 订单状态
        OrderVo order = orders.get(0);
        Long oid = checkOrderStatus(order);
        if (oid != null) {
            return new PayMsg(PayMsg.failure_code, "无效的订单状态");
        }
        // 是否重复支付
        PayFlow paidRecord = payFlowService.getByOrderNum(orderNum, payType, null);
        if (paidRecord != null && PayFlowService.PAYMENT_SUCCESS.equals(paidRecord.getPayStatus())) {
            return new PayMsg(PayMsg.failure_code, "请勿重复支付");
        }
        //校验是否可以支付
        PayMsg msg = canPay(order,json);
        if (!OrderMsg.valid_pass_code.equals(msg.getCode())) {
            return msg;
        }
        // 保存支付记录
        PayFlow flow = savePayFlow(orderNum, userId, payType, paidRecord, order, channel);
        if (flow == null) {
            return new PayMsg(PayMsg.failure_code, "保存支付记录失败.");
        }
        return msg;
    }

    /**
     * 保存支付记录, 如果支付记录存在,支付次数+1
     *
     * @param order
     * @return
     */
    private PayFlow savePayFlow(String orderNum, String userId, String payType, PayFlow payFlow, OrderVo order, String channel) {
        try {
            if (payFlow == null) {
                //支付金额
                BigDecimal payAmount = order.getTotalMount();
                //话费充值使用积分支付计算手续费s
                BigDecimal serviceCharge = order.getServiceCharge();
                if (PayFlowService.PAYTYPE_DOOOLY.equals(payType) && serviceCharge != null && OrderService.ProductType.MOBILE_RECHARGE.getCode() == order.getProductType()) {
                    payAmount = payAmount.add(order.getServiceCharge());
                }
                // 新增支付记录
                PayFlow flow = new PayFlow();
                flow.setOrderNumber(orderNum);
                flow.setPayType(payType);
                flow.setUserId(userId);
                flow.setAmount(payAmount);
                flow.setPayStatus(PayFlowService.TRADING);
                flow.setPaySumbitTime(new Date());
                flow.setPayCount(1);
                flow.setChannel(channel);
                logger.info("flow = {}", flow);
                int rows = payFlowService.insert(flow);
                return rows == 0 ? null : flow;
            } else {
                // 修改支付次数
                PayFlow flow = new PayFlow();
                flow.setPayCount(payFlow.getPayCount() + 1);
                flow.setId(payFlow.getId());
                int rows = payFlowService.update(flow);
                return rows > 0 ? payFlowService.getById(String.valueOf(payFlow.getId())) : null;
            }
        } catch (Exception e) {
            logger.error("savePayFlow e = {}", e);
        }
        return null;
    }

    @Override
    public ResultModel dooolyPayCallback(JSONObject retJson) {
        String merchantOrderNo = retJson.getString("merchantOrderNo");//商户订单号
        PayFlow payFlow = payFlowDao.getByOrderNum(merchantOrderNo, null, null);
        JSONObject json = new JSONObject();
        json.put("payFlowId", payFlow.getId());
        json.put("orderNum", merchantOrderNo);
        json.put("integralPayStatus", retJson.getString("integralPayStatus"));
        json.put("payAmount", retJson.getString("payAmount"));
        json.put("payType", retJson.getString("payType"));
        json.put("realPayType", retJson.getString("realPayType"));
        json.put("code", MessageDataBean.success_code);
        PayMsg payMsg = payCallback(PayFlowService.PAYTYPE_CASHIER_DESK, PaymentService.CHANNEL_WECHAT, json.toJSONString());
        return new ResultModel(Integer.parseInt(payMsg.getCode()), payMsg.getMess());
    }

    @Override
    public ResultModel getPayResult(JSONObject json) {
        String payType = PayFlowService.PAYTYPE_CASHIER_DESK;
        String orderNum = json.getString("orderNum");
        OrderVo order = orderService.getByOrderNum(orderNum);
        Integer isSource = order.getIsSource();
        //自营商品全是兜礼支付
        ResultModel payMsg = null;
        //调用支付平台查询API,查询结果并处理支付记录
        AdBusiness business = adBusinessDao.getById(String.valueOf(order.getBussinessId()));
        // 积分支付查询新接口
        JSONObject param = new JSONObject();
        param.put("merchantOrderNo", orderNum);
        param.put("nonceStr", RandomStringUtils.random(6, "123456789zxc"));
        param.put("id", business.getId());
        param.put("businessId", business.getBusinessId());
        if (isSource == 3) {
            //说明是自营订单
            PayFlow flow = payFlowService.getByOrderNum(orderNum, payType, null);
            if (flow == null) {
                return new ResultModel(GlobalResultStatusEnum.SELECT_DATA_INFO, "未找到支付记录.");
            }
            if (PayFlowService.PAYMENT_SUCCESS.equals(flow.getPayStatus())) {
                //得到支付平台通知并已经处理过支付结果, 直接返回结果
                payMsg = ResultModel.ok();
            } else {
                payMsg = queryNewPayResult(param);
                if (payMsg.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                    //说明支付成功处理结果
                    JSONObject retJson = new JSONObject();
                    retJson.put("code", GlobalResultStatusEnum.SUCCESS.getCode());
                    retJson.put("orderNum", orderNum);
                    retJson.put("payFlowId", flow.getId());
                    //retJson.put("integralPayStatus", retJson.getString("integralPayStatus"));
                    //retJson.put("payAmount", retJson.getString("payAmount"));
                    //retJson.put("payType", retJson.getString("payType"));
                    //retJson.put("realPayType", retJson.getString("realPayType"));
                    retJson.put("code", MessageDataBean.success_code);
                    payCallback(PayFlowService.PAYTYPE_CASHIER_DESK, PaymentService.CHANNEL_WECHAT, json.toJSONString());
                }
            }
        } else {
            payMsg = queryNewPayResult(param);
        }
        // 跳转支付结果页面需要数据
        if (payMsg != null && GlobalResultStatusEnum.SUCCESS.getCode() == payMsg.getCode()) {
            List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
            if (!CollectionUtils.isEmpty(orders)) {
                OrderVo order1 = orders.get(0);
                Map<String, Object> map = new HashMap<>();
                map.put("orderNum", order1.getOrderNumber());
                map.put("orderId", order.getOrderId());
                map.put("oid", order.getId());
                map.put("totalAmount", order.getTotalMount());
                //最终支付结果code
                map.put("code", payMsg.getCode());
                //手续费
                if (order.getServiceCharge() != null) {
                    map.put("serviceCharge", order.getServiceCharge());

                }
                //话费优惠活动- 分享需要的参数
                if (OrderService.ProductType.MOBILE_RECHARGE_PREFERENCE.getCode() == order.getProductType()) {
                    AdRechargeRecord record = adRechargeRecordDao.getRecordByOrderNumber(order.getOrderNumber());
                    map.put("openId", record.getOpenId());
                    map.put("activityParam", record.getActivityParam());
                }
                payMsg.setData(map);
            }
        }
        return payMsg;
    }

    @Override
    public ResultModel queryNewPayResult(JSONObject param) {
        String id = param.getString("id");//商户表主键
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(id);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param);
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_QUERY_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            //说明回调成功
            String payStatus = String.valueOf(data.get("payStatus"));
            if (PayConstants.PAY_STATUS_1.equals(payStatus)) {
                //说明支付成功处理结果
                return new ResultModel(GlobalResultStatusEnum.PAY_STATUS_SUCCESS, data);
            } else if (PayConstants.PAY_STATUS_2.equals(payStatus)) {
                return new ResultModel(GlobalResultStatusEnum.PAY_STATUS_FAIL, data);
            } else {
                return new ResultModel(GlobalResultStatusEnum.PAY_STATUS_NON, data);
            }
        } else {
            return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"));
        }
    }

    /***
     * 处理回调结果
     * @param payType
     * @param retStr
     * @return
     */
    private PayMsg payCallback(String payType, String channel, String retStr) {
        PaymentService paymentService = PaymentServiceFactory.getPayService(payType);
        logger.info("paymentService = {}", paymentService);
        if (paymentService != null) {
            return paymentService.handlePayResult(retStr, channel);
        }
        return new PayMsg(PayMsg.failure_code, "invalied payType=" + payType);
    }

    /**
     * 准备支付参数
     *
     * @param param
     * @return
     */
    private PayMsg prePayNew(JSONObject param) {
        // 校验订单
        String orderNum = param.getString("orderNum");
        List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
        if (CollectionUtils.isEmpty(orders)) {
            return new PayMsg(PayMsg.failure_code, "没有找到订单");
        }
        // 订单状态
        OrderVo order = orders.get(0);
        Long oid = checkOrderStatus(order);
        if (oid != null) {
            return new PayMsg(PayMsg.failure_code, "无效的订单状态");
        }
        //校验是否可以支付
        return canPay(order, param);
    }

    private PayMsg canPay(OrderVo order, JSONObject json) {
        logger.info("productType={}", order.getProductType());
        AdUser user = adUserDao.getById(order.getUserId().intValue());
        if (!StringUtils.isEmpty(order.getActType())) {
            //是否普通订单
            if (!OrderService.ActivityType.COMMON_ORDER.getActType().equals(order.getActType())) {
                //若果活动结束或者下架,未完成的订单不能支付
                OrderItemVo item = order.getItems().get(0);
                long userId = order.getUserId();
                String str[] = item.getProductSkuId().split("-");
                int skuId = Integer.valueOf(str[1]);
                ActivityInfo actInfo = orderService.getActivityInfo(String.valueOf(user.getGroupNum()), skuId);
                logger.info("canPay() actInfo = {},actType = {}", actInfo, order.getActType());
                if (actInfo != null) {
                    if (actInfo.getActivityName().equals(order.getActType())) {
                        //用户限购数量
                        String activityName = actInfo.getActivityName();
                        Integer actLimitNum = actInfo.getBuyNumberLimit();
                        int buyQuantity = item.getNumber().intValue();
                        int byNum = orderService.getBuyNum(userId, skuId, activityName);
                        logger.info("canPay() actLimitNum = {},byNum + buyQuantity = {}", actLimitNum, byNum + buyQuantity);
                        if (actLimitNum != null && actLimitNum < byNum + buyQuantity) {
                            return new PayMsg(OrderMsg.purchase_limit_code, String.format("此商品每个账号仅限购买%s次", actLimitNum));
                        }
                    } else {
                        //找到的活动与订单活动不匹配
                        logger.info("活动与订单活动不匹配");
                        return new PayMsg(OrderMsg.end_activity_code, OrderMsg.end_activity_mess);
                    }
                } else {
                    //未找到活动
                    return new PayMsg(OrderMsg.end_activity_code, OrderMsg.end_activity_mess);
                }
            }
        }
        Integer payType = json.getInteger("payType");
        if (order.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode()
                && (payType ==0 || payType ==2)) {
            //获取已使用额度
            AdRechargeConf conf = adRechargeConfDao.getRechargeConf(String.valueOf(user.getGroupNum()));
            logger.info("conf = {}", conf);
            if (conf == null) {
                return new PayMsg(OrderMsg.failure_code, "没有找到话费充值配置");
            }
            //每月限制
            BigDecimal monthLimit = conf.getMonthLimit();
            //每月优惠额度
            BigDecimal discountsMonthLimit = conf.getDiscountsMonthLimit();
            if(discountsMonthLimit == null){
                discountsMonthLimit = new BigDecimal("0");
            }
            //每月积分购买话费的金额
            BigDecimal consumptionAmount = adOrderReportDao.getConsumptionAmount(order.getUserId());
            if (consumptionAmount == null) {
                consumptionAmount = new BigDecimal("0");
            }
            BigDecimal subtract = consumptionAmount.add(discountsMonthLimit).subtract(monthLimit);
            if(subtract.compareTo(BigDecimal.ZERO)>=0){
                //说明已经超出限额
                return new PayMsg(OrderMsg.failure_code, "您已超出每月限额");
            }
        }
        //构建收银台接口需要参数
        JSONObject param = new JSONObject();
        param.put("businessId", order.getBussinessId());
        param.put("isPayPassword", user.getIsPayPassword());
        PayMsg payMsg = new PayMsg(OrderMsg.valid_pass_code, OrderMsg.valid_pass__mess);
        payMsg.setJsonData(param);
        return payMsg;
    }

    /**
     * 校验订单是否为待支付状态
     *
     * @param order
     * @return
     */
    private Long checkOrderStatus(OrderVo order) {
        if (order.getType() != OrderService.OrderStatus.NEED_TO_PAY.getCode()) {
            return order.getOrderId();
        }
        return null;
    }

    @Override
    public ResultModel getTradeInfo(JSONObject param) {
        String businessId = param.getString("businessId");
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param);
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.GET_PAYFROM_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            return ResultModel.ok(data);
        } else {
            return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"));
        }
    }

    @Override
    public ResultModel integralPay(JSONObject param) {
        String businessId = param.getString("businessId");
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param);
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.INTEGRAL_PAY_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            return ResultModel.ok(data);
        } else {
            return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"));
        }
    }

    @Override
    public ResultModel dooolyPayRefund(JSONObject param) {
        String id = param.getString("id");
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(id);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        parameters.put("client_id", clientId);
        parameters.put("timestamp", timestamp);
        parameters.put("access_token", accessToken);
        parameters.put("param", param);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param);
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_REFUND_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            String refundStatus = String.valueOf(data.get("refundStatus"));
            if (PayConstants.REFUND_STATUS_S.equals(refundStatus)) {
                //说明退款成功处理结果
                return new ResultModel(GlobalResultStatusEnum.REFUND_STATUS_SUCCESS, data);
            } else if (PayConstants.REFUND_STATUS_F.equals(refundStatus)) {
                return new ResultModel(GlobalResultStatusEnum.REFUND_STATUS_FAIL, data);
            } else {
                return new ResultModel(GlobalResultStatusEnum.REFUND_STATUS_NON, data);
            }
        } else {
            return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"));
        }
    }

    @Override
    public ResultModel dooolyRefundCallback(JSONObject json) {
        String code = json.getString("code");
        String info = json.getString("info");
        JSONObject param = json.getJSONObject("param");
        String merchantOrderNo = json.getString("merchantOrderNo");
        String outRefundNo = json.getString("outRefundNo");
        String merchantRefundNo = json.getString("merchantRefundNo");
        String refundStatus = json.getString("refundStatus");
        AdRefundFlow flow = adRefundFlowDao.selectByPrimaryKey(Long.valueOf(merchantRefundNo));
        if (refundStatus.equals(flow.getRefundStatus())) {
            //相等说明处理过了
            return ResultModel.ok();
        } else if (refundStatus.equals(REFUND_STATUS_S)) {
            //说明退款成功
            OrderVo order = checkOrderStatus(merchantOrderNo);
            int u1 = updateRefundFlow(String.valueOf(flow.getId()), outRefundNo, REFUND_STATUS_S, code, info);
            // 修改订单状态-已退款
            int u2 = orderService.updateOrderRefund(order, String.valueOf(order.getUserId()));
            //积分退款要修改businessId一致
            updateBusinessId(order);
            //修改退货记录表
            AdReturnFlow adReturnFlow = returnFlowService.getByOrderId(order.getOrderId());
            adReturnFlow.setType("1");
            returnFlowService.updateByPrimaryKeySelective(adReturnFlow);
            if (u1 > 0 && u2 > 0) {
                //退款后处理
                afterRefundProcess(order);
                return ResultModel.ok();
            } else {
                logger.error("u1 = {},u2 = {}", u1, u2);
                return ResultModel.error(GlobalResultStatusEnum.SIGN_VALID_ERROR);
            }
        } else if (refundStatus.equals(REFUND_STATUS_I)) {
            //退款失败更新状态值
            updateRefundFlow(String.valueOf(flow.getId()), outRefundNo, REFUND_STATUS_I, code, info);
            logger.info("autoRefund error! orderNum = {}", merchantOrderNo);
        }
        return ResultModel.ok();
    }


    @Override
    public ResultModel refund(JSONObject json) {
        String userId = json.getString("userId");
        String orderNum = json.getString("orderNum");
        PayMsg payMsg = refundService.autoRefund(Long.parseLong(userId), orderNum);
        return new ResultModel(Integer.parseInt(payMsg.getCode()),payMsg.getMess());
    }

    private int updateRefundFlow(String refundFlowId, String refundId, String refundStatus, String errCode, String errReason) {
        try {
            AdRefundFlow flow = new AdRefundFlow();
            flow.setId(Long.valueOf(refundFlowId));
            flow.setRefundStatus(refundStatus);
            flow.setRefundId(refundId);
            flow.setErrorCode(errCode);
            flow.setErrorReason(errReason);
            return adRefundFlowDao.updateByPrimaryKeySelective(flow);
        } catch (Exception e) {
            logger.error("updateRefundFlow error,e = {}", e);
        }
        return 0;
    }

    private OrderVo checkOrderStatus(String orderNum) {
        try {
            OrderVo o = new OrderVo();
            o.setOrderNumber(orderNum);
            o.setType(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
            o.setState(OrderService.PayState.PAID.getCode());
            return orderService.getOrder(o).get(0);
        } catch (Exception e) {
            logger.error("checkOrderStatus() error = {},orderNum = {}", e, orderNum);
        }
        return null;
    }

    private int updateBusinessId(OrderVo order) {
        try {
            AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
            int u4 = orderDao.updateBussinessIdByNum(order.getOrderNumber(), business.getBusinessId());
            logger.info("updateBussinessIdByNum u4 = {}", u4);
            return u4;
        } catch (Exception e) {
            logger.error("updateBusinessId() error = {},orderNum = {}", e, order.getOrderNumber());
        }
        return 0;
    }

    /**
     * 退款成功后执行处理器
     *
     * @param order
     */
    private void afterRefundProcess(OrderVo order) {
        List<AfterRefundProcessor> afterPayProcessors = AfterRefundProcessorFactory.getAllProcessors();
        for (AfterRefundProcessor afterRefundProcessor : afterPayProcessors) {
            logger.info("afterRefundProcess() afterRefundProcessor = {}", afterRefundProcessor);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.error("执行afterRefundProcess class = {}", afterRefundProcessor);
                    afterRefundProcessor.process(order);
                }
            }).start();
        }
    }
}