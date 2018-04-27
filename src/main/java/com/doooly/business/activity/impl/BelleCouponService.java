package com.doooly.business.activity.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.doooly.business.common.service.impl.AdCouponActivityService;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.entity.reachad.AdCoupon;
import com.doooly.entity.reachad.AdCouponActivity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doooly.business.activity.BelleCouponServiceI;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;

/**
 * 百丽优惠券活动 business接口实现
 *
 * @author yuelou.zhang
 * @date 2017年9月18日
 * @version 1.0
 */
@Service
@Transactional
public class BelleCouponService implements BelleCouponServiceI {

	private static Logger logger = LoggerFactory.getLogger(BelleCouponService.class);

	private String OLD_ACTIVITYID = PropertiesConstants.dooolyBundle.getString("OLD_ACTIVITYID");
	private String OLD_COUPONID = PropertiesConstants.dooolyBundle.getString("OLD_COUPONID");

	private String OLD_ACTIVITYID2 = PropertiesConstants.dooolyBundle.getString("OLD_ACTIVITYID2");
	private String OLD_COUPONID2 = PropertiesConstants.dooolyBundle.getString("OLD_COUPONID2");

	// 已核销
	private static final String IS_USED = "1";
	// 未核销
	private static final String NOT_USED = "0";

	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;
	@Autowired

	private AdCouponCodeDao adCouponCodeDao;
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;
	@Autowired
	private AdCouponActivityService adCouponActivityService;

	/**
	 * 领取百丽活动
	 * @param idFlag
	 * @param userId
	 * @return
	 */
	@Override
	public MessageDataBean receiveCoupon2(String userId, String idFlag) {
		try {
			logger.info("userId = {},idFlag = {}", userId, idFlag);
			HashMap<String, Object> data = new HashMap<String, Object>();
			AdCouponActivity activity = adCouponActivityService.getActivityIdByIdFlag(idFlag);
			if(activity == null){
				return new MessageDataBean("6666","未找到活动,无效的活动标识");
			}
			//活动是否结束
			int activityId = activity.getId();
			if ((new Date()).after(activity.getEndDate())) {
				return new MessageDataBean("1001","活动已结束");
			}
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(String.valueOf(activityId));
			AdCouponActivityConn activityConn = list.get(0);
			AdCoupon adCoupon = activityConn.getCoupon();
			//之前是否领取过百丽
			AdCouponCode couponCode1 = adCouponCodeDao.getCouponCode(Integer.valueOf(userId), Integer.valueOf(OLD_ACTIVITYID), Integer.valueOf(OLD_COUPONID));
			AdCouponCode couponCode2 = adCouponCodeDao.getCouponCode(Integer.valueOf(userId), Integer.valueOf(OLD_ACTIVITYID2), Integer.valueOf(OLD_COUPONID2));
			AdCouponCode couponCode3 = adCouponCodeDao.getCouponCode(Integer.valueOf(userId), activityId, adCoupon.getId().intValue());

			logger.info("couponCode1 = {},couponCode2 = {},couponCode3 = {}", couponCode1, couponCode2,couponCode3);
			if (couponCode1 != null || couponCode2 != null || couponCode3 != null) {
				return new MessageDataBean("1002", "已领过该券");
			}

			AdCouponCode code = freeCouponBusinessServiceI.sendCoupon(adCoupon.getBusinessId(), activityId, Integer.valueOf(userId), adCoupon.getId().intValue());
			logger.info("领取到的百丽优惠券券码 code = {}", code.getCode());
			// 判断库存
			if (StringUtils.isEmpty(code.getCode())) {
				return new MessageDataBean("1003", "该券已领完");
			} else {
				HashMap map = new HashMap<String, String>();
				MessageDataBean bean = new MessageDataBean("1000", "成功领取");
				map.put("actConnId", activityConn.getId());
				bean.setData(map);
				return bean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("userId = {},idFlag = {}, e = {}", userId, idFlag, e);
		}
		return new MessageDataBean("1004","出现异常");
	}

	@Override
	public MessageDataBean receiveCoupon(String userId, String activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			HashMap<String, Object> data = new HashMap<String, Object>();
			// 1.根据活动id获取活动卡券关联
			AdCouponActivityConn activityConn = adCouponActivityConnDao.getByActivityId(activityId);
			// 2.根据userId && activityId && couponId 获取券码
			AdCouponCode couponCode = adCouponCodeDao.getCouponCode(Integer.valueOf(userId),
					Integer.valueOf(activityId), activityConn.getCoupon().getId().intValue());
			// 3.判断是否领取并核销过优惠券
			if (couponCode != null) {
				// A.领取并核销过
				if (StringUtils.equals(IS_USED, couponCode.getIsUsed())) {
					messageDataBean.setCode(MessageDataBean.already_used_code);
				}
				// B.领取未核销过
				if (StringUtils.equals(NOT_USED, couponCode.getIsUsed())) {
					messageDataBean.setCode(MessageDataBean.success_code);
					data.put("actConnId", activityConn.getId());
					messageDataBean.setData(data);
				}
			} else {
				// C.未领取过优惠券则发券
				AdCouponCode code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(),
						Integer.valueOf(activityId), Integer.valueOf(userId),
						activityConn.getCoupon().getId().intValue());
				logger.info("领取到的百丽优惠券券码 code=" + code.getCode());
				// 判断库存
				if (StringUtils.isEmpty(code.getCode())) {
					messageDataBean.setCode(MessageDataBean.no_data_code);
				} else {
					messageDataBean.setCode(MessageDataBean.success_code);
					data.put("actConnId", activityConn.getId());
					messageDataBean.setData(data);
				}
			}
		} catch (Exception e) {
			logger.error("userId为[ " + userId + " ]的用户领取百丽优惠券时报错,错误信息为", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

}
