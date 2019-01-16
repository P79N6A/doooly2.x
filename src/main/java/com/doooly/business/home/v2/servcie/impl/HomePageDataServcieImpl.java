package com.doooly.business.home.v2.servcie.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.business.home.v2.servcie.HomePageDataServcie;
import com.doooly.business.myorder.dto.HintReq;
import com.doooly.business.myorder.dto.HintResp;
import com.doooly.business.myorder.service.OrderService;
import com.doooly.common.DooolyResponseStatus;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.dao.reachad.AdAppHomePageDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.base.BaseResponse;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.coupon.FindExclusiveCouponResponse;
import com.doooly.dto.coupon.GetAdCouponActivityInfosResponse;
import com.doooly.dto.coupon.ReceiveExclusiveCouponRequest;
import com.doooly.dto.coupon.ReceiveExclusiveCouponResponse;
import com.doooly.dto.home.*;
import com.doooly.entity.coupon.AdCouponActivityInfos;
import com.doooly.entity.coupon.ExclusiveCoupon;
import com.doooly.entity.coupon.ReceiveExclusiveCoupon;
import com.doooly.entity.home.HomePageDataV2;
import com.doooly.entity.home.SplashScreenDataContract;
import com.doooly.entity.home.UserGuideFinish;
import com.doooly.entity.home.UserGuideFlow;
import com.doooly.entity.reachad.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @className: HomePageDataServcieImpl
 * @description: 获取兜礼APP首页数据的业务层
 * @author: wangchenyu
 * @date: 2018-02-12 16:40
 */
@Service
public class HomePageDataServcieImpl implements HomePageDataServcie {
	private static Logger log = LoggerFactory.getLogger(HomePageDataServcieImpl.class);
	@Autowired
	protected StringRedisTemplate redisTemplate;
	@Autowired
	private AdAppHomePageDao adAppHomePageDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;
	@Autowired
	private AdGroupDao adGroupDao;
	
	@Autowired
	private OrderService orderservice;

