package com.doooly.business.activity.impl;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.XiaoBaiNianBusinessServiceI;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;

/**
 * 小拜年活动业务Service实现
 * 
 * @author zilei.sun
 * @version 2017年8月29日
 */
@Service
public class XiaoBaiNianBusinessService implements XiaoBaiNianBusinessServiceI {

	private static Logger logger = Logger.getLogger(XiaoBaiNianBusinessService.class);

	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;
	@Autowired
	private AdCouponCodeDao adCouponCodeDao;
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;

	@Override
	public MessageDataBean xiaoBaiNianCoupon(Integer adId, Integer activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
				AdCouponCode code = new AdCouponCode();
				HashMap<String, Object> data = new HashMap<String, Object>();
				AdCouponActivityConn activityConn = new AdCouponActivityConn();
					activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 1);
					code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId,
							adId, activityConn.getCoupon().getId().intValue());
						AdCouponCode couponCode = adCouponCodeDao.getCouponCode(adId,activityId,activityConn.getCoupon().getId().intValue());
						AdCouponActivityConn couponDetail = adCouponActivityConnDao.getCouponDetail(adId.toString(), String.valueOf(activityConn.getId()));
						AdCouponActivity adCouponActivity = adCouponActivityDao.get(activityId.toString());
						logger.info("===================adCouponActivity" + adCouponActivity + "=======================");
						if(null != couponCode && null != couponCode.getIsUsed()){
							if(StringUtils.endsWith("1", couponCode.getIsUsed())){
								messageDataBean.setCode(MessageDataBean.already_used_code);
							}else{
								messageDataBean.setCode(MessageDataBean.success_code);
								data.put("actConn", couponDetail);
								data.put("actConnId", activityConn.getId());
								data.put("adCouponActivity", adCouponActivity);
							}
						}else{
							messageDataBean.setCode("1004");
						}
						
					messageDataBean.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("ID为" + adId + "的用户领取小拜年券时报错,错误信息为" + e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}


}
