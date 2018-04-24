package com.doooly.business.activity.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.SignupActivityServiceI;
import com.doooly.dao.reachad.AdActivityCommentDao;
import com.doooly.dao.reachad.AdAutoColumnDao;
import com.doooly.dao.reachad.AdGroupActivityConnDao;
import com.doooly.dao.reachad.AdInviteActivityDao;
import com.doooly.dao.reachad.AdJoinUserDao;
import com.doooly.dao.reachad.AdSupportDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdActivityComment;
import com.doooly.entity.reachad.AdAutoColumn;
import com.doooly.entity.reachad.AdGroupActivityConn;
import com.doooly.entity.reachad.AdInviteActivity;
import com.doooly.entity.reachad.AdJoinUser;
import com.doooly.entity.reachad.AdSupport;

/**
 * @Description: 报名活动实现
 * @author: qing.zhang
 * @date: 2017-04-25
 */
@Service
public class SignupActivityService implements SignupActivityServiceI {

	private final static int basic_type = 0;// 基本信息输入框
	private final static int radio_type = 1;// 单选项目
	private final static int checkbox_type = 2;// 多选项目
	private final static int select_type = 3;// 下拉框项目

	@Autowired
	private AdInviteActivityDao adInviteActivityDao;
	@Autowired
	private AdActivityCommentDao adActivityCommentDao;
	@Autowired
	private AdAutoColumnDao adAutoColumnDao;
	@Autowired
	private AdJoinUserDao adJoinUserDao;
	@Autowired
	private AdGroupActivityConnDao adGroupActivityConnDao;
	@Autowired
	private AdSupportDao adSupportDao;

