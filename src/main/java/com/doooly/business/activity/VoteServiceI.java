package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2017-05-31
 */
public interface VoteServiceI {

    MessageDataBean getActivityDetail(Integer activityId, String userWechatOpenId);// 获取活动详情

    MessageDataBean findVoteOptionList(String userWechatOpenId);// 获取首页选项详情

    MessageDataBean browserCount(Integer activityId);//添加浏览次数

    MessageDataBean clickVote(Integer activityId,Integer optionId, String userWechatOpenId);//投票

    MessageDataBean findVoteCountByOpenId(String userWechatOpenId);//投票数

    MessageDataBean findVoteOption();//选项列表
}
