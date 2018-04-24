package com.doooly.business.freeCoupon.service.Impl;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doooly.business.freeCoupon.service.HomeCouponServiceI;
import com.doooly.business.utils.Pagelab;
import com.doooly.dao.reachad.AdCouponDao;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdCoupon;

/**
 * APP首页优惠券 业务Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年8月11日
 */
@Service
@Transactional
public class HomeCouponService implements HomeCouponServiceI {

	private static Logger logger = Logger.getLogger(MyCouponsBusinessService.class);

	@Autowired
	private AdCouponDao adCouponDao;

	@Override
	public HashMap<String, Object> getBusinessList() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询第三方商户优惠券的商家集合
			List<AdBusiness> businessList = adCouponDao.getThirdBusinessList();
			map.put("businessList", businessList);
		} catch (Exception e) {
			logger.error("查询第三方商户优惠券的商家集合异常！！！", e);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getCouponListByBusinessId(String businessId, int currentPage, int pageSize) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询总数(某商家旗下的优惠券数量)
			int totalNum = adCouponDao.getCouponNumByBusinessId(businessId);
			if (totalNum > 0) {
				Pagelab pagelab = new Pagelab(currentPage, pageSize);
				pagelab.setTotalNum(totalNum);
				// 根据商家id获取旗下的优惠券
				List<AdCoupon> couponList = adCouponDao.getCouponListByBusinessId(businessId, pagelab.getStartIndex(),
						pagelab.getPageSize());
				map.put("couponList", couponList);
				map.put("countPage", pagelab.getCountPage());// 总页码
			}
		} catch (Exception e) {
			logger.error("根据商家id获取旗下的优惠券异常！！！", e);
		}
		return map;
	}

}