	@Override
	public GetHomePageDataV2Response getHomePageDataV2(GetHomePageDataV2Request request,
			GetHomePageDataV2Response response) {
		Integer userId = request.getUserId();
		HomePageDataV2 homePageData = new HomePageDataV2();
		BigDecimal zeroBigDecimal = new BigDecimal(0.00).setScale(2, BigDecimal.ROUND_DOWN);
		// 查询会员名字、会员所属企业、会员头像、会员可用积分、是否为认证会员
		AdAppUserInfos adUserInfos = adAppHomePageDao.findAdUserInfos(request.getUserId());
		homePageData.setMemberName(adUserInfos.getMemberName());
		homePageData.setIsPayPassword(adUserInfos.getIsPayPassword());
		homePageData.setIsSetPayPassword(adUserInfos.getIsSetPayPassword());
		homePageData.setMemberCompanyName(adUserInfos.getEnterpriseName());
		homePageData.setMemberHeadImgUrl(StringUtils.isBlank(adUserInfos.getMemberHeadImgURL())
				? adUserInfos.getEnterpriseLogoURL() : adUserInfos.getMemberHeadImgURL());
		homePageData.setAvailablePoints(adUserInfos.getAvailablePoints() == null ? zeroBigDecimal
				: adUserInfos.getAvailablePoints().setScale(2, BigDecimal.ROUND_DOWN));
		homePageData.setAuthFlag(adUserInfos.getAuthFlag() == null ? 0 : adUserInfos.getAuthFlag());
		// 查询会员的福利券
		int expiredNum = 0;
		List<AdAppUserCoupon> adAppUserCouponList = adAppHomePageDao.getAdUserCouponInfos(request.getUserId());
		for (AdAppUserCoupon coupon : adAppUserCouponList) {
			if (coupon.getExpiredDays() <= 3) {
				expiredNum += 1;
			}
		}
		homePageData.setCouponNum(adAppUserCouponList.size());
		homePageData.setCouponExpireNum(expiredNum);

		// 查询会员购物节省金额
		AdAppUserShoppingThrift adAppUserShoppingThrift = adAppHomePageDao.getAdUserThirftInfos(request.getUserId());
		homePageData.setThriftTotal(adAppUserShoppingThrift.getThriftTotal());
		homePageData.setThriftAmount(adAppUserShoppingThrift.getThriftAmount() == null ? zeroBigDecimal
				: adAppUserShoppingThrift.getThriftAmount().setScale(2, BigDecimal.ROUND_DOWN));

		// 1.Redis获取用户各个状态订单数
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String orderTotal = opsForValue.get("ordertotal:" + userId + ":0");// 已下单
		String finishTotal = opsForValue.get("ordertotal:" + userId + ":1");// 已完成
		String cancelTotal = opsForValue.get("ordertotal:" + userId + ":2");// 已取消
		log.info(String.format("用户 %s redis中已下单数:%s,已完成数:%s,已取消数:%s", userId, orderTotal, finishTotal, cancelTotal));
		// 2.DB获取用户各个状态订单数
		AdUserConn adUser = adUserDao.getOrderTotal(String.valueOf(userId));
		int orderTotalMap = adUser.getOrderTotal();
		int finishTotalMap = adUser.getFinishTotal();
		int cancelTotalMap = adUser.getCancelTotal();
		log.info(String.format("用户 %s DB中已下单数:%s,已完成数:%s,已取消数:%s", userId, orderTotalMap, finishTotalMap,
				cancelTotalMap));
		// 3.有新订单 设置flag
		if (StringUtils.isNotEmpty(orderTotal) && orderTotalMap > Integer.valueOf(orderTotal)) {
			homePageData.setNewOrderFlag(true);
		} else {
			homePageData.setNewOrderFlag(false);
		}
		if (StringUtils.isNotEmpty(finishTotal) && finishTotalMap > Integer.valueOf(finishTotal)) {
			homePageData.setNewFinishFlag(true);
		} else {
			homePageData.setNewFinishFlag(false);
		}
		if (StringUtils.isNotEmpty(cancelTotal) && cancelTotalMap > Integer.valueOf(cancelTotal)) {
			homePageData.setNewCancelFlag(true);
		} else {
			homePageData.setNewCancelFlag(false);
		}
		AdGroup adGroup = adGroupDao.findGroupByUserId(userId+"");
		homePageData.setAdGroup(adGroup);
		response.setData(homePageData);
		response.setStatus(DooolyResponseStatus.SUCCESS);
		return response;
	}

	@Override
	public UserGuideFlowResponse getUserAppGuideFlow(Integer userId, UserGuideFlowResponse response) {
		if (userId <= 0) {
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}

		AdUserBusinessExpansion userGuideInfos = adAppHomePageDao.findUserGuideInfos(userId);
		response.setStatus(DooolyResponseStatus.SUCCESS);
		UserGuideFlow userGuideFlow = new UserGuideFlow(userGuideInfos);
		response.setData(userGuideFlow);
		return response;
	}

