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
import com.doooly.business.activity.AnswerQuestionBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.AnswerQuestionRestServiceI;

/**
 * 答题活动 rest service实现
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
@Component
@Path("/answer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnswerQuestionRestService implements AnswerQuestionRestServiceI {

	private static Logger logger = Logger.getLogger(AnswerQuestionRestService.class);

	@Autowired
	private AnswerQuestionBusinessServiceI answerQuestionBusinessService;

	@POST
	@Path(value = "/showQuestion")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String showQuestion(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 获取题目序号
			String questionOrder = obj.getString("questionOrder");
			// 获取题目选项类型
			String optionType = obj.getString("optionType");
			// 获取题目及其选项
			HashMap<String, Object> map = answerQuestionBusinessService.getTopicAndOptionsByUserIdAndOrder(userId,
					questionOrder, optionType);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取题目及其选项异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/submitAnswer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String submitAnswer(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 获取题目序号
			String questionOrder = obj.getString("questionOrder");
			// 获取题目选项类型
			String optionType = obj.getString("optionType");
			// 提交答案
			HashMap<String, Object> map = answerQuestionBusinessService.submitAnswer(userId, questionOrder, optionType);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("提交答案 获取心理年龄 发券 异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	@POST
	@Path(value = "/validateAnswer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String validateAnswer(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 返回验证结果
			HashMap<String, Object> map = answerQuestionBusinessService.validateAnswer(userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取验证答题结果异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
