package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myaccount.service.AdSystemNoticeServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.SystemNoitceRestServiceI;
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

@Component
@Path("/systemNoitce")
public class SystemNoitceRestService implements SystemNoitceRestServiceI {

    private static Logger logger = Logger.getLogger(SystemNoitceRestService.class);
    @Autowired
    private AdSystemNoticeServiceI systemNoitceService;

    @POST
    @Path(value = "/list")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSystemNoitceList(JSONObject obj, @Context HttpServletRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String token = request.getHeader("token");
            logger.info("APP访问,当前用户token:" + token);
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/微信
            Integer currentPage = obj.getInteger("currentPage");
            Integer pageSize = obj.getInteger("pageSize");
            String versionCode = obj.getString("versionCode");//版本号
            messageDataBean = systemNoitceService.getSystemNoitceList(userId, target,currentPage,pageSize,token,versionCode);
        } catch (Exception e) {
            logger.error("获取消息通知列表数据异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/typeList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSystemNoitceByTypeList(JSONObject obj, @Context HttpServletRequest request) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String token = request.getHeader("token");
            logger.info("APP访问,当前用户token:" + token);
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/iso/android
            Integer currentPage = obj.getInteger("currentPage");
            Integer pageSize = obj.getInteger("pageSize");
            String versionCode = obj.getString("versionCode");//版本号
            String noticeType = obj.getString("noticeType");//推送类型
            messageDataBean = systemNoitceService.getSystemNoitceByTypeList(userId, target,currentPage,pageSize,token,versionCode,noticeType);
        } catch (Exception e) {
            logger.error("获取消息通知列表数据异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/getNoReadNum")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getNoReadNum(JSONObject obj) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/微信
            messageDataBean = systemNoitceService.getNoReadNum(userId, target);
        } catch (Exception e) {
            logger.error("获取未读消息数据异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/getNoReadNumByType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getNoReadNumByType(JSONObject obj) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/微信
            String noticeType = obj.getString("noticeType");//类型 app/微信
            messageDataBean = systemNoitceService.getNoReadNumByType(userId, target,noticeType);
        } catch (Exception e) {
            logger.error("获取未读消息数据异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/getListByType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getListByType(JSONObject obj) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/微信
            messageDataBean = systemNoitceService.getListByType(userId, target);
        } catch (Exception e) {
            logger.error("获取未读消息数据异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/updateReadType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateReadType(JSONObject obj) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/微信
            messageDataBean = systemNoitceService.updateReadType(userId, target);
        } catch (Exception e) {
            logger.error("更新消息读取状态异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/updateReadNoticeType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateReadNoticeType(JSONObject obj) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/微信
            String noticeType = obj.getString("noticeType");//消息类别
            messageDataBean = systemNoitceService.updateReadNoticeType(userId, target,noticeType);
        } catch (Exception e) {
            logger.error("更新消息读取状态异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/send")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sendMessage(JSONObject obj) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userId = obj.getString("userId");
            String target = obj.getString("target");//类型 app/微信
            String content = obj.getString("content");//内容
            messageDataBean = systemNoitceService.sendMessage(userId, target,content);
        } catch (Exception e) {
            logger.error("发送个人消息异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/sendAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sendMessageToAll(JSONObject obj) {
        logger.info("发送消息开始");
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String content = obj.getString("content");//内容
            messageDataBean = systemNoitceService.sendMessage(content);
        } catch (Exception e) {
            logger.error("发送全局消息异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }

    @POST
    @Path(value = "/sendUrlAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sendUrlMessageToAll(JSONObject obj) {
        logger.info("发送消息开始");
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String content = obj.getString("content");//内容
            String url = obj.getString("newTargetUrl");//跳转链接
            String target = obj.getString("target");//跳转链接
            messageDataBean = systemNoitceService.sendUrlMessage(content,url,target);
        } catch (Exception e) {
            logger.error("发送全局消息异常！",e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        logger.info(messageDataBean.toJsonString());
        return messageDataBean.toJsonString();
    }
}
