package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdRechargeRecord;

/**
 * 话费充值记录DAO
 * 
 * @author yangwenwei
 * @date 2018年4月16日
 * @version 1.0
 */
public interface AdRechargeRecordDao extends BaseDaoI<AdRechargeRecord>{

	AdRechargeRecord getLastRecord(String userId);
	AdRechargeRecord getRecordByOrderNumber(String orderNumber);
	/*
	 * 当type为0的时候,修改record记录为已完成,
	 * 为1的时候,修改record记录为逻辑删除
	*/
	int updateStateOrDelFlag(@Param("orderNumber")String orderNumber , @Param("type")int type);
}