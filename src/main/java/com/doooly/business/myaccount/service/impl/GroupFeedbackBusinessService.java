package com.doooly.business.myaccount.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.myaccount.service.GroupFeedbackBusinessServiceI;
import com.doooly.dao.reachad.AdGroupFeedbackDao;
import com.doooly.entity.reachad.AdGroupFeedback;

/**
 * 反馈给公司业务Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年3月6日
 */
@Service
@Transactional
public class GroupFeedbackBusinessService implements GroupFeedbackBusinessServiceI {

	private static Logger logger = Logger.getLogger(GroupFeedbackBusinessService.class);

	@Autowired
	private AdGroupFeedbackDao adGroupFeedbackDao;

	@Override
	public Integer saveFeedback(JSONObject obj) {
		logger.info("新增企业反馈jsonStr:" + obj.toJSONString());

		// jsonObj转JavaBean
		AdGroupFeedback adGroupFeedback = JSONObject.toJavaObject(obj, AdGroupFeedback.class);

		return adGroupFeedbackDao.insert(adGroupFeedback);
	}

}
