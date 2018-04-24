package com.doooly.business.common.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.AdCouponActivityServiceI;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.entity.reachad.AdCouponActivity;
@Service
public class AdCouponActivityService implements AdCouponActivityServiceI {
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;

	/**
	 * 通过活动ID查询活动信息
	 */
	@Override
	public AdCouponActivity getActivityInfo(String familyActivityId) {
		return adCouponActivityDao.get(familyActivityId);
	}

	public AdCouponActivity getActivityIdByIdFlag(String idFlag) {
		return adCouponActivityDao.getActivityIdByIdFlag(idFlag);
	}

}