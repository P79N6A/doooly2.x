package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

/**
 *宝钢化工厨神大赛报名活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月4日
 */
public interface CookGodGameServiceI {

	MessageDataBean getActivityDetail(Integer activityId,String userId);

	MessageDataBean apply(Integer activityId, Integer userId);

	
}
