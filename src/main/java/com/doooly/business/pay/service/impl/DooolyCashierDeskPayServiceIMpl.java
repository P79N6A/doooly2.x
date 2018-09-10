package com.doooly.business.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.AdOrderFlow;
import com.doooly.business.pay.bean.AdOrderSource;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.service.AbstractPaymentService;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.dao.reachad.AdOrderFlowDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 兜礼收银台支付
 * @author: qing.zhang
 * @date: 2018-08-30
 */
@Service
public class DooolyCashierDeskPayServiceIMpl extends AbstractPaymentService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AdOrderFlowDao adOrderFlowDao;

    @Autowired
    private MallBusinessService mallBusinessService;

    @Override
    public String getPayType() {
        return PayFlowService.PAYTYPE_CASHIER_DESK;
    }

    @Override
    protected PayMsg buildPayParams(List<OrderVo> orders, PayFlow flow, JSONObject json) {
        return null;
    }

    @Override
    protected Map<String, Object> resolveAndVerifyResult(String retStr, String payType, String channel) {
        try {
            JSONObject json = JSONObject.parseObject(retStr);
            String code = json.getString("code");
            String integralPayStatus = json.getString("integralPayStatus");
            // 积分扣除成功
            if (PayMsg.success_code.equals(code) && "1".equals(integralPayStatus)) {
                String orderNum = json.getString("orderNum");
                String payFlowId = json.getString("payFlowId");
                String realPayType = json.getString("realPayType");
                String payAmount = json.getString("payAmount");//现金支付金额
                //修改订单金额加上手续费
                logger.info("retStr {}", retStr);
                OrderVo order = orderService.getByOrderNum(orderNum);
                AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
                BigDecimal serviceCharge = order.getServiceCharge();
                if (OrderService.ProductType.MOBILE_RECHARGE.getCode() == order.getProductType() && serviceCharge != null) {
                    OrderVo o = new OrderVo();
                    o.setOrderNumber(order.getOrderNumber());
                    o.setTotalMount(order.getTotalMount().add(order.getServiceCharge()));
                    o.setUpdateBy("resolveAndVerifyResult");
                    int i = orderService.updateByNum(o);
                    logger.info("updateByNum() i = {}", i);
                }
                if (realPayType.equals("2")) {
                    //表示是混合支付 在插入一条微信支付流水
                    Order o = new Order();
                    o.setUserid(order.getUserId());
                    o.setOrderUserId(order.getUserId());
                    o.setBussinessId(business.getBusinessId());
                    o.setStoresId(OrderService.STORESID);
                    o.setPayPassword(null);
                    o.setVerificationCode(null);
                    o.setAmount(new BigDecimal(payAmount));
                    o.setTotalAmount(order.getTotalMount());
                    o.setPrice(new BigDecimal("0"));//混合支付第二笔流水设为0
                    o.setTotalPrice(order.getTotalPrice());
                    o.setPayType(PayFlowService.PayType.getCodeByName("weixin"));
                    o.setOrderNumber(order.getOrderNumber());
                    o.setSerialNumber(order.getOrderNumber());
                    o.setOrderDate(order.getOrderDate());
                    //o.setOriginOrderNumber(null);
                    o.setState(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
                    o.setOrderType(1);
                    o.setType(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
                    o.setSource(order.getIsSource());
                    o.setIsPayPassword(0);
                    o.setOrderDetail(null);
                    o.setIsRebate(0);
                    o.setBusinessRebate(new BigDecimal("0"));
                    o.setUserRebate(new BigDecimal("0"));
                    o.setCreateDateTime(new Date());
                    o.setCheckState(0);
                    orderDao.insert(o);
                    //同步一条支付流水
                    AdOrderFlow adOrderFlow = new AdOrderFlow();
                    try {
                        PayFlow payFlow = payFlowService.getById(payFlowId);
                        logger.info("同步ad_pay_fow到ad_order_flow开始. ==> order={}, ==> payFlow={}", order, payFlow);
                        adOrderFlow.setOrderReportId(order.getId());
                        adOrderFlow.setSerialNumber(payFlow.getTransNo());
                        adOrderFlow.setPayType((short) 3);
                        adOrderFlow.setAmount(new BigDecimal(payAmount));
                        adOrderFlow.setCreateBy(String.valueOf(order.getUserId()));
                        adOrderFlow.setType("1");
                        adOrderFlow.setDelFlag("0");
                        adOrderFlow.setRemarks(String.valueOf(payFlow.getId()));
                        adOrderFlow.setUpdateDate(null);
                        adOrderFlow.setUpdateBy(null);
                        adOrderFlow.setCreateDate(payFlow.getPaySumbitTime());
                        int rows = adOrderFlowDao.insert(adOrderFlow);
                        logger.info("SynToPayOrderFlowProcessor成功. rows = {}", rows);
                        if (rows <= 0) {
                            logger.error("同步ad_pay_fow到ad_order_flow失败. adOrderFlow = {}", adOrderFlow);
                        }
                    } catch (Exception e) {
                        logger.error("同步ad_pay_fow到ad_order_flow出现异常. adOrderFlow = {},e = {}", adOrderFlow, e);
                    }
                }
                //支付方式设为收银台
                PayFlow payFlow = payFlowService.getById(payFlowId);
                payFlow.setPayType(PayFlowService.PAYTYPE_CASHIER_DESK);
                payFlowService.update(payFlow);
                //插入订单来源表
                AdOrderSource adOrderSource = new AdOrderSource();
                adOrderSource.setOrderNumber(orderNum);
                adOrderSource.setBusinessId(business.getId());
                adOrderSource.setCashDeskSource("d");
                adOrderSource.setTraceCodeSource("d");

                // 返回解析结果数据
                List<OrderVo> orders = orderService.getByOrdersNum(orderNum);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("orderNum", orderNum);
                map.put("payFlowId", payFlowId);
                map.put("realPayType", realPayType);
                map.put("orders", orders);
                // 积分支付没有交易流水号
                map.put("transNo", "0");
                return map;
            } else {
                logger.error("支付失败");
            }
        } catch (Exception e) {
            logger.error("e = {}", e);
        }
        return null;
    }

    @Override
    protected PayMsg queryPayResult(PayFlow flow) {
        return new PayMsg(PayMsg.success_code, PayMsg.success_mess);
    }
}