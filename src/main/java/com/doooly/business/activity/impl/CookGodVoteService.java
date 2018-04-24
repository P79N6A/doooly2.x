package com.doooly.business.activity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.CookGodVoteServiceI;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdInviteActivityDao;
import com.doooly.dao.reachad.AdJoinUserDao;
import com.doooly.dao.reachad.AdVoteOptionDao;
import com.doooly.dao.reachad.AdVoteRecordDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdInviteActivity;
import com.doooly.entity.reachad.AdVoteOption;
import com.doooly.entity.reachad.AdVoteRecord;

/**
 *宝钢化工厨神大赛投票活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月6日
 */
@Service
public class CookGodVoteService implements CookGodVoteServiceI {

	private static Logger logger = Logger.getLogger(CookGodVoteService.class);
	@Autowired
	private AdInviteActivityDao adInviteActivityDao;
	@Autowired
	private AdJoinUserDao adJoinUserDao;
	@Autowired
    private AdVoteOptionDao adVoteOptionDao;
    @Autowired
    private AdVoteRecordDao adVoteRecordDao;
    @Autowired
    private AdGroupDao adGroupDao;
	@Override
	public MessageDataBean getIndexData(Integer activityId,String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 活动详情
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);
		if (activityDetail != null) {
			map.put("activityDetail", activityDetail);
			
			//获取首页列表
			List<AdVoteOption> adVoteOptionListForFirst = adVoteOptionDao.findListByActivityId(0,activityId);
			if (!adVoteOptionListForFirst.isEmpty()) {
				if (adVoteOptionListForFirst.size() > 20) {
					map.put("userCount", 20);
				}else {
					map.put("userCount", adVoteOptionListForFirst.size());
				}
				map.put("playerListForIndex", adVoteOptionListForFirst);
				
				//每人最多投5票
				int voteCountByOpenId = adVoteRecordDao.findVoteCountByOpenId(userId);
				if (voteCountByOpenId >4) {
					map.put("votenum", 5);
				}else {
					map.put("votenum", voteCountByOpenId);
				}
				messageDataBean.setCode(MessageDataBean.success_code);
			}else {
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
			Date now = new Date();
			if (activityDetail.getValidDate().before(now)) {
				map.put("isEnd", 0);
			}else {
				map.put("isEnd", 1);
			}
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		
		messageDataBean.setData(map);
		return messageDataBean;
	}
	@Override
	public MessageDataBean vote(Integer activityId,String userId,Integer optionId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 活动详情
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);
		if (activityDetail != null) {
			//每人最多投5票
			int voteCountByOpenId = adVoteRecordDao.findVoteCountByOpenId(userId);
			if (voteCountByOpenId >4) {
				map.put("votenum", 5);
				messageDataBean.setCode(MessageDataBean.success_code);
			}else {
				AdVoteRecord adVoteRecord = new AdVoteRecord();
				adVoteRecord.setOptionId(optionId);
				adVoteRecord.setUserWechatOpenId(userId);
				adVoteRecord.setCreateDate(new Date());
				adVoteRecord.setActivityId(activityId);
				adVoteRecordDao.insert(adVoteRecord);
				adVoteOptionDao.updateVoteCount(optionId);
				map.put("votenum", voteCountByOpenId+1);
				messageDataBean.setCode(MessageDataBean.success_code);
			}
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}
	@Override
	public MessageDataBean rank(Integer activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 活动详情
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);
		if (activityDetail != null) {
			map.put("activityDetail", activityDetail);
			
			//获取排名
			List<AdVoteOption> adVoteOptionListForRank = adVoteOptionDao.findListByActivityId(1,activityId);
			if (adVoteOptionListForRank.isEmpty()) {
				
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}else {
				if (adVoteOptionListForRank.size() > 20) {
					map.put("userCount", 20);
				}else {
					map.put("userCount", adVoteOptionListForRank.size());
				}
				map.put("playerListForRank", adVoteOptionListForRank);
				messageDataBean.setCode(MessageDataBean.success_code);
				Date now = new Date();
				if (activityDetail.getValidDate().before(now)) {
					map.put("isEnd", 0);
				}else {
					map.put("isEnd", 1);
				}
			}
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}
	@Override
	public MessageDataBean getActivityDetail(Integer activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 活动详情
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);
		List<AdVoteOption> adVoteOptionListForFirst = adVoteOptionDao.findListByActivityId(0,activityId);
		if (activityDetail != null) {
			if (!adVoteOptionListForFirst.isEmpty()) {
				if (adVoteOptionListForFirst.size() > 20) {
					map.put("userCount", 20);
				}else {
					map.put("userCount", adVoteOptionListForFirst.size());
				}
			}
			map.put("activityDetail", activityDetail);
			messageDataBean.setCode(MessageDataBean.success_code);
			Date now = new Date();
			if (activityDetail.getValidDate().before(now)) {
				map.put("isEnd", 0);
			}else {
				map.put("isEnd", 1);
			}
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	

}
