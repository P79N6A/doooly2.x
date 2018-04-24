package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdIntegralAcquireRecord;

/**
 * 用户领取积分活动明细表DAO
 * 
 * @author yangwenwei
 * @date 2018年2月27日
 * @version 1.0
 */
public interface AdIntegralAcquireRecordDao {

	AdIntegralAcquireRecord checkIsHadProvided(@Param("userId")Long userId);

	void insert(AdIntegralAcquireRecord adIntegralAcquireRecord);

}