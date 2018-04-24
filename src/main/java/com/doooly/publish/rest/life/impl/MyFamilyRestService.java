package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myfamily.MyFamilyServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.MyFamilyRestServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 我的家属接口实现
 * @author: qing.zhang
 * @date: 2017-07-25
 */
@Component
@Path("/myfamily")
public class MyFamilyRestService implements MyFamilyRestServiceI{
    private static final Logger logger = LoggerFactory.getLogger(MyFamilyRestService.class);

    @Autowired
    private MyFamilyServiceI myFamilyServiceI;

    /**
     * 获取家庭详情
     */
    @POST
    @Path(value = "/getMyFamily")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getMyFamily(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer userId = json.getInteger("userId");
            messageDataBean = myFamilyServiceI.getMyFamily(userId);
        } catch (Exception e) {
            logger.error("获取家庭出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 初始化家庭
     * @param json
     * @return
     */
    @POST
    @Path(value = "/initMyFamily")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String initMyFamily(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer userId = json.getInteger("userId");
            List<HashMap<String, Object>> invitationFamilyList = (List<HashMap<String, Object>>) json.get("invitationFamilyList");
            messageDataBean = myFamilyServiceI.initMyFamily(userId,invitationFamilyList);
        } catch (Exception e) {
            logger.error("初始化家庭出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取家庭列表信息
     * @param json
     * @return
     */
    @POST
    @Path(value = "/getMyFamilyList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getMyFamilyList(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer userId = json.getInteger("userId");
            messageDataBean = myFamilyServiceI.getMyFamilyList(userId);
        } catch (Exception e) {
            logger.error("获取家庭列表信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 确认同意开启积分共享
     * @param json
     * @return
     */
    @POST
    @Path(value = "/confirmSharePoint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String confirmSharePoint(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer userId = json.getInteger("userId");
            Integer isPointShare = json.getInteger("isPointShare");
            messageDataBean = myFamilyServiceI.confirmSharePoint(userId,isPointShare);
        } catch (Exception e) {
            logger.error("确认同意开启积分共享出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }


}