	@Override
	public FindExclusiveCouponResponse findExclusiveCoupon(String userId, FindExclusiveCouponResponse response) {
		String exclusiveCouponActivityId = redisTemplate.opsForValue().get("doooly_exclusive_coupon_activityId");
		if (StringUtils.isEmpty(exclusiveCouponActivityId)) {
			exclusiveCouponActivityId = PropertiesHolder.getProperty("doooly.exclusive.coupon.activityId");
		}
		String exclusiveCouponCode = PropertiesHolder.getProperty("doooly.exclusive.coupon.code");
		// 1.先查询用户所属企业，是否参加专属优惠券活动
		AdGroupActivityConn adGroupActivityConn = adAppHomePageDao
				.findAdGroupActivityConnByActivityIdAndUserId(exclusiveCouponActivityId, userId);
		if (adGroupActivityConn == null) {
			log.info("adGroupActivityConn == null，userId：" + userId + "，用户所属企业《没有参加》专属优惠券活动。");
			response.setData(null);
		} else {
			log.info("adGroupActivityConn != null，userId：" + userId + "，用户所属企业《参加了》专属优惠券活动。");
			// 2.查询专属优惠券有哪些
			List<ExclusiveCoupon> exclusiveCouponList = adAppHomePageDao.findExclusiveCoupon(exclusiveCouponActivityId,
					exclusiveCouponCode);
			log.info("专属优惠券 exclusiveCouponList.size() = " + exclusiveCouponList.size());
			List<Long> couponIds = new ArrayList<Long>();
			Long activityId = null;
			if (exclusiveCouponList.size() > 0) {
				activityId = exclusiveCouponList.get(0).getActivityId();
				// 3.查询用户领取了哪些专属优惠券
				List<AdCouponCode> AdCouponCodeList = findUserReceivedExclusiveCoupon(userId, couponIds, activityId,
						exclusiveCouponList);
				log.info("用户已领取的专属优惠券 AdCouponCodeList.size() = " + AdCouponCodeList.size());
				couponIds = receivedCouponIds(couponIds, AdCouponCodeList);
				exclusiveCouponList = processingExclusiveCouponList(exclusiveCouponList, couponIds);
			}
			response.setData(exclusiveCouponList);
			response.setIsReceivedAll(exclusiveCouponList);
		}
		response.setStatus(DooolyResponseStatus.SUCCESS);
		return response;
	}

	/**
	 * 查询用户，领取过哪些专属优惠券
	 */
	private List<AdCouponCode> findUserReceivedExclusiveCoupon(String userId, List<Long> couponIds, Long activityId,
			List<ExclusiveCoupon> exclusiveCouponList) {
		for (ExclusiveCoupon exclusiveCoupon : exclusiveCouponList) {
			couponIds.add(exclusiveCoupon.getCouponId());
		}
		// 查询用户，领过哪些专属优惠券
		Map selectConditionMap = new HashMap();
		selectConditionMap.put("userId", userId);
		selectConditionMap.put("activityId", activityId);
		selectConditionMap.put("couponIds", couponIds);
		log.info("userId = {}, activityId = {}, couponIds = {}", userId, activityId, couponIds);
		return adAppHomePageDao.findUserReceivedExclusiveCoupon(selectConditionMap);
	}

	/**
	 * 返回用户已领取专属优惠券的id集合
	 */
	private List<Long> receivedCouponIds(List<Long> couponIds, List<AdCouponCode> adCouponCodeList) {
		couponIds.clear();
		for (AdCouponCode adCouponCode : adCouponCodeList) {
			couponIds.add(adCouponCode.getCoupon());
		}
		return couponIds;
	}

	/**
	 * 循环为专属优惠券中的isReceived赋值
	 */
	private List<ExclusiveCoupon> processingExclusiveCouponList(List<ExclusiveCoupon> exclusiveCouponList,
			List<Long> couponIds) {
		for (ExclusiveCoupon exclusiveCoupon : exclusiveCouponList) {
			for (Long couponId : couponIds) {
				if (exclusiveCoupon.getCouponId().equals(couponId)) {
					exclusiveCoupon.setIsReceived(1);
					break;
				}
			}
			if (exclusiveCoupon.getIsReceived() == null || exclusiveCoupon.getIsReceived() != 1) {
				exclusiveCoupon.setIsReceived(0);
			}
		}
		return exclusiveCouponList;
	}

