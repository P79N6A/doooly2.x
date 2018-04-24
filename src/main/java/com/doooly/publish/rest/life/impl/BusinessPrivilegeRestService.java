package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.BusinessPrivilegeServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.BusinessPrivilegeRestServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Description: 商户特权开通
 * @author: qing.zhang
 * @date: 2017-09-12
 */
@Component
@Path("/business/privilege")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BusinessPrivilegeRestService implements BusinessPrivilegeRestServiceI{

    private static Logger logger = Logger.getLogger(BusinessPrivilegeRestService.class);
    @Autowired
    private BusinessPrivilegeServiceI businessPrivilegeServiceI;

    @POST
    @Path(value = "/getActivityDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String getActivityDetail(JSONObject obj) {
        Integer businessId = obj.getInteger("businessId");
        Integer userId = obj.getInteger("userId");
        MessageDataBean messageDataBean = businessPrivilegeServiceI.getActivityDetail(businessId,userId);
        logger.info("商户特权活动活动页面返回值:"+messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }
    @POST
    @Path(value = "/apply")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String apply(JSONObject obj) {
        Integer businessId = obj.getInteger("businessId");
        Integer userId = obj.getInteger("userId");
        MessageDataBean messageDataBean = businessPrivilegeServiceI.apply(businessId,userId);
        logger.info("商户特权活动活动页面返回值:"+messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

}