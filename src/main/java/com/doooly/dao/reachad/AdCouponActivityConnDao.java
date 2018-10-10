package com.doooly.dao.reachad;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdCouponActivityConn;

public interface AdCouponActivityConnDao extends BaseDaoI<AdCouponActivityConn> {
	// 获取对应一级标签下的对应卡券
	List<AdCouponActivityConn> findCouponByCategory(@Param("catagoryId") Integer catagoryId,
			@Param("type") Integer type, @Param("adId") Integer adId, @Param("date") Date date);

	// 修改关联表中库存,直接领取时点击之后库存减一
	void reduceRemindQuantity(@Param("couponId") Integer couponId, @Param("activityId") Integer activityId);

	// 获取已经结束并且应该发放卡券的活动及卡券信息
	List<AdCouponActivityConn> getShouldCouponAndActivity(@Param("date") Date date);

	// 报名活动修改关联表库存
	void updateUsedQuantity(AdCouponActivityConn adCouponActivityConn);

	// 查询该活动下的卡券发放时间未到期的数量
	Integer getCountByActivityIdImmediate(@Param("activityId") Integer activityId, @Param("date") Date date);

	/**
	 * 获取有效的卡券活动关联
	 * 
	 * @param userId
	 *            会员id
	 */
	List<AdCouponActivityConn> findAvailableCoupons(String userId);

	/**
	 * 根据卡券活动关联id查询卡券详情
	 * 
	 * @param actConnId
	 * 			会员卡券关联id
	 * @param userId
	 * 			会员id
	 */
	AdCouponActivityConn getCouponDetailsByConnId(@Param("actConnId")String actConnId,@Param("userId")String userId);
	
	/**
	 * 获取待使用卡券列表
	 * 
	 * @param userId
	 *            会员id
	 * @param couponCategory
	 *            卡券类型
	 */
	List<AdCouponActivityConn> findUnuseCoupons(@Param("userId")String userId,@Param("couponCategory")String couponCategory);
	
	/**
	 * 获取已使用卡券列表
	 * 
	 * @param userId
	 *            会员id
	 * @param couponCategory
	 *            卡券类型
	 */
	List<AdCouponActivityConn> findUsedCoupons(@Param("userId")String userId,@Param("couponCategory")String couponCategory);
	
	/**
	 * 获取已过期卡券列表
	 * 
	 * @param userId
	 *            会员id
	 * @param couponCategory
	 *            卡券类型
	 */
	List<AdCouponActivityConn> findExpiredCoupons(@Param("userId")String userId,@Param("couponCategory")String couponCategory);
	
	/**
	 * 获取未领取卡券列表
	 * 
	 * @param userId
	 *            会员id
	 */
	List<AdCouponActivityConn> findUnclaimedCoupons(String userId);
	
	/**
	 * 根据会员id跟卡券活动关联id查询卡券详情
	 * 
	 * @param userId
	 *            会员id
	 * @param actConnId
	 *            卡券活动关联id
	 */
	AdCouponActivityConn getCouponDetail(@Param("userId")String userId,@Param("actConnId")String actConnId);

	AdCouponActivityConn getByActivityIdAndLevel(@Param("activityId")Integer activityId,@Param("level") int level);
	
	//根据活动id获取活动卡券关联
	AdCouponActivityConn getByActivityId(String activityId);
	AdCouponActivityConn getByActivityId(@Param("activityId")Integer activityId, @Param("couponId") Long couponId);

	List<AdCouponActivityConn> getActivityConnByActivityId(String activityId);

	AdCouponActivityConn getByActivityIdAndCouponId(@Param("activityId")String activityId, @Param("couponId")String couponId);

	void BatchreduceRemindQuantity(@Param("couponId")Integer couponId, @Param("activityId")Integer activityId,
			@Param("count")int count);

	List<AdCouponActivityConn> getNowActivityCoupon();

	void updateCouponActivityConnCount(@Param("businessId")String businessId, @Param("activityId")Integer activityId,
			@Param("couponId")Integer couponId, @Param("count")int size);

	Integer getVoucherCouponNum(@Param("userId")String userId,@Param("amount")String amount);
	
	Integer getRechargeCouponNum(String userId);
	
}
