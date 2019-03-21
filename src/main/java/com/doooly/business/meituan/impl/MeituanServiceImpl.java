package com.doooly.business.meituan.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.meituan.MeituanService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.*;
import com.doooly.business.pay.bean.AdOrderSource;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.payment.service.NewPaymentServiceI;
import com.doooly.business.payment.utils.SignUtil;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PaymentConstants;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.MeituanProductTypeEnum;
import com.doooly.common.meituan.RsaUtil;
import com.doooly.common.util.BeanMapUtil;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.RandomUtil;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.meituan.EasyLogin;
import com.doooly.entity.reachad.AdBusinessExpandInfo;
import com.doooly.entity.reachad.AdOrderReport;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.Order;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.reach.redis.utils.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;

/**
 * Created by wanghai on 2018/12/13.
 */
@Service
public class MeituanServiceImpl implements MeituanService{

    private static Logger logger = LoggerFactory.getLogger(MeituanServiceImpl.class);

    @Autowired
    private AdUserDao adUserDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AdOrderReportDao adOrderReportDao;

    @Autowired
    private AdOrderDetailDao adOrderDetailDao;

    @Autowired
    private AdOrderSourceDao adOrderSourceDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AdBusinessExpandInfoDao adBusinessExpandInfoDao;

    @Autowired
    private NewPaymentServiceI newPaymentServiceI;

    @Autowired
    private ConfigDictServiceI configDictServiceI;



    @Override
    public String easyLogin(String entToken, String staffNo, String staffPhoneNo,MeituanProductTypeEnum productTypeEnum) throws Exception{
        EasyLogin easyLogin = new EasyLogin();
        easyLogin.setEntToken(entToken);
        easyLogin.setStaffNo(staffNo);
        easyLogin.setStaffPhoneNo(staffPhoneNo);
        easyLogin.setProductType(productTypeEnum.getCode());
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
        String phone = json.getString("mobile");
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

        BigDecimal total = json.getBigDecimal("tradeAmount");
        String orderNum = json.getString("serialNum") + "-" + json.getString("sqtOrderId");
        JSONObject param = new JSONObject();
        param.put("businessId", MeituanConstants.meituan_bussinesss_serial);//商户编号
        param.put("cardNumber", adUser.getTelephone());
        param.put("merchantOrderNo", orderNum);
        param.put("outTradeNo",json.getString("sqtOrderId"));
        param.put("tradeType", "DOOOLY_JS");
        param.put("notifyUrl", json.getString("notifyUrl"));
        param.put("body", json.getString("goodsName"));
        param.put("isSource", 2);
        param.put("orderDate", DateUtils.formatDateTime(new Date()));
        param.put("storesId", "A001");
        param.put("price", total);
        param.put("amount", total);
        param.put("clientIp", json.get("clientIp"));
        param.put("nonceStr", RandomUtil.getRandomStr(16));
        param.put("isPayPassword", adUser.getIsPayPassword());
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonDetail = new JSONObject();
        jsonDetail.put("code", "");
        jsonDetail.put("goods", json.getString("goodsName"));
        jsonDetail.put("number", 1);
        jsonDetail.put("price", total);
        jsonDetail.put("category", "0000");
        jsonDetail.put("tax", 0);
        jsonDetail.put("amount", total);
        jsonArray.add(jsonDetail);
        param.put("orderDetail", jsonArray.toJSONString());
        logger.info("美团下单参数param=========" + param);

        AdBusinessExpandInfo adBusinessExpandInfo = adBusinessExpandInfoDao.getByBusinessId(MeituanConstants.meituan_bussinesss_id);
        String accessToken = redisTemplate.opsForValue().get(String.format(PaymentConstants.PAYMENT_ACCESS_TOKEN_KEY, adBusinessExpandInfo.getClientId()));
        logger.info("美团下预付单参数=======accessToken========" + accessToken);
        if (accessToken == null) {
            ResultModel authorize = newPaymentServiceI.authorize(MeituanConstants.meituan_bussinesss_id);
            if (authorize.getCode() == GlobalResultStatusEnum.SUCCESS.getCode()) {
                Map<Object, Object> data = (Map<Object, Object>) authorize.getData();
                accessToken = (String) data.get("access_token");
            } else {
                msg.setCode(GlobalResultStatusEnum.FAIL.getCode() + "");
                msg.setMess("接口授权认证失败");
                return msg;
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
        String mobile = json.getString("mobile");
        String meituanTestMobile = configDictServiceI.getValueByTypeAndKeyNoCache("meituan_test_mobile","meituan_test_mobile");
        if (StringUtils.isNotBlank(meituanTestMobile) && !meituanTestMobile.contains(mobile)) {
            //下单成功返回信息
            msg.getData().put("orderNum", orderNum);
            msg.getData().put("userId",adUser.getId());
            return msg;
        }
        String result = HTTPSClientUtils.sendHttpPost(object, PaymentConstants.UNIFIED_ORDER_URL);
        JSONObject jsonResult = JSONObject.parseObject(result);
        logger.info("美团下单返回：{}",result);
        if (jsonResult.getInteger("code") == GlobalResultStatusEnum.SUCCESS.getCode()) {

            OrderVo orderVo = new OrderVo();
            orderVo.setOrderNumber(orderNum);
            orderVo.setUpdateDate(new Date());
            orderVo.setRemarks(json.getString("sqtOrderId"));
            adOrderReportDao.updateByNum(orderVo);

            //下单成功返回信息
            msg.getData().put("orderNum", orderNum);
            msg.getData().put("userId",adUser.getId());
        } else {
            msg.setCode("1001");
            msg.setMess("下单失败");
        }
        return msg;
    }
}
