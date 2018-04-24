package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.appversion.AppVersionServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.AppVersionRestServiceI;
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
 * @Description: app 版本信息service
 * @author: qing.zhang
 * @date: 2017-07-31
 */
@Component
@Path("/doooly")
public class AppVersionRestService implements AppVersionRestServiceI {

    private static final Logger logger = LoggerFactory.getLogger(AppVersionRestService.class);

    @Autowired
    private AppVersionServiceI appVersionServiceI;

    /**
     * 获取版本信息
     */
    @POST
    @Path(value = "/getVersionInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getVersionInfo(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String uniqueIdentification = json.getString("uniqueIdentification");
            String versionCode = json.getString("versionCode");
            String deviceNumber = json.getString("deviceNumber");
            String deviceName = json.getString("deviceName");
            String type = json.getString("type");
            messageDataBean = appVersionServiceI.getVersionInfo(uniqueIdentification, versionCode, deviceNumber, deviceName, type);
        } catch (Exception e) {
            logger.error("获取版本信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 更新版本下载链接信息
     */
    @POST
    @Path(value = "/updateDownloadUrl")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateDownloadUrl(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String downloadUrl = json.getString("downloadUrl");
            String versionCode = json.getString("versionCode");
            String type = json.getString("type");
            messageDataBean = appVersionServiceI.updateDownloadUrl(downloadUrl, type, versionCode);
        } catch (Exception e) {
            logger.error("获取版本信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 更新版本下载链接信息
     */
    @POST
    @Path(value = "/getDownloadUrl")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getDownloadUrl(JSONObject json) {
        logger.info("进入查询app系在地址");
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String type = json.getString("type");
            logger.info("type==========" + type + "===========");
            messageDataBean = appVersionServiceI.getDownloadUrl(type);
            logger.info("messageDataBean==========" + messageDataBean + "===========");
        } catch (Exception e) {
            logger.error("获取版本信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }


    /**
     * 获取app启动图片链接信息
     */
    @POST
    @Path(value = "/getStartUpUrl")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getStartUpUrl(JSONObject json) {
        logger.info("进入查询app启动图片地址");
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String groupNum = json.getString("groupNum");//企业id
            String versions = json.getString("versions");//当前版本
            logger.info("groupId==========versions=========" + groupNum + "===========" + versions + "===========");
            messageDataBean = appVersionServiceI.getStartUpUrl(groupNum, versions);
            logger.info("messageDataBean==========" + messageDataBean + "===========");
        } catch (Exception e) {
            logger.error("获取版本信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 保存用户和极光绑定信息
     */
    @POST
    @Path(value = "/saveRegistrationID")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveRegistrationID(JSONObject json) {
        logger.info("保存用户和极光绑定信息开始");
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String uniqueIdentification = json.getString("uniqueIdentification");
            String registrationId = json.getString("registrationId");
            String userId = json.getString("userId");
            String type = json.getString("type");
            logger.info("uniqueIdentification==========registrationId=========userId==========type=========" + uniqueIdentification + "==========="
                    + registrationId + "===========" + userId + "===========" + type + "===========");
            messageDataBean = appVersionServiceI.saveRegistrationID(uniqueIdentification, registrationId,userId,type);
            logger.info("messageDataBean==========" + messageDataBean + "===========");
        } catch (Exception e) {
            logger.error("保存用户和极光绑定信息出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }
}
