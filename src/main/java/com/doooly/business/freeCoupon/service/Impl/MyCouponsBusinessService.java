package com.doooly.business.freeCoupon.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doooly.business.freeCoupon.service.MyCouponsBusinessServiceI;
import com.doooly.dao.reachad.AdBusinessStoreDao;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.entity.reachad.AdBusinessStore;
import com.doooly.entity.reachad.AdCouponActivityConn;

import cn.jiguang.common.utils.StringUtils;

@Service
@Transactional
public class MyCouponsBusinessService implements MyCouponsBusinessServiceI {

	private static Logger logger = Logger.getLogger(MyCouponsBusinessService.class);

	/** 待使用 */
	private static final String UNUSE = "unuse";
	/** 已使用 */
	private static final String USED = "used";
	/** 已过期 */
	private static final String EXPIRED = "expired";
	/** 未领取 */
	private static final String UNCLAIMED = "unclaimed";

	@Autowired
	private AdCouponCodeDao adCouponCodeDao;

	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;

	@Autowired
	private AdBusinessStoreDao adBusinessStoreDao;

	@Override
	public HashMap<String, Object> getCouponListByType(String userId, String couponType,String couponCategory) {
		logger.info(String.format("“我的卡券”获取卡券列表 userId=%s,couponType=%s,couponCategory=%s",userId,couponType,couponCategory));
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdCouponActivityConn> actConnList = new ArrayList<AdCouponActivityConn>();
		try {
			switch (couponType) {
			case UNUSE:
				logger.info(userId + "||查询待使用卡券！");
				actConnList = adCouponActivityConnDao.findUnuseCoupons(userId,couponCategory);
				for (AdCouponActivityConn activityConn : actConnList) {
					String couponCode = activityConn.getCouponCode();
					if (StringUtils.isNotEmpty(couponCode) && "0".equals(activityConn.getIsView())) {
						adCouponCodeDao.updateCodeIsView(couponCode);
					}
				}
				break;
			case USED:
				logger.info(userId + "||查询已使用卡券！");
				actConnList = adCouponActivityConnDao.findUsedCoupons(userId,couponCategory);
				break;
			case EXPIRED:
				logger.info(userId + "||查询已过期卡券！");
				actConnList = adCouponActivityConnDao.findExpiredCoupons(userId,couponCategory);
				break;
			case UNCLAIMED:
				logger.info(userId + "||查询未领取卡券！");
				actConnList = adCouponActivityConnDao.findUnclaimedCoupons(userId);
				break;
			default:
				logger.info(userId + "||查询卡券type未知！");
				break;
			}
		} catch (Exception e) {
			logger.error(userId + "||查询卡券列表异常！！！e = {}",e);
		}
		map.put("actConnList", actConnList);
		return map;
	}

	@Override
	public HashMap<String, Object> getCouponDetail(String userId, String actConnId) {
		logger.info(String.format("“我的卡券-去使用”获取卡券详细信息   userId=%s , actConnId=%s", userId, actConnId));
		HashMap<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		try {
			// 根据会员id跟卡券活动关联id查询卡券详情
			AdCouponActivityConn actConn = adCouponActivityConnDao.getCouponDetail(userId, actConnId);

			// 获取卡券有效时间
			Date beginDate = actConn.getCoupon().getBeginDate();
			Date endDate = actConn.getCoupon().getEndDate();
			Long businessId = actConn.getAdBusiness().getId();
			// 查询商家关联的门店
			List<AdBusinessStore> adBusinessStoreList = adBusinessStoreDao
					.findList(Integer.valueOf(businessId.toString()));

			map.put("actConn", actConn);
			map.put("beginDate", format.format(beginDate));
			map.put("endDate", format.format(endDate));
			map.put("adBusinessStoreList", adBusinessStoreList);
		} catch (Exception e) {
			logger.error(userId + "||查询卡券详细信息异常！！！ e = {}",e);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getVoucherCouponNum(String userId, String amount) {
		logger.info(String.format("获取可用抵扣券数量   userId=%s , amount=%s", userId, amount));
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			//获取可用抵扣券数量(卡券金额要小于等于商品金额)
			Integer couponNum = adCouponActivityConnDao.getVoucherCouponNum(userId, amount);
			map.put("couponNum", couponNum);
		} catch (Exception e) {
			logger.error(userId + "||获取可用抵扣券数量异常！！！e = {}",e);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getRechargeCouponNum(String userId) {
		logger.info(String.format("获取可用话费充值抵扣券数量   userId=%s", userId));
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			//获取可用话费充值抵扣券数量
			Integer couponNum = adCouponActivityConnDao.getRechargeCouponNum(userId);
			map.put("couponNum", couponNum);
		} catch (Exception e) {
			logger.error(userId + "||获取可用话费充值抵扣券数量异常！！！e = {}",e);
		}
		return map;
	}

}