	/**
	 * 活动详情
	 *
	 * @param activityId
	 * @return
	 */
	public MessageDataBean getActivityDetail(Integer activityId,Integer supportUser) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 活动详情
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);
        //是否给活动点过赞
        AdSupport adSupport = new AdSupport();
        adSupport.setActivityId(activityId);
        adSupport.setSupportUser(supportUser);
        adSupport.setType(0);
        // 报名
		List<Map> adJoinUsers = adJoinUserDao.getAllAdJoinUser(activityId);
		if (activityDetail != null) {
			map.put("activityDetail", activityDetail);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		map.put("adJoinUsers", adJoinUsers);// 报名详情
        map.put("ifClickLike",adSupportDao.findAdSupport(adSupport)?0:1);
		map.put("adJoinUserNum", adJoinUserDao.getJoinUserNum(activityId));// 报名人数
		messageDataBean.setData(map);
		return messageDataBean;
	}

	/**
	 * 获取所有评论
	 *
	 * @param activityId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public MessageDataBean getAllComment(Integer activityId, Integer currentPage, Integer pageSize,Integer supportUser) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 开始索引
		int startIndex = (currentPage - 1) * pageSize;
		List<Map> commentList = adActivityCommentDao.getAllComment(activityId, startIndex, pageSize);
        for (Map map1 : commentList) {
            //是否给评论点过赞
            AdSupport adSupport = new AdSupport();
            adSupport.setCommentId((Integer) map1.get("id"));
            adSupport.setSupportUser(supportUser);
            adSupport.setType(1);
            map1.put("ifClickLike",adSupportDao.findAdSupport(adSupport)?0:1);
            //图片处理
            if(map1.get("pictrues")!=null){
                String[] pictrues = StringUtils.split(String.valueOf(map1.get("pictrues")), ";");
                map1.put("pictrues",pictrues);
            }
        }
        int commentTotal = adActivityCommentDao.getCount(activityId);
		if (commentList != null && commentList.size() > 0) {
			map.put("commentList", commentList);// 所有评论
		}
		map.put("commentTotal", commentTotal);// 评论总数
        int totalPage = (commentTotal-1)/pageSize +1;
        map.put("totalPage",totalPage);
		messageDataBean.setData(map);
		return messageDataBean;
	}

	/**
	 * 获取报名表单详情
	 *
	 * @return
	 */
	public MessageDataBean getSignupForm(Integer activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdAutoColumn> basicTypes = adAutoColumnDao.getSignupForm(activityId, basic_type);
		List<AdAutoColumn> radioTypes = adAutoColumnDao.getSignupForm(activityId, radio_type);
		List<AdAutoColumn> checkboxTypes = adAutoColumnDao.getSignupForm(activityId, checkbox_type);
		List<AdAutoColumn> selectTypes = adAutoColumnDao.getSignupForm(activityId, select_type);
		if (basicTypes != null && basicTypes.size() > 0) {
			map.put("basicTypes", basicTypes);
		}
		if (radioTypes != null && radioTypes.size() > 0) {
			map.put("radioTypes", radioTypes);
		}
		if (checkboxTypes != null && checkboxTypes.size() > 0) {
			map.put("checkboxTypes", checkboxTypes);
		}
		if (selectTypes != null && selectTypes.size() > 0) {
			map.put("selectTypes", selectTypes);
		}
		// 企业下拉框数据公司一级
		List<AdGroupActivityConn> groups = adGroupActivityConnDao.findGroup(activityId);
		map.put("groups", groups);
		// 二级公司
		if (groups != null && groups.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (AdGroupActivityConn group : groups) {
				sb.append(group.getGroupId() + ",");
			}
			List<AdGroupActivityConn> secondGroup = adGroupActivityConnDao
					.findSecondGroup(sb.substring(0, sb.length() - 1));
			if (CollectionUtils.isNotEmpty(secondGroup)) {
				map.put("secondGroup", secondGroup);
				sb.setLength(0);// 清空
				// 所有部门
				for (AdGroupActivityConn group : secondGroup) {
					sb.append(group.getCompanyId() + ",");
				}
				List<Map> departments = adGroupActivityConnDao.findDepartment(sb.substring(0, sb.length() - 1));
				map.put("departments", departments);
			}
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	/**
	 * 报名
	 *
	 * @param joinUser
	 * @param adJoinUsers
	 * @return
	 */
	public MessageDataBean signupActivity(Integer joinUser, List<Map> adJoinUsers) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Boolean existUser = adJoinUserDao.findAdJoinUserByJoinUser(joinUser);
		if (existUser) {
			// 说明该用户报名过了
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("您已经报过名了");
			return messageDataBean;
		}
        adJoinUserDao.batchInsert(adJoinUsers);
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	/**
	 * 参赛选手
	 * 
	 * @param activityId
	 * @return
	 */
	public MessageDataBean getAllJoinUser(Integer activityId,Integer supportUser) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Map> allJoinUser = adJoinUserDao.findAllJoinUser(activityId);// 详细信息
        for (Map map1 : allJoinUser) {
            //是否给评论点过赞
            AdSupport adSupport = new AdSupport();
            adSupport.setJoinRecordId((Integer) map1.get("joinRecordId"));
            adSupport.setSupportUser(supportUser);
            adSupport.setType(2);
            map1.put("ifClickLike",adSupportDao.findAdSupport(adSupport)?0:1);
        }
		List<Map> joinUsers = adJoinUserDao.findJoinUser(activityId);// 报名总数
		map.put("allJoinUser", allJoinUser);
		map.put("joinUsers", joinUsers);
		messageDataBean.setData(map);
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@Override
	public MessageDataBean clickLike(AdSupport adSupport) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Boolean existClick = adSupportDao.findAdSupport(adSupport);
		if (existClick) {
			// 说明该用户点过赞了
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("您已经点过赞了");
			return messageDataBean;
		}
		adSupportDao.insertSupport(adSupport);
		if (adSupport.getType() == 0) {
			// 说明活动点赞，更新活动点赞数
			adInviteActivityDao.updateSupportCount(adSupport.getActivityId());
		} else if (adSupport.getType() == 1) {
			// 说明评论点赞,更新点赞数
			adActivityCommentDao.updateSupportCount(adSupport.getCommentId());
		} else if (adSupport.getType() == 2) {
			// 说明报名项目点赞,更新点赞数
			adJoinUserDao.updateSupportCount(adSupport.getJoinRecordId());
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@Override
	public MessageDataBean browserCount(Integer activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		adInviteActivityDao.updateBrowserCount(activityId);// 修改浏览次数
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getMap(Integer activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		AdInviteActivity activityDetail = adInviteActivityDao.getActivityDetail(activityId);// 获取地址
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("address", activityDetail.getAddress());
		messageDataBean.setData(map);
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@Override
	public void saveActivityComment(AdActivityComment activityComment) {
		activityComment.setSupportCount(0);
		adActivityCommentDao.insert(activityComment);
	}

}
