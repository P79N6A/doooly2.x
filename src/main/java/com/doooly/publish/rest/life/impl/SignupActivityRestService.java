package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.SignupActivityServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdActivityComment;
import com.doooly.entity.reachad.AdSupport;
import com.doooly.publish.rest.life.SignupActivityRestServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * @Description: 活动报名接口实现
 * @author: qing.zhang
 * @date: 2017-04-25
 */
@Component
@Path("/signup")
public class SignupActivityRestService implements SignupActivityRestServiceI {
    private static final Logger logger = LoggerFactory.getLogger(SignupActivityRestService.class);

    @Autowired
    private SignupActivityServiceI signupActivityServiceI;

    /**
     * 获取活动详情
     */
    @POST
    @Path(value = "/getActivityDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getActivityDetail(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer activityId = json.getInteger("activityId");
            Integer supportUser = json.getInteger("supportUser");
            messageDataBean = signupActivityServiceI.getActivityDetail(activityId,supportUser);
        } catch (Exception e) {
            logger.error("获取活动详情出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取评论
     */
    @POST
    @Path(value = "/getAllComment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllComment(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer activityId = json.getInteger("activityId");
            Integer currentPage = json.getInteger("currentPage") == null ? 1 : json.getInteger("currentPage");//当前页
            Integer pageSize = json.getInteger("pageSize") == null ? 4 : json.getInteger("pageSize");//每页大小
            Integer supportUser = json.getInteger("supportUser");
            messageDataBean = signupActivityServiceI.getAllComment(activityId, currentPage, pageSize,supportUser);
            messageDataBean.setCode(MessageDataBean.success_code);
        } catch (Exception e) {
            logger.error("获取评论详情出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取报名表单
     */
    @POST
    @Path(value = "/getSignupForm")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSignupForm(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer activityId = json.getInteger("activityId");
            messageDataBean = signupActivityServiceI.getSignupForm(activityId);
            messageDataBean.setCode(MessageDataBean.success_code);
        } catch (Exception e) {
            logger.error("获取报名表单出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 报名
     */
    @POST
    @Path(value = "/signupActivity")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String signupActivity(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer joinUser = json.getInteger("joinUser");
            List<Map> adJoinUsers = (List<Map>) json.get("adJoinUsers");
            messageDataBean = signupActivityServiceI.signupActivity(joinUser,adJoinUsers);
        } catch (Exception e) {
            logger.error("报名失败", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 参赛选手
     */
    @POST
    @Path(value = "/getAllJoinUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllJoinUser(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer activityId = json.getInteger("activityId");
            Integer supportUser = json.getInteger("supportUser");
            messageDataBean = signupActivityServiceI.getAllJoinUser(activityId,supportUser);
            messageDataBean.setCode(MessageDataBean.success_code);
        } catch (Exception e) {
            logger.error("获取参赛选手出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 点赞
     * @param json
     * @return
     */
    @POST
    @Path(value = "/clickLike")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String clickLike(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            AdSupport adSupport = json.getObject("adSupport", AdSupport.class);
            messageDataBean = signupActivityServiceI.clickLike(adSupport);
        } catch (Exception e) {
            logger.error("点赞出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 浏览次数
     * @param json
     * @return
     */
    @POST
    @Path(value = "/browserCount")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String browserCount(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer activityId = json.getInteger("activityId");
            messageDataBean = signupActivityServiceI.browserCount(activityId);
        } catch (Exception e) {
            logger.error("更新浏览次数", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取地址
     * @param json
     * @return
     */
    @POST
    @Path(value = "/getMap")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getMap(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer activityId = json.getInteger("activityId");
            messageDataBean = signupActivityServiceI.getMap(activityId);
        } catch (Exception e) {
            logger.error("获取地址", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 上传评论
     *
     * @param json
     * @return
     */
    @POST
    @Path(value = "/saveActivityComment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveActivityComment(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            AdActivityComment adActivityComment = json.getObject("adActivityComment", AdActivityComment.class);
            if (adActivityComment != null) {
                signupActivityServiceI.saveActivityComment(adActivityComment);
                messageDataBean.setCode(MessageDataBean.success_code);
            } else {
                messageDataBean.setCode(MessageDataBean.failure_code);
            }
        } catch (Exception e) {
            logger.error("上传评论出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }
}
