package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

/**
 *宝钢化工厨神大赛投票活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月6日
 */
public interface CookGodVoteServiceI {

	MessageDataBean getIndexData(Integer activityId,String userId);

	MessageDataBean vote(Integer activityId, String userId,Integer optionId);
	MessageDataBean rank(Integer activityId);
	MessageDataBean getActivityDetail(Integer activityId);

	
}
