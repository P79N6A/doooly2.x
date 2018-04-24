package com.doooly.dao.reachad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdCoupon;

public interface AdCouponDao extends BaseDaoI<AdCoupon> {

	List<AdCoupon> findHotCouponsByType(Integer type);

	void excuteSendGiftCouponProc(Map<String, Object> paraMap);

	String findBusinessIdByCouponId(Integer couponId);

	/** 查询第三方商户优惠券的商家集合 */
	List<AdBusiness> getThirdBusinessList();

	/** 查询总数(某商家旗下的优惠券数量) */
	int getCouponNumByBusinessId(@Param("businessId")String businessId);

	/** 根据商家id获取旗下的优惠券(分页) */
	List<AdCoupon> getCouponListByBusinessId(@Param("businessId") String businessId,
			@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

	void updateCouponCount(HashMap<String, Object> param);
}