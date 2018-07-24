package com.doooly.business.redisUtil.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.redisUtil.RedisUtilService;
import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdProductCategoryDao;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdProductCategory;

/**
 * 缓存工具类
 * 
 * @author linking
 * @date 2017-2-20
 * @version 1.0
 */
@Service
public class RedisUtilServiceImpl implements RedisUtilService {

	/** 日志 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected StringRedisTemplate redisTemplate;

	/** 优惠码DAO */
	@Autowired
	protected AdCouponCodeDao adCouponCodeDao;

	/** 分类类别DAO */
	@Autowired
	protected AdProductCategoryDao adProductCategoryDao;

	/**
	 * 缓存商城一级分类
	 */
	@Override
	public void setMallFirstCategories(List<AdProductCategory> adProductCategories) {
		logger.info("=========setMallFirstCategories参数信息：" + adProductCategories);
		try {
			// json 转 str
			String firstCategoriesStr = JSONObject.toJSONString(adProductCategories);
			logger.info("========一级类别字符串-firstCategoriesStr：" + firstCategoriesStr);
			// 放入redis
			redisTemplate.opsForSet().add(RedisConstants.mall_all_first_category, firstCategoriesStr);
		} catch (Exception e) {
			logger.info("===============系统异常==============");
			e.printStackTrace();
		}
	}

