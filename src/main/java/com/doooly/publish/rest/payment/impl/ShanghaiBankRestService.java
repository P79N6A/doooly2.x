package com.doooly.publish.rest.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.shanghaibank.service.ShangHaiBankService;
import com.doooly.common.util.IdGeneratorUtil;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.payment.ShanghaiBankRestServiceI;
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
 * @Description: 上海银行相关接口控制器
 * @author: qing.zhang
 * @date: 2018-05-29
 */
@Component
@Path("/shangHaiBank")
public class ShanghaiBankRestService implements ShanghaiBankRestServiceI {

    private static final Logger logger = LoggerFactory.getLogger(ShanghaiBankRestService.class);

    @Autowired
    private ShangHaiBankService shangHaiBankService;

    /**
     * 虚账户开户
     *
     * @param json
     * @return
     */
    @POST
    @Path(value = "/c19SCrVirSingleAccount")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String c19SCrVirSingleAccount(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String cNName = json.getString("cNName");
            Integer type = json.getInteger("type");
            String businessId = json.getString("businessId");
            Long groupId = json.getLong("groupId");
            messageDataBean = shangHaiBankService.c19SCrVirSingleAccount(cNName,type,businessId,groupId);
        } catch (Exception e) {
            logger.error("上海银行虚账户开户失败", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 虚账户提款
     *
     * @param json
     * @return
     */
    @POST
    @Path(value = "/c19VirWithDrawalsInq")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String c19VirWithDrawalsInq(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String eAcctNo = json.getString("eAcctNo");
            String amount  = String.valueOf(json.getBigDecimal("amount").setScale(2, BigDecimal.ROUND_DOWN));
            String usage = json.getString("usage");
            String channelFlowNo = IdGeneratorUtil.getOrderNumber(3);
            messageDataBean = shangHaiBankService.c19VirWithDrawalsInq(channelFlowNo, eAcctNo, amount, usage);
        } catch (Exception e) {
            logger.error("上海银行虚账户提款失败", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 虚账户代缴
     *
     * @param json
     * @return
     */
    @POST
    @Path(value = "/c19SingleCharge")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String c19SingleCharge(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String businessId = json.getString("businessId");
            String eAcctNo = json.getString("eAcctNo");
            String amount  = String.valueOf(json.getBigDecimal("amount").setScale(2, BigDecimal.ROUND_DOWN));
            String sign = json.getString("sign");
            messageDataBean = shangHaiBankService.c19SingleCharge(businessId, eAcctNo, amount,sign);
        } catch (Exception e) {
            logger.error("上海银行虚账户代缴失败", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }


    /**
     * 虚账户代发
     *
     * @param json
     * @return
     */
    @POST
    @Path(value = "/c19VirSReTrigSer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
    public String c19VirSReTrigSer(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String amount  = String.valueOf(json.getBigDecimal("amount").setScale(2, BigDecimal.ROUND_DOWN));
            String businessId = json.getString("businessId");
            String groupId = json.getString("groupId");
            String type = json.getString("type");
            messageDataBean = shangHaiBankService.c19VirSReTrigSer(amount,businessId,groupId,type);
        } catch (Exception e) {
            logger.error("上海银行虚账户代发失败", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }


    /**
     * 虚账户打款到账通知
     *
     * @param json
     * @return
     */
    @POST
    @Path(value = "/c19WithDrawalsNotice")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED})
    public String c19WithDrawalsNotice(JSONObject json) {
        String checkvalue = json.getString("checkvalue");
        String encmsg = json.getString("encmsg");
        String service = json.getString("service");
        return shangHaiBankService.c19WithDrawalsNotice(checkvalue, encmsg, service);
    }

    /**
     * 虚账户余额查询到账通知
     *
     * @param json
     * @return
     */
    @POST
    @Path(value = "/c19VirAcctBlanceInq")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String c19VirAcctBlanceInq(JSONObject json) {
        String cNName = json.getString("cNName");
        return shangHaiBankService.c19VirAcctBlanceInq(cNName).toJsonString();
    }
}