	@Override
	public ReceiveExclusiveCouponResponse receiveExclusiveCoupon(ReceiveExclusiveCouponRequest request,
			ReceiveExclusiveCouponResponse response) {
		response = assertReceiveExclusiveCouponRequestParams(request, response);
		if (response.getCode() != null) {
			return response;
		}

		List<String> couponIdList = Arrays.asList(request.getCouponIds().split(","));
		MessageDataBean messageDataBean = null;
		List<ReceiveExclusiveCoupon> receiveList = new ArrayList<ReceiveExclusiveCoupon>();
		ReceiveExclusiveCoupon receiveExclusiveCoupon = null;
		// 循环领取专属优惠券
		for (String couponId : couponIdList) {
			receiveExclusiveCoupon = new ReceiveExclusiveCoupon();
			receiveExclusiveCoupon.setUserId(request.getUserId());
			receiveExclusiveCoupon.setActivityId(request.getActivityId());
			receiveExclusiveCoupon.setCouponId(Integer.valueOf(couponId));
			messageDataBean = freeCouponBusinessServiceI.receiveCoupon(receiveExclusiveCoupon.getUserId(),
					receiveExclusiveCoupon.getCouponId(), receiveExclusiveCoupon.getActivityId(), null);
			if (MessageDataBean.success_code.equals(messageDataBean.getCode())
					|| MessageDataBean.already_receive_code.equals(messageDataBean.getCode())) {
				// 领取成功，或以领取
				receiveExclusiveCoupon.setIsReceived(1);
			} else {
				// 领取失败
				log.warn("领券失败：receiveExclusiveCoupon = {}, messageDataBean = {}",
						JSONObject.toJSONString(receiveExclusiveCoupon), JSONObject.toJSONString(messageDataBean));
				receiveExclusiveCoupon.setIsReceived(0);
			}
			// receiveExclusiveCoupon对象，添加到receiveList中
			receiveList.add(receiveExclusiveCoupon);
		}

		response.setStatus(DooolyResponseStatus.SUCCESS);
		response.setData(receiveList);
		return response;
	}

	/**
	 * 断言ReceiveExclusiveCouponRequest请求信息
	 */
	private ReceiveExclusiveCouponResponse assertReceiveExclusiveCouponRequestParams(
			ReceiveExclusiveCouponRequest request, ReceiveExclusiveCouponResponse response) {
		// userId 参数值错误
		if (request.getUserId() <= 0) {
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}
		// activityId 参数值错误
		if (request.getActivityId() <= 0) {
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}
		// couponids 参数值错误
		if (StringUtils.isBlank(request.getCouponIds())) {
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}
		List<String> couponIdList = Arrays.asList(request.getCouponIds().split(","));
		for (String couponId : couponIdList) {
			if (!StringUtils.isNumeric(couponId) || "0".equals(couponId)) {
				response.setStatus(DooolyResponseStatus.BAD_PARAMS);
				return response;
			}
		}
		// 验参完成，无问题，返回空的 response 对象
		return response;
	}

	@Override
	public BaseResponse insertOrUpdateUserGuideFinish(GuideFinishRequest request, BaseResponse response) {
		response = assertGuideFinishRequestParams(request, response);
		if (response.getCode() != null) {
			return response;
		}
		// 1.先查询记录是否存在
		UserGuideFinish userGuideFinish = new UserGuideFinish();
		userGuideFinish.setUserId(request.getUserId());
		userGuideFinish.setBusinessType("app-guide");
		userGuideFinish = adAppHomePageDao.findUserGuide(userGuideFinish);
		int num = 0;
		if (userGuideFinish == null) {
			// 2.记录不存在，重新创建UserGuideFinish，插入数据库
			userGuideFinish = new UserGuideFinish();
			userGuideFinish.setUserId(request.getUserId());
			userGuideFinish.setBusinessType("app-guide");
			if (request.getSecretCode() == 1) {
				userGuideFinish.setNoviceGuideFinished(1);
			} else {
				userGuideFinish.setIntegralGuideFinished(1);
			}
			num = adAppHomePageDao.insertUserGuideFinish(userGuideFinish);
			log.info("insertUserGuideFinish num = " + num);
		} else {
			if (request.getSecretCode() == 1) {
				userGuideFinish.setNoviceGuideFinished(1);
			} else {
				userGuideFinish.setIntegralGuideFinished(1);
			}
			num = adAppHomePageDao.updateUserGuideFinish(userGuideFinish);
			log.info("updateUserGuideFinish num = " + num);
		}

		if (num == 1) {
			response.setStatus(DooolyResponseStatus.SUCCESS);
		} else {
			response.setStatus(DooolyResponseStatus.FAIL);
		}
		return response;
	}

