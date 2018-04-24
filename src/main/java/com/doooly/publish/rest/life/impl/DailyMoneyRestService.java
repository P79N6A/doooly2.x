package com.doooly.publish.rest.life.impl;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dailymoney.service.DailyMoneyBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.DailyMoneyRestServiceI;

/**
 * 每日赚钱Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
@Component
@Path("/dailyMoney")
public class DailyMoneyRestService implements DailyMoneyRestServiceI {

	private static Logger logger = Logger.getLogger(DailyMoneyRestService.class);

	@Autowired
	private DailyMoneyBusinessServiceI dailyMoneyBusinessService;

	@POST
	@Path(value = "/answerSituation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getAnswerSituation(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 获取用户答题情况(包括昨日收入、总财富值等)
			HashMap<String, Object> map = dailyMoneyBusinessService.getAnswerSituation(userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取用户答题情况异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/showQuestion")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String showQuestion(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 获取题目及其选项
			HashMap<String, Object> map = dailyMoneyBusinessService.getTopicAndOptions(userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取题目及其选项异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/answerQuestion")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String answerQuestion(JSONObject obj) {
		// 回答问题 提交答案
		Integer result = dailyMoneyBusinessService.saveAnswerRecord(obj);
		logger.info("新增答题记录result:" + result);
		MessageDataBean messageDataBean = new MessageDataBean();
		messageDataBean.setCode(result == 1 ? MessageDataBean.success_code : MessageDataBean.failure_code);
		return messageDataBean.toJsonString();
	}

	@Override
	public void dailyMoneyScheduled(JSONObject obj) {
		dailyMoneyBusinessService.updateUserWealth();
	}

}
