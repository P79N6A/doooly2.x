package com.doooly.business.dailymoney.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dailymoney.service.DailyMoneyBusinessServiceI;
import com.doooly.business.redisUtil.RedisUtilService;
import com.doooly.dao.reachad.AdAnswerRecordDao;
import com.doooly.dao.reachad.AdQuestionOptionDao;
import com.doooly.dao.reachad.AdQuestionRuleDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachlife.LifeActivityDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAnswerRecord;
import com.doooly.entity.reachad.AdQuestionOption;
import com.doooly.entity.reachad.AdQuestionRule;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachlife.LifeActivity;

/**
 * 每日赚钱业务Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
@Service
@Transactional
public class DailyMoneyBusinessService implements DailyMoneyBusinessServiceI {

	private static Logger logger = Logger.getLogger(DailyMoneyBusinessService.class);

	@Autowired
	private AdAnswerRecordDao adAnswerRecordDao;
	@Autowired
	private AdQuestionOptionDao adQuestionOptionDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdQuestionRuleDao adQuestionRuleDao;
	@Autowired
	private LifeActivityDao lifeActivityDao;
	/** redis工具类 */
	@Autowired
	private RedisUtilService redisUtilService;
	@Override
	public HashMap<String, Object> getAnswerSituation(String userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 获取用户财富值
			int totalWealth = adUserDao.getTotalWealth(userId);
			logger.info("会员Id:" + userId + ",财富值:" + totalWealth);

			// 获取用户四天内的答题记录
			List<AdAnswerRecord> recordList = adAnswerRecordDao.findRecentlyAnswerRecords(userId);
			logger.info("会员Id:" + userId + ",最近四天答题记录:" + recordList);

			// 获取用户总财富值在所有用户中的百分比
			int totalWealthPercent = adUserDao.getTotalWealthPercent(totalWealth);
			logger.info("会员Id:" + userId + ",总财富值在所有用户中的百分比:" + totalWealthPercent + "%");

			// 获取答题记录中的连续答对天数
			AdAnswerRecord record = adAnswerRecordDao.getAnswerRecordByUserId(userId, 1);
			int continueRightDays = (record == null ? 0 : record.getContinueRightDays());

			map.put("totalWealth", totalWealth);
			map.put("recordList", recordList);
			map.put("totalWealthPercent", totalWealthPercent);
			map.put("continueRightDays", continueRightDays);
		} catch (Exception e) {
			logger.error("获取用户答题情况异常！！！");
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getTopicAndOptions(String userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 根据今天的答题记录查找题目及其选项,保证当天该用户的题目相同
			List<AdQuestionOption> optionList = adQuestionOptionDao.getTopicAndOptionsByRecord(userId, new Date());
			if (optionList == null || optionList.isEmpty()) {
				// 若今天无答题记录，则随机抽取一道题目
				optionList = adQuestionOptionDao.getTopicAndOptionsRandom();
				// 保存一条答题记录(不包含选项)
				this.insertAnswerRecord(optionList.get(0).getQuestionId(), userId);
			}
			map.put("optionList", optionList);
		} catch (Exception e) {
			logger.error("获取题目及其选项异常！！！");
		}
		return map;
	}

	@Override
	public Integer saveAnswerRecord(JSONObject obj) {
		logger.info("更新答题记录jsonStr:" + obj.toJSONString());

		// jsonObj转JavaBean
		AdAnswerRecord adAnswerRecord = JSONObject.toJavaObject(obj, AdAnswerRecord.class);

		return adAnswerRecordDao.updateOptionType(adAnswerRecord);
	}

	private Integer insertAnswerRecord(Long questionId, String userId) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info(String.format("新增答题记录 questionId=%s AnswerUser=%s AnswerTime=%s", questionId, userId,
				format.format(new Date())));
		// 新增答题记录，但是不包含选项
		AdAnswerRecord adAnswerRecord = new AdAnswerRecord();
		adAnswerRecord.setQuestionId(questionId);
		adAnswerRecord.setAnswerUser(Long.valueOf(userId));
		adAnswerRecord.setAnswerTime(format.format(new Date()));
		return adAnswerRecordDao.insert(adAnswerRecord);
	}

	@Override
	// @Scheduled(cron = "0 0 0 ? * *")
	public void updateUserWealth() {
		logger.info("-------------更新用户财富值定时任务begin-------------");
		// 获取昨天答题正确的记录
		List<AdAnswerRecord> recordList = adAnswerRecordDao.findCorrectRecordsOfYesterday();
		if (recordList != null && !recordList.isEmpty()) {
			logger.info("需要更新财富值的用户total:" + recordList.size());
			for (int i = 0; i < recordList.size(); i++) {
				AdAnswerRecord adAnswerRecord = recordList.get(i);
				// 获取用户id
				String userId = adAnswerRecord.getAnswerUser().toString();
				// 获取当前财富值
				int currWealth = adUserDao.getTotalWealth(userId);
				// 获取需要增加的财富值
				int points = Integer.parseInt(adAnswerRecord.getPoints());
				// 更新用户财富值
				adUserDao.updateWealthByUserId(userId, currWealth + points);
				logger.info(String.format("更新第[ %s ]个  || 会员id:%s || 更新前财富值:%s || 要增加的财富值:%s", i + 1, userId,
						currWealth, points));
				// 更新答题记录中的连续答对天数
				AdAnswerRecord record = adAnswerRecordDao.getAnswerRecordByUserId(userId, 2);
				int continueRightDays = (record == null ? 0 : record.getContinueRightDays());
				adAnswerRecord.setContinueRightDays(continueRightDays + 1);
				int updateResult = adAnswerRecordDao.update(adAnswerRecord);
				logger.info(String.format("更新第[ %s ]个  || 会员id:%s || 答题记录id:%s || 结果为:%s", i + 1, userId,
						adAnswerRecord.getId(), updateResult));
			}
		}
		logger.info("-------------更新用户财富值定时任务end-------------");
	}

	/**
	 * 处理活动规则
	 * 
	 */
	public void handleQuestionRules() {
		logger.info("-------------活动规则验证begin-------------");
		try {
			// 获取全部活动规则
			List<AdQuestionRule> ruleList = adQuestionRuleDao.getAll(new AdQuestionRule());
			if (ruleList != null && !ruleList.isEmpty()) {
				for (AdQuestionRule rule : ruleList) {
					String ruleType = rule.getType();
					switch (ruleType) {
					case "0":
						// 每连续7天答对，将获得1兜礼积分

						break;
					case "1":
						// 总分累计超过300分，将有50%机会获得iPhone7一部

						break;
					case "2":
						// 连续答题超过30天，将获兜礼50元代金券一份

						break;
					case "3":
						// 如连续答题并答对超过300天，将成为兜礼VIP，享有兜礼一级特权

						break;
					case "4":
						// 每年1月1日将评选出答题达人冠军，直接获取iPhone7一部
						Calendar calendar = Calendar.getInstance();
						int currMonth = calendar.get(Calendar.MONTH);
						int currDate = calendar.get(Calendar.DATE);
						if (Calendar.JANUARY == currMonth && 1 == currDate) {
							// 获取财富值最高的用户 直接送手机 只执行一次 TODO
							AdUser user = adUserDao.getUserWithMaxWealth();
							logger.info("财富值最高的会员手机号：" + user.getTelephone());
						}
						break;
					default:

						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("处理活动规则异常！！！");
		}
		logger.info("-------------活动规则验证end-------------");
	}

	@Override
	public MessageDataBean getUserCode(String telephone) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		//判断该手机号是否已领取
		LifeActivity lifeActivity = lifeActivityDao.getUserRecord(telephone);
		if (lifeActivity == null) {
			List<String> listPop=redisUtilService.PopDataFromRedis("lyf_coupon_code",1);
			logger.info("========codeList:" + listPop + ",codeList == null:" + (listPop == null));
			if (listPop != null) {
				JSONObject parseObject = JSON.parseObject(listPop.get(0));
				LifeActivity a = new LifeActivity();
				a.setCompany("来伊份发券活动");
				a.setResultCode(listPop.get(0));
				a.setTelephone(telephone);
				lifeActivityDao.insert(a);
				data.put("couponCode", parseObject.getString("coupon_code"));
				data.put("couponPassword", parseObject.getString("coupon_password"));
				messageDataBean.setData(data);
				messageDataBean.setCode(MessageDataBean.success_code);
			}else {
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		}else {
			JSONObject parseObject = JSON.parseObject(lifeActivity.getResultCode());
			data.put("couponCode", parseObject.getString("coupon_code"));
			data.put("couponPassword", parseObject.getString("coupon_password"));
			messageDataBean.setData(data);
			messageDataBean.setCode(MessageDataBean.success_code);
		}
		return messageDataBean;
	}

}
