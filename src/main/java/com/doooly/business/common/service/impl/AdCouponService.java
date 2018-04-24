package com.doooly.business.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doooly.business.common.service.AdCouponServiceI;
import com.doooly.common.service.impl.BaseServiceImpl;
import com.doooly.dao.reachad.AdCouponDao;
import com.doooly.entity.reachad.AdCoupon;

/**
 * 
 * @author lxl
 */
@Service
public class AdCouponService implements AdCouponServiceI {

	/** 兑换券DAO */
	@Autowired
	private AdCouponDao adCouponDao;

}
