package com.doooly.dao.reachad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdCouponCode;

public interface AdCouponCodeDao extends BaseDaoI<AdCouponCode> {

	/**
	 * 根据条件查询用户活动已领取券码数
	 * 
	* @author  hutao 
	* @date 创建时间：2018年8月23日 下午4:42:46 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	int getUserCouponCountByIds(AdCouponCode coupon);
	/**
	 * @name B系统更新优惠码对应用户
	 * @param dataMap
	 *            userId(IN)-用户ID productSN(IN)-商品SN couponCodeId(OUT)-优惠码主键
	 */
	public void excuteSendGiftCouponProc(Map<String, Object> dataMap);

	// 根据活动id查询数据库该活动下的所有已分配卡券数量
	public int getSendCount(@Param("activityId") Integer activityId, @Param("couponId") Integer couponId);

	/**
	 * @param activity_id-活动ID
	 *            coupon_id-优惠券ID code-优惠码
	 */
	public List<AdCouponCode> getCodeByCoupon(AdCouponCode adCouponCode);

	/**
	 * @param businessid-商家编号
	 *            activity_id-活动ID coupon_id-优惠券ID 获取code字符串集合
	 */
	public List<String> getCodeStrList(AdCouponCode adCouponCode);

	/**
	 * @param activity_id-活动ID
	 *            coupon_id-优惠券ID code-优惠码 更新数据库兑换码
	 */
	public int updateCouponCode(@Param("adCouponCode")AdCouponCode adCouponCode,@Param("businessId")String businessId);
	
	/**
	 * @param activity_id-活动ID
	 *            coupon_id-优惠券ID couponId-优惠券ID
	 */
	public String checkIfSendCode(AdCouponCode adCouponCode);
	
	/**
	 * @param activity_id-活动ID
	 *            coupon_id-优惠券ID 不过滤话费抵扣卷类型
	 */
	public List<AdCouponCode> checkIfSendCodeNoPhone(AdCouponCode adCouponCode);
	
	/**
	 * 更新兑换码为被查看状态
	 * 
	 * @param code 兑换码code
	 *           
	 */
	int updateCodeIsView(String code);

	/**
	 * 查询兑换码
	 * @param userId
	 * @param activityId
	 * @param couponId
	 * @return
	 */
	public AdCouponCode getCouponCode(@Param("userId")Integer userId, @Param("activityId")Integer activityId, @Param("couponId")Integer couponId);

	/**
	 * 查询已经拥有该活动卡券的用户
	 * @param wangYiActivityId
	 * @param id
	 * @param userIds
	 * @return
	 */
	public List<Long> checkIsHadCode(@Param("activityId")String wangYiActivityId, @Param("couponId")Long id,
			@Param("userIds")List<String> userIds);

	/**
	 * 批量更新卡券至用户
	 * @param userCodeList
	 * @return
	 */
	public int batchUpdateCouponCode(@Param("list")List<HashMap<String, Object>> userCodeList,@Param("businessId")String businessId);


	AdCouponCode getSelfCoupon(@Param("userId")long userId, @Param("couponId")String couponCodeId);

	int unlockCoupon(@Param("userId")long userId, @Param("couponId")String couponCodeId);

	int lockCoupon(@Param("userId")long userId, @Param("couponId")String couponCodeId);
}