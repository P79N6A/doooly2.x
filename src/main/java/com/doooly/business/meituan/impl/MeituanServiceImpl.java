package com.doooly.business.meituan.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.meituan.MeituanService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.*;
import com.doooly.business.pay.bean.AdOrderSource;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.RsaUtil;
import com.doooly.common.util.BeanMapUtil;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.meituan.EasyLogin;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.Order;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/13.
 */
@Service
public class MeituanServiceImpl implements MeituanService{

    @Autowired
    private AdUserDao adUserDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AdOrderReportDao adOrderReportDao;

    @Autowired
    private AdOrderDetailDao adOrderDetailDao;

    @Autowired
    private AdOrderSourceDao adOrderSourceDao;


    @Override
    public String easyLogin(String entToken, String staffNo, String staffPhoneNo) throws Exception{
        EasyLogin easyLogin = new EasyLogin();
        easyLogin.setEntToken(entToken);
        easyLogin.setStaffNo(staffNo);
        easyLogin.setStaffPhoneNo(staffPhoneNo);
        Map<String,Object> paramMap = BeanMapUtil.transBean2Map(easyLogin);
        paramMap.remove("signature");
        paramMap =  BeanMapUtil.sortMapByKey(paramMap);
        String signature = getSiginature(paramMap);
        paramMap.put("signature",signature);
        paramMap =  BeanMapUtil.sortMapByKey(paramMap);
        String ret = MeituanConstants.meituan_access_url + convertMapToUrl(paramMap);
        return ret;
    }


