package com.doooly.common.constants;

/**
 * @author linking
 * @redis 缓存命名常量类
 */
public class RedisConstants {
	/**
	 * 商城所有一级类别
	 */
	public static String mall_all_first_category = "mall_all_first_category";
	/**
	 * 商城一级类别前缀
	 */
	public static String mall_first_category_ = "mall_first_category_";

	/**
	 * 缓存优惠券key规则-商家ID_优惠券ID
	 */
	public static final String businessId_couponId = "";

	/**
	 * 缓存优惠券key规则-商家ID_优惠券ID_活动ID
	 */
	public static final String businessId_couponId_activityId = "";

	/**
	 * Redis 缓存过期时间 单位（秒）
	 */
	public static final int REDIS_CACHE_EXPIRATION_DATE = 86400;

}