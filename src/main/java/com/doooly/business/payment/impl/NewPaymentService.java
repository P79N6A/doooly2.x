package com.doooly.business.payment.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.business.AdBusinessServiceI;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.myorder.service.MyOrderServiceI;
import com.doooly.business.order.service.AdOrderReportServiceI;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.AdOrderBig;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.bean.PayTypeEnum;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessor;
import com.doooly.business.pay.processor.refundprocessor.AfterRefundProcessorFactory;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.pay.service.PaymentService;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.pay.service.impl.PaymentServiceFactory;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.constants.PayConstants;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.business.payment.utils.SignUtil;
import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.business.recharge.AdRechargeConfServiceI;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.RedisLock;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.PaymentConstants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.common.util.RandomUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.payment.PayRecordMapper;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessExpandInfoDao;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dao.reachad.AdRechargeConfDao;
import com.doooly.dao.reachad.AdRechargeRecordDao;
import com.doooly.dao.reachad.AdReturnDetailDao;
import com.doooly.dao.reachad.AdReturnFlowDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.common.OrderMsg;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.payment.PayRecordDomain;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdRechargeConf;
import com.doooly.entity.reachad.AdRechargeRecord;
import com.doooly.entity.reachad.AdReturnFlow;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.Order;
import com.doooly.entity.reachad.OrderDetail;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.doooly.business.pay.service.RefundService.REFUND_STATUS_S;
import static com.koalii.bc.asn1.x509.X509ObjectIdentifiers.id;
import static com.sun.tools.doclint.Entity.nu;
import static com.sun.tools.doclint.Entity.or;

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
    protected PayFlowService payFlowService;
    @Autowired
    protected AdRechargeRecordDao adRechargeRecordDao;
    @Autowired
    private MallBusinessService mallBusinessService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private AdReturnFlowDao adReturnFlowDao;
    @Autowired
    private AdReturnDetailDao adReturnDetailDao;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AdOrderReportServiceI adOrderReportServiceI;
    @Autowired
    private AdUserServiceI adUserServiceI;
    @Autowired
    private AdRechargeConfServiceI adRechargeConfServiceI;
    @Autowired
    private AdBusinessServiceI adBusinessServiceI;
    @Autowired
    private MyOrderServiceI myOrderServiceI;
    @Autowired
    private PayRecordMapper payRecordMapper;

    // 退款同步，唯一标识，放入缓存；如未领取设置值为4个0（0000），如已领取直接返回缓存值；
    private static String SYNC_REFUND_CODE_KEY = "sync_refund_code:%s";
    // 退款同步，唯一标识，缓存值4个0（0000）
    private static String SYNC_REFUND_CODE_VALUE = "0000";
    // 支付同步，key；
    private static String SYNC_PAY_CODE_KEY = "sync_pay_code:%s";

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
        String orderNum = jsonObject.getString("orderNum");
        JSONObject orderSummary = getOrderSummary(jsonObject);
        if (orderSummary == null) {
            return new ResultModel(GlobalResultStatusEnum.FAIL, "登录用户和下单用户不匹配");
        }
        logger.info("订单参数=======orderSummary========" + orderSummary.toJSONString());
        JSONObject param = orderSummary.getJSONObject("param");
        JSONObject retJson = orderSummary.getJSONObject("retJson");//页面展示数据集合
        String businessId = orderSummary.getString("businessId");//从返回参数获取商户id来查询扩展信息
        //改造缓存 === zhangqing 20181227
        //AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        logger.info("下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = this.authorize(businessId);
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
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.UNIFIED_ORDER_URL);
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonResult.get("data");
            String payId = (String) data.get("payId");
            String integralRebatePayAmount = (String) data.get("integralRebatePayAmount");
            retJson.put("payId", payId);
            retJson.put("payMethod",adBusinessExpandInfo.getPayMethod());
            if (StringUtils.isNotBlank(integralRebatePayAmount)) {
                retJson.put("integralRebatePayAmount", integralRebatePayAmount);
            }
            //获取跳转链接
            PayRecordDomain payRecordDomain = new PayRecordDomain();
            payRecordDomain.setMerchantOrderNo(orderNum);
            payRecordDomain = payRecordMapper.getPayRecordDomain(payRecordDomain);
            if(payRecordDomain != null && StringUtils.isNotBlank(payRecordDomain.getRedirectUrl())){
                retJson.put("redirectUrl", payRecordDomain.getRedirectUrl());
            }else {
                retJson.put("redirectUrl", "");
            }
            logger.info("payment unifiedorder result data={}", data);
            return ResultModel.ok(retJson);
        } else {
            return ResultModel.error(GlobalResultStatusEnum.SIGN_VALID_ERROR);
        }
    }

    @Override
    public ResultModel unifiedorderV2(JSONObject jsonObject) {
        JSONObject orderSummary = getOrderSummaryV2(jsonObject);
        if (orderSummary == null) {
            return new ResultModel(GlobalResultStatusEnum.FAIL, "登录用户和下单用户不匹配");
        }
        logger.info("订单参数=======orderSummary========" + orderSummary.toJSONString());
        JSONObject param = orderSummary.getJSONObject("param");
        JSONObject retJson = orderSummary.getJSONObject("retJson");//页面展示数据集合
        String businessId = orderSummary.getString("businessId");//从返回参数获取商户id来查询扩展信息
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        logger.info("下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = this.authorize(businessId);
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
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.UNIFIED_ORDER_URL);
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonResult.get("data");
            String payId = (String) data.get("payId");
            String integralRebatePayAmount = (String) data.get("integralRebatePayAmount");
            retJson.put("payId", payId);
            retJson.put("payMethod",adBusinessExpandInfo.getPayMethod());
            if (StringUtils.isNotBlank(integralRebatePayAmount)) {
                retJson.put("integralRebatePayAmount", integralRebatePayAmount);
            }
            logger.info("payment unifiedorder v2 result data={}", data);
            return ResultModel.ok(retJson);
        } else {
            return new ResultModel(jsonResult.getInteger("code"),jsonResult.getString("info"));
        }
    }

    private JSONObject getOrderSummaryV2(JSONObject json) {
        logger.info("getOrderSummaryV2() json = {}", json);
        JSONObject result = new JSONObject();
        String orderNum = json.getString("orderNum");//订单号
        String redirectUrl = json.getString("redirectUrl");//支付成功跳转链接
        long userId = json.getLong("userId");
        String bigOrderNumber;//大订单号
        String businessId = WebService.BUSINESSID;//商户编号
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNum);
        order.setUserId(userId);
        List<OrderVo> orderVos = new ArrayList<>();
        OrderVo orderLimt = adOrderReportServiceI.getOrderLimt(order);
        if(orderNum.contains("N")){
            //说明是自营子订单
            bigOrderNumber = String.valueOf(orderLimt.getBigOrderNumber());
            order.setBigOrderNumber(bigOrderNumber);
            businessId = orderLimt.getBussinessBussinessId();
        }else {
            bigOrderNumber = orderNum;
        }
        //查询大订单
        AdOrderBig adOrderBig = new AdOrderBig();
        adOrderBig.setId(bigOrderNumber);
        adOrderBig = adOrderReportServiceI.getAdOrderBig(adOrderBig);
        //查询子订单
        if(adOrderBig == null){
            //自营没有大订单下单
            adOrderBig = new AdOrderBig();
            adOrderBig.setId(orderNum);
            adOrderBig.setIsSource("3");
            adOrderBig.setTotalPrice(orderLimt.getTotalPrice());
            adOrderBig.setTotalAmount(orderLimt.getTotalMount());
            adOrderBig.setOrderDate(orderLimt.getOrderDate());
            orderVos.add(orderLimt);
        }else if(orderNum.contains("N")){
            //兜礼子订单
            orderVos.add(orderLimt);
        }else {
            //大订单
            order.setBigOrderNumber(bigOrderNumber);
            order.setIsSource(Integer.parseInt(adOrderBig.getIsSource()));
            orderVos = adOrderReportServiceI.getOrders(order);
        }
        if(!CollectionUtils.isEmpty(orderVos) && orderVos.size()==1){
            businessId = String.valueOf(orderVos.get(0).getBussinessBussinessId());
        }
        AdUser paramUser = new AdUser();
        paramUser.setId(userId);
        AdUser user = adUserServiceI.getUser(paramUser);
        String dirIntegral = getDirIntegral(orderVos,adOrderBig,user);
        JSONObject retJson = new JSONObject();
        retJson.put("totalFree",adOrderBig.getTotalAmount().toString());
        retJson.put("dirIntegral",String.valueOf(dirIntegral));//定向积分
        getServiceCharge(orderVos,retJson,user);
        //组建预支付订单参数
        String price = String.valueOf(adOrderBig.getTotalPrice().setScale(2, BigDecimal.ROUND_DOWN));
        String amount = String.valueOf(adOrderBig.getTotalAmount().setScale(2, BigDecimal.ROUND_DOWN));
        JSONObject param = new JSONObject();
        param.put("cardNumber", user.getTelephone());
        param.put("businessId", businessId);
        param.put("merchantOrderNo", orderNum);
        param.put("bigOrderNumber", adOrderBig.getId());
        param.put("tradeType", "DOOOLY_JS");
        param.put("notifyUrl", PaymentConstants.PAYMENT_NOTIFY_URL);
        param.put("redirectUrl", redirectUrl);
        param.put("body", "兜礼订单-"+adOrderBig.getId());
        param.put("isSource", adOrderBig.getIsSource());
        param.put("orderDate", DateUtils.formatDateTime(adOrderBig.getOrderDate()));
        param.put("storesId", "A001");
        param.put("price", price);
        param.put("amount", amount);
        param.put("clientIp", json.get("clientIp"));
        param.put("nonceStr", RandomUtil.getRandomStr(16));
        param.put("isPayPassword", user.getIsPayPassword());
        logger.info("下单参数param=========" + param);
        result.put("param", param);
        retJson.put("userIntegral", user.getIntegral());
        retJson.put("isPayPassword", user.getIsPayPassword());
        logger.info("retJson = {}", retJson);
        result.put("retJson", retJson);
        result.put("businessId", businessId);
        return result;
    }

    public String getDirIntegral( List<OrderVo> orderVos ,AdOrderBig adOrderBig,AdUser adUser) {
        //String addIntegralAuthorizationUrl = configManager.getWsUrl() + RestConstants.CHECK_INTEGRAL_CONSUMPTION_URL_V2;
        String getDirIntegralUrl =WebService.WEBURL+"services/rest/getDirIntegral/V2";
        //String addIntegralAuthorizationUrl ="http://localhost:8012/api/services/rest/checkIntegralConsumption/V2";
        JSONObject params = new JSONObject();
        params.put("businessId", WebService.BUSINESSID);
        params.put("dirIntegralSwitch", "1");
        params.put("totalAmount", String.valueOf(adOrderBig.getTotalAmount()));
        params.put("payPassword", adUser.getIsPayPassword());
        params.put("verificationCode", "111");
        params.put("cardNumber", adUser.getCardNumber());
        params.put("bigOrderNumber", adOrderBig.getId());
        JSONArray subOrders = new JSONArray();
        //List<JSONObject> subOrders = new ArrayList<>();
        for (OrderVo order : orderVos) {
            JSONObject js = new JSONObject();
            js.put("amount",String.valueOf(order.getTotalMount()));
            js.put("price",String.valueOf(order.getTotalPrice()));
            js.put("storesId","001");
            js.put("orderDate", DateUtil.formatDate(order.getOrderDate(),"yyyy-MM-dd hh:mm:ss"));
            js.put("orderNumber",order.getOrderNumber());
            js.put("serialNumber",order.getOrderNumber());
            js.put("businessId",order.getBussinessBussinessId());
            List<OrderItemVo> items = order.getItems();
            JSONArray array = new JSONArray();
            for (OrderItemVo adOrderDetailDomain : items) {
                BigDecimal tax = adOrderDetailDomain.getTax() == null ? new BigDecimal("0"): adOrderDetailDomain.getTax();
                array.add(createGoods(adOrderDetailDomain.getCode(), adOrderDetailDomain.getGoods(), adOrderDetailDomain.getNumber().toString(), adOrderDetailDomain.getPrice().toString(), adOrderDetailDomain.getCategoryId(), adOrderDetailDomain.getAmount().toString(), tax.toEngineeringString()));
            }
            js.put("orderDetails",array);
            subOrders.add(js);
        }
        params.put("subOrders",subOrders);
        logger.info("查询可用定向积分V2，参数{}",params.toJSONString());
        String resposeResult = HTTPSClientUtils.sendPost(params, getDirIntegralUrl);
        logger.info("查询可用定向积分V2，结果{}",resposeResult);
        JSONObject resultJson = JSON.parseObject(resposeResult);
        String dirIntegral1 = "0";
        if(resultJson!= null && resultJson.getString("code").equals("0")){
            dirIntegral1 = resultJson.getString("dirIntegral");
        }
        return dirIntegral1;
    }

    // 构造商品JSON
    public static JSONObject createGoods(String code, String goods, String number, String price, String category,
                                         String amount, String tax) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("goods", goods);
        json.put("number", number);
        json.put("price", price);
        json.put("category", category);
        json.put("amount", amount);
        json.put("tax", tax);
        // json.put("firstCategory", "服饰");
        // json.put("brandName", "李宁");
        return json;
    }

    private String createOrderDetailForVerificationCode18() {
        JSONArray array = new JSONArray();
        JSONObject orderDetail = new JSONObject();
        orderDetail.put("code", "1001");
        orderDetail.put("goods", "获取验证码");
        orderDetail.put("number", "1");
        orderDetail.put("price", "0");
        orderDetail.put("category", "0");
        orderDetail.put("amount", "0");
        orderDetail.put("tax", "0");
        array.add(orderDetail);
        return array.toString();
    }

    //获取下单商品sku集合
    private List<String> getSkus(List<OrderVo> orderVos) {
        List<String> skus = new ArrayList<>();
        for (OrderVo orderVo : orderVos) {
            List<OrderItemVo> items = orderVo.getItems();
            for (OrderItemVo item : items) {
                skus.add(item.getProductSkuId().split("-")[1]);
            }
        }
        return skus;
    }

    //计算手续费和已用额度
    private void getServiceCharge(List<OrderVo> orderVos, JSONObject retJson, AdUser user) {
        BigDecimal serviceCharge = new BigDecimal("0");
        for (OrderVo orderVo : orderVos) {
            if(orderVo.getProductType()==OrderService.ProductType.MOBILE_RECHARGE.getCode() && orderVo.getServiceCharge() != null){
                serviceCharge = serviceCharge.add(orderVo.getServiceCharge());
            }
        }
        retJson.put("serviceCharge",serviceCharge);
        //每月积分购买话费的金额
        Map<String, Object> paramOrderMap = new HashMap<>();
        paramOrderMap.put("userId", user.getId());
        BigDecimal consumptionAmount = adOrderReportServiceI.getConsumptionAmountByMap(paramOrderMap);
        retJson.put("consumptionAmount", consumptionAmount == null ? "0" : consumptionAmount);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("groupId", user.getGroupNum());
        AdRechargeConf conf = adRechargeConfServiceI.getRechargeConf(paramMap);
        retJson.put("monthLimit", (conf == null || conf.getMonthLimit() == null) ? "0" : conf.getMonthLimit().toString());
        retJson.put("consumptionAmount",consumptionAmount);
    }

    /**
     * 饿了么下单
     * @param jsonObject
     * @return
     */
    @Override
    public ResultModel unifiedElmorder(JSONObject jsonObject) {
        JSONObject orderSummary = getElmOrderSummary(jsonObject);
        if (orderSummary == null) {
            return new ResultModel(GlobalResultStatusEnum.FAIL, "系统异常");
        }
        logger.info("订单参数=======orderSummary========" + orderSummary.toJSONString());
        JSONObject param = orderSummary.getJSONObject("param");
        JSONObject retJson = orderSummary.getJSONObject("retJson");//页面展示数据集合
        String businessId = orderSummary.getString("businessId");//从返回参数获取商户id来查询扩展信息
        //改造缓存 === zhangqing 20181227
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        logger.info("下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = this.authorize(businessId);
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
        logger.info(sign);
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", param.toJSONString());
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.UNIFIED_ORDER_URL);
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            return ResultModel.success_ok(retJson);
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
    private JSONObject getElmOrderSummary(JSONObject json) {
        logger.info("getElmOrderSummary() json = {}", json);
        JSONObject result = new JSONObject();
        String orderNo = json.getString("orderNo");
        long businessId = json.getLong("businessId");
        String bNo = json.getString("bNo");
        JSONArray foodsInfo = json.getJSONArray("foodsInfo");
        JSONArray orderExtras = json.getJSONArray("orderExtras");
        JSONObject foodsInfo1 = foodsInfo.getJSONObject(0);
        AdUser paramUser = new AdUser();
        paramUser.setCardNumber(bNo);
        AdUser user = adUserServiceI.getUser(paramUser);
        //组建预支付订单参数
        AdBusiness paramBusiness = new AdBusiness();
        paramBusiness.setId(businessId);
        AdBusiness business = adBusinessServiceI.getBusiness(paramBusiness);
        String price = json.getString("totalFee");
        String amount = json.getString("totalFee");
        JSONObject param = new JSONObject();
        param.put("businessId", business.getBusinessId());//商户编号
        param.put("cardNumber", user.getTelephone());
        param.put("merchantOrderNo", orderNo);
        param.put("tradeType", "DOOOLY_JS");
        param.put("notifyUrl", PaymentConstants.PAYMENT_NOTIFY_URL);
        param.put("body", foodsInfo1.getString("foodName"));
        param.put("isSource", "0");
        param.put("orderDate", DateUtils.formatDateTime(new Date()));
        param.put("storesId", "A001");
        param.put("price", price);
        param.put("amount", amount);
        param.put("clientIp", json.get("clientIp"));
        param.put("nonceStr", RandomUtil.getRandomStr(16));
        param.put("isPayPassword", user.getIsPayPassword());
        JSONArray jsonArray = new JSONArray();
        //商品信息
        for (Object jsonObject : foodsInfo) {
            JSONObject params = (JSONObject) jsonObject;
            JSONObject jsonDetail = new JSONObject();
            jsonDetail.put("code",params.getString("foodId"));
            jsonDetail.put("goods", params.getString("foodName"));
            jsonDetail.put("number", params.getString("count"));
            jsonDetail.put("price", params.getString("price"));
            jsonDetail.put("category", "0000");
            jsonDetail.put("tax", "0");
            jsonDetail.put("amount", params.getString("price"));
            jsonArray.add(jsonDetail);
        }
        //拓展信息
        for (Object orderExtra : orderExtras) {
            JSONObject params = (JSONObject) orderExtra;
            JSONObject jsonDetail = new JSONObject();
            jsonDetail.put("code",params.getString("categoryId"));
            jsonDetail.put("goods", params.getString("name"));
            jsonDetail.put("number", params.getString("quantity"));
            jsonDetail.put("price", params.getString("price"));
            jsonDetail.put("category", "0000");
            jsonDetail.put("tax", "0");
            jsonDetail.put("amount", params.getString("price"));
            jsonArray.add(jsonDetail);
        }
        param.put("orderDetail", jsonArray.toJSONString());
        logger.info("下单参数param=========" + param);
        result.put("param", param);
        result.put("businessId",business.getId());//商户id
        return result;
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
        OrderVo o = adOrderReportServiceI.getOrderLimt(order);
        if (o == null) {
            return null;
        }
        OrderItemVo item = o.getItems().get(0);
        String sku = item.getSku() != null ? item.getSku() : "";
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

        if ((o.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode()
                || o.getProductType() == OrderService.ProductType.NEXUS_RECHARGE_ACTIVITY.getCode()
        ) && o.getServiceCharge() != null) {
            retJson.put("serviceCharge", o.getServiceCharge().compareTo(BigDecimal.ZERO) == 0 ? null : o.getServiceCharge());
        }
        //用户缓存改造
        //AdUser user = adUserDao.getById(order.getUserId().intValue());
        AdUser paramUser = new AdUser();
        paramUser.setId(order.getUserId());
        AdUser user = adUserServiceI.getUser(paramUser);

        List<OrderVo> orderVos = Arrays.asList(o);
        AdOrderBig adOrderBig = new AdOrderBig();
        adOrderBig.setId(String.valueOf(o.getId()));
        adOrderBig.setTotalAmount(o.getTotalMount());
        String dirIntegral = getDirIntegral(orderVos,adOrderBig,user);
        retJson.put("dirIntegral",String.valueOf(dirIntegral));//定向积分

        //话费充值需要校验积分消费金额,用到此参数
        if (o.getProductType() == OrderService.ProductType.MOBILE_RECHARGE.getCode()
                || o.getProductType() == OrderService.ProductType.NEXUS_RECHARGE_ACTIVITY.getCode()) {
            //用户消费金额 优化改造20181227
            //BigDecimal consumptionAmount = adOrderReportDao.getConsumptionAmount(userId);
            //每月积分购买话费的金额
            Map<String, Object> paramOrderMap = new HashMap<>();
            paramOrderMap.put("userId", userId);
            BigDecimal consumptionAmount = adOrderReportServiceI.getConsumptionAmountByMap(paramOrderMap);
            retJson.put("consumptionAmount", consumptionAmount == null ? "0" : consumptionAmount);
            //AdRechargeConf conf = adRechargeConfDao.getRechargeConf(user.getGroupNum() + "");
            //20181227改造缓存---zhangqing
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("groupId", user.getGroupNum());
            AdRechargeConf conf = adRechargeConfServiceI.getRechargeConf(paramMap);
            retJson.put("monthLimit", (conf == null || conf.getMonthLimit() == null) ? "0" : conf.getMonthLimit().toString());
        }
        //组建预支付订单参数
        //20181227 缓存改造 ---zhangqing
        //AdBusiness business = adBusinessDao.getById(String.valueOf(o.getBussinessId()));
        AdBusiness paramBusiness = new AdBusiness();
        paramBusiness.setId(o.getBussinessId());
        AdBusiness business = adBusinessServiceI.getBusiness(paramBusiness);
        //AdUserIntegral userIntegral = userIntegralDao.getDirIntegralByUserId(Long.valueOf(userId));
        String price = String.valueOf(o.getTotalPrice().setScale(2, BigDecimal.ROUND_DOWN));
        String amount = String.valueOf(o.getTotalMount().setScale(2, BigDecimal.ROUND_DOWN));
        JSONObject param = new JSONObject();
        param.put("businessId", business.getBusinessId());//商户编号
        param.put("cardNumber", user.getTelephone());
        param.put("merchantOrderNo", o.getOrderNumber());
        param.put("tradeType", "DOOOLY_JS");
        param.put("notifyUrl", PaymentConstants.PAYMENT_NOTIFY_URL);
        param.put("expireTime", DateUtils.formatDateTime(DateUtils.add(o.getOrderDate(), Calendar.MINUTE, 15)));
        param.put("body", orderDesc);
        param.put("isSource", o.getIsSource());
        param.put("orderDate", DateUtils.formatDateTime(o.getOrderDate()));
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
        jsonDetail.put("price", item.getPrice());
        jsonDetail.put("category", item.getCategoryId());
        jsonDetail.put("tax", item.getTax());
        jsonDetail.put("amount", item.getAmount());
        jsonArray.add(jsonDetail);
        param.put("orderDetail", jsonArray.toJSONString());
        logger.info("下单参数param=========" + param);
        result.put("param", param);
        retJson.put("company", business.getCompany());
        retJson.put("userIntegral", user.getIntegral());
        retJson.put("isPayPassword", user.getIsPayPassword());
        if(Constants.GIFT_ORDER_TYPE.equals(o.getRemarks())){
            retJson.put("orderType","1");
        }
        logger.info("retJson = {}", retJson);
        result.put("retJson", retJson);
        result.put("businessId", o.getBussinessId());//商户id
        return result;
    }


    @Override
    public ResultModel getPayForm(JSONObject params) {
        //自营商品全是兜礼支付
        PayMsg payMsg = prePayNew(params);
        //为空或者校验失败直接返回错误信息
        if (payMsg != null && !payMsg.getCode().equals(OrderMsg.valid_pass_code)) {
            return new ResultModel(Integer.parseInt(payMsg.getCode()), payMsg.getMess());
        }
        JSONObject jsonData = payMsg.getJsonData();
        String businessId = jsonData.getString("businessId");
        String isPayPassword = jsonData.getString("isPayPassword");
        params.put("isPayPassword", isPayPassword);
        //AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        logger.info("下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = this.authorize(businessId);
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
        parameters.put("param", params.toJSONString());
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", params.toJSONString());
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
    public ResultModel getPayFormV2(JSONObject params) {
        //自营商品全是兜礼支付
        PayMsg payMsg = prePayNewV2(params);
        //为空或者校验失败直接返回错误信息
        if (payMsg != null && !payMsg.getCode().equals(OrderMsg.valid_pass_code)) {
            return new ResultModel(Integer.parseInt(payMsg.getCode()), payMsg.getMess());
        }
        String businessId = params.getString("businessId");
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
        long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String clientId = adBusinessExpandInfo.getClientId();
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
        logger.info("下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = this.authorize(businessId);
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
        parameters.put("param", params.toJSONString());
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        JSONObject object = new JSONObject();
        object.put("client_id", clientId);
        object.put("timestamp", timestamp);
        object.put("access_token", accessToken);
        object.put("param", params.toJSONString());
        object.put("sign", sign);
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.GET_PAYFROM_URL_V2);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            return ResultModel.ok(data);
        } else {
            return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"));
        }
    }

    private PayMsg prePayNewV2(JSONObject params) {
        String orderNum = params.getString("orderNum");//订单号
        long userId = params.getLong("userId");
        String bigOrderNumber;//大订单号
        String businessId = WebService.BUSINESSID;//商户编号
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNum);
        order.setUserId(userId);
        List<OrderVo> orderVos = new ArrayList<>();
        OrderVo orderLimt = adOrderReportServiceI.getOrderLimt(order);
        if(orderNum.contains("N")){
            //说明是自营子订单
            bigOrderNumber = String.valueOf(orderLimt.getBigOrderNumber());
            order.setBigOrderNumber(bigOrderNumber);
            businessId = orderLimt.getBussinessBussinessId();
        }else {
            bigOrderNumber = orderNum;
        }
        //查询大订单
        AdOrderBig adOrderBig = new AdOrderBig();
        adOrderBig.setId(bigOrderNumber);
        adOrderBig = adOrderReportServiceI.getAdOrderBig(adOrderBig);
        //查询子订单
        if(adOrderBig == null){
            //自营没有大订单下单
            adOrderBig = new AdOrderBig();
            adOrderBig.setId(orderNum);
            adOrderBig.setIsSource("3");
            adOrderBig.setTotalPrice(orderLimt.getTotalPrice());
            adOrderBig.setTotalAmount(orderLimt.getTotalMount());
            adOrderBig.setOrderDate(orderLimt.getOrderDate());
            orderVos.add(orderLimt);
        }else if(orderNum.contains("N")){
            //兜礼子订单
            orderVos.add(orderLimt);
        }else {
            //大订单
            order.setBigOrderNumber(bigOrderNumber);
            order.setIsSource(Integer.parseInt(adOrderBig.getIsSource()));
            orderVos = adOrderReportServiceI.getOrders(order);
        }
        PayMsg payMsg = null;
        for (OrderVo orderVo : orderVos) {
            payMsg = canPay(orderVo, params);
            //为空或者校验失败直接返回错误信息
            if (payMsg != null && !payMsg.getCode().equals(OrderMsg.valid_pass_code)) {
                return payMsg;
            }
        }
        JSONObject jsonData = payMsg.getJsonData();
        if(orderVos.size()==1){
            //说明子订单支付并且只有一个订单
            businessId = jsonData.getString("businessId");
        }
        String isPayPassword = jsonData.getString("isPayPassword");
        params.put("isPayPassword", isPayPassword);
        params.put("businessId", businessId);
        return payMsg;
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
    public ResultModel dooolyPayCallback(JSONObject resultJson) {
        logger.info("收银台支付回调通知结果:{}", resultJson);
        String client_id = resultJson.getString("client_id");
        String timestamp = resultJson.getString("timestamp");
        String param = resultJson.getString("param");
        String returnSign = resultJson.getString("sign");
        JSONObject retJson = JSONObject.parseObject(param);
        String businessId = retJson.getString("businessId");
        AdBusiness byBusinessId = adBusinessDao.getByBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(String.valueOf(byBusinessId.getId()));
        SortedMap<Object, Object> parameters = new TreeMap<>();
        parameters.put("client_id", client_id);
        parameters.put("timestamp", timestamp);
        parameters.put("param", param);
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
        logger.info("回传sign,{},解密sign,{},解密参数{}", returnSign, sign, parameters);
        if (!sign.equals(returnSign)) {
            return new ResultModel(GlobalResultStatusEnum.FAIL, "参数解密失败");
        }
        String merchantOrderNo = retJson.getString("merchantOrderNo");//商户订单号
        JSONObject json = new JSONObject();
        json.put("orderNum", merchantOrderNo);
        json.put("integralPayStatus", retJson.getString("integralPayStatus"));
        json.put("payAmount", retJson.getString("payAmount"));
        json.put("payType", retJson.getString("payType"));
        json.put("realPayType", retJson.getString("realPayType"));
        json.put("outTradeNo", retJson.getString("outTradeNo"));
        json.put("payEndTime", retJson.getString("payEndTime"));
        json.put("code", MessageDataBean.success_code);
        PayMsg payMsg = payCallback(PayFlowService.PAYTYPE_CASHIER_DESK, PaymentService.CHANNEL_WECHAT, json.toJSONString());
        return new ResultModel(Integer.parseInt(payMsg.getCode()), payMsg.getMess());
    }

    @Override
    public ResultModel dooolyPayCallbackV2(JSONObject resultJson) {
        logger.info("收银台支付回调通知结果V2:{}", resultJson);
        String param = resultJson.getString("param");
        JSONObject retJson = JSONObject.parseObject(param);
        String orderNum = retJson.getString("orderNum");//大订单号
        Long userId = retJson.getLong("userId");//大订单号
        String bigOrderNumber = "";//大订单号
        String businessId = WebService.BUSINESSID;//商户编号
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNum);
        order.setUserId(userId);
        List<OrderVo> orderVos = new ArrayList<>();
        OrderVo orderLimt = adOrderReportServiceI.getOrderLimt(order);
        if(orderNum.contains("N")){
            //说明是自营子订单
            bigOrderNumber = String.valueOf(orderLimt.getBigOrderNumber());
            order.setBigOrderNumber(bigOrderNumber);
            businessId = orderLimt.getBussinessBussinessId();
        }else {
            bigOrderNumber = orderNum;
        }
        //查询大订单
        AdOrderBig adOrderBig = new AdOrderBig();
        adOrderBig.setId(bigOrderNumber);
        adOrderBig = adOrderReportServiceI.getAdOrderBig(adOrderBig);
        //查询子订单
        if(adOrderBig == null){
            //自营没有大订单下单
            adOrderBig = new AdOrderBig();
            adOrderBig.setId(orderNum);
            adOrderBig.setIsSource("3");
            adOrderBig.setTotalPrice(orderLimt.getTotalPrice());
            adOrderBig.setTotalAmount(orderLimt.getTotalMount());
            adOrderBig.setOrderDate(orderLimt.getOrderDate());
            orderVos.add(orderLimt);
        }else if(orderNum.contains("N")){
            //兜礼子订单
            orderVos.add(orderLimt);
        }else {
            //大订单
            order.setBigOrderNumber(bigOrderNumber);
            order.setIsSource(Integer.parseInt(adOrderBig.getIsSource()));
            orderVos = adOrderReportServiceI.getOrders(order);
        }
        for (OrderVo orderVo : orderVos) {
            JSONObject json = new JSONObject();
            String orderNumber = orderVo.getOrderNumber();
            json.put("orderNum", orderNumber);
            json.put("integralPayStatus", retJson.getString("integralPayStatus"));
            json.put("payAmount", retJson.getString("payAmount"));
            json.put("payType", retJson.getString("payType"));
            json.put("realPayType", retJson.getString("realPayType"));
            json.put("outTradeNo", retJson.getString("outTradeNo"));
            json.put("payEndTime", retJson.getString("payEndTime"));
            json.put("code", MessageDataBean.success_code);
            PayMsg payMsg = payCallback(PayFlowService.PAYTYPE_CASHIER_DESK, PaymentService.CHANNEL_WECHAT, json.toJSONString());
            logger.info("收银台支付回调同步结果大订单号:{},子订单号:{},处理结果code:{},处理结果mess,{}",bigOrderNumber,orderNumber,payMsg.getCode(),payMsg.getMess());
        }
        return ResultModel.ok();
    }

    //取消商家订单超时未支付的
    @Override
    public ResultModel cancelMerchantOrder() {
        logger.info("cancelMerchantOrder start");
        List<PayRecordDomain> payRecordDomains = payRecordMapper.getNeedCancaelMerchantOrder();
        for (PayRecordDomain payRecordDomain : payRecordDomains) {
            AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
            paramAdBusinessExpandInfo.setBusinessId(payRecordDomain.getBusinessId());
            AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
            long timestamp = System.currentTimeMillis() / 1000;//时间搓当前
            JSONObject param = new JSONObject();
            param.put("businessId",payRecordDomain.getBusinessId());
            param.put("merchantOrderNo",payRecordDomain.getMerchantOrderNo());
            param.put("payId",payRecordDomain.getPayId());
            SortedMap<Object, Object> parameters = new TreeMap<>();
            String clientId = adBusinessExpandInfo.getClientId();
            String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, clientId));
            parameters.put("client_id", clientId);
            parameters.put("timestamp", timestamp);
            parameters.put("access_token", accessToken);
            parameters.put("param", param.toJSONString());
            String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
            logger.info("取消订单参数=======param========" + param);
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
            object.put("param", param.toJSONString());
            object.put("sign", sign);
            String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_CANCEL_URL);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject !=null && jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
                logger.info("cancelMerchantOrder 成功,merchantOrderNo,{}",payRecordDomain.getMerchantOrderNo());
            } else {
                logger.info("cancelMerchantOrder 失败,merchantOrderNo,{}",payRecordDomain.getMerchantOrderNo());
            }
        }
        logger.info("cancelMerchantOrder 完成");
        return ResultModel.ok();
    }

    @Override
    public ResultModel getPayResult(JSONObject json) {
        String orderNum = json.getString("orderNum");
        //20181227--改造收银台
        //OrderVo order = orderService.getByOrderNum(orderNum);
        OrderVo paramOrder = new OrderVo();
        paramOrder.setOrderNumber(orderNum);
        OrderVo order= adOrderReportServiceI.getOrderLimt(paramOrder);
        //自营商品全是兜礼支付
        ResultModel payMsg = null;
        //调用支付平台查询API,查询结果并处理支付记录
        //AdBusiness business = adBusinessDao.getById(String.valueOf(order.getBussinessId()));
        //20181227 缓存改造 ---zhangqing
        //AdBusiness business = adBusinessDao.getById(String.valueOf(o.getBussinessId()));
        AdBusiness paramBusiness = new AdBusiness();
        paramBusiness.setId(order.getBussinessId());
        AdBusiness business = adBusinessServiceI.getBusiness(paramBusiness);
        // 积分支付查询新接口
        JSONObject param = new JSONObject();
        param.put("merchantOrderNo", orderNum);
        param.put("nonceStr", RandomStringUtils.random(6, "123456789zxc"));
        param.put("id", business.getId());
        param.put("businessId", business.getBusinessId());
        param.put("isSource", 3);//标记是兜礼的查询
        //20181109注释掉不区分来源都用新的，用订单状态判断废除 ad_pay_flow表
        if (OrderService.PayState.PAID.getCode() == order.getState()) {
            //得到支付平台通知并已经处理过支付结果, 直接返回结果
            payMsg = ResultModel.ok();
        } else {
            payMsg = queryNewPayResult(param);
            if (payMsg.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                Map<Object, Object> data = (Map<Object, Object>) payMsg.getData();
                logger.info("查询结果data{}", data);
                //说明支付成功处理结果
               /* JSONObject retJson = new JSONObject();
                retJson.put("code", GlobalResultStatusEnum.SUCCESS.getCode());
                retJson.put("orderNum", orderNum);
                retJson.put("integralPayStatus", data.get("payStatus"));
                retJson.put("payAmount", data.get("orderAmount"));
                retJson.put("realPayType", data.get("payType"));
                retJson.put("outTradeNo", data.get("outTradeNo"));
                retJson.put("payEndTime", data.get("payEndTime"));
                payCallback(PayFlowService.PAYTYPE_CASHIER_DESK, PaymentService.CHANNEL_WECHAT, retJson.toJSONString());*/
            }
        }
        // 跳转支付结果页面需要数据
        if (payMsg != null && GlobalResultStatusEnum.SUCCESS.getCode() == payMsg.getCode()) {
            List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
            if (!CollectionUtils.isEmpty(orders)) {
                OrderVo order1 = orders.get(0);
                Map<String, Object> map = new HashMap<>();
                if("gift_order".equals(order1.getRemarks())){
                    MessageDataBean messageDataBean = new MessageDataBean();
                    try {
                        messageDataBean = myOrderServiceI.getLiftOrder(json);
                        logger.info("获得礼包领取成功页返回数据" + messageDataBean.toJsonString());
                    } catch (Exception e) {
                        logger.error("获得礼包领取成功页出错", e);
                        messageDataBean.setCode(MessageDataBean.failure_code);
                    }
                    map = messageDataBean.getData();
                    map.put("orderType","1");
                }else {
                    map.put("orderType","0");
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
                    //获取跳转链接
                    PayRecordDomain payRecordDomain = new PayRecordDomain();
                    payRecordDomain.setMerchantOrderNo(orderNum);
                    payRecordDomain = payRecordMapper.getPayRecordDomain(payRecordDomain);
                    if(payRecordDomain != null){
                        String returnUrl = payRecordDomain.getRedirectUrl();
                        if(StringUtils.isNotBlank(returnUrl) && (returnUrl.contains("localhost")||
                                returnUrl.contains("doooly")||returnUrl.contains("reach"))){
                            returnUrl = returnUrl + payRecordDomain.getMerchantOrderNo();
                        }
                        map.put("redirectUrl", returnUrl);
                    }else {
                        map.put("redirectUrl", "");
                    }
                }
                payMsg.setData(map);
            }
        }
        return payMsg;
    }

    @Override
    public ResultModel getPayResultV2(JSONObject json) {
        String orderNum = json.getString("orderNum");//订单号
        if(orderNum.contains("N")){
            //说明是自营子订单 走v1接口
            return getPayResult(json);
        }
        AdOrderBig adOrderBig = new AdOrderBig();
        adOrderBig.setId(orderNum);
        //查询大订单状态
        AdOrderBig adOrderBig1 = adOrderReportServiceI.getAdOrderBig(adOrderBig);
        ResultModel payMsg;
        if (OrderService.PayState.PAID.getCode() == adOrderBig1.getState()) {
            //得到支付平台通知并已经处理过支付结果, 直接返回结果
            payMsg = ResultModel.ok();
            Map<String, Object> map = new HashMap<>();
            map.put("totalAmount", adOrderBig1.getTotalAmount());
            payMsg.setData(map);
        } else {
            payMsg = ResultModel.error(GlobalResultStatusEnum.PAY_STATUS_NON);
        }
        return payMsg;
    }

    @Override
    public ResultModel queryNewPayResult(JSONObject param) {
        String id = param.getString("id");//商户表主键
        //20181227 --收银台缓存改造
        //AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(id);
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(id);
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
        object.put("param", param.toJSONString());
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
        JSONObject json = JSONObject.parseObject(retStr);
        String orderNum = json.getString("orderNum");
        String lockKey = String.format(SYNC_REFUND_CODE_KEY, orderNum + ":" + payType + ":" + payType);
        boolean b = redisLock.lock(lockKey, 15);
        if (!b) {
            return new PayMsg(PayMsg.success_code, "同步处理中");
        }
        try {
            PaymentService paymentService = PaymentServiceFactory.getPayService(payType);
            logger.info("paymentService = {}", paymentService);
            if (paymentService != null) {
                return paymentService.handlePayResult(retStr, channel);
            }
        } finally {
            redisLock.unlock(lockKey);
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
        //20181227===收银台改造优化
      /*  List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
        if (CollectionUtils.isEmpty(orders)) {
            return new PayMsg(PayMsg.failure_code, "没有找到订单");
        }
        OrderVo order = orders.get(0);*/
        OrderVo paramOrder = new OrderVo();
        paramOrder.setOrderNumber(orderNum);
        OrderVo order= adOrderReportServiceI.getOrderLimt(paramOrder);
        if (order == null) {
            return new PayMsg(PayMsg.failure_code, "没有找到订单");
        }
        //校验是否可以支付
        return canPay(order, param);
    }

    private PayMsg canPay(OrderVo order, JSONObject json) {
        logger.info("productType={}", order.getProductType());
        // 订单状态
        if (order.getState() == OrderService.PayState.PAID.getCode()) {
            return new PayMsg(PayMsg.coupon_stock_zero_code, "订单已支付，请勿重新支付。");
        }
        if (order.getType() != OrderService.OrderStatus.NEED_TO_PAY.getCode()) {
            return new PayMsg(PayMsg.coupon_stock_zero_code, "订单已经取消，请重新下单。");
        }
        if( Constants.GIFT_ORDER_TYPE.equals(order.getRemarks())){
            // 礼包商品判断能否领取
            String mqMessageJson = stringRedisTemplate.opsForValue().get(Constants.GIFT_ORDER_REDIS_MESS+order.getOrderNumber());
            JSONObject jsonParam =  JSONObject.parseObject(mqMessageJson);
            JSONObject resultJson = HttpClientUtil.httpPost(Constants.PROJECT_ACTIVITY_URL + "gift/bag/isReceive", jsonParam);
            if(resultJson!= null && resultJson.getInteger("code") != null && GlobalResultStatusEnum.SUCCESS.getCode()!= resultJson.getInteger("code")){
                logger.info("判断能否领取礼包：" + resultJson.toJSONString());
                return new PayMsg(OrderMsg.coupon_stock_zero_code, resultJson.getString("info"));
            }
        }
        //用户缓存改造
        //AdUser user = adUserDao.getById(order.getUserId().intValue());
        AdUser paramUser = new AdUser();
        paramUser.setId(order.getUserId());
        AdUser user = adUserServiceI.getUser(paramUser);
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
                        //int byNum = orderService.getBuyNum(userId, skuId, activityName);
                        //20181226 优化-==zhangqing
                        Map<String,Object> paramOrderMap = new HashMap<>();
                        paramOrderMap.put("userId",userId);
                        paramOrderMap.put("productSkuId",item.getProductSkuId());
                        paramOrderMap.put("actType",activityName);
                        int byNum = adOrderReportServiceI.getBuyNum(paramOrderMap);
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
                && (payType == 0 || payType == 2)) {
            //获取已使用额度
            //AdRechargeConf conf = adRechargeConfDao.getRechargeConf(String.valueOf(user.getGroupNum()));
            //20181227改造缓存---zhangqing
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("groupId",user.getGroupNum().toString());
            AdRechargeConf conf = adRechargeConfServiceI.getRechargeConf(paramMap);
            logger.info("conf = {}", conf);
            if (conf == null) {
                return new PayMsg(OrderMsg.failure_code, "没有找到话费充值配置");
            }
            //每月限制
            BigDecimal monthLimit = conf.getMonthLimit();
            //每月优惠额度
            BigDecimal discountsMonthLimit = conf.getDiscountsMonthLimit();
            if (discountsMonthLimit == null) {
                discountsMonthLimit = new BigDecimal("0");
            }
            //每月积分购买话费的金额
            //BigDecimal consumptionAmount = adOrderReportDao.getConsumptionAmount(order.getUserId());
            //每月积分购买话费的金额 == 收银台改造20181227--zhangqing
            Map<String,Object> paramOrderMap = new HashMap<>();
            paramOrderMap.put("userId",order.getUserId());
            BigDecimal consumptionAmount = adOrderReportServiceI.getConsumptionAmountByMap(paramOrderMap);
            if (consumptionAmount == null) {
                consumptionAmount = new BigDecimal("0");
            }
            //已使用金额+订单实付金额+手续费-每月限制金额
            BigDecimal serviceCharge = order.getServiceCharge()==null ? BigDecimal.ZERO : order.getServiceCharge();
            BigDecimal subtract = consumptionAmount.add(order.getTotalMount()).add(serviceCharge).subtract(monthLimit);
            if (subtract.compareTo(BigDecimal.ZERO) > 0) {
                //说明已经超出限额
                return new PayMsg(OrderMsg.failure_code, "您已超出每月限额");
            }
        }
        //构建收银台接口需要参数
        JSONObject param = new JSONObject();
        param.put("businessId", order.getBussinessId());
        param.put("isPayPassword", "2".equals(user.getIsPayPassword()) ? "2" : 1);//处理密码模式都是验证码模式
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
        if (order.getState() == OrderService.PayState.PAID.getCode()) {
            //状态已完成
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
        parameters.put("param", param.toJSONString());
        String sign = SignUtil.createSign(parameters, adBusinessExpandInfo.getClientSecret());
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
        object.put("param", param.toJSONString());
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
        String orderNum = param.getString("orderNum");
        //20181227 改造优化
        //OrderVo orderVo = adOrderReportDao.getByOrderNum(orderNum).get(0);
        OrderVo paramOrder = new OrderVo();
        paramOrder.setOrderNumber(orderNum);
        OrderVo orderVo= adOrderReportServiceI.getOrderLimt(paramOrder);
        String businessId = String.valueOf(orderVo.getBussinessId());
        //AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(businessId);
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
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
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.INTEGRAL_PAY_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //说明获取成功
            Map<Object, Object> data = (Map<Object, Object>) jsonObject.get("data");
            data.put("orderId",orderVo.getId());
            return ResultModel.ok(data);
        } else {
            return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"));
        }
    }

    @Override
    public ResultModel integralPayV2(JSONObject param) {
        String orderNum = param.getString("orderNum");
        long userId = param.getLong("userId");
        String bigOrderNumber;//大订单号
        String businessId = WebService.BUSINESSID;//商户编号
        OrderVo order = new OrderVo();
        order.setOrderNumber(orderNum);
        order.setUserId(userId);
        if(orderNum.contains("N")){
            //说明是自营子订单
            OrderVo orderLimt = adOrderReportServiceI.getOrderLimt(order);
            bigOrderNumber = String.valueOf(orderLimt.getBigOrderNumber());
            order.setBigOrderNumber(bigOrderNumber);
            businessId = orderLimt.getBussinessBussinessId();
        }
        AdBusinessExpandInfo paramAdBusinessExpandInfo = new AdBusinessExpandInfo();
        paramAdBusinessExpandInfo.setBusinessId(businessId);
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessServiceI.getBusinessExpandInfo(paramAdBusinessExpandInfo);
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
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.INTEGRAL_PAY_URL_V2);
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
    public ResultModel dooolyApplyPayRefund(JSONObject param) {
        String id = param.getString("id");
        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(id);
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
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.ORDER_APPLY_REFUND_URL);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return new ResultModel(jsonObject.getInteger("code"), jsonObject.getString("info"), jsonObject.get("data"));
    }

    @Override
    public ResultModel dooolyRefundCallback(JSONObject json) {
        logger.info("退款通知参数：{}", json.toJSONString());
        String paramstr = json.getString("param");
        JSONObject param = JSONObject.parseObject(paramstr);
        String merchantOrderNo = param.getString("merchantOrderNo");
        String outRefundNo = param.getString("outRefundNo");
        String merchantRefundNo = param.getString("merchantRefundNo");
        String payType = param.getString("payType");
        String refundFee = param.getString("refundFee");
        String settlementRefundFee = param.getString("settlementRefundFee");
        String refundStatus = param.getString("refundStatus");
        try {
            //添加redis锁防止并发同步====redis检测用户是否已经收到通知了
            if (!redisTemplate.opsForValue().setIfAbsent(
                    String.format(SYNC_REFUND_CODE_KEY, outRefundNo + ":" + payType + ":" + merchantRefundNo),
                    SYNC_REFUND_CODE_VALUE)) {
                //说明已经通知了
                return new ResultModel(GlobalResultStatusEnum.FAIL, "已经收到通知了");
            }
            if (refundStatus.equals(REFUND_STATUS_S)) {
                //说明退款成功
                //在查询下订单
                OrderVo order = checkOrderStatus(merchantOrderNo);
                // 修改订单状态-已退款
                orderService.updateOrderRefund(order, String.valueOf(order.getUserId()));
                //退款成功
                int payType1 = Integer.parseInt(payType);
                if (payType1 != 0 && refundFee != null && refundFee != null && (new BigDecimal(refundFee).compareTo(new BigDecimal("0")) > 0)) {
                    //非积分需要插入流水
                    payType1 = PayTypeEnum.getDooolyCodeByCode(payType1);
                    saveOneOrder(order, payType1, refundFee, refundFee, merchantRefundNo);
                }
                //积分退款要修改businessId一致
                updateBusinessId(order);
                //退款后处理
                Order o = new Order();
                o.setPayType(payType1);
                o.setSerialNumber(merchantOrderNo);
                o.setOrderNumber(order.getOrderNumber());
                o.setState(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
                o.setType(OrderService.OrderStatus.RETURN_ORDER.getCode());
                afterRefundProcess(order, o);
            }
        } catch (NumberFormatException e) {
            logger.info("退款通知异常：{}", e);
        } finally {
            //删掉redis锁key
            redisTemplate.delete(String.format(SYNC_REFUND_CODE_KEY, outRefundNo + ":" + payType + ":" + merchantRefundNo));
        }
        return ResultModel.ok();
    }

    private int saveOneOrder(OrderVo order, int payType, String amount, String price, String merchantRefundNo) {
        try {
            int rows = 0;
            logger.info("同步订单到_order开始. order ={} ===> payType = {}", order, payType);
            AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
            Order o = new Order();
            o.setUserid(order.getUserId());
            o.setOrderUserId(order.getUserId());
            o.setBussinessId(business.getBusinessId());
            o.setStoresId(OrderService.STORESID);
            o.setPayPassword(null);
            o.setVerificationCode(null);
            o.setAmount(new BigDecimal(amount));
            o.setTotalAmount(order.getTotalMount());
            o.setPrice(new BigDecimal(price));
            o.setTotalPrice(order.getTotalPrice());
            //积分是0其他是2现金
            o.setPayType(payType);
            o.setOrderNumber(order.getOrderNumber());
            o.setSerialNumber(merchantRefundNo);
            o.setOrderDate(new Date());
            o.setState(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
            o.setOrderType(1);
            o.setType(OrderService.OrderStatus.RETURN_ORDER.getCode());
            o.setSource(order.getIsSource());
            o.setIsPayPassword(0);
            o.setOrderDetail(null);
            o.setIsRebate(0);
            o.setBusinessRebate(new BigDecimal("0"));
            o.setUserRebate(new BigDecimal("0"));
            o.setCreateDateTime(new Date());
            o.setCheckState(0);
            rows = orderDao.insert(o);
            logger.info("同步订单到_order结束. rows = {}", rows);
            //同步detail
            if (o.getId() != null) {
                List<AdReturnFlow> listByOrderId = adReturnFlowDao.getListByOrderId(order.getId(), merchantRefundNo, String.valueOf(payType));
                for (AdReturnFlow adReturnFlow : listByOrderId) {
                    List<OrderItemVo> items = adReturnDetailDao.getList(adReturnFlow);
                    for (int i = 0; i < items.size(); i++) {
                        OrderItemVo itVo = items.get(i);
                        OrderDetail d = new OrderDetail();
                        d.setOrderid(o.getId().intValue());
                        d.setCode(itVo.getCode());
                        String goods;
                        if (itVo.getSku() != null) {
                            goods = itVo.getGoods() + itVo.getSku();
                        } else {
                            goods = itVo.getGoods();
                        }
                        d.setGoods(goods);
                        d.setAmount(itVo.getAmount());
                        d.setPrice(itVo.getPrice());
                        d.setNumber(itVo.getNumber());
                        d.setTax(itVo.getTax());
                        d.setCategory(itVo.getCategoryId());
                        d.setFirstCategory(null);
                        d.setSecondCategory(null);
                        d.setBrandName(null);
                        d.setCreatedatetime(new Date());
                        int r = orderDao.insertDetail(d);
                        if (r > 0) {
                            logger.info("同步订单到_orderDetail结束. index ={},rows = {}", i, rows);
                        }
                        rows += r;
                    }
                }
            }
            return rows;
        } catch (Exception e) {
            logger.error("同步订单到_order出现异常. order = {},e = {}", order, e);
        }
        return 0;
    }


    @Override
    public ResultModel refund(JSONObject json) {
        logger.info("退款请求参数，json = {}", json);
        String userId = json.getString("userId");
        String orderNum = json.getString("orderNum");
        String returnFlowNumber = json.getString("returnFlowNumber");
        String payType = json.getString("payType");
        ResultModel resultModel = refundService.dooolyCashDeskRefund(Long.parseLong(userId), orderNum, returnFlowNumber, payType,null);
        logger.info("退款返回结果，resultModel = {}", resultModel.toJsonString());
        return resultModel;
    }

    @Override
    public ResultModel applyRefund(JSONObject json) {
        String userId = json.getString("userId");
        String orderNum = json.getString("orderNum");
        String totalAmount = json.getString("totalAmount");
        ResultModel resultModel = refundService.applyRefund(Long.parseLong(userId), orderNum, totalAmount);
        return resultModel;
    }

    private OrderVo checkOrderStatus(String orderNum) {
        try {
            OrderVo o = new OrderVo();
            o.setOrderNumber(orderNum);
            //o.setType(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
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
     * @param order ad_order_report对象
     * @param o     _order对象
     */
    private void afterRefundProcess(OrderVo order, Order o) {
        List<AfterRefundProcessor> afterPayProcessors = AfterRefundProcessorFactory.getAllProcessors();
        for (AfterRefundProcessor afterRefundProcessor : afterPayProcessors) {
            logger.info("afterRefundProcess() afterRefundProcessor = {}", afterRefundProcessor);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.error("执行afterRefundProcess class = {}", afterRefundProcessor);
                    afterRefundProcessor.process(order, o);
                }
            }).start();
        }
    }
}