	/**
	 * 断言GuideFinishRequest请求信息
	 */
	private BaseResponse assertGuideFinishRequestParams(GuideFinishRequest request, BaseResponse response) {
		// userId 参数值错误
		if (request.getUserId() <= 0) {
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}
		// secretCode 参数值错误
		if (request.getSecretCode() != 1 && request.getSecretCode() != 2) {
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}
		// 验参完成，无问题，返回空的 response 对象
		return response;
	}

	@Override
	public BaseResponse setExclusiveCouponActivityId(JSONObject json, BaseResponse response) {
		String secretCode = json.getString("secretCode");
		String activityId = json.getString("activityId");
		if (!"reachlife558558".equals(secretCode)) {
			log.warn("No permission setExclusiveCouponActivityId : secretCode = " + secretCode);
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}
		if ("0".equals(activityId) || !StringUtils.isNumeric(activityId)) {
			log.warn("setExclusiveCouponActivityId error : activityId = " + activityId);
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}

		redisTemplate.opsForValue().set("doooly_exclusive_coupon_activityId", activityId);
		log.info("在redis中设置专属优惠券的activityId：成功。activityId = " + activityId);
		response.setStatus(DooolyResponseStatus.SUCCESS);
		return response;
	}

	@Override
	public GetSplashScreenResponse getSplashScreen(String groupId, GetSplashScreenResponse response) {
		if (StringUtils.isEmpty(groupId) || "0".equals(groupId) || !StringUtils.isNumeric(groupId)) {
			response.setStatus(DooolyResponseStatus.BAD_PARAMS);
			return response;
		}
		SplashScreenDataContract splashScreenDataContract = adAppHomePageDao.getSplashScreen(groupId);
		if (splashScreenDataContract == null) {
			response.setStatus(DooolyResponseStatus.FAIL);
			return response;
		}

		response.setStatus(DooolyResponseStatus.SUCCESS);
		splashScreenDataContract.setTimestamp(System.currentTimeMillis());
		response.setData(splashScreenDataContract);
		return response;
	}

	@Override
	public GetAdCouponActivityInfosResponse getAdCouponActivityInfos(String activityId,
			GetAdCouponActivityInfosResponse response) {
		AdCouponActivityInfos adCouponActivityInfos = adAppHomePageDao.getAdCouponActivityInfos(activityId);
		response.setStatus(DooolyResponseStatus.SUCCESS);
		response.setData(adCouponActivityInfos);
		return response;
	}


	@Override
	public GetHomePageDataV2Response getHomePageDataV2_2(GetHomePageDataV2Request request,
			GetHomePageDataV2Response response) {
		response = this.getHomePageDataV2(request, response);
		HomePageDataV2 homePageData = response.getData();
		if (homePageData != null) {
			// 查询待返积分
			BigDecimal returnPoint = adUserDao.getReturnPoint(String.valueOf(request.getUserId()));
			homePageData.setReturnPoints(returnPoint != null ? returnPoint.toString() : "0.00");
			HintReq req = new HintReq();
			req.setUserId(String.valueOf(request.getUserId()));
			HintResp resp = orderservice.getHint(req);
			homePageData.setNewOrderFlag(resp.isNewOrderFlag());
			homePageData.setNewFinishFlag(resp.isNewFinishFlag());
			homePageData.setNewCancelFlag(resp.isNewCancelFlag());
		}
		return response;
	}
	


}
