package com.doooly.business.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.CookGodGameServiceI;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdInviteActivityDao;
import com.doooly.dao.reachad.AdJoinUserDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdInviteActivity;

/**
 *宝钢化工厨神大赛报名活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月4日
 */
@Service
public class CookGodGameService implements CookGodGameServiceI {

	private static Logger logger = Logger.getLogger(CookGodGameService.class);
	@Autowired
	private AdInviteActivityDao adInviteActivityDao;
	@Autowired
	private AdJoinUserDao adJoinUserDao;
	@Autowired
	private AdGroupDao adGroupDao;
	@Override
	public MessageDataBean getActivityDetail(Integer activityId,String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 活动详情
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);
		if (activityDetail != null) {
			map.put("activityDetail", activityDetail);
			messageDataBean.setCode(MessageDataBean.success_code);
		
		Integer userCount = adJoinUserDao.getJoinUserNum(activityId);
		if (userCount >= 20) {
			map.put("userCount", 20);
			messageDataBean.setCode(MessageDataBean.time_out_code);
		}else {
			map.put("userCount", userCount);
		}
		if (!StringUtils.isBlank(userId)) {
			Boolean findAdJoinUserByJoinUser = adJoinUserDao.findAdJoinUserByJoinUser(Integer.valueOf(userId));
			if (findAdJoinUserByJoinUser) {
				map.put("type", 1);
			}else {
				map.put("type", 0);
			}
		}
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}
	@Override
	public MessageDataBean apply(Integer activityId,Integer userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 活动详情
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);
		if (activityDetail != null) {
			map.put("activityDetail", activityDetail);
			messageDataBean.setCode(MessageDataBean.success_code);
			
			Integer userCount = adJoinUserDao.getJoinUserNum(activityId);
			
			Boolean findAdJoinUserByJoinUser = adJoinUserDao.findAdJoinUserByJoinUser(userId);
			if (findAdJoinUserByJoinUser) {
				map.put("isSuccess", 0);
				map.put("type", 1);
				map.put("userCount", userCount);
			}else {
				try {
					if (userCount <20) {
						List<Map> adJoinUsers = new ArrayList<>();
						Map<String, Object> mapForSave = new HashMap<>();
						if (adGroupDao.findGroupByUserId(userId+"").getGroupName().equals("上海宝钢化工有限公司")) {
							mapForSave.put("activityId", activityId);
							mapForSave.put("itemId", 0);
							mapForSave.put("joinUser", userId);
							adJoinUsers.add(mapForSave);
							adJoinUserDao.batchInsert(adJoinUsers);
							map.put("userCount", userCount+1);
						}else {
							map.put("userCount", userCount);
						}
						map.put("isSuccess", 1);
						map.put("type", 1);
					}
					else {
						map.put("userCount", 20);
						map.put("isSuccess", 0);
						map.put("type", 0);
						messageDataBean.setCode(MessageDataBean.time_out_code);
					}
				} catch (Exception e) {
					map.put("isSuccess", 0);
					e.printStackTrace();
				}
			}
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	

}
