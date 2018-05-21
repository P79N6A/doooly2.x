package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.ShareRecordServiceI;
import com.doooly.business.guide.service.AdArticleServiceI;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.AdArticleRestServiceI;
import com.doooly.publish.rest.life.ShareRecordRestServiceI;
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
 * @Description: 导购
 * @author: qing.zhang
 * @date: 2018-02-26
 */
@Component
@Path("/shareRecord")
public class ShareRecordRestService implements ShareRecordRestServiceI {

    private static final Logger logger = LoggerFactory.getLogger(ShareRecordRestService.class);

    @Autowired
    private ShareRecordServiceI shareRecordServiceI;

  

    /**
     * 判断是否进行分享记录
     */
    @POST
    @Path(value = "/isSetShareRecord")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
	public String isSetShareRecord(JSONObject json) {
		// TODO Auto-generated method stub
    	MessageDataBean messageDataBean = new MessageDataBean();
    	String telephone = json.getString("telephone");
    	String userId = json.getString("userId");
    	try {
			messageDataBean=shareRecordServiceI.isSetShareRecord(userId,telephone);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
		}
    	return messageDataBean.toJsonString();
	}


    /**
     * 对记录进行发券
     */
    @POST
    @Path(value = "/sendByShareRecord")
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
	public String sendByShareRecord(JSONObject json) {
		// TODO Auto-generated method stub
    	MessageDataBean messageDataBean = new MessageDataBean();
    	String telephone = json.getString("telephone");
    	String userId = json.getString("userId");
    	String telephoneUserId = json.getString("telephoneUserId");
    		try {
    			messageDataBean=shareRecordServiceI.sendByShareRecord(userId,telephone,telephoneUserId);
    		} catch (Exception e) {
    			e.printStackTrace();
    			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
    		}
    	return messageDataBean.toJsonString();
	}

}
