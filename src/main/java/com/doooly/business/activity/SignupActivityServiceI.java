package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdActivityComment;
import com.doooly.entity.reachad.AdSupport;

import java.util.List;
import java.util.Map;

/**
 * @Description: 活动报名service
 * @author: qing.zhang
 * @date: 2017-04-25
 */
public interface SignupActivityServiceI {

	MessageDataBean getActivityDetail(Integer activityId,Integer supportUser);// 获取活动详情

	MessageDataBean getAllComment(Integer activityId, Integer currentPage, Integer pageSize,Integer supportUser);// 查询所有评论

	MessageDataBean getSignupForm(Integer activityId);// 获取报名表单

	MessageDataBean signupActivity(Integer joinUser, List<Map> adJoinUsers);// 报名

	MessageDataBean getAllJoinUser(Integer activityId,Integer supportUser);// 参赛选手

	MessageDataBean clickLike(AdSupport adSupport);// 点赞

	void saveActivityComment(AdActivityComment adActivityComment);//保存评论

    MessageDataBean browserCount(Integer activityId);//添加浏览次数

    MessageDataBean getMap(Integer activityId);//获取地址
}
