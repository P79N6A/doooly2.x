package com.doooly.business.activity.impl;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.BelleCouponServiceI;
import com.doooly.business.activity.LoveKaiHealthCouponServiceI;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.publish.rest.life.LoveKaiHealthCouponRestServiceI;

/**
 * 爱启健康优惠券活动 business接口实现
 * 
 * @author yuelou.zhang
 * @date 2017年9月18日
 * @version 1.0
 */
@Service
@Transactional
public class LoveKaiHealthCouponService implements LoveKaiHealthCouponServiceI {

	private static Logger logger = Logger.getLogger(LoveKaiHealthCouponService.class);
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
				logger.info("领取到的爱启健康优惠券券码 code=" + code.getCode());
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
			logger.error("userId为[ " + userId + " ]的用户领取爱启健康优惠券时报错,错误信息为", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

}
