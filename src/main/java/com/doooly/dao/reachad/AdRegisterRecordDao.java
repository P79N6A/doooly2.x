package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdRegisterRecord;

public interface AdRegisterRecordDao extends BaseDaoI<AdRegisterRecord> {
	// 获取该活动下该卡券的报名人数
	List<AdRegisterRecord> getRecordByCouponAndActivity(@Param("couponId") Integer couponId,
			@Param("activityId") Integer activityId);

	/**
	 * 获取某卡券活动关联 总报名人数
	 * 
	 * @param adRegisterRecord
	 *            包含couponId跟卡券活动id
	 * 
	 */
	Integer getTotalRegisterNum(AdRegisterRecord adRegisterRecord);
}
