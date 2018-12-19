package com.doooly.publish.rest.meituan.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.meituan.MeituanService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.service.RefundService;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.business.payment.impl.NewPaymentService;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.OrderMsg;
import com.doooly.entity.reachad.AdUser;
import com.doooly.publish.rest.meituan.MeituanRestService;
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
        return retMap;
    }

    @GET
    @Path("/payTest")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public void payTest(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        try {
            response.sendRedirect("https://admin.doooly.com/reachtest/activity_v1.0.0/#/airport?a=11");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GET
    @Path("/pay")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public OrderMsg pay(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        JSONObject jsonObject = getJsonObjectFromRequest(request);
        logger.info("美团调用pay：{}",GsonUtils.toString(jsonObject));
        boolean signValid = validSign(jsonObject);
        OrderMsg orderMsg = null;
        try {
            if (signValid) {
                //orderMsg = meituanService.createOrderMeituan(jsonObject);
                response.sendRedirect(configDictServiceI.getValueByTypeAndKey("MEITUAN_PAY_URL","MEITUAN_PAY_URL"));
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
            OrderVo orderVo = orderService.getByOrderNum(serialNum);
            ResultModel resultModel = refundService.applyRefund(orderVo.getUserId(), serialNum,String.valueOf(orderVo.getTotalMount()));
            if (resultModel.getCode() == 1000) {
                retMap.put("status",0);
                retMap.put("msg","success");
            } else {
                retMap.put("status",500);
                retMap.put("msg",resultModel.getInfo());
            }
        } else {
            retMap.put("status",500);
            retMap.put("msg","签名校验失败");
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
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String key = (String)enu.nextElement();
            String value = request.getParameter(key);
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
