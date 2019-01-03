package com.doooly.publish.rest.activity.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.activity.ActivityPublishI;
import com.doooly.service.activity.ActivityServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 运营活动接口实现类
 * @Author: Mr.Wu
 * @Date: 2018/12/30
 */
@Component
@Path("/act/activity")
public class ActivityPublishImpl implements ActivityPublishI {
    private static Logger logger = LoggerFactory.getLogger(ActivityPublishImpl.class);

    @Autowired
    private ActivityServiceI activityService;

    /**
     * 生成活动key
     * @param obj
     *      activityId: 活动ID
     *      keyNum: 生成数量
     * @return
     */
    @Override
    @POST
    @Path(value = "/generateKey")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generateKey(JSONObject obj) {
        String activityId = obj.getString("activityId");
        Integer keyNum = obj.getInteger("keyNum");

        if (keyNum != null && keyNum > 0) {
            return activityService.generateKey(activityId, keyNum);
        } else {
            return new MessageDataBean(MessageDataBean.failure_code, "keyNum is null or it's less than 0").toJSONString();
        }
    }

    /**
     * 通过手机号码参加活动
     * @param obj
     *      activityId：活动标识（活动唯一标识，注意，不是表id）
     *      phone：手机号码
     *      groupId: 企业id
     * @return
     */
    @Override
    @POST
    @Path(value = "/campaignByPhone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String campaignByPhone(JSONObject obj) {
        return activityService.campaignByPhone(obj);
    }

    /**
     * 获得中奖纪录
     * @param obj
     *  activityId: 活动id
     *  rowNum: 显示行数
     * @return
     */
    @Override
    @GET
    @Path(value = "/winningRecord")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getWinningRecord(JSONObject obj) {
        String activityId = obj.getString("activityId");
        Integer rowNum = obj.getInteger("rowNum");

        if (rowNum == null || rowNum <= 0) {
            rowNum = 100;
        }
        return activityService.getWinningRecord(activityId, rowNum);
    }

}
