package com.doooly.business.activity.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.EasternAirlinesBusinessServiceI;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdCouponDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCoupon;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;

/**
 * 东航e家活动业务Service实现
 * 
 * @author zilei.sun
 * @version 2017年9月19日
 */
@Service
public class EasternAirlinesBusinessService implements EasternAirlinesBusinessServiceI {

	private static Logger logger = Logger.getLogger(EasternAirlinesBusinessService.class);

	@Autowired
	private AdCouponCodeDao adCouponCodeDao;
	@Autowired
	private AdCouponDao adCouponDao;
	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;

	@Override
	public MessageDataBean eastAirlinesEfamily(Integer userId, String activityId) {
		logger.info("查询参数userId==" + userId + " ;activityId==" + activityId);

		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			// 1.根据活动id获取活动卡券关联
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
			// 2.根据userId && activityId && couponId 获取券码
			AdCouponCode couponCode = null;
			AdCoupon adCoupon = null;
			AdCouponActivityConn adCouponActivityConn = null;
			for (AdCouponActivityConn conn : list) {
				couponCode = adCouponCodeDao.getCouponCode(userId,Integer.valueOf(activityId), conn.getCoupon().getId().intValue());
				if(null != couponCode ){
					adCoupon = adCouponDao.get(conn.getCoupon().getId().toString());
					adCouponActivityConn = conn;
					break;
				}
			}
			// 3.判断是否领取过优惠券
			if (couponCode != null) {
				// A.已领取过
				messageDataBean.setCode(MessageDataBean.success_code);
				Date beginDate1 = adCoupon.getBeginDate();
				Date endDate1 = adCoupon.getEndDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
				String beginDate = sdf.format(beginDate1);
				String endDate = sdf.format(endDate1);
				
				data.put("couponCode", couponCode.getCode());
				data.put("endDate", endDate);
				data.put("beginDate", beginDate);
				if(adCouponActivityConn.getLevel() == 1){
					data.put("flag", "0");
				}else{
					data.put("flag", "1");
				}
				messageDataBean.setData(data);
			} else {
				// B.已激活但未领取过优惠券
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("userId为[ " + userId + " ]的用户查询优惠券领取情况时报错,错误信息为" + e.getMessage());
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean eastAirlinesEfamilyCouponCheck(String activityId) {
		logger.info("查询参数activityId==" + activityId);

		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		try {
			AdCouponActivity activity = adCouponActivityDao.get(activityId);
			Date beginDate1 = activity.getBeginDate();
			Date endDate1 = activity.getEndDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			String beginDate = sdf.format(beginDate1);
			String endDate = sdf.format(endDate1);
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
			StringBuffer buffer = new StringBuffer();
			Long diandianCouponId = null;
			Long lyfCouponId = null;
			if(list.size() > 0){
				for (AdCouponActivityConn adCouponActivityConn : list) {
					AdCoupon adCoupon = adCouponDao.get(adCouponActivityConn.getCoupon().getId().toString());
					if(adCouponActivityConn.getLevel() == 1){
						diandianCouponId = adCoupon.getId();
					}
					if(adCouponActivityConn.getLevel() == 2){
						lyfCouponId = adCoupon.getId();
					}
					if(adCouponActivityConn.getCouponRemindQuantity() == 0){
						buffer.append("0");
					}else{
						buffer.append("1");
					}
				}
			}
			Integer type = null;
			switch (buffer.toString()) {
			case "00": 
				type = 1;
				break;
			case "01": 
				type = 2;
				break;
			case "10": 
				type = 3;
				break;
			case "11": 
				type = 4;
				break;
			default:
				break;
			}
			data.put("type", type);
			data.put("activity", activity);
			data.put("beginDate", beginDate);
			data.put("endDate", endDate);
			data.put("diandianCouponId", diandianCouponId);
			data.put("lyfCouponId", lyfCouponId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询优惠券活动时报错,错误信息为" + e.getMessage());
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		
		return messageDataBean;
	}

	@Override
	public MessageDataBean receiveCoupon(Integer userId, String activityId, String couponId) {
		logger.info("查询参数activityId==" + activityId + ";userId==" + userId + ";couponId==" + couponId);
		
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			AdCoupon adCoupon = adCouponDao.get(couponId);
			Date beginDate1 = adCoupon.getBeginDate();
			Date endDate1 = adCoupon.getEndDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			String beginDate = sdf.format(beginDate1);
			String endDate = sdf.format(endDate1);
			AdCouponActivityConn activityConn = adCouponActivityConnDao.getByActivityIdAndCouponId(activityId,couponId);
			AdCouponCode code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(),
					Integer.valueOf(activityId), userId,Integer.valueOf(couponId));
			logger.info("领取到的东航e家惠券券码 code=" + code.getCode());
			// 判断库存
			if (StringUtils.isEmpty(code.getCode())) {
				messageDataBean.setCode(MessageDataBean.no_data_code);
			} else {
				logger.info("领取成功进入code====" + code);
				messageDataBean.setCode(MessageDataBean.success_code);
				data.put("beginDate", beginDate);
				data.put("endDate", endDate);
				data.put("couponCode", code.getCode());
				
				logger.info("领取成功进入level====" + activityConn.getLevel());
				if(activityConn.getLevel() == 1){
					data.put("flag", "0");
				}else{
					data.put("flag", "1");
				}
				messageDataBean.setData(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("userId为[ " + userId + " ]的用户领取到的东航e家惠券时报错,错误信息为" + e.getMessage());
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		
		return messageDataBean;
	}

}
