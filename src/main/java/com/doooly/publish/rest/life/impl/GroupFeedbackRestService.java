package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myaccount.service.GroupFeedbackBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.GroupFeedbackRestServiceI;

/**
 * 反馈给公司Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年3月6日
 */
@Component
@Path("/myaccount")
public class GroupFeedbackRestService implements GroupFeedbackRestServiceI {

	private static Logger logger = Logger.getLogger(GroupFeedbackRestService.class);

	@Autowired
	private GroupFeedbackBusinessServiceI groupFeedbackBusinessService;

	@Override
	@POST
	@Path(value = "/saveFeedback")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveFeedback(JSONObject obj) {
		// 保存企业反馈
		Integer result = groupFeedbackBusinessService.saveFeedback(obj);
		logger.info("新增企业反馈result:" + result);
		MessageDataBean messageDataBean = new MessageDataBean();
		messageDataBean.setCode(result == 1 ? MessageDataBean.success_code : MessageDataBean.failure_code);
		return messageDataBean.toJsonString();
	}

}