	/**
	 * 读取商城一级分类
	 */
	@Override
	public List<AdProductCategory> getMallFirstCategories() {
		try {
			// 取出redis
			Set<String> firstCategoriesStr = redisTemplate.opsForSet().members(RedisConstants.mall_all_first_category);
			if (!firstCategoriesStr.isEmpty() && firstCategoriesStr.iterator().hasNext()) {
				List<AdProductCategory> adProductCategoryList = (List<AdProductCategory>) JSONObject
						.parseArray(firstCategoriesStr.iterator().next(), AdProductCategory.class);
				logger.info("firstCategoryList:" + JSONObject.toJSONString(adProductCategoryList.get(0)));
				return adProductCategoryList;
			} else {
				// ===========缓存中未获取到数据,重新读取数据库start==============
				List<AdProductCategory> firstCategoryList = adProductCategoryDao.findFirstCategory();
				if (firstCategoryList != null && firstCategoryList.size() > 0) {
					// 重新放入缓存
					setMallFirstCategories(firstCategoryList);
					return firstCategoryList;
				} else {
					logger.info("===========数据库中为读取到数据============");
				}
				// ===========缓存中未获取到数据,重新读取数据库end================
			}
		} catch (Exception e) {
			logger.info("===============系统异常==============");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 缓存商城二级分类
	 */
	@Override
	public void setMallSecondCategories(Integer firstCategoryId, List<AdProductCategory> adProductCategories) {
		logger.info("========setMallSecondCategories参数信息-firstCategoryId:" + firstCategoryId + ",adProductCategories:"
				+ adProductCategories);
		try {
			if (firstCategoryId >= 0 && adProductCategories != null && adProductCategories.size() > 0) {
				// json 转 str
				String secondCategoriesStr = JSONObject.toJSONString(adProductCategories);
				logger.info("========一级类别字符串-secondCategoriesStr：" + secondCategoriesStr);
				// 放入redis
				redisTemplate.opsForSet().add(RedisConstants.mall_first_category_ + firstCategoryId,
						secondCategoriesStr);
			}
		} catch (Exception e) {
			logger.info("===============系统异常==============");
			e.printStackTrace();
		}
	}

	/**
	 * 获取商城二级分类
	 */
	@Override
	public List<AdProductCategory> getMallSecondCategories(Integer firstCategoryId, Integer type) {
		try {
			logger.info("=========getMallSecondCategories参数信息：" + firstCategoryId);
			// 取出redis
			Set<String> secondCategoriesStr = redisTemplate.opsForSet()
					.members(RedisConstants.mall_first_category_ + firstCategoryId);
			if (!secondCategoriesStr.isEmpty() && secondCategoriesStr.iterator().hasNext()) {
				List<AdProductCategory> adProductCategoryList = (List<AdProductCategory>) JSONObject
						.parseArray(secondCategoriesStr.iterator().next(), AdProductCategory.class);
				logger.info("secondCategoryList:" + JSONObject.toJSONString(adProductCategoryList));
				return adProductCategoryList;
			} else {
				// =================缓存中未获取到数据,数据库查询start================
				List<AdProductCategory> secondCategoryList = adProductCategoryDao
						.findSecondCategory(Integer.valueOf(firstCategoryId), type);
				setMallSecondCategories(firstCategoryId, secondCategoryList);
				return secondCategoryList;
				// =================缓存中未获取到数据,数据库查询start================
			}
		} catch (Exception e) {
			logger.info("===============系统异常==============");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 商家优惠券所有券码放入缓存,统一处理
	 */
	@Override
	public boolean PushDataToRedis(String codeKey, List<String> codeList) {
		logger.info("==============PushCodeToRedis参数信息-codeKey:" + codeKey + ",codeList:" + codeList);
		try {
			if (codeKey != null && codeList != null) {
				// 放入缓存
				redisTemplate.opsForList().leftPushAll(codeKey, codeList);
				return true;
			} else {
				logger.info("===========参数值错误===========");
			}
		} catch (Exception e) {
			logger.info("===============系统异常==============");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取商家优惠券码缓存数据
	 */
	@Override
	public List<String> PopDataFromRedis(String codeKey, int count) {
		logger.info("=========getAdCouponCodeList参数信息-codeKey：" + codeKey);
		try {
			if (codeKey != null) {
				// redis获取code数据
				List<String> listPop = this.PopOpsList(codeKey, count);
				if (listPop != null && listPop.size() > 0) {
					logger.info("==========listPop:" + listPop);
					return listPop;
				} else {
					logger.info("=========未在redis中获取到兑换码集合-listPop:" + listPop);
					// List<String> codeListDB = this.getCodeFromDB(codeKey);
					/*
					 * if (codeListDB != null && codeListDB.size() > 0) { //
					 * 放入缓存 if (this.PushDataToRedis(codeKey, codeListDB)) { //
					 * 获取缓存数据 List<String> codeListRedis =
					 * this.PopOpsList(codeKey, count); return codeListRedis; }
					 * }
					 */
				}
			} else {
				logger.info("==========参数错误============");
			}
		} catch (Exception e) {
			logger.info("===============系统异常==============");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取商家优惠券码缓存数据
	 */
	@Override
	public Long totalDataOfRedis(String codeKey) {
		logger.info("=========totalDataOfRedis参数信息-codeKey：" + codeKey);
		try {
			if (codeKey != null) {
				// 获取缓存数据量
				Long totalCount = redisTemplate.opsForList().size(codeKey);
				return totalCount;
			} else {
				logger.info("==========参数错误============");
			}
		} catch (Exception e) {
			logger.info("===============系统异常==============");
			e.printStackTrace();
		}
		return 0L;
	}

	/**
	 * codeKey解析获取code集合数据
	 */
	public List<String> getCodeFromDB(String codeKey) {
		try {
			// 解析参数
			String businessId = codeKey.substring(0, codeKey.indexOf("+") - 1);
			Long couponId = null;
			// Long activityId = null;
			// 验证键规则
			if (codeKey.split("[+]").length - 1 == 1) {
				couponId = Long.valueOf(codeKey.substring(codeKey.indexOf("+") + 1, codeKey.length()));
			} else {
				couponId = Long.valueOf(codeKey.substring(codeKey.indexOf("+") + 1, codeKey.lastIndexOf("+")));
				// activityId =
				// Long.valueOf(codeKey.substring(codeKey.lastIndexOf("+") + 1,
				// codeKey.length()));
			}
			AdCouponCode adCouponCode = new AdCouponCode();
			adCouponCode.setBusinessid(businessId);
			adCouponCode.setCoupon(couponId);
			// adCouponCode.setActivityId(activityId);
			// 数据库获取code数据
			List<String> codeList = adCouponCodeDao.getCodeStrList(adCouponCode);
			if (codeList != null && codeList.size() > 0) {
				return codeList;
			}
		} catch (NumberFormatException e) {
			logger.info("=====(getCodeFromDB)系统错误========");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 批量抓取缓存兑换码
	 */
	@SuppressWarnings("unchecked")
	public List<String> PopOpsList(final String codeKey, int count) {
		try {
			// 获取缓存数据量
			Long totalCount = redisTemplate.opsForList().size(codeKey);
			/*
			 * if (totalCount == 0) { return null; }
			 */
			if (totalCount == 0) {
				logger.info("========缓存data数据量：" + totalCount);
				return null;
			}
			if (totalCount < count || count == -1) {
				count = Integer.valueOf(totalCount.toString());
			}
			// 获取数量
			final int PopCount = count;
			List<Object> listPop = redisTemplate.opsForList().getOperations()
					.executePipelined(new RedisCallback<String>() {
						@Override
						public String doInRedis(RedisConnection connection) {
							StringRedisConnection stringConn = (StringRedisConnection) connection;
							for (int index = 0; index < PopCount; index++) {
								stringConn.rPop(codeKey);
							}
							return null;
						}
					});
			logger.info("========获取数据数量-listPop：" + listPop.size());
			if (listPop.get(0) != null && listPop.get(0) != "null") {
				return (List<String>) JSONObject.parse(JSONObject.toJSONString(listPop));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteRedisData(String redisKey) {
		logger.info("=========redisKey:" + redisKey);
		try {
			redisTemplate.delete(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分发券码 微信端获取活动、优惠券下一个优惠码
	 */
	@Override
	public AdCouponCode getOneCodeFromRedis(String businessId, Integer couponId, Integer activityId) {
		return null;
	}

	/** 活动、优惠券管理,优惠券码缓存至对应活动 */
	@Override
	public void setActivityCodeList(AdCouponCode adCouponCode, int count) {
	}

	/** 活动、优惠券管理,优惠券码缓存至对应活动 */
	@Override
	public void redisDataSet(String codeKey, Set<String> set) {
		redisTemplate.opsForSet().members(codeKey).addAll(set);
		// redisTemplate
		logger.info("=====set:" + redisTemplate.opsForSet().size(codeKey));
	}

	/**
	 * 批量缓存数据
	 */
	public void setOpsList(String codeKey) {
		// 批量放入Redis list
		/*
		 * listOps.getOperations().executePipelined(new RedisCallback<T>() {
		 * 
		 * @Override public T doInRedis(RedisConnection connection) throws
		 * DataAccessException { StringRedisConnection strConnection =
		 * (StringRedisConnection)connection; JSONObject jsonUserData = null;
		 * for(ReUserContractDetail detail:contractDetailList){
		 * if(StringUtils.isBlank(detail.getDooolyCardNumber())||StringUtils.
		 * isBlank(detail.getDooolyMobile())) continue; jsonUserData = new
		 * JSONObject(); jsonUserData.put("cardNumber",
		 * detail.getDooolyCardNumber()); jsonUserData.put("telephone",
		 * detail.getDooolyMobile()); jsonUserData.put("type", 0);
		 * strConnection.lPush(key, jsonUserData.toJSONString()); } return null;
		 * } });
		 */
	}

	/*
	 * logger.info("=========兑换码参数-adCouponCode：" +
	 * JSONObject.toJSONString(adCouponCode) + ",放入数量-count:" + count); try { if
	 * (adCouponCode != null && adCouponCode.getBusinessid() != "" &&
	 * adCouponCode.getCoupon() >= 0 && adCouponCode.getActivityId() >= 0) { //
	 * 所有优惠券码对应key final String allCodeKey = adCouponCode.getBusinessid() + "_"
	 * + adCouponCode.getCoupon(); // 活动下优惠券对应key final String activityCodeKey =
	 * adCouponCode.getBusinessid() + "_" + adCouponCode.getCoupon() + "_" +
	 * adCouponCode.getActivityId(); // 取出redis Set<String> codeListStr =
	 * redisTemplate.opsForSet().members(allCodeKey); // redis获取活动、优惠券code if
	 * (codeListStr.size() > 0) { // 比较数量 if (codeListStr.size() < count) {
	 * count = codeListStr.size(); } List<Object> listPop =
	 * this.getOpsList(allCodeKey, count); if (listPop != null && listPop.size()
	 * > 0) { // 放入redis,缓存活动、优惠券码
	 * redisTemplate.opsForSet().add(activityCodeKey,
	 * JSONObject.toJSONString(listPop));
	 * logger.info("============(setActivityCodeList)缓存券码数据长度-listPop.size：" +
	 * listPop.size()); } else { logger.info(
	 * "=========(setActivityCodeList)redis中读取code,设置活动券码失败-listPop:" +
	 * listPop); } } else { // 查询数据库,重新缓存所有券码 AdCouponCode codeParam = new
	 * AdCouponCode(); codeParam.setBusinessid(adCouponCode.getBusinessid());
	 * codeParam.setCoupon(adCouponCode.getCoupon()); List<AdCouponCode>
	 * codeList = this.setAdCouponCodeList(codeParam); if (codeList != null &&
	 * codeList.size() > 0) { // 比较数量 if (codeList.size() < count) { count =
	 * codeList.size(); } List<Object> listPop = this.getOpsList(allCodeKey,
	 * count); if (listPop != null && listPop.size() > 0) { // 放入redis,缓存活动、优惠券码
	 * redisTemplate.opsForSet().add(activityCodeKey,
	 * JSONObject.toJSONString(listPop));
	 * logger.info("============(setActivityCodeList)缓存券码数据长度-listPop.size：" +
	 * listPop.size()); } else { logger.info(
	 * "=========(setActivityCodeList)redis中读取code,设置活动券码失败-listPop:" +
	 * listPop); } } else {
	 * logger.info("========(setActivityCodeList)数据库未获取到数据-codeList：" +
	 * codeList); }
	 * 
	 * logger.info("=========数据库获取所有券码-参数businessId：" +
	 * adCouponCode.getBusinessid() + ",couponId:" +
	 * adCouponCode.getBusinessid()); } } else {
	 * logger.info("===========(setActivityCodeList)参数错误============="); } }
	 * catch (Exception e) { logger.info("===============系统异常==============");
	 * e.printStackTrace(); }
	 */

	/*
	 * logger.info("=========getAdCouponCodeList参数信息-businessId:" + businessId +
	 * ",couponId:" + couponId + ",activityId:" + activityId); try { if
	 * (businessId != "" && couponId >= 0 && activityId >= 0) { // 活动优惠券码,缓存key
	 * final String codeKey = businessId + "_" + couponId + "_" + activityId; //
	 * 批量获取redis list信息 List<Object> listPop = this.getOpsList(codeKey, 1); //
	 * redis中可以获取到值 if (listPop != null && listPop.size() > 0) { AdCouponCode
	 * adCouponCode = (AdCouponCode) listPop.get(0); if (adCouponCode != null &&
	 * adCouponCode.getCode() != "") {
	 * logger.info("========取出code-adCouponCode：" +
	 * JSONObject.toJSONString(adCouponCode)); return adCouponCode; } else {
	 * logger.info("==========listPop.get(0)-adCouponCode:" + adCouponCode); } }
	 * else { logger.info("=========首次未在redis中获取到兑换码集合：" + listPop); // 商家编号
	 * AdCouponCode adCouponCode = new AdCouponCode();
	 * adCouponCode.setBusinessid(businessId);
	 * adCouponCode.setCoupon(Long.valueOf(couponId));
	 * adCouponCode.setActivityId(Long.valueOf(activityId)); // 查询数据库数据,并放入缓存
	 * List<AdCouponCode> adCouponCodeList = setAdCouponCodeList(adCouponCode);
	 * if (adCouponCodeList != null && adCouponCodeList.size() > 0) { //
	 * 批量获取redis list信息 listPop = this.getOpsList(codeKey, 1); // 获取code
	 * AdCouponCode adCouponCodeNew = (AdCouponCode) listPop.get(0);
	 * logger.info("========取出code-adCouponCodeNew：" +
	 * JSONObject.toJSONString(adCouponCodeNew)); return adCouponCodeNew; } else
	 * { logger.info("=========(getOneCodeFromRedis)数据库中未查询到券码=========="); } }
	 * } else { logger.info("==========参数错误==============="); } } catch
	 * (Exception e) { logger.info("===============系统异常==============");
	 * e.printStackTrace(); } return null;
	 */

	// 数据库查询优惠券码
	// List<AdCouponCode> adCouponCodeList =
	// adCouponCodeDao.getCodeByCoupon(adCouponCode);
	/*
	 * if (adCouponCodeList != null && adCouponCodeList.size() > 0) { //
	 * 优惠券码,json 转 str String codeListStr =
	 * JSONObject.toJSONString(adCouponCodeList);
	 * logger.info("========优惠券下优惠码(初始数据)-couponCodeStr：" + codeListStr); //
	 * 优惠券所有券码key数据 String codeKey = adCouponCode.getBusinessid() + "_" +
	 * adCouponCode.getCoupon(); // 放入活动券码key数据 if (adCouponCode.getActivityId()
	 * != null && adCouponCode.getActivityId() >= 0) { codeKey += "_" +
	 * adCouponCode.getActivityId(); } // 放入redis //
	 * redisTemplate.opsForSet().add(codeKey, codeListStr);
	 * //JSONArray.parseArray(codeListStr); redisTemplate.opsForList().
	 * redisTemplate.opsForList().leftPushAll(codeKey, aList); return
	 * adCouponCodeList; } else {
	 * logger.info("==========数据库无数据-adCouponCodeList：" + adCouponCodeList); }
	 */

	/*
	 * // 存储所有兑换码数据key String codeKey = adCouponCode.getBusinessid() + "_" +
	 * adCouponCode.getCoupon(); // activityId为空则 if
	 * (adCouponCode.getActivityId() != null && adCouponCode.getActivityId() >=
	 * 0) { codeKey += "_" + adCouponCode.getActivityId(); } // 取出redis
	 * Set<String> codeListStr = redisTemplate.opsForSet().members(codeKey); //
	 * 有数据,取缓存;无数据,取数据库 if (!codeListStr.isEmpty() &&
	 * codeListStr.iterator().hasNext()) { List<AdCouponCode> adCouponCodeList =
	 * (List<AdCouponCode>) JSONObject
	 * .parseArray(codeListStr.iterator().next(), AdCouponCode.class); return
	 * adCouponCodeList; } else { List<AdCouponCode> codeList =
	 * setAdCouponCodeList(adCouponCode); return codeList; }
	 */
}
