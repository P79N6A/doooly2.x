package com.doooly.business.common.service;

import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachlife.LifeMember;
import com.doooly.entity.reachlife.LifeProduct;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lxl
 * @name 兑换券Service
 */
public interface AdCouponCodeServiceI {

	/**
	 * 发放优惠码
	 * 
	 * @param lifeProduct-商品类
	 * @param lifeMember-会员信息
	 * @return code-返回码(1000-成功,1001-失败)
	 * @return msg-返回信息
	 * @return couponCode-优惠码
	 */
	public HashMap<String, Object> giveCouponCode(LifeProduct lifeProduct, LifeMember lifeMember) throws Exception;

    AdCouponCode getSelfCouponByMap(Map<String, Object> paramMap);
}
