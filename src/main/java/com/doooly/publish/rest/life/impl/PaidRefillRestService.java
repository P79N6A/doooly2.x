package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.PaidRefillServiceI;
import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.AdArticleRestServiceI;
import com.doooly.publish.rest.life.PaidRefillRestServiceI;
import com.sun.jersey.spi.container.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Description: 立即参与
 * @author: wenwei.yang
 * @date: 2018-02-26
 */
@Component
@Path("/paidRefill")
public class PaidRefillRestService implements PaidRefillRestServiceI {

    private static final Logger logger = LoggerFactory.getLogger(PaidRefillRestService.class);

    @Autowired
    private PaidRefillServiceI paidRefillServiceI;
    @Autowired
    private StringRedisTemplate redisTemplate;

  
    @POST
    @Path(value = "/partInstant")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
	public String paidRefill(JSONObject json) {
    	MessageDataBean messageDataBean = new MessageDataBean();
        try {
            
            messageDataBean = paidRefillServiceI.getPaidRefill(json);
        } catch (Exception e) {
            logger.error("话费充值活动活动立即参与出错", e);
            messageDataBean.setCode(MessageDataBean.failure_code);
            messageDataBean.setMess(MessageDataBean.failure_mess);
        }
        return messageDataBean.toJsonString();
	}
    @POST
    @Path(value = "/isHadDone")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String isHadDone(JSONObject json) {
    	MessageDataBean messageDataBean = new MessageDataBean();
    	try {
    		String userId = json.getString("userId");//商品id
    		messageDataBean = paidRefillServiceI.getIsHadDone(userId);
    	} catch (Exception e) {
    		logger.error("话费充值活动活动判断是否参与出错", e);
    		messageDataBean.setCode(MessageDataBean.failure_code);
    		messageDataBean.setMess(MessageDataBean.failure_mess);
    	}
    	return messageDataBean.toJsonString();
    }
    @POST
    @Path(value = "/getQRCode")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String getQRCode(JSONObject json) {
    	MessageDataBean messageDataBean = new MessageDataBean();
    	try {
            String openId = json.getString("openId");//子Openid
            String channel = json.getString("channel");//channel
            String activityId = json.getString("activityId");//活动id
    		messageDataBean = paidRefillServiceI.getQRCode(openId,channel,activityId);
    	} catch (Exception e) {
    		logger.error("话费充值活动活动,二维码落地页获取二维码参数出错", e);
    		messageDataBean.setCode(MessageDataBean.failure_code);
    		messageDataBean.setMess(MessageDataBean.failure_mess);
    	}
    	return messageDataBean.toJsonString();
    }

}
