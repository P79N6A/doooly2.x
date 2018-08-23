package com.doooly.business.activity.impl;

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
import com.doooly.entity.reachad.AdCoupon;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdGroupActivityConn;

@Service
public class MURechargeActivityService implements CommonActivityServiceI{
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
		AdCouponActivityConn activityConn = couponActivityConnDao.getByActivityId(activityId);
		Long couponId = activityConn.getCoupon().getId();
		//1.验证活动有效性
		int checkResult = couponActivityDao.checkActivityValid(activityId);
		if(checkResult==0){
			return new MessageDataBean("2012","活动未开始或已结束");
		}
		//2.验证是否有资格参与
		checkResult = groupActivityConnDao.checkUserEligibleActivities(userId, activityId);
		if(checkResult == 0){
			return new MessageDataBean("2013","企业未参与该活动");
		}
		//3.是否重复领取
		AdCouponCode coupon = new AdCouponCode();
		coupon.setActivityId(activityId.longValue());
		coupon.setUserId(userId.longValue());
		coupon.setCoupon(couponId);
		int couponCount = couponCodeDao.getUserCouponCountByIds(coupon);
		if(couponCount>0){
			return new MessageDataBean(MessageDataBean.already_receive_code, MessageDataBean.already_receive_mess);
		}
		//4.发放活动券
		MessageDataBean messageDataBean = freeCouponBusinessServiceI.receiveCoupon(userId, couponId.intValue(), activityId,
				null);
		return messageDataBean;
	}

}
