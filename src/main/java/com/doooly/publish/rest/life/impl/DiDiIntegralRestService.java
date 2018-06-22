package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.didi.service.DiDiIntegralServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.DiDiIntegralRestServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

/**
 * @Description: 滴滴
 * @author: qing.zhang
 * @date: 2018-03-06
 */
@Component
@Path("/didiIntegral")
public class DiDiIntegralRestService implements DiDiIntegralRestServiceI{

    private static final Logger logger = LoggerFactory.getLogger(AppVersionRestService.class);

    @Autowired
    private DiDiIntegralServiceI diDiIntegralServiceI;

    /**
     * 进入商户详情页
     * @param json
     * @return
     */
    @POST
    @Path("getDiDiIntegral")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getDiDiIntegral(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Long businessId = json.getLong("businessId");
            Long userId = json.getLong("userId");// 会员id
            messageDataBean = diDiIntegralServiceI.getDiDiIntegral(businessId, userId);
        } catch (Exception e) {
            logger.error("获取商户详情页信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 积分兑换接口
     * @param json
     * @return
     */
    @POST
    @Path("exchangeIntegral")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String exchangeIntegral(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Long businessId = json.getLong("businessId");//商户id
            Long userId = json.getLong("userId");// 会员id
            BigDecimal amount = json.getBigDecimal("amount");//兑换积分数量
            String orderNumber = json.getString("orderNumber");//订单号
            Integer code = json.getInteger("code");// 验证码
            if("1000061".equals(String.valueOf(userId))){
                messageDataBean = diDiIntegralServiceI.exchangeIntegral(businessId, userId,amount,code,orderNumber);
            }else {
                messageDataBean.setCode(MessageDataBean.failure_code);
                messageDataBean.setMess("环境错误");
            }
        } catch (Exception e) {
            logger.error("积分兑换出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 进入积分兑换页面
     * @param json
     * @return
     */
    @POST
    @Path("toExchangeIntegral")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String toExchangeIntegral(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Long businessId = json.getLong("businessId");//商户id
            Long userId = json.getLong("userId");// 会员id
            messageDataBean = diDiIntegralServiceI.toExchangeIntegral(businessId, userId);
        } catch (Exception e) {
            logger.error("积分兑换出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }


    /**
     * 获取积分消费验证码接口
     * @param json
     * @return
     */
    @POST
    @Path("getCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getCode(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Long businessId = json.getLong("businessId");//商户id
            Long userId = json.getLong("userId");// 会员id
            BigDecimal amount = json.getBigDecimal("amount");//兑换积分数量
            String orderNumber = json.getString("orderNumber");//订单号
            messageDataBean = diDiIntegralServiceI.getCode(businessId, userId,amount,orderNumber);
        } catch (Exception e) {
            logger.error("积分兑换出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }
}
