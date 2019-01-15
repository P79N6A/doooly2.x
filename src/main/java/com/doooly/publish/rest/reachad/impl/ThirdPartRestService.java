package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.thirdpart.ThirdPartServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.impl.UserRestService;
import com.doooly.publish.rest.reachad.ThirdPartRestServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @Description: 第三方登录
 * @author: qing.zhang
 * @date: 2019-01-15
 */
@Component
@Path("/third")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThirdPartRestService implements ThirdPartRestServiceI{

    private static Logger logger = Logger.getLogger(UserRestService.class);

    @Autowired
    private ThirdPartServiceI thirdPartServiceI;

    @POST
    @Path(value = "/getGroupInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getGroupInfo(JSONObject json,@Context HttpServletRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            messageDataBean = thirdPartServiceI.getGroupInfo(json,request);
        } catch (Exception e) {
            logger.error("获取企业信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/thirdLogin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String thirdLogin(JSONObject json,@Context HttpServletRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            messageDataBean = thirdPartServiceI.thirdLogin(json,request);
        } catch (Exception e) {
            logger.error("第三方登录出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
    }
}