    @Override
    public String getSiginature(Map<String, Object> paramMap) throws Exception{
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Object> entry : paramMap.entrySet()) {
            if (sb.length() == 0) {
                sb.append(entry.getKey() + "=" + entry.getValue());
            } else {
                sb.append("&").append(entry.getKey() + "=" + entry.getValue());
            }
        }
        System.out.println(new Gson().toJson(paramMap));
        System.out.println(sb.toString());
        RSAPrivateKey rsaPrivateKey = RsaUtil.loadPrivateKey(MeituanConstants.private_key);
        String signature = RsaUtil.sign(sb.toString().getBytes(),rsaPrivateKey);
        signature = URLEncoder.encode(signature,"utf-8");
        System.out.println(signature);
        return signature;
    }

    @Override
    public String convertMapToUrl(Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String,Object> entry : paramMap.entrySet()) {
            if (sb.length() == 1) {
                sb.append(entry.getKey() + "=" + entry.getValue());
            } else {
                sb.append("&").append(entry.getKey() + "=" + entry.getValue());
            }
        }
        return sb.toString();
    }


    public String convertMapToUrlEncode(Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String,Object> entry : paramMap.entrySet()) {
            if (sb.length() == 1) {
                try {
                    sb.append(entry.getKey() + "=" + URLEncoder.encode(String.valueOf(entry.getValue()),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sb.append("&").append(entry.getKey() + "=" + URLEncoder.encode(String.valueOf(entry.getValue()),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    @Override
    public OrderMsg createOrderMeituan(JSONObject json) {
        String phone = json.getString("buyer_openid");
        OrderMsg msg = new OrderMsg(OrderMsg.success_code, OrderMsg.success_mess);
        if (StringUtils.isEmpty(phone)) {
            msg.setCode(OrderMsg.failure_code);
            msg.setMess("用户标示为空");
            return msg;
        }
        AdUser adUser = new AdUser();
        adUser.setTelephone(phone);
        adUser = adUserDao.get(adUser);
        if (adUser == null) {
            msg.setCode(OrderMsg.failure_code);
            msg.setMess("用户查询失败");
            return msg;
        }

        OrderVo orderVo = new OrderVo();
        BigDecimal total = json.getBigDecimal("total");
        String orderNum = json.getString("outer_trade_no");
        if (orderNum.contains(MeituanConstants.app_id)) {
            String[] a = orderNum.split(MeituanConstants.app_id);
            orderNum = a[1];
        }

        //_order
        Order o = new Order();
        o.setIsRebate(0);
        o.setOrderNumber(orderNum);
        o.setBusinessRebate(new BigDecimal("0"));
        o.setUserRebate(new BigDecimal("0"));
        o.setCreateDateTime(new Date());
        o.setState(0);
        o.setSource(2);//合作商家
        orderDao.insert(o);

        //ad_order_report
        OrderVo order = new OrderVo();
        Date orderDate = new Date();
        order.setBussinessId(1001);
        order.setOrderId(o.getId());
        order.setUserId(adUser.getId());
        order.setOrderNumber(orderNum);
        order.setStoresId(orderVo.getStoresId());
        order.setTotalMount(total);
        order.setTotalPrice(total);
        order.setOrderDate(orderDate);
        order.setState(OrderService.PayState.UNPAID.getCode());
        order.setType(OrderService.OrderStatus.NEED_TO_PAY.getCode());
        order.setIsUserRebate('0');
        order.setUserRebate(orderVo.getUserRebate());
        order.setUserReturnAmount(new BigDecimal("0"));
        order.setIsBusinessRebate(orderVo.getIsBusinessRebate());
        order.setBusinessRebateAmount(new BigDecimal("0"));
        order.setBillingState('0');
        order.setDelFlag('0');
        order.setDelFlagUser('0');
        order.setCreateBy(String.valueOf(adUser.getId()));
        order.setIsFirst('0');
        order.setIsSource(2);//合作商家
        order.setFirstCount(0);
        order.setAirSettleAccounts(null);
        order.setRemarks(orderVo.getRemarks());
        order.setUpdateDate(null);
        order.setCreateDate(orderDate);
        order.setConsigneeName(orderVo.getConsigneeName());
        order.setConsigneeAddr(orderVo.getConsigneeAddr());
        order.setConsigneeMobile(orderVo.getConsigneeMobile());
        order.setProductType(orderVo.getProductType());
        order.setActType(OrderService.ActivityType.COMMON_ORDER.getActType());
        order.setVoucher(BigDecimal.ZERO);
        order.setCouponId("");
        //支持支付方式 ==> 1:积分,2:微信, 3.支付宝; 多个以逗号分割
        if(order.getProductType() == OrderService.ProductType.NEXUS_RECHARGE.getCode()){
            //全家集享卡只支持积分支付
            order.setSupportPayType("1");
        }else{
            if (BigDecimal.ZERO.compareTo(total) == 0) {
                //0元订单
                order.setSupportPayType("0");
                order.setServiceCharge(BigDecimal.ZERO);
            } else {
                //非0元订单
                String supportPayType = orderVo.getSupportPayType();
                if (StringUtils.isEmpty(orderVo.getSupportPayType())) {
                    supportPayType = "all";
                }
                order.setSupportPayType(supportPayType);
                order.setServiceCharge(orderVo.getServiceCharge());
            }
        }
        adOrderReportDao.insert(order);


        //ad_order_detail
        OrderItemVo orderItem = new OrderItemVo();
        orderItem.setOrderReportId(o.getId());
        orderItem.setCategoryId("");
        orderItem.setCode("");
        orderItem.setGoods(json.getString("subject"));
        orderItem.setAmount(total);
        orderItem.setPrice(total);
        orderItem.setNumber(new BigDecimal(1));
        orderItem.setCreateBy(adUser.getId()+"");
        orderItem.setDelFlag(0);
        orderItem.setRemarks("");
        orderItem.setTax(null);
        orderItem.setUpdateDate(null);
        orderItem.setUpdateBy(null);
        orderItem.setCreateDate(new Date());
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        orderItemVoList.add(orderItem);
        adOrderDetailDao.bantchInsert(o.getId(),orderItemVoList);


        //ad_order_source
        AdOrderSource adOrderSource = new AdOrderSource();
        adOrderSource.setOrderNumber(order.getOrderNumber());
        adOrderSource.setBusinessId(order.getBussinessId());
        adOrderSource.setCashDeskSource("d");
        adOrderSource.setTraceCodeSource("d");
        adOrderSourceDao.insert(adOrderSource);

        //下单成功返回信息
        msg.getData().put("orderNum", orderNum);
        msg.getData().put("userId",adUser.getId());
        return msg;
    }
}
