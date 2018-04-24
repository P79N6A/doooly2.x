package com.doooly.business.redisUtil;

import java.util.List;
import java.util.Set;

import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdProductCategory;

/**
 * 缓存工具类
 * 
 * @author linking
 * @date 2017-2-20
 * @version 1.0
 */
public interface RedisUtilService {

	/**
	 * 缓存商城所有一级类别
	 */
	public void setMallFirstCategories(List<AdProductCategory> adProductCategories);

	/**
	 * 获取商城所有一级类别
	 */
	public List<AdProductCategory> getMallFirstCategories();

	/**
	 * @param firstCategoryId-一级类别ID
	 *            adProductCategories-一级类别下子类别 缓存商城二级类别
	 */
	public void setMallSecondCategories(Integer firstCategoryId, List<AdProductCategory> adProductCategories);

	/**
	 * @param firstCategoryId-一级类别ID
	 *            type-所属类别（热销,普通类别） 获取商城二级类别
	 */
	public List<AdProductCategory> getMallSecondCategories(Integer firstCategoryId, Integer type);

	/**
	 * 公用缓存兑换码集合 商家所有券码key规则：businessId_couponId
	 * 活动券码key规则：businessId_couponId_activityId
	 */
	public boolean PushDataToRedis(String codeKey, List<String> codeList);

	/**
	 * 公用缓存兑换码集合 商家所有券码key规则：businessId_couponId
	 * 活动券码key规则：businessId_couponId_activityId
	 */
	public List<String> PopDataFromRedis(String codeKey, int count);

	/**
	 * 公用缓存兑换码集合 商家所有券码key规则：businessId_couponId
	 * 活动券码key规则：businessId_couponId_activityId
	 * 获取codeKey数据总量
	 */
	public Long totalDataOfRedis(String codeKey);

	/**
	 * 后台管理-活动管理,缓存优惠券码至对应活动,key规则：businessId_couponId_activityId
	 */
	public void setActivityCodeList(AdCouponCode adCouponCode, int count);

	/**
	 * @param businessId-商家编号,
	 *            couponId-优惠券ID, activityId-活动ID 缓存兑换码集合
	 */
	public AdCouponCode getOneCodeFromRedis(String businessId, Integer couponId, Integer activityId);

	/**
	 * 清除所有缓存
	 * 
	 */
	public void deleteRedisData(String redisKey);

	/** set操作 */
	public void redisDataSet(String redisKey, Set<String> set);

}
