package com.doooly.business.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.bean.WxRefundParams;
import com.doooly.business.pay.service.AbstractRefundService;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.pay.utils.WxUtil;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.common.constants.PaymentConstants;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 退款接口
 *
 * @author 2017-11-09 11:05:50 WANG
 */
@Service
public class RefundServiceImpl extends AbstractRefundService {

    @Autowired
    private AdUserDao adUserDao;
    @Autowired
    private MallBusinessService mallBusinessService;
    @Autowired
    private NewPaymentServiceI newPaymentServiceI;

    @Override
    public PayMsg doRefund(OrderVo order, PayFlow payFlow) {
        logger.info("doRefund start . orderNumber = {}", order.getOrderNumber());
        try {
            // 积分退款
            //旧的支付方式
            if(payFlow != null && payFlow.getPayType().equals(PayFlowService.PAYTYPE_DOOOLY)){
                return dooolyRefund(order,payFlow);
            }
        } catch (Exception e) {
            logger.info("doRefund() e = {}", e);
        }
        return new PayMsg(PayMsg.failure_code, PayMsg.failure_mess);
    }

    /**
     * doooly 支付退款
     *
     * @param order
     * @return
     */
    public ResultModel dooolyPayRefund(OrderVo order,String merchantRefundNo,String refundType) {
        // 积分退款
        AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
        JSONObject params = new JSONObject();
        params.put("businessId", business.getBusinessId());
        params.put("merchantOrderNo", order.getOrderNumber());
        params.put("merchantRefundNo", merchantRefundNo);
        params.put("refundType", refundType);
        params.put("id", business.getId());
        ResultModel resultModel = newPaymentServiceI.dooolyPayRefund(params);
        logger.info("收银台退款返回结果code:{},info:{},data:{}",resultModel.getCode(),resultModel.getInfo(),resultModel.getData());
        if (resultModel.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
            //退款成功
            try {
                // 积分退成功发送短信
                List<OrderItemVo> items = order.getItems();
                OrderItemVo orderItem = items.get(0);
                AdUser adUser = adUserDao.getById(order.getUserId().intValue());
                String mobiles = adUser.getTelephone();
                String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.refund_success_template_code;
                JSONObject paramSMSJSON = new JSONObject();
                String product ;
                if(StringUtils.isNotBlank(orderItem.getSku())){
                    product = orderItem.getGoods() + "-" + orderItem.getSku();
                }else {
                    product = orderItem.getGoods();
                }
                Map<String,Object> resultMap = (Map<String, Object>) resultModel.getData();
                BigDecimal refundFee = (BigDecimal) resultMap.get("refundFee");
                BigDecimal refundIntegral = (BigDecimal) resultMap.get("refundIntegral");
                paramSMSJSON.put("product",product );
                paramSMSJSON.put("integral", refundFee.add(refundIntegral));
                int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
                logger.info("sendMsg orderNum = {},i = {}", order.getOrderNumber(), i);
            } catch (Exception e) {
                logger.error("sendMsg has an error. e = {}", e);
            }
        }
        return resultModel;
    }

    /**
     * doooly 申请支付退款
     *
     * @param order
     * @return
     */
    public ResultModel dooolyApplyRefund(OrderVo order) {
        // 积分退款
        AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
        AdUser adUser = adUserDao.getById(order.getUserId().intValue());
        //订单子项
        JSONArray jsonArray = new JSONArray();
        List<OrderItemVo> items = order.getItems();
        for (OrderItemVo item : items) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",item.getCode());
            String goods;
            if(item.getSku()!= null){
                goods = item.getGoods()+"-"+ item.getSku();
            }else {
                goods = item.getGoods();
            }
            jsonObject.put("goods",goods);
            jsonObject.put("number",item.getNumber());
            jsonObject.put("amount",item.getAmount());
            jsonObject.put("category",item.getCategoryId());
            jsonObject.put("price",item.getPrice());
            jsonObject.put("tax",item.getTax());
            jsonArray.add(jsonObject);
        }
        JSONObject params = new JSONObject();
        params.put("businessId", business.getBusinessId());
        params.put("storesId", order.getStoresId()==null?"":order.getStoresId());
        params.put("cardNumber", adUser.getCardNumber());
        params.put("merchantOrderNo", order.getOrderNumber());
        params.put("orderDetail", jsonArray.toJSONString());
        params.put("merchantRefundNo", UUID.randomUUID().toString().replace("-", ""));
        params.put("refundPrice",  String.valueOf(order.getTotalPrice().setScale(2, BigDecimal.ROUND_DOWN)));
        params.put("refundAmount",  String.valueOf(order.getTotalMount().setScale(2, BigDecimal.ROUND_DOWN)));

