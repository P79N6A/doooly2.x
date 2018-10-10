package com.doooly.business.activity.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.CommonActivityServiceI;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdGroupActivityConnDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;

@Service
public class MURechargeActivityService implements CommonActivityServiceI {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;

	@Autowired
	private AdCouponActivityConnDao couponActivityConnDao;
	@Autowired
	private AdGroupActivityConnDao groupActivityConnDao;
	@Autowired
	private AdCouponActivityDao couponActivityDao;
	@Autowired
	private AdCouponCodeDao couponCodeDao;

	@Override
	public MessageDataBean sendActivityCoupon(Integer userId, Integer activityId) {
		// 1.验证活动有效性
		AdCouponActivityConn activityConn = couponActivityConnDao.getByActivityId(activityId, null);
		// 1.1验证活动是否存在
		if (activityConn == null) {
			return new MessageDataBean("2010", "活动不存在");
		}
		Long couponId = activityConn.getCoupon().getId();
		AdCouponActivity couponActivity = couponActivityDao.getActivityById(activityId);
		// 1.2验证活动是否开启
		if (Integer.valueOf(couponActivity.getState()) == AdCouponActivity.ACTIVITY_CLOSE) {
			return new MessageDataBean("2011", "活动已关闭");
		}
		// 1.3验证活动是否开始
		Date nowDate = new Date();
		if (couponActivity.getBeginDate().compareTo(nowDate) > 0) {
			return new MessageDataBean("2012", "活动尚未开始");
		}
		// 1.4 验证活动是否结束
		if (couponActivity.getEndDate().compareTo(nowDate) < 0) {
			return new MessageDataBean("2013", "活动已结束");
		}

		// 2.验证是否有资格参与
		int checkResult = groupActivityConnDao.checkUserEligibleActivities(userId, activityId);
		if (checkResult == 0) {
			return new MessageDataBean("2014", "企业未参与该活动");
		}
		// 3.验证是否已领取优惠券
		AdCouponCode coupon = new AdCouponCode();
		coupon.setActivityId(activityId.longValue());
		coupon.setUserId(userId.longValue());
		coupon.setCoupon(couponId);
		int couponCount = couponCodeDao.getUserCouponCountByIds(coupon);
		if (couponCount > 0) {
			return new MessageDataBean(MessageDataBean.already_receive_code, MessageDataBean.already_receive_mess);
		}
		// 4.验证通过正常发放活动券
		MessageDataBean messageDataBean = freeCouponBusinessServiceI.receiveCoupon(userId, couponId.intValue(),
				activityId, null);
		return messageDataBean;
	}

}
