package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.forum.invitation.AdForumInvitationServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.AdForumInvitationI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 论坛邀请
 * @Author: Mr.Wu
 * @Date: 2018/11/20
 */
@Component
@Path("/forumInvitation")
@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdForumInvitationImpl implements AdForumInvitationI {
    private static Logger log = LoggerFactory.getLogger(AdForumInvitationImpl.class);

    @Autowired
    AdForumInvitationServiceI adForumInvitationServiceI;

    /**
     * 论坛活动邀请报名注册
     * @param paramJSON
     * @return
     */
    @POST
    @Path(value = "/registerForum")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Override
    public String registerForum(JSONObject paramJSON) {
        log.info("论坛邀请报名参数={}", paramJSON);
        MessageDataBean messageDataBean= new MessageDataBean();
        messageDataBean = adForumInvitationServiceI.registerForum(paramJSON);
        return messageDataBean.toJsonString();
    }

    /**
     * 批量发送短信，一次性接口
     * @return
     */
    @POST
    @Path(value = "/batchSendMsg")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Override
    public String batchSendMsg() {
        log.info("论坛邀请批量发送短信");
        MessageDataBean messageDataBean= new MessageDataBean();
        adForumInvitationServiceI.batchSendMsg();
        messageDataBean.setCode("1000");
        messageDataBean.setMess("调用成功");
        return messageDataBean.toJsonString();
    }
}
