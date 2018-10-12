package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.oneNumber.service.OneNumberServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.OneNumberRestServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2017-08-28
 */
@Component
@Path("/oneNumber")
public class OneNumberRestService implements OneNumberRestServiceI{

    private static final Logger logger = LoggerFactory.getLogger(OneNumberRestService.class);

    @Autowired
    private OneNumberServiceI oneNumberServiceI;

    @POST
    @Path(value = "/getTargetUrl")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getTargetUrl(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        logger.info("请求参数为---->"+ json);
        try {
            String userId = json.getString("userId");
            String businessId = json.getString("businessId");
            String targetUrl = json.getString("targetUrl");
            messageDataBean = oneNumberServiceI.getTargetUrl(userId,businessId,targetUrl);
        } catch (Exception e) {
            logger.error("获取1号通跳转链接出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }
}
