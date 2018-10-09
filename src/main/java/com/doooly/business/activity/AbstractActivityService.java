package com.doooly.business.activity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.common.constants.ActivityConstants.ActivityEnum;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdGroupActivityConnDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;

public abstract class AbstractActivityService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;
	private AdCouponActivityConnDao couponActivityConnDao;
	private AdGroupActivityConnDao groupActivityConnDao;
	private AdCouponActivityDao couponActivityDao;
	private AdCouponCodeDao couponCodeDao;

	public AbstractActivityService() {
		super();
	}

	public AbstractActivityService(FreeCouponBusinessServiceI freeCouponBusinessServiceI,
			AdCouponActivityConnDao couponActivityConnDao, AdGroupActivityConnDao groupActivityConnDao,
			AdCouponActivityDao couponActivityDao, AdCouponCodeDao couponCodeDao) {
		super();
		this.freeCouponBusinessServiceI = freeCouponBusinessServiceI;
		this.couponActivityConnDao = couponActivityConnDao;
		this.groupActivityConnDao = groupActivityConnDao;
		this.couponActivityDao = couponActivityDao;
		this.couponCodeDao = couponCodeDao;
	}

	/**
	 * 发放券流程 1.发放券前准备（扩展）可选 2.验证活动有效性 3.发放礼品券 4.发放券后业务处理（扩展）可选
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午10:05:27
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	public final MessageDataBean send(JSONObject sendJsonReq) {
		long start = System.currentTimeMillis();
		MessageDataBean result = null;
		// 1.发放券前准备（扩展）可选
		if (isDoBefore()) {
			result = doBefore(sendJsonReq);
			if (result != null) {
				return result;
			}
		}
		// 用户ID
		Integer userId = sendJsonReq.getInteger("userId");
		// 活动ID
		Integer activityId = sendJsonReq.getInteger("activityId");
		// 礼品券ID
		Integer couponId = sendJsonReq.getInteger("couponId");
		// 2.验证活动有效性
		result = validParams(userId, activityId);
		// 2.1若result不为空，则有效性验证失败
		if (result != null) {
			log.warn("验证活动有效性失败，paramReqJson={}, error={}", sendJsonReq.toString(), JSONObject.toJSONString(result));
			return result;
		}
		// 3.发放礼品券
		result = sendCoupon(userId, Arrays.asList(couponId), activityId);
		// 4.发放券后业务处理（扩展）可选
		if (isDoAfter()) {
			doAfter(sendJsonReq);
		}
		log.info("活动发放礼品券流程结束，activityId={}, userId={}, result={}, cost(ms)={}", activityId, userId,
				JSONObject.toJSONString(result), System.currentTimeMillis() - start);
		return result;
	}

	/**
	 * 根据返回结果是否调用doBefore方法
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午9:32:31
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return true:调用doBefore()方法 false:不调用doBefore方法
	 * 
	 */
	protected Boolean isDoBefore() {
		return false;
	}

	/**
	 * 预留扩展使用，非发券正常功能 发放券前是否需要进行其它业务处理
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午9:37:54
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	protected MessageDataBean doBefore(JSONObject beforeJson) {
		return null;
	}

	/**
	 * 验证请求参数有效性
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午9:44:18
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	protected MessageDataBean validParams(Integer userId, Integer activityId) {
		// 1.验证活动有效性
		AdCouponActivityConn activityConn = couponActivityConnDao.getByActivityId(activityId);
		// 1.1验证活动是否存在
		if (activityConn == null) {
			return new MessageDataBean(ActivityEnum.ACTIVITY_NOT_EXIST);
		}
		Long couponId = activityConn.getCoupon().getId();
		AdCouponActivity couponActivity = couponActivityDao.getActivityById(activityId);
		// 1.2验证活动是否开启
		if (Integer.valueOf(couponActivity.getState()) == AdCouponActivity.ACTIVITY_CLOSE) {
			return new MessageDataBean(ActivityEnum.ACTIVITY_CLOSED);
		}
		// 1.3验证活动是否开始
		Date nowDate = new Date();
		if (couponActivity.getBeginDate().compareTo(nowDate) > 0) {
			return new MessageDataBean(ActivityEnum.ACTIVITY_NOT_STARTED);
		}
		// 1.4 验证活动是否结束
		if (couponActivity.getEndDate().compareTo(nowDate) < 0) {
			return new MessageDataBean(ActivityEnum.ACTIVITY_ENDED);
		}

		// 2.验证是否有资格参与
		int checkResult = groupActivityConnDao.checkUserEligibleActivities(userId, activityId);
		if (checkResult == 0) {
			return new MessageDataBean(ActivityEnum.ACTIVITY_NOT_PARTICIPATING);
		}
		// 3.验证是否已领取优惠券
		AdCouponCode coupon = new AdCouponCode();
		coupon.setActivityId(activityId.longValue());
		coupon.setUserId(userId.longValue());
		coupon.setCoupon(couponId);
		int couponCount = couponCodeDao.getUserCouponCountByIds(coupon);
		if (couponCount > 0) {
			return new MessageDataBean(ActivityEnum.ACTIVITY_RECEIVED);
		}
		return null;
	}

	/**
	 * 发放礼品券
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午9:45:28
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	public MessageDataBean sendCoupon(Integer userId, List<Integer> couponIdList, Integer activityId) {
		MessageDataBean messageDataBean = null;
		for (Integer couponId : couponIdList) {
			messageDataBean = freeCouponBusinessServiceI.receiveCoupon(userId, couponId, activityId, null);
			if (!messageDataBean.getCode().equals(MessageDataBean.success_code)) {
				log.warn("发放礼品券出错，error={}", JSONObject.toJSONString(messageDataBean));
				break;
			}
		}
		return messageDataBean;
	}

	/**
	 * 根据返回结果是否调用doAfter方法
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午9:32:31
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return true:调用doAfter()方法 false:不调用doAfter方法
	 * 
	 */
	protected Boolean isDoAfter() {
		return false;
	}

	/**
	 * 预留扩展使用，非发券正常功能 发放券后是否需要进行其它业务处理
	 * 
	 * @author hutao
	 * @date 创建时间：2018年10月9日 上午9:37:54
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	protected void doAfter(JSONObject beforeJson) {
	}
}
