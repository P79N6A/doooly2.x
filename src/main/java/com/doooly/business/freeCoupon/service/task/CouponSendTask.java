package com.doooly.business.freeCoupon.service.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdGiveCouponErrorDao;
import com.doooly.dao.reachad.AdRegisterRecordDao;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdGiveCouponError;
import com.doooly.entity.reachad.AdRegisterRecord;

@Service
@Lazy(false)
public class CouponSendTask {

	private static Logger logger = Logger.getLogger(CouponSendTask.class);

	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;
	@Autowired
	private AdRegisterRecordDao adRegisterRecordDao;
	@Autowired
	private AdGiveCouponErrorDao adGiveCouponErrorDao;
	@Autowired
	private AdCouponActivityDao adCouponActivityDao;
	@Autowired
	private AdCouponCodeDao adCouponCodeDao;
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;

	// 定时给报名活动的参与者发券
	// @Scheduled(cron = "0 0/1 * * * ?")
	public void luckySendCoupon() {
		try {
			logger.info("-------------发放报名活动券码给用户定时任务begin-------------");
			// 获取已经结束并且应该发放卡券的活动及卡券信息
			List<AdCouponActivityConn> activities = adCouponActivityConnDao.getShouldCouponAndActivity(new Date());
			if (!activities.isEmpty()) {
				for (AdCouponActivityConn adCouponActivityConn : activities) {
					// 获取该活动下该卡券的报名人数
					List<AdRegisterRecord> records = adRegisterRecordDao.getRecordByCouponAndActivity(
							adCouponActivityConn.getCouponId(), adCouponActivityConn.getActivityId());
					if (!records.isEmpty()) {
						if (records.size() <= adCouponActivityConn.getCouponRemindQuantity()) {
							// 报名人数小于等于活动剩余数量,报名者全部获得卡券
							for (AdRegisterRecord adRegisterRecord : records) {
								AdCouponCode adCouponCode = freeCouponBusinessServiceI.sendCoupon(
										adRegisterRecord.getBusinessId(), adRegisterRecord.getActivityId(),
										adRegisterRecord.getUserId(), adCouponActivityConn.getCouponId());
								if (!"".equals(adCouponCode.getCode())) {
									logger.info(adRegisterRecord.getUserId() + "用户无法获取卡券,商品编号为:"
											+ adCouponActivityConn.getProductSn() + "活动id:"
											+ adCouponActivityConn.getActivityId() + "卡券id:"
											+ adCouponActivityConn.getCouponId());
									// 后续操作 添加临时表
									addError(adCouponActivityConn, adRegisterRecord);
								}
							}
						} else {
							// 报名人数大于活动剩余数量,报名者抽出剩余卡券数量获取卡券
							List<AdRegisterRecord> luckyUser = this.getLuckyUser(records.size(),
									adCouponActivityConn.getCouponRemindQuantity(), records);
							for (AdRegisterRecord adRegisterRecord : luckyUser) {
								AdCouponCode adCouponCode = freeCouponBusinessServiceI.sendCoupon(
										adRegisterRecord.getBusinessId(), adRegisterRecord.getActivityId(),
										adRegisterRecord.getUserId(), adCouponActivityConn.getCouponId());
								if (!"".equals(adCouponCode.getCode())) {
									logger.info(adRegisterRecord.getUserId() + "用户无法获取卡券,商品编号为:"
											+ adCouponActivityConn.getProductSn() + "活动id:"
											+ adCouponActivityConn.getActivityId() + "卡券id:"
											+ adCouponActivityConn.getCouponId());
									// 后续操作 添加临时表
									addError(adCouponActivityConn, adRegisterRecord);
								}
							}
						}
						// 根据活动id查询数据库该活动下的所有已分配卡券数量,来更改库存剩余和已使用的数量
						Integer count = adCouponCodeDao.getSendCount(adCouponActivityConn.getActivityId(),
								adCouponActivityConn.getCouponId());
						adCouponActivityConn
								.setCouponRemindQuantity(adCouponActivityConn.getCouponRemindQuantity() - count);
						adCouponActivityConn
								.setCouponUsedQuantity(adCouponActivityConn.getCouponUsedQuantity() + count);
						adCouponActivityConnDao.updateUsedQuantity(adCouponActivityConn);
					} else {
						logger.info("-------------该报名活动无人报名-------------");
						// 关闭活动
						adCouponActivityDao.closeActivity(adCouponActivityConn.getActivityId());
					}
				}
				// 调取方法判断是否活动下所有券码发放完成,然后关闭活动
				closeTheShouldClosedActivity(activities);
			} else {
				logger.info("-------------没有需要发放的卡券活动-------------");
			}
			logger.info("-------------发放报名活动券码给用户定时任务定时任务end-------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 调取方法判断是否活动下所有券码发放完成,然后关闭活动
	private void closeTheShouldClosedActivity(List<AdCouponActivityConn> activities) {
		Set<Integer> activityIdSet = new HashSet<Integer>();
		for (AdCouponActivityConn adCouponActivityConn2 : activities) {
			activityIdSet.add(adCouponActivityConn2.getActivityId());
		}
		for (Integer activityId : activityIdSet) {
			Integer count = adCouponActivityConnDao.getCountByActivityIdImmediate(activityId, new Date());
			if (count == 0) {
				adCouponActivityDao.closeActivity(activityId);
			}
		}
	}

	// 后续操作 添加临时表 添加发券失败的用户信息
	private void addError(AdCouponActivityConn adCouponActivityConn, AdRegisterRecord adRegisterRecord) {
		AdGiveCouponError e = new AdGiveCouponError();
		e.setUserId(adRegisterRecord.getUserId());
		e.setActivityId(adCouponActivityConn.getActivityId());
		e.setCouponId(adCouponActivityConn.getCouponId());
		e.setCreateDate(new Date());
		adGiveCouponErrorDao.insert(e);
	}

	// 抽取幸运获奖者
	private List<AdRegisterRecord> getLuckyUser(Integer max, Integer luckyNum, List<AdRegisterRecord> records) {
		List<AdRegisterRecord> luckyRecords = new ArrayList<AdRegisterRecord>();
		int a = 1;
		for (int i = 0; i < luckyNum; i++) {
			int index = (int) ((Math.random() * (max - a)));
			luckyRecords.add(records.get(index));
			records.remove(index);
		}
		if (luckyRecords.size() < luckyNum) {
			getLuckyUser(max, luckyNum, records);
		}
		return luckyRecords;
	}
}
