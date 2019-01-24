package com.doooly.publish.rest.meituan.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.business.common.util.HttpClientUtil;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.meituan.MeituanService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.impl.NewPaymentService;
import com.doooly.common.IPUtils;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.MeituanProductTypeEnum;
import com.doooly.common.meituan.StaffTypeEnum;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.OrderMsg;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdUser;
import com.doooly.publish.rest.meituan.MeituanRestService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reach.redis.utils.GsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wanghai on 2018/12/14.
 */
@Component
@Path("/meituan")
public class MeituanRestServiceImpl implements MeituanRestService {

    private static final Logger logger = LoggerFactory.getLogger(MeituanRestServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private MeituanService meituanService;

    @Autowired
    private ConfigDictServiceI configDictServiceI;

    @Autowired
    private NewPaymentService newPaymentService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private AdUserDao adUserDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GET
    @Path("/getMeituanEasyLoginUrl")
    @Produces("application/json;charset=UTF-8")
    @Consumes("application/json;charset=UTF-8")
    public String getMeituanEasyLoginUrl(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        String token = request.getHeader("token");
        String userId = request.getHeader("userId");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isBlank(userId)) {
            userId = request.getParameter("userId");
        }
        String productType = request.getParameter("productType");
        if (StringUtils.isBlank(productType)) {
            productType = MeituanProductTypeEnum.WAIMAI.getCode();
        }
        String loginUrl = "";
        if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(userId)) {
            AdUser adUser = adUserDao.getById(Integer.parseInt(userId));
            if (adUser != null) {
                try {
                    //判断是否已经同步
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("staffs",Arrays.asList(adUser.getTelephone()));
                    jsonObject.put("staffType", StaffTypeEnum.StaffTypeEnum50.getCode());
                    String dooolyScheduleUrl = configDictServiceI.getValueByTypeAndKeyNoCache("dooolyScheduleUrl","dooolyScheduleUrl");
                    String ret = HttpClientUtil.doPost(dooolyScheduleUrl + "/meituan/getStaffs",jsonObject.toJSONString());
                    List<Map<String,Object>> listMap = GsonUtils.son.fromJson(ret,new TypeToken<List<Map<String,Object>>>(){}.getType());
                    logger.info("美团免登录查询员工结果：{}",ret);
                    if (listMap != null && listMap.size() > 0) {
                        loginUrl = meituanService.easyLogin(token,adUser.getCardNumber(),adUser.getTelephone(),MeituanProductTypeEnum.getMeituanProductTypeByCode(productType));
                    } else {
                        //先同步用户
                        JSONObject jsonObjectUser = new JSONObject();
                        jsonObjectUser.put("name",adUser.getName());
                        jsonObjectUser.put("phone",adUser.getTelephone());
                        jsonObjectUser.put("entStaffNum",adUser.getCardNumber());
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.add(jsonObjectUser);
                        JSONObject jsonObjectParam = new JSONObject();
                        jsonObjectParam.put("staffInfoVOList",jsonArray);
                        jsonObjectParam.put("staffType",StaffTypeEnum.StaffTypeEnum30.getCode());
                        String retStaff = HttpClientUtil.doPost(dooolyScheduleUrl + "/meituan/batchSynStaffs",jsonObjectParam.toJSONString());
                        List<Map<String,Object>> listStaffMap = GsonUtils.son.fromJson(retStaff,new TypeToken<List<Map<String,Object>>>(){}.getType());
                        logger.info("美团免登录同步员工结果：{}",retStaff);
                        if (listStaffMap != null && listStaffMap.size() > 0) {
                            loginUrl = meituanService.easyLogin(token,adUser.getCardNumber(),adUser.getTelephone(),MeituanProductTypeEnum.getMeituanProductTypeByCode(productType));
                        }
                    }
                    logger.info("用户:{}免登录url:{}",adUser.getTelephone(),loginUrl);
                    response.sendRedirect(loginUrl);
                } catch (Exception e) {
                    logger.error("getMeituanEasyLoginUrl异常",e);
                }
            }
        }
        return loginUrl;
    }


    @GET
    @Path("/easyLogin")
    @Produces("application/json;charset=UTF-8")
    @Consumes("application/json;charset=UTF-8")
    @Override
    public Map<String, Object> easyLogin(@Context HttpServletRequest request) {
        String entToken = request.getParameter("entToken");
        logger.info("美团调用easyLogin：{}",entToken);
        Map<String,Object> retMap = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        int status = 0;
        String message = "调用失败";
        if (StringUtils.isNotEmpty(entToken)) {
            String userId = String.valueOf(stringRedisTemplate.boundValueOps(entToken).get());
            AdUser adUser = null;
            try {
                adUser = adUserDao.getById(Integer.parseInt(userId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (adUser != null) {
                status = 1;
                message = "调用成功";
                data.put("loginStatus",1);
                data.put("staffPhoneNo",adUser.getTelephone());
            }
        }
        retMap.put("status",status);
        retMap.put("message",message);
        retMap.put("data",data);
        logger.info("美团调用easyLogin ret：{}",GsonUtils.toString(retMap));
        return retMap;
    }

    @GET
    @Path("/payTest")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String payTest(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        try {
            response.sendRedirect("https://admin.doooly.com/reachtest/activity_v1.0.0/#/airport?a=11");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 从创建支付订单开始
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/pay")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public OrderMsg pay(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        JSONObject jsonObject = getJsonObjectFromRequest(request);
        logger.info("美团调用pay：{}",GsonUtils.toString(jsonObject));
        boolean signValid = true;//validSign(jsonObject);
        OrderMsg orderMsg = new OrderMsg(OrderMsg.success_code,OrderMsg.success_mess);
        try {
            if (signValid) {
                /**
                 * 未支付订单同步：{"amount":"45.00","orderNumber":"11063373","serialNumber":"0","price":"45.00","storesId":"A001",
                 * "businessId":"TEST_wf12e42a800b585piaoniu",
                 * "orderDetail":[{"amount":"45.00","category":"5","code":"3883739","goods":"测试展览演出","number":1,"price":"45.00",
                 * "tax":"0"},{"amount":"0.00","category":"0000","code":"0000","goods":"运费","number":1,"price":"0.00","tax":"0"}],
                 * "type":1,"orderDate":"2018-12-26 14:40:54","cardNumber":"88588800005"}
                 */
                //商家未支付订单同步接口
                //下单接口
                jsonObject.put("clientIp", IPUtils.getIpAddr(request));
                jsonObject.put("notifyUrl",MeituanConstants.url_meituan_pay_notify_doooly);
                orderMsg = meituanService.createOrderMeituan(jsonObject);
                logger.info("美团创建订单返回：{}",GsonUtils.son.toJson(orderMsg));
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("userId",orderMsg.getData().get("userId"));
                jsonObject1.put("orderSource","meituan");
                jsonObject1.put("return_url","https://app.jia-fu.cn/app-takeaway/h5/rec/sqt/checkstand");
                String redirectUrl = configDictServiceI.getValueByTypeAndKeyNoCache("MEITUAN_PAY_URL","MEITUAN_PAY_URL") +
                        orderMsg.getData().get("orderNum") +  meituanService.convertMapToUrlEncode(jsonObject1);
                logger.info("美团pay跳转url：{}",redirectUrl);
                orderMsg.getData().put("redirectUrl",redirectUrl);
                //response.sendRedirect(redirectUrl);
            } else {
                orderMsg = new OrderMsg(OrderMsg.invalid_sign_code,OrderMsg.invalid_sign_mess);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderMsg;
    }


    @POST
    @Path("/refund")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> refund(JSONObject jsonObject) {
        logger.info("美团调用refund：{}",GsonUtils.toString(jsonObject));
        boolean signValid = validSign2(jsonObject);
        Map<String,Object> retMap = new HashMap<>();
        if (signValid) {
            //商企通订单号
            String serialNum = jsonObject.getString("serialNum");
            if (StringUtils.isBlank(serialNum)) {
                retMap.put("status",500);
                retMap.put("msg","参数错误");
            }
            //退款
            OrderVo orderVo = orderService.getByOrderNum(serialNum);
            PayMsg payMsg = refundService.autoRefund(orderVo.getUserId(), orderVo.getOrderNumber());
            //ResultModel resultModel = refundService.applyRefund(orderVo.getUserId(), serialNum,String.valueOf(orderVo.getTotalMount()));
            if ("1000".equals(payMsg.getCode())) {
                retMap.put("status",0);
                retMap.put("msg","success");
            } else {
                retMap.put("status",500);
                retMap.put("msg",payMsg.getMess());
            }
        } else {
            retMap.put("status",500);
            retMap.put("msg","签名校验失败");
        }
        return retMap;
    }


    /**
     * 放到收银台
     * orderNum
     * amount
     * @param jsonObject
     * @return
     */
    @POST
    @Path("/payNotify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> payNotify(JSONObject jsonObject) {
        logger.info("调用美团支付通知：{}",GsonUtils.toString(jsonObject));
        Map<String,Object> retMap = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        params.put("token",MeituanConstants.token);
        params.put("version",MeituanConstants.version);

        String paramStr = jsonObject.getString("param");
        Map<String,Object> paramMap= GsonUtils.son.fromJson(paramStr,Map.class);
        String payStatus = String.valueOf(paramMap.get("payStatus"));
        String outTradeNo = String.valueOf(paramMap.get("outTradeNo"));
        String orderPrice = String.valueOf(paramMap.get("orderPrice"));
        String ret = "";
        if ("1".equals(payStatus)) {
            Map<String,Object> contentParams = new HashMap<>();
            contentParams.put("orderSN",jsonObject.getString("orderNum"));
            contentParams.put("amount",jsonObject.getString("amount"));
            contentParams.put("sign",MeituanConstants.sign);
            contentParams.put("ts",new Date().getTime()/1000);
            params.put("content",contentParams);
            ret = HttpClientUtil.doPost(MeituanConstants.url_meituan_pay_notify,GsonUtils.toString(params));
            retMap = GsonUtils.son.fromJson(ret,Map.class);
            Map<String,Object> dataMap = GsonUtils.son.fromJson(String.valueOf(retMap.get("data")),Map.class);
            logger.info("支付通知美团返回：{}",ret);
            if ("0".equals(String.valueOf(dataMap.get("status")))) {
                retMap.put("code",OrderMsg.success_code);
                retMap.put("info",OrderMsg.success_mess);
            } else {
                return null;
            }
        } else {
            retMap.put("payStatus",payStatus);
            retMap.put("outTradeNo",outTradeNo);
            retMap.put("orderPrice",orderPrice);
            logger.info("美团订单支付失败：{}",GsonUtils.son.toJson(retMap));
        }
        return retMap;
    }


    @POST
    @Path("/queryOrderByOrderNum")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object> queryOrderByOrderSN(JSONObject jsonObject) {
        String orderNum = jsonObject.getString("orderNum");
        Map<String,Object> retMap = new HashMap<>();
        if (StringUtils.isNotEmpty(orderNum)) {
            Map<String,Object> params = new HashMap<>();
            params.put("orderSN",orderNum);
            String dooolyScheduleUrl = configDictServiceI.getValueByTypeAndKeyNoCache("dooolyScheduleUrl","dooolyScheduleUrl");
            String ret = HttpClientUtil.doPost(dooolyScheduleUrl + "/meituan/queryOrderByOrderSN",GsonUtils.toString(params));
            retMap = GsonUtils.son.fromJson(ret,Map.class);
        }
        return retMap;
    }



    public static Map<String,String> getParamMapFromRequest(HttpServletRequest request) {
        Map<String,String> paramMap = new HashMap<>();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String key = (String)enu.nextElement();
            String value = request.getParameter(key);
            paramMap.put(key,value);
        }
        return paramMap;
    }


    public static JSONObject getJsonObjectFromRequest(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String charset = request.getParameter("encoding");
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String key = (String)enu.nextElement();
            String value = request.getParameter(key);
            try {
                value = new String(value.getBytes("iso-8859-1"),charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            jsonObject.put(key,value);
        }
        return jsonObject;
    }

    public static Map<String,String> getParamMapFromJson(JSONObject jsonObject) {
        Map<String,String> paramMap = new HashMap<>();
        for(Map.Entry<String,Object> entry : jsonObject.entrySet()) {
            paramMap.put(entry.getKey(),entry.getValue().toString());
        }
        return paramMap;
    }


    public static boolean validSign(JSONObject jsonObject) {
        Map<String,String> paramMap = getParamMapFromJson(jsonObject);
        String meituanSign  = paramMap.get("sign");
        paramMap.remove("sign");
        String signRaw = MeituanConstants.appSecret + concatParamsForSign(paramMap);
        String sign = DigestUtils. sha1Hex (signRaw).toUpperCase();
        boolean bool = sign.equals(meituanSign);
        if (!bool) {
            logger.info("验证签名失败，参数：{}，美团签名：{}，我方签名：{}",GsonUtils.toString(paramMap),meituanSign,sign);
        }
        return bool;
    }


    public static boolean validSign2(JSONObject jsonObject) {
        Map<String,String> paramMap = getParamMapFromJson(jsonObject);
        String meituanSign  = paramMap.get("sign");
        paramMap.remove("sign");
        String signRaw = MeituanConstants.exhaust_key + concatParamsForSign(paramMap);
        String sign = DigestUtils. sha1Hex (signRaw).toUpperCase();
        boolean bool = sign.equals(meituanSign);
        if (!bool) {
            logger.info("验证签名失败，参数：{}，美团签名：{}，我方签名：{}",GsonUtils.toString(paramMap),meituanSign,sign);
        }
        return bool;
    }



    public static String concatParamsForSign(Map<String, String> params) {
        Object[] key_arr = params.keySet().toArray();
        Arrays.sort(key_arr);
        String str = "";
        for (Object key : key_arr) {
            if(key.equals("sign")){
                continue;
            }
            String val = params.get(key);
            str += key + val;
        }
        return str;
    }
}