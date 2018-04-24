package com.doooly.business.activity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.YanXuanBusinessServiceI;
import com.doooly.business.common.service.impl.AdCouponActivityService;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdVoteRecordDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCoupon;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdVoteRecord;

/**
 * 网易严选活动业务Service实现
 * 
 * @author yipeng.zhao
 * @version 2017年9月19日
 */
@Service
public class YanXuanBusinessService implements YanXuanBusinessServiceI {

	private static Logger logger = Logger.getLogger(YanXuanBusinessService.class);

	private static final String YANXUAN_USER_INFO = "USER:DATA:0d4e9c9a82eer97d866yanxuan";

	private static final String WANG_YI_ACTIVITY_ID = ResourceBundle.getBundle("doooly")
			.getString("wang_yi_activity_id");

	private static String zsetKey = "COUPON:USER:SET";

	@Autowired
	private AdCouponCodeDao adCouponCodeDao;
	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessService;
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;
	@Autowired
	private AdCouponActivityService adCouponActivityService;
	@Autowired
	private AdVoteRecordDao adVoteRecordDao;

	@Override
	public MessageDataBean getCounpon(String userId, String telephone, String type, String cardNumber,
			String activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
			AdCouponActivityConn adCouponActivityConn = list.get(0);
			AdCoupon adCoupon = adCouponActivityConn.getCoupon();
			AdCouponActivityConn activityConn = adCouponActivityConnDao.getByActivityId(activityId);
			// AdCouponCode couponCode =
			// adCouponCodeDao.getCouponCode(Integer.valueOf(userId),
			// Integer.valueOf(activityId), adCoupon.getId().intValue());
			// 判断是否还有库存
			String businessId = adCoupon.getBusinessId();
			Integer couponId = adCouponActivityConn.getCouponId();
			Long size = redisTemplate.opsForList().size(businessId + "+" + couponId + "+" + activityId);
			if (size == 0) {
				messageDataBean.setCode(MessageDataBean.no_data_code);
				return messageDataBean;
			}
			AdCouponActivity adCouponActivity = adCouponActivityDao.get(activityConn.getActivityId() + "");
			// 判断兑换券是否已经过期
			Date now = new Date();
			Date activityDate = adCouponActivity.getEndDate();
			if (now.after(activityDate)) {
				messageDataBean.setCode("4000");
				return messageDataBean;
			}
			String result = redisTemplate.opsForValue().get("yanxuan_user_telephone");
			// 保存到redis
			result = result == null ? "" : result + "," + telephone;
			result = result.substring(1);
			redisTemplate.opsForValue().set("yanxuan_user_telephone", result, 86400);
			// 放入缓存池
			List<String> aList = new ArrayList<String>();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("telephone", telephone);
			jsonObject.put("type", type);
			jsonObject.put("cardNumber", cardNumber);
			jsonObject.put("activityInfo", businessId + "+" + couponId + "+" + activityId);
			aList.add(jsonObject.toJSONString());
			redisTemplate.opsForList().leftPushAll(YANXUAN_USER_INFO, aList);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MessageDataBean sendWangYiCoupon(String data) {
		MessageDataBean messageDataBean = new MessageDataBean();
		List<JSONObject> jsonData = (List<JSONObject>) JSONObject.parse(data);
		try {
			ArrayList<String> mobiles = new ArrayList<String>();
			ArrayList<String> userIds = new ArrayList<String>();
			for (JSONObject jsonObject : jsonData) {
				mobiles.add(jsonObject.getString("telephone"));
			}
			// 如没有手机号则直接返回
			if (mobiles.size() == 0) {
				messageDataBean.setCode(MessageDataBean.success_code);
				return messageDataBean;
			}
			userIds = adUserDao.findByTelephones(mobiles);
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(WANG_YI_ACTIVITY_ID);
			AdCouponActivityConn adCouponActivityConn = list.get(0);
			AdCoupon adCoupon = adCouponActivityConn.getCoupon();
			// 批量优化start
			// 把已经领取过活动卡券的用户筛选出去
			List<Long> hadCodeUsers = adCouponCodeDao.checkIsHadCode(WANG_YI_ACTIVITY_ID, adCoupon.getId(), userIds);
			for (Long userId : hadCodeUsers) {
				if (userIds.contains(userId.toString())) {
					userIds.remove(userId.toString());
				}
			}
			String businessId = adCoupon.getBusinessId();
			Integer couponId = adCouponActivityConn.getCouponId();
			Integer activityId = adCouponActivityConn.getActivityId();
			Long size = redisTemplate.opsForList().size(businessId + "+" + couponId + "+" + activityId);
			if (size == 0) {
				// 直接返回
				messageDataBean.setCode(MessageDataBean.no_data_code);
				return messageDataBean;
			}
			if (size < userIds.size()) {
				for (int i = userIds.size() - 1; i >= size; i--) {
					// 索引大于券码索引,则移除
					if (i >= size) {
						userIds.remove(i);
					}
				}
			}
			List<String> userList = freeCouponBusinessService.sendCoupons(businessId, activityId, userIds, couponId);

			// for (String userId : userIds) {
			// freeCouponBusinessService.sendCoupon(businessId, activityId,
			// Integer.valueOf(userId), couponId);
			// }
			// 批量优化end
			// for (String mobile : mobiles) {
			// AdUser adUser = adUserDao.findByMobile(mobile);
			// AdCouponCode couponCode = adCouponCodeDao.getCouponCode(adUser
			// .getId().intValue(), Integer
			// .valueOf(WANG_YI_ACTIVITY_ID), adCoupon.getId()
			// .intValue());
			// // 查询是否有用户已经领取过券
			// // 不为空则表示已经领取过卡券
			// if (couponCode == null) {
			// userIds.add(adUser.getId().toString());
			// }
			// }
			// String businessId = adCoupon.getBusinessId();
			// Integer couponId = adCouponActivityConn.getCouponId();
			// Integer activityId = adCouponActivityConn.getActivityId();
			//
			// Long size = redisTemplate.opsForList().size(
			// businessId + "+" + couponId + "+" + activityId);
			// // 判断缓存卡券是否有足够余量
			// if (size < userIds.size()) {
			// messageDataBean.setCode(MessageDataBean.no_data_code);
			// return messageDataBean;
			// }
			// for (String userId : userIds) {
			// freeCouponBusinessService.sendCoupon(businessId, activityId,
			// Integer.valueOf(userId), couponId);8+
			// }

			if (userList != null && userList.size() > 0) {
				messageDataBean.setCode(MessageDataBean.success_code);
			} else {
				messageDataBean.setCode(MessageDataBean.failure_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("================发送网易严选卡券失败=====================");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

	/**
	 * 获取网易严选的信息
	 * 
	 * @param userId
	 * @param telephone
	 * @param type
	 * @param cardNumber
	 * @param activityId
	 * @return
	 */
	public MessageDataBean getCounponInfo(String userId, String telephone, String type, String cardNumber,
			String activityId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			// 判断该用户是否已经领取过卡券
			//
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
			AdCouponActivityConn adCouponActivityConn = list.get(0);
			AdCoupon adCoupon = adCouponActivityConn.getCoupon();
			AdCouponActivityConn activityConn = adCouponActivityConnDao.getByActivityId(activityId);
			AdCouponCode couponCode = adCouponCodeDao.getCouponCode(Integer.valueOf(userId),
					Integer.valueOf(activityId), adCoupon.getId().intValue());

			// 不为空则表示已经领取过卡券
			if (couponCode != null) {
				// 已经领取
				messageDataBean.setCode(MessageDataBean.already_receive_code);
				data.put("actConnId", activityConn.getId());
				messageDataBean.setData(data);
				return messageDataBean;
			}

			// 缓存中查询是否已经正在发放卡券
			String result = redisTemplate.opsForValue().get("yanxuan_user_telephone");
			if (result.contains(telephone)) {
				// 已经正在发券
				messageDataBean.setCode("3000");
				return messageDataBean;
			}
			// AdCouponActivity activity = adCouponActivityDao.get(activityId);
			// Date beginDate1 = activity.getBeginDate();
			// Date endDate1 = activity.getEndDate();
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			// String beginDate = sdf.format(beginDate1);
			// String endDate = sdf.format(endDate1);
			// data.put("beginDate", beginDate);
			// data.put("endDate", endDate);
			// messageDataBean.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	/**
	 * 判断是否可以打开我的福利
	 * 
	 * @param userId
	 * @param activityId
	 * @return
	 */
	public MessageDataBean myCoupon(String userId, String activityId) {

		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
			AdCouponActivityConn adCouponActivityConn = list.get(0);
			AdCoupon adCoupon = adCouponActivityConn.getCoupon();
			AdCouponActivityConn activityConn = adCouponActivityConnDao.getByActivityId(activityId);
			AdCouponCode couponCode = adCouponCodeDao.getCouponCode(Integer.valueOf(userId),
					Integer.valueOf(activityId), adCoupon.getId().intValue());

			if (couponCode != null) {
				// 已经领取
				messageDataBean.setCode(MessageDataBean.already_receive_code);
				data.put("actConnId", activityConn.getId());
				messageDataBean.setData(data);
				return messageDataBean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MessageDataBean sendCoupons(String data) {
		MessageDataBean messageDataBean = new MessageDataBean();
		List<JSONObject> jsonData = (List<JSONObject>) JSONObject.parse(data);
		try {
			// 归类不同活动卡券
			ArrayList<Map<String, List<JSONObject>>> userCodeList = new ArrayList<Map<String, List<JSONObject>>>();
			while (jsonData.size() != 0) {
				ArrayList<JSONObject> list = new ArrayList<JSONObject>();
				HashMap<String, List<JSONObject>> hashMap = new HashMap<String, List<JSONObject>>();
				String s = jsonData.get(0).getString("activityInfo");
				for (int i = 0; i < jsonData.size(); i++) {
					if (s.equals(jsonData.get(i).getString("activityInfo"))) {
						list.add(jsonData.get(i));
						jsonData.remove(i);
						i--;
					}
				}
				hashMap.put(s, list);
				userCodeList.add(hashMap);
			}

			for (int i = 0; i < userCodeList.size(); i++) {
				Map<String, List<JSONObject>> map = userCodeList.get(i);
				Set<String> keySet = map.keySet();
				String key = "";
				for (String str : keySet) {
					key = str;
				}
				String[] ids = key.split("\\+");
				String businessId = ids[0];
				String couponId = ids[1];
				String activityId = ids[2];
				List<JSONObject> userInfoList = map.get(key);

				ArrayList<String> mobiles = new ArrayList<String>();
				List<String> userIds = new ArrayList<String>();
				for (JSONObject jsonObject : userInfoList) {
					mobiles.add(jsonObject.getString("telephone"));
				}
				// 如没有手机号则直接返回
				if (mobiles.size() == 0) {
					continue;
				}
				userIds = adUserDao.findByTelephones(mobiles);
				List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
				AdCouponActivityConn adCouponActivityConn = list.get(0);
				AdCoupon adCoupon = adCouponActivityConn.getCoupon();
				// 批量优化start
				// 把已经领取过活动卡券的用户筛选出去
				List<Long> hadCodeUsers = adCouponCodeDao.checkIsHadCode(activityId, adCoupon.getId(), userIds);
				for (Long userId : hadCodeUsers) {
					if (userIds.contains(userId.toString())) {
						userIds.remove(userId.toString());
					}
				}
				Long size = redisTemplate.opsForList().size(businessId + "+" + couponId + "+" + activityId);
				if (size == 0) {
					// 直接返回
					messageDataBean.setCode(MessageDataBean.no_data_code);
					return messageDataBean;
				}
				if (size < userIds.size()) {
					for (int j = userIds.size() - 1; j >= size; j--) {
						// 索引大于券码索引,则移除
						if (j >= size) {
							userIds.remove(j);
						}
					}
				}
				List<String> userList = freeCouponBusinessService.sendCoupons(businessId, Integer.valueOf(activityId),
						userIds, Integer.valueOf(couponId));
				if (userList == null || userList.size() == 0) {
					messageDataBean.setCode(MessageDataBean.failure_code);
				}
			}

			messageDataBean.setCode(MessageDataBean.success_code);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("================发送网易严选卡券失败=====================");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

	/*
	 * ==========================================================严选活动2==========
	 * ===============================================
	 */

	/**
	 * 获取用户领取网易卡券信息
	 * 
	 * @param userId
	 * @param telephone
	 * @param type
	 * @param cardNumber
	 * @param idFlag
	 * @return
	 */
	public MessageDataBean getCouponTwoInfo(String userId, String telephone, String type, String cardNumber,
			String idFlag) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 通过后台配置的IDFLAG 来获取活动的id
			// String activityId =
			// adCouponActivityService.getActivityIdByIdFlag(idFlag);
			// 判断该用户是否已经领取过卡券
			// List<AdCouponActivityConn> list = adCouponActivityConnDao
			// .getActivityConnByActivityId(activityId);
			// AdCouponActivityConn adCouponActivityConn = list.get(0);
			// AdCoupon adCoupon = adCouponActivityConn.getCoupon();
			// AdCouponActivityConn activityConn = adCouponActivityConnDao
			// .getByActivityId(activityId);
			// AdCouponCode couponCode = adCouponCodeDao.getCouponCode(
			// Integer.valueOf(userId), Integer.valueOf(activityId),
			// adCoupon.getId().intValue());

			// // 不为空则表示已经领取过卡券
			// if (couponCode != null) {
			// // 已经领取
			// messageDataBean.setCode(MessageDataBean.already_receive_code);
			// data.put("actConnId", activityConn.getId());
			// messageDataBean.setData(data);
			// return messageDataBean;
			// }
			//
			// // 缓存中查询是否已经正在发放卡券
			// String result = redisTemplate.opsForValue().get(
			// "yanxuan_user_telephone");
			// if (result.contains(telephone)) {
			// // 已经正在发券
			// messageDataBean.setCode("3000");
			// return messageDataBean;
			// }
			AdCouponActivity couponActivity = adCouponActivityService.getActivityIdByIdFlag(idFlag);
			String activityId = couponActivity.getId() + "";
			String state = adVoteRecordDao.getByUserId(userId, activityId);
			// 没有点击过按钮，跳转到我要特权页面
			if (state == null) {
				messageDataBean.setCode(MessageDataBean.success_code);
				return messageDataBean;
			} else if ("1".equals(state)) {
				// 已点击我要特权，数据未同步到严选,跳转成功页面
				messageDataBean.setCode("2000");
				return messageDataBean;
			} else {
				// 跳转到前往绑定页面
				messageDataBean.setCode("3000");
				return messageDataBean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	/**
	 * 点击我要特权，数据放入缓存池中
	 * 
	 * @param userId
	 * @param telephone
	 * @param type
	 * @param cardNumber
	 * @param idFlag
	 * @return
	 */
	public MessageDataBean getPrivilege(String userId, String telephone, String type, String cardNumber, String idFlag,
			String sex) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			AdCouponActivity couponActivity = adCouponActivityService.getActivityIdByIdFlag(idFlag);
			String activityId = couponActivity.getId() + "";
			String state = adVoteRecordDao.getByUserId(userId, activityId);
			// 没有点击过按钮，跳转到我要特权页面
			if ("1".equals(state)) {
				messageDataBean.setCode("3000");
				return messageDataBean;
			} else if ("2".equals(state) || "3".equals(state) || "4".equals(state)) {
				// 跳转到前往绑定页面
				messageDataBean.setCode("3001");
				return messageDataBean;
			}
			// 通过后台配置的IDFLAG 来获取活动的id
			List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
			AdCouponActivityConn adCouponActivityConn = list.get(0);
			AdCoupon adCoupon = adCouponActivityConn.getCoupon();
			AdCouponActivityConn activityConn = adCouponActivityConnDao.getByActivityId(activityId);
			AdCouponActivity adCouponActivity = adCouponActivityDao.get(activityConn.getActivityId() + "");
			// 判断兑换券是否已经过期
			Date now = new Date();
			Date activityDate = adCouponActivity.getEndDate();
			if (now.after(activityDate)) {
				messageDataBean.setCode("4000");
				return messageDataBean;
			}
			// 判断是否还有库存
			String businessId = adCoupon.getBusinessId();
			Integer couponId = adCouponActivityConn.getCouponId();
			Long size = redisTemplate.opsForList().size(businessId + "+" + couponId + "+" + activityId);
			if (size == 0) {
				messageDataBean.setCode(MessageDataBean.no_data_code);
				return messageDataBean;
			}
			String result = redisTemplate.opsForValue().get("yanxuan_user_telephone");
			// 保存到redis
			result = result == null ? "," + telephone + ":" + sex : result + "," + telephone + ":" + sex;
			result = result.substring(1);
			redisTemplate.opsForValue().set("yanxuan_user_telephone", result);
			// 放入缓存池
			List<String> aList = new ArrayList<String>();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("telephone", telephone);
			jsonObject.put("type", type);
			jsonObject.put("cardNumber", cardNumber);
			jsonObject.put("activityInfo", businessId + "+" + couponId + "+" + activityId);
			aList.add(jsonObject.toJSONString());
			redisTemplate.opsForList().leftPushAll(YANXUAN_USER_INFO, aList);
			// 把用户操作进行记录
			AdVoteRecord adVoteRecord = new AdVoteRecord();
			adVoteRecord.setActivityId(Integer.valueOf(activityId));
			adVoteRecord.setUserWechatOpenId(userId);
			adVoteRecord.setCreateDate(new Date());
			adVoteRecord.setOptionId(Integer.valueOf(sex));
			adVoteRecord.setState('1');
			adVoteRecord.setMobile(telephone);
			adVoteRecordDao.insert(adVoteRecord);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	/**
	 * 点击前往绑定
	 * 
	 * @param userId
	 * @param telephone
	 * @param type
	 * @param cardNumber
	 * @return
	 */
	public MessageDataBean toBind(String userId, String telephone, String type, String cardNumber, String idFlag) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			AdCouponActivity couponActivity = adCouponActivityService.getActivityIdByIdFlag(idFlag);
			String activityId = couponActivity.getId() + "";
			String state = adVoteRecordDao.getByUserId(userId, activityId);
			// 正在发送卡券中
			if ("3".equals(state)) {
				messageDataBean.setCode("2000");
				return messageDataBean;
			} else if ("4".equals(state)) {
				// 已经领取到券
				messageDataBean.setCode("3000");
				return messageDataBean;
			} else {
//				ZSetOperations<String, String> zsetOps = redisTemplate.opsForZSet();
				// 点击绑定的用户手机号
				// 将数据附带当前时间加入缓存池(只传手机号即可)
//				zsetOps.add(zsetKey, telephone + ":" + idFlag, System.currentTimeMillis());
				redisTemplate.opsForValue().set("yanxuan_activity_id", activityId , 30 ,TimeUnit.DAYS);
				adVoteRecordDao.updateState('3', userId, activityId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

	/**
	 * 发送卡券
	 * 
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MessageDataBean sendWangYiTwoCoupons(String msg) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			logger.info("================================msg:" + msg + "============================");
			List<String> mobileList = (List<String>) JSONObject.parse(msg);
			logger.info("================================mobileList:" + mobileList + "============================");
			List<String> userIds = new ArrayList<String>();
			List<String> mobiles = new ArrayList<String>();
			if (mobileList != null && mobileList.size() > 0) {
				String activityId = mobileList.get(0).split(":")[1];
				for (String telephone : mobileList) {
					String[] result = telephone.split(":");
					mobiles.add(result[0]);
				}
				userIds = adUserDao.findByTelephones(mobiles);
//				AdCouponActivity couponActivity = adCouponActivityService.getActivityIdByIdFlag(idFlag);
//				String activityId = couponActivity.getId() + "";
				List<AdCouponActivityConn> list = adCouponActivityConnDao.getActivityConnByActivityId(activityId);
				AdCouponActivityConn adCouponActivityConn = list.get(0);
				AdCoupon adCoupon = adCouponActivityConn.getCoupon();
				// 判断是否还有库存
				String businessId = adCoupon.getBusinessId();
				Integer couponId = adCouponActivityConn.getCouponId();
				List<String> userList = freeCouponBusinessService.sendCoupons(businessId, Integer.valueOf(activityId), userIds,
						couponId);
				if (userList != null && userList.size()> 0 ){
					for (String userId : userList) {
						adVoteRecordDao.updateState('4', userId, activityId);
					}
				}
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		messageDataBean.setCode(MessageDataBean.success_code);
		return messageDataBean;
	}

}
