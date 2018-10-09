package com.doooly.business.activity.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.AbstractActivityService;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdGroupActivityConnDao;

/**
 * 
    * @ClassName: XingFuJiaoHangActivityService  
    * @Description: 幸福交行活动  
    * @author hutao  
    * @date 2018年10月9日  
    *
 */
@Service
public class DailyCommonActivityService extends AbstractActivityService {
	@Autowired
	private static FreeCouponBusinessServiceI freeCouponBusinessServiceI;
	@Autowired
	private static AdCouponActivityConnDao couponActivityConnDao;
	@Autowired
	private static AdGroupActivityConnDao groupActivityConnDao;
	@Autowired
	private static AdCouponActivityDao couponActivityDao;
	@Autowired
	private static AdCouponCodeDao couponCodeDao;

	
	public DailyCommonActivityService() {
		super(freeCouponBusinessServiceI, couponActivityConnDao, groupActivityConnDao, couponActivityDao, couponCodeDao);
	}

}
