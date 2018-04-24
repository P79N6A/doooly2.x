package com.doooly.business.activity.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doooly.business.activity.AnswerQuestionBusinessServiceI;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdAnswerRecordDao;
import com.doooly.dao.reachad.AdQuestionOptionDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAnswerRecord;
import com.doooly.entity.reachad.AdQuestionOption;

/**
 * 答题活动业务Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
@Service
public class AnswerQuestionBusinessService implements AnswerQuestionBusinessServiceI {

	private static Logger logger = Logger.getLogger(AnswerQuestionBusinessService.class);

	@Autowired
	private AdQuestionOptionDao adQuestionOptionDao;
	@Autowired
	private AdAnswerRecordDao adAnswerRecordDao;

	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;

	@Override
	@Transactional
	public HashMap<String, Object> validateAnswer(String userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("验证是否答题 userId:" + userId);
			// 是否答题完成,是否需要发券
			List<AdAnswerRecord> answerFinishRecordList = adAnswerRecordDao.getAnswerRecord(userId);
			// 验证通过,可答题
			map.put("validateRecordList", null);
			if (answerFinishRecordList != null && answerFinishRecordList.size() > 0) {
				// 答题完成记录
				map.put("answerFinishRecordList", answerFinishRecordList);
				// 需要给用户发券
				if (answerFinishRecordList.get(0).getCouponCount() > 0) {
					List<AdAnswerRecord> recordList = adAnswerRecordDao.getAnswerCouponByUserId(userId);
					if (recordList != null && recordList.size() > 0) {
						map.put("validateRecordList", recordList);
					} else {
						map.put("answerFinishRecordList", null);
						// 删除答题记录
						adAnswerRecordDao.deleteRecordByUserId(userId);
					}
				}
			} else {
				// 删除答题记录
				adAnswerRecordDao.deleteRecordByUserId(userId);
			}
		} catch (Exception e) {
			logger.error("验证是否答题异常！！！", e);
		}
		return map;
	}
	
	@Override
	@Transactional
	public HashMap<String, Object> getTopicAndOptionsByUserIdAndOrder(String userId, String questionOrder,
			String optionType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info(String.format("【五一答题活动】会员id:%s 题目序号:%s 题目选项:%s", userId, questionOrder, optionType));
			// 根据题目序号 获取 题目及其选项
			List<AdQuestionOption> adQuestionOptionList = new ArrayList<AdQuestionOption>();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("questionOrder", questionOrder);
			if ("1".equals(questionOrder) && StringUtils.isEmpty(optionType)) {
				// 获取第一题的题目及其选项
				adQuestionOptionList = adQuestionOptionDao.getTopicAndOptionsFirst(paramMap);
			} else {
				// 获取下一题的题目及其选项
				paramMap.put("optionType", optionType);
				adQuestionOptionList = adQuestionOptionDao.getTopicAndOptionsNext(paramMap);
				// 根据答题序号跟选项 获取当前答题相关信息
				AdQuestionOption option = adQuestionOptionDao.getRecordByOrderAndType(paramMap);
				// 保存当前题答题记录
				this.insertAnswerRecord(option, userId);
			}
			map.put("adQuestionOptionList", adQuestionOptionList);
		} catch (Exception e) {
			logger.error("获取题目及其选项异常！！！", e);
		}
		return map;
	}

	@Override
	@Transactional
	public HashMap<String, Object> submitAnswer(String userId, String questionOrder, String optionType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info(String.format("【五一答题活动】会员id:%s 题目序号:%s 题目选项:%s", userId, questionOrder, optionType));
			// -----------------1.保存答题记录----------------
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("questionOrder", questionOrder);
			paramMap.put("optionType", optionType);
			// 根据答题序号跟选项 获取当前答题相关信息
			AdQuestionOption option = adQuestionOptionDao.getRecordByOrderAndType(paramMap);
			// 保存当前题答题记录
			this.insertAnswerRecord(option, userId);
			
			// -----------------2.获取心理年龄等信息----------------
			List<AdAnswerRecord> recordList = adAnswerRecordDao.getAnswerRecordAgeByUserId(userId);
			// 起始年龄——>20岁
			int mentalAge = 20;
			for (Iterator<AdAnswerRecord> iterator = recordList.iterator(); iterator.hasNext();) {
				AdAnswerRecord adRecord = iterator.next();
				mentalAge += adRecord.getAge();
				if(StringUtils.isBlank(adRecord.getCouponId())){
					iterator.remove();
				}
			}
			mentalAge = this.createRandomNum(mentalAge);
			
			// -----------------3.根据答题记录选项发券----------------
			for (Iterator<AdAnswerRecord> iterator = recordList.iterator(); iterator.hasNext();) {
				AdAnswerRecord adAnswerRecord = iterator.next();
				Integer couponId = Integer.valueOf(adAnswerRecord.getCouponId());
				Integer activityId = Integer.valueOf(adAnswerRecord.getActivityId());
				String productSn = adAnswerRecord.getProductSn();
				MessageDataBean messageDataBean = freeCouponBusinessServiceI.receiveCoupon(Integer.valueOf(userId),
						couponId, activityId, productSn);
				logger.info("提交答案submitAnswer发券：couponId:" + couponId + ",activityId:" + activityId + ",productSn:"
						+ productSn + ",userId:" + userId + ",code:" + messageDataBean.code);
				if (!MessageDataBean.success_code.equals(messageDataBean.getCode())) {
					iterator.remove();
				}
			}
			logger.info("获取用户心理年龄  userId:" + userId + ",mentalAge:" + mentalAge);
			map.put("mentalAge", mentalAge);
			map.put("recordList", recordList);
		} catch (Exception e) {
			logger.error("提交答案 获取心理年龄 发券 异常！！！", e);
		}
		return map;
	}

	/**
	 * 保存答题记录
	 * 
	 * @param option
	 *            答题信息
	 * @param userId
	 *            用户id
	 * @return
	 */
	private Integer insertAnswerRecord(AdQuestionOption option, String userId) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AdAnswerRecord record = new AdAnswerRecord();
		record.setQuestionId(option.getQuestionId());
		record.setOptionType(option.getType());
		record.setAnswerUser(Long.valueOf(userId));
		record.setAnswerTime(format.format(new Date()));
		record.setActivityId(option.getActivityId());
		record.setCouponId(option.getCouponId());
		record.setAge(option.getAge());
		record.setOrderNum(option.getQuestionOrder());
		return adAnswerRecordDao.insert(record);
	}

	/**
	 * 生成随机数
	 * 
	 */
	private int createRandomNum(int mentalAge) {
		int age = 0;
		Random random = new Random();
		if (mentalAge == 20) {
			age = mentalAge + random.nextInt(5);
		} else {
			age = mentalAge - random.nextInt(5);
		}
		return age;
	}

}
