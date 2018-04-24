package com.doooly.dao.reachad;

import com.doooly.entity.coupon.AdCouponActivityInfos;
import com.doooly.entity.coupon.ExclusiveCoupon;
import com.doooly.entity.home.SplashScreenDataContract;
import com.doooly.entity.home.UserGuideFinish;
import com.doooly.entity.reachad.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 王晨宇 on 2018/2/23.
 */
public interface AdAppHomePageDao {
	AdAppUserInfos findAdUserInfos(int userId);
	AdAppUserInfos findAdUserInfosAndNoviceGuide(int userId);
	List<AdAppUserCoupon> getAdUserCouponInfos(int userId);
	AdAppUserShoppingThrift getAdUserThirftInfos(int userId);
	AdGroupActivityConn findAdGroupActivityConnByActivityIdAndUserId(
			@Param("exclusiveCouponActivityId") String exclusiveCouponActivityId,
	        @Param("userId") String userId);
	List<ExclusiveCoupon> findExclusiveCoupon(
			@Param("exclusiveCouponActivityId") String exclusiveCouponActivityId,
	        @Param("exclusiveCouponCode") String exclusiveCouponCode);
	List<AdCouponCode> findUserReceivedExclusiveCoupon(Map selectConditionMap);
	UserGuideFinish findUserGuide(UserGuideFinish userGuideFinish);
	int insertUserGuideFinish(UserGuideFinish userGuideFinish);
	int updateUserGuideFinish(UserGuideFinish userGuideFinish);
	AdUserBusinessExpansion findUserGuideInfos(int userId);
	SplashScreenDataContract getSplashScreen(String groupId);
	AdCouponActivityInfos getAdCouponActivityInfos(String activityId);
}