        //饿了么回调地址
        String notifyUrl = null;
        if (StringUtils.isNotBlank(order.getElmRefundNotifyUrl())) {
            notifyUrl = order.getElmRefundNotifyUrl();
        } else if (3 == order.getIsSource()) {
            notifyUrl = PaymentConstants.PAYMENT_REFUND_NOTIFY_URL;
        }
        params.put("notifyUrl", notifyUrl);
        params.put("nonceStr", UUID.randomUUID().toString().replace("-", ""));
        params.put("id", business.getId());
        ResultModel resultModel = newPaymentServiceI.dooolyApplyPayRefund(params);
        return resultModel;
    }

    private PayMsg wxAppRefund(OrderVo order, PayFlow payFlow) {
        WxRefundParams params = new WxRefundParams();
        params.setAppid(WxJsapiPayServiceImpl.WXJS_APPID);
        params.setMch_id(WxJsapiPayServiceImpl.WXJS_MCH_ID);
        params.setNonce_str(WxUtil.getNonceStr());
        // params.setSign(sign);
        params.setTransaction_id(payFlow.getTransNo());
        params.setOut_refund_no("");
        // params.setTotal_fee(total_fee);
        // params.setRefund_fee(refund_fee);
        // params.set
        return null;
    }

    /***
     * 积分退款
     *
     * @param order
     * @param payFlow
     * @return
     */
    public PayMsg dooolyRefund(OrderVo order, PayFlow payFlow) {

//		// 积分退款
//		AdUser adUser = adUserDao.getById(String.valueOf(order.getUserId()));
//		JSONObject params = new JSONObject();
//		params.put("orderNumber", order.getOrderNumber());
//		params.put("serialNumber", order.getOrderNumber());
//		params.put("cardNumber", adUser.getCardNumber());
//		params.put("integral", order.getTotalMount().toString());
//		params.put("type", "5");
//		params.put("businessId", WebService.BUSINESSID);
//		params.put("storesId", WebService.STOREID);
//		//订单子项
//		OrderItemVo item = order.getItems().get(0);
//		JSONObject jsonDetail = new JSONObject();
//		jsonDetail.put("code", item.getId());
//		jsonDetail.put("goods", item.getGoods() + item.getSku());
//		jsonDetail.put("number", item.getNumber());
//		jsonDetail.put("price", item.getPrice().toString());
//		jsonDetail.put("category", item.getCategoryId());
//		params.put("orderDetail", jsonDetail);
//		JSONObject ret = WebService.checkAddIntegralAuthorizationPort(params);
//		logger.info("ret = {}", ret);
//		if (ret != null) {
//			if (ret.getInteger("code") == 0) {
//				try {
//					// 积分退成功发送短信
//					OrderItemVo orderItem = order.getItems().get(0);
//					String mobiles = adUser.getTelephone();
//					String alidayuSmsCode = "SMS_145596516";
//					JSONObject paramSMSJSON = new JSONObject();
//					paramSMSJSON.put("product", orderItem.getGoods() + "-" + orderItem.getSku());
//					paramSMSJSON.put("integral", payFlow.getAmount().toString());
//					int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
//					logger.info("sendMsg orderNum = {},i = {}", order.getOrderNumber(), i);
//				} catch (Exception e) {
//					logger.error("sendMsg has an error. e = {}", e);
//				}
//				PayMsg payMsg = new PayMsg(PayMsg.success_code, "积分退款成功!", new HashMap<String, Object>());
//				payMsg.data.put("refund_id", "0");
//				return payMsg;
//			} else {
//				return new PayMsg(PayMsg.failure_code, ret.getString("info"));
//			}
//		} else {
//			return new PayMsg(PayMsg.failure_code, "退款出现异常!");
//		}

        String url = WebService.ADDINTEGRALAUTHORIZATION;
        // 积分退款
        AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
        AdUser adUser = adUserDao.getById(order.getUserId().intValue());
        JSONObject params = new JSONObject();
        params.put("orderNumber", order.getOrderNumber());
        params.put("serialNumber", order.getOrderNumber());
        params.put("cardNumber", adUser.getCardNumber());
        params.put("integral", order.getTotalMount().toString());
        params.put("orderType", "5");
        params.put("businessId", WebService.BUSINESSID);
        params.put("storesId", WebService.STOREID);
        params.put("username", WebService.USERNAME);
        params.put("password", WebService.PASSWORD);
        //订单子项
        OrderItemVo item = order.getItems().get(0);
        JSONObject orderDetail = new JSONObject();
        orderDetail.put("orderNumber", order.getOrderNumber());
        orderDetail.put("serialNumber", order.getOrderNumber());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonDetail = new JSONObject();
        jsonDetail.put("code", item.getId());
        jsonDetail.put("goods", item.getGoods() + item.getSku());
        jsonDetail.put("number", item.getNumber());
        jsonDetail.put("price", item.getPrice().toString());
        jsonDetail.put("category", item.getCategoryId());
        jsonArray.add(jsonDetail);
        orderDetail.put("orderDetail", jsonArray);
        params.put("orderDetail", orderDetail);

        String ret = HTTPSClientUtils.sendPostNew(params.toJSONString(), url);
        logger.info("result = {}", ret);
        if (ret != null) {
            JSONObject json = JSON.parseObject(ret);
            if (json.getInteger("code") == 0) {
                try {
                    // 积分退成功发送短信
                    OrderItemVo orderItem = order.getItems().get(0);
                    String mobiles = adUser.getTelephone();
                    String alidayuSmsCode = ThirdPartySMSConstatns.SMSTemplateConfig.refund_success_template_code;
                    JSONObject paramSMSJSON = new JSONObject();
                    paramSMSJSON.put("product", orderItem.getGoods() + "-" + orderItem.getSku());
                    paramSMSJSON.put("integral", payFlow.getAmount().toString());
                    int i = ThirdPartySMSUtil.sendMsg(mobiles, paramSMSJSON, alidayuSmsCode, null, true);
                    logger.info("sendMsg orderNum = {},i = {}", order.getOrderNumber(), i);
                } catch (Exception e) {
                    logger.error("sendMsg has an error. e = {}", e);
                }
                PayMsg payMsg = new PayMsg(PayMsg.success_code, "积分退款成功!", new HashMap<String, Object>());
                payMsg.data.put("refund_id", "0");
                return payMsg;
            } else {
                return new PayMsg(PayMsg.failure_code, json.getString("info"));
            }
        } else {
            return new PayMsg(PayMsg.failure_code, "退款出现异常!");
        }

    }

}
