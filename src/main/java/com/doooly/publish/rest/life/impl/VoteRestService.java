package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.VoteServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.VoteRestServiceI;
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
 * @Description: 中油投票活动实现
 * @author: qing.zhang
 * @date: 2017-05-31
 */
@Component
@Path("/vote")
public class VoteRestService implements VoteRestServiceI{
    private static final Logger logger = LoggerFactory.getLogger(VoteRestService.class);

    @Autowired
    private VoteServiceI voteServiceI;

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
            String userWechatOpenId = json.getString("userWechatOpenId");
            messageDataBean = voteServiceI.getActivityDetail(activityId,userWechatOpenId);
        } catch (Exception e) {
            logger.error("获取活动详情出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取首页选项详情
     */
    @POST
    @Path(value = "/findVoteOptionList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findVoteOptionList(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userWechatOpenId = json.getString("userWechatOpenId");
            messageDataBean = voteServiceI.findVoteOptionList(userWechatOpenId);
        } catch (Exception e) {
            logger.error("获取活动详情出错", e);
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
            messageDataBean = voteServiceI.browserCount(activityId);
        } catch (Exception e) {
            logger.error("更新浏览次数", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }


    /**
     * 投票
     * @param json
     * @return
     */
    @POST
    @Path(value = "/clickVote")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String clickVote(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            Integer activityId = json.getInteger("activityId");
            Integer optionId = json.getInteger("optionId");
            String userWechatOpenId = json.getString("userWechatOpenId");
            messageDataBean = voteServiceI.clickVote(activityId,optionId,userWechatOpenId);
        } catch (Exception e) {
            logger.error("投票出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取投票总数详情
     */
    @POST
    @Path(value = "/findVoteCountByOpenId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findVoteCountByOpenId(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            String userWechatOpenId = json.getString("userWechatOpenId");
            messageDataBean = voteServiceI.findVoteCountByOpenId(userWechatOpenId);
        } catch (Exception e) {
            logger.error("获取投票总数出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

    /**
     * 获取选项列表详情
     */
    @POST
    @Path(value = "/findVoteOption")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findVoteOption(JSONObject json) {
        MessageDataBean messageDataBean = new MessageDataBean();
        try {
            messageDataBean = voteServiceI.findVoteOption();
        } catch (Exception e) {
            logger.error("获取投票总数出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
        }
        return messageDataBean.toJsonString();
    }

}
