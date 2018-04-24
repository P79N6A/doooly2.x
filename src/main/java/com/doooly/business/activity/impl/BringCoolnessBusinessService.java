package com.doooly.business.activity.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doooly.business.activity.BringCoolnessBusinessServiceI;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdAwardsRecordDao;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdGroupActivityConnDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAwardsRecord;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.Order;

/**
 * 答题活动业务Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
@Service
public class BringCoolnessBusinessService implements BringCoolnessBusinessServiceI {

	private static Logger logger = Logger.getLogger(BringCoolnessBusinessService.class);

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;
	@Autowired
	private AdAwardsRecordDao adAwardsRecordDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdGroupActivityConnDao adGroupActivityConnDao;
	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;

	ReentrantLock lock = new ReentrantLock();

	@Override
	public MessageDataBean sendCoolnessGift(Integer adId, Integer activityId, Integer type) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			AdUser adUser = adUserDao.selectByPrimaryKey((long) adId);
			AdCouponActivity adCouponActivity = adCouponActivityDao.get(activityId + "");
			Order order = orderDao.getPaidOrder(adId, adCouponActivity.getBeginDate());
			AdAwardsRecord adAwardsRecord = adAwardsRecordDao.getByUserId(adId);
			List<Order> orderActivityList = orderDao.getNewestOrder(adId, adCouponActivity.getBeginDate(),
					adCouponActivity.getEndDate());
			if (orderActivityList.isEmpty()) {
				logger.info("ID为" + adId + "的用户未在活动期内下过订单");
				messageDataBean.setCode(MessageDataBean.have_not_code);
				return messageDataBean;
			}
			if (orderActivityList.size() > 1 && adAwardsRecord != null) {
				logger.info("ID为" + adId + "的用户在活动期内下过订单并且已经参与过抽奖");
				messageDataBean.setCode(MessageDataBean.no_data_code);
				return messageDataBean;
			}
			Order orderActivity = orderActivityList.get(0);
			// 首先判断该用户在活动时间前有没有下过非零订单
			// 再次判断该用户在获奖记录表中有无数据(有数据则是老用户,这种情况下无数据则为新用户)
			if (order == null && adAwardsRecord == null) {
				AdCouponCode code = new AdCouponCode();
				HashMap<String, Object> data = new HashMap<String, Object>();
				AdCouponActivityConn activityConn = new AdCouponActivityConn();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dLarge = df.parse(df.format(orderActivity.getOrderDate()));
				Date dSmall = df.parse(df.format(adCouponActivity.getBeginDate()));
				Integer a = (int) ((dLarge.getTime() - dSmall.getTime()) / (1000 * 60 * 60 * 24));
				Integer weekNum = (a / 7 + 1);
				AdAwardsRecord record = new AdAwardsRecord();
				record.setUserId(adId);
				record.setType(type);
				record.setGroupId(adUser.getGroupNum().intValue());
				record.setWeekNum(weekNum.intValue());
				record.setOrderNum(orderActivity.getOrderNumber());
				lock.lock();
				// sql里记得count+1
				Integer count = adAwardsRecordDao.getGroupCountByGroupId(adUser.getGroupNum(), weekNum);
				adAwardsRecordDao.insert(record);
				lock.unlock();
				logger.info("送清凉抽奖用户为新用户,ID为:" + adId + ",参与时间周数为:" + weekNum + ",为当前单位第" + count + "位获奖者");
				// 判断该用户所属企业是否在一二等将的范围内
				if (adGroupActivityConnDao.getRealGIft(activityId, adUser.getGroupNum()) == null) {
					// 发三等奖四等奖
					logger.info("当前id为" + adId + "的用户企业不在一二等奖范围");
					code = sendThirdAndForth(adId, activityId, count);
					if (code.getCode() != null) {
						record.setCouponCode(code.getCode());
						record.setPriceLevel(code.getId() + "");
						adAwardsRecordDao.update(record);
						messageDataBean.setCode(MessageDataBean.success_code);
						data.put("level", code.getId());
						data.put("name", code.getBusinessid());
						messageDataBean.setData(data);
					} else {
						messageDataBean.setCode(MessageDataBean.no_data_code);
					}
					return messageDataBean;
				}
				logger.info("当前id为" + adId + "的用户企业在一二等奖范围");
				// 计算该公司下单数量,每周第四十个发一等奖
				// count = 8;
				if (count == 40) {
					activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 1);
					code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId,
							adId, activityConn.getCoupon().getId().intValue());
					record.setPriceLevel("1");
					data.put("level", 1);
					data.put("name", activityConn.getCoupon().getProductName());
					messageDataBean.setData(data);
					// 每周每八个发二等奖 共4个
				} else if (count < 40 && count % 8 == 0) {
					activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 2);
					code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId,
							adId, activityConn.getCoupon().getId().intValue());
					record.setPriceLevel("2");
					data.put("level", 2);
					data.put("name", activityConn.getCoupon().getProductName());
					messageDataBean.setData(data);
				} else {
					// 直接发三四等奖
					code = sendThirdAndForth(adId, activityId, count);
					if (code.getCode() == null) {
						messageDataBean.setCode(MessageDataBean.no_data_code);
						return messageDataBean;
					}
					record.setPriceLevel(code.getId() + "");
					data.put("level", code.getId());
					data.put("name", code.getBusinessid());
					messageDataBean.setData(data);
				}
				messageDataBean.setCode(MessageDataBean.success_code);
				record.setCouponCode(code.getCode());
				adAwardsRecordDao.update(record);
			} else if (order == null && adAwardsRecord != null) {
				logger.info("ID为" + adId + "的用户已点击参与过送清凉活动");

				messageDataBean.setCode(MessageDataBean.already_used_code);
			} else {
				logger.info("ID为" + adId + "的用户为老用户");
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("ID为" + adId + "的用户点击抽奖时报错,错误信息为" + e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

	private AdCouponCode sendThirdAndForth(Integer adId, Integer activityId, Integer count) {
		AdCouponCode code;
		AdCouponActivityConn activityConn;
		if (count % 2 != 0) {
			// 发三等奖
			activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 3);
			code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId, adId,
					activityConn.getCoupon().getId().intValue());
			if (code.getCode() == null) {
				logger.info("送清凉活动三等奖已空,发送四等奖");
				code = this.sendForth(adId, activityId);
			} else {
				code.setId(3l);
				code.setBusinessid(activityConn.getCoupon().getProductName());
			}
		} else {
			// 发四等奖
			code = sendForth(adId, activityId);
		}
		return code;
	}

	private AdCouponCode sendForth(Integer adId, Integer activityId) {
		AdCouponCode code = new AdCouponCode();
		AdCouponActivityConn activityConn = new AdCouponActivityConn();
		switch ((int) (Math.random() * 2 + 1)) {
		case 1:
			activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 4);
			code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId, adId,
					activityConn.getCoupon().getId().intValue());
			if (code.getCode() == null) {
				logger.info("送清凉活动四等奖(1)已空,发送四等奖(2)");
				activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 5);
				code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId, adId,
						activityConn.getCoupon().getId().intValue());
			}
			break;
		case 2:
			activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 5);
			code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId, adId,
					activityConn.getCoupon().getId().intValue());
			if (code.getCode() == null) {
				logger.info("送清凉活动四等奖(2)已空,发送四等奖(1)");
				activityConn = adCouponActivityConnDao.getByActivityIdAndLevel(activityId, 4);
				code = freeCouponBusinessServiceI.sendCoupon(activityConn.getCoupon().getBusinessId(), activityId, adId,
						activityConn.getCoupon().getId().intValue());
			}
			break;
		default:
			break;
		}
		code.setId(4l);
		code.setBusinessid(activityConn.getCoupon().getProductName());
		return code;
	}

	@Override
	public MessageDataBean getBringCoolnessUsers(Integer activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			AdCouponActivity adCouponActivity = adCouponActivityDao.get(activityId + "");
			Date endDate = df.parse(df.format(adCouponActivity.getEndDate()));
			Date nowDate = df.parse(df.format(new Date()));
			if (nowDate.getTime() > endDate.getTime()) {
				messageDataBean.setCode(MessageDataBean.time_out_code);
				return messageDataBean;
			}
			List<AdAwardsRecord> list = adAwardsRecordDao.findList();
			if (!list.isEmpty()) {
				data.put("list", list);
				messageDataBean.setData(data);
				messageDataBean.setCode(MessageDataBean.success_code);
				logger.info("送清凉参与会员数据获取成功:" + messageDataBean.toJsonString());
			} else {
				logger.info("送清凉暂无参与会员数据");
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

}
