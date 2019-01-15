package com.doooly.business.activity.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.doooly.business.common.service.AdUserServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.ActivityServiceI;
import com.doooly.business.common.service.AdSendGiftServiceI;
import com.doooly.business.common.service.impl.AdUserService;
import com.doooly.business.utils.Pagelab;
import com.doooly.dao.reachad.AdActivityCategoryDao;
import com.doooly.dao.reachad.AdBasicTypeDao;
import com.doooly.dao.reachad.AdBusinessActivityOrderDao;
import com.doooly.dao.reachad.AdBusinessActivityRuleDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachlife.LifeMemberDao;
import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.dto.activity.ActivityOrderRes;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdActivityCategory;
import com.doooly.entity.reachad.AdBusinessActivityInfo;
import com.doooly.entity.reachad.AdBusinessActivityOrder;
import com.doooly.entity.reachad.AdBusinessActivityRule;
import com.doooly.entity.reachad.AdBusinessActivityRule.RuleType;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachlife.LifeMember;

@Service
public class ActivityService implements ActivityServiceI {
	
	private static Logger logger = Logger.getLogger(ActivityService.class);
	
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;
	@Autowired
	private AdUserServiceI adUserService;
	@Autowired
	private AdBusinessActivityRuleDao activityRuleDao;
	@Autowired
	private AdBusinessActivityOrderDao activityOrderDao;
	@Autowired
	private AdSendGiftServiceI adSendGiftService;
	@Autowired
	private LifeMemberDao lifeMemberDao;
	@Autowired
	private AdBasicTypeDao adBasicTypeDao;
	@Autowired
	private AdActivityCategoryDao activityCategoryDao;

	public ActivityOrderRes activityOrderService(ActivityOrderReq req) {
		if (req == null) {
			return new ActivityOrderRes("1001", "请求参数不能为空!");
		}
		System.out.println(req.toString());
		AdUser user = null;
		LifeMember member = null;
		try {
			user = adUserService.get(req.getTelephone(), req.getCardNumber());
			if (user == null) {
				return new ActivityOrderRes("1005", "该订单会员不是兜礼会员!");
			}
			member = lifeMemberDao.getByAdId(user.getId());
			if (member == null) {
				return new ActivityOrderRes("1010", "A库没有该会员信息!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 获取该商家活动详情及所有活动细则
		List<AdBusinessActivityRule> rules = activityRuleDao.getActivityRule(req.getBusinessId());
		if (rules == null || rules.size() <= 0) {
			return new ActivityOrderRes("1002", "该商家无优惠活动!");
		}
		AdBusinessActivityInfo info = rules.get(0).getInfo();
		if (info.getBeginTime().after(req.getOrderDate()) || info.getEndTime().before(req.getOrderDate())) {
			return new ActivityOrderRes("1003", "该订单日期不再活动有效期内!");
		}
		boolean match = false;
		double ruleValue = 0D;
		for (AdBusinessActivityRule rule : rules) {
			ruleValue = Double.parseDouble(rule.getValue());
			if (RuleType.EQUAL_TO.ordinal() == rule.getType()) {
				match = (req.getTotalAmount() == ruleValue);
			} else if (RuleType.GREATER_THAN.ordinal() == rule.getType()) {
				match = (req.getTotalAmount() > ruleValue);
			} else if (RuleType.GREATER_OR_EQUAL.ordinal() == rule.getType()) {
				match = (req.getTotalAmount() >= ruleValue);
			} else if (RuleType.LESS_THAN.ordinal() == rule.getType()) {
				match = (req.getTotalAmount() < ruleValue);
			} else if (RuleType.LESS_OR_EQUAL.ordinal() == rule.getType()) {
				match = (req.getTotalAmount() <= ruleValue);
			} else
				;
		}

		if (!match) {
			return new ActivityOrderRes("1004", "该订单不符合活动规则!");
		}

		AdBusinessActivityOrder order = null;

		order = activityOrderDao.getActivityOrder(user.getId(), info.getId(), req.getBusinessId());
		if (order != null) {
			return new ActivityOrderRes("1006", "该会员已经在该活动中领取过礼品");
		}

		order = activityOrderDao.getByOrderNumber(req.getOrderNumber());
		if (order != null) {
			return new ActivityOrderRes("1011", "该会员已经在该活动中领取过礼品,该订单属于多笔流水!");
		}

		order = new AdBusinessActivityOrder();
		order.setUserId(user.getId());
		order.setActivityId(info.getId());
		order.setBusinessId(req.getBusinessId());
		order.setOrderNo(req.getOrderNumber());
		order.setOrderDate(req.getOrderDate());
		order.setCreateDate(new Date());
		order.setCardNumber(req.getCardNumber() == null || req.getCardNumber().equals("") ? user.getCardNumber()
				: req.getCardNumber());
		order.setTelephone(
				req.getTelephone() == null || req.getTelephone().equals("") ? user.getTelephone() : req.getTelephone());
		order.setMemberId(member.getId());
		order.setStoreId(req.getStoreId());
		return adSendGiftService.sendGift(order);
	}

	@Override
	public MessageDataBean getHotActivity(JSONObject params) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			int groupId =params.getInteger("groupId");
			int currentPage = params.getInteger("currentPage");
			int pageSize = params.getInteger("pageSize");
			int client = params.getInteger("client");
			String categoryId = params.getString("categoryId");
			String isRecommendation = params.getString("isRecommendation");
			logger.info("params = " + params);
			Pagelab pagelab = new Pagelab(currentPage, pageSize);
			int totalNum = adCouponActivityDao.getHotActivityCnt(client, groupId, categoryId, isRecommendation);
			if (totalNum > 0) {
				pagelab.setTotalNum(totalNum);
				List<Map<String,String>> activityList = adCouponActivityDao.getHotActivity(client,groupId, pagelab.getStartIndex(), pagelab.getPageSize(), categoryId, isRecommendation);
//				logger.info("activityList=============" + activityList);
				map.put("list", activityList);
				map.put("countPage", pagelab.getCountPage());
			}else{
				map.put("countPage", 0);
			}
			logger.info("totalNum = " +  totalNum);
		} catch (Exception e) {
			logger.error("Error occured when executing 'getHotActivity(" + params + ")' , e = " + e);
			return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess, null);
		}
		return new MessageDataBean(MessageDataBean.success_code, MessageDataBean.success_mess, map);
	}

	@Override
	public MessageDataBean getActivityInfo(JSONObject results) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
		String id = results.getString("id");
		AdCouponActivity adCouponActivity = adCouponActivityDao.get(id);
		logger.info("=================adCouponActivity:" + adCouponActivity + "=============================");
		data.put("adCouponActivity", adCouponActivity);
		
		} catch (Exception e) {
			logger.error("Error occured when executing 'getActivityInfo(" + results + ")' , e = " + e);
			return new MessageDataBean(MessageDataBean.failure_code, MessageDataBean.failure_mess, null);
		}
		return new MessageDataBean(MessageDataBean.success_code, MessageDataBean.success_mess, data);
	}

	/**
	 * 查询热门活动发现列表
	 */
	@Override
	public MessageDataBean getActivityCategoryList(JSONObject results) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String client = results.getString("client");
			AdActivityCategory adActivityCategory = new AdActivityCategory();
			adActivityCategory.setPlatform(client);
			List<AdActivityCategory> adBasicTypeList = activityCategoryDao.findList(adActivityCategory);
			map.put("adBasicTypeList", adBasicTypeList);
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} catch (Exception e) {
			messageDataBean.setCode(MessageDataBean.failure_code);
			e.printStackTrace();
		}
		return messageDataBean;
	}
	

}
