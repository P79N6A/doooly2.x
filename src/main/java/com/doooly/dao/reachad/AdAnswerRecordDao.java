package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdAnswerRecord;

/**
 * 答题记录DAO
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public interface AdAnswerRecordDao extends BaseDaoI<AdAnswerRecord> {

	/**
	 * 获取最近四天的答题情况
	 * 
	 * @param userId
	 *            会员id
	 * 
	 */
	List<AdAnswerRecord> findRecentlyAnswerRecords(String userId);

	/**
	 * 获取昨天答题正确的记录
	 * 
	 * 
	 */
	List<AdAnswerRecord> findCorrectRecordsOfYesterday();

	/**
	 * 更新答题记录的选项类型
	 * 
	 * 
	 */
	Integer updateOptionType(AdAnswerRecord record);
	
	/**
	 * 获取用户n天前答题记录
	 * 
	 * @param userId
	 * 			会员id
	 * @param intervalDays
	 * 			N天前的记录
	 */
	AdAnswerRecord getAnswerRecordByUserId(@Param(value = "userId") String userId,@Param(value = "intervalDays")int intervalDays);
	
	/**
	 * 根据userId获取五一答题活动记录
	 * 
	 */
	List<AdAnswerRecord> getAnswerRecordAgeByUserId(String userId);
	
	/**
	 * 根据userId获取五一答题活动记录（发过券的）
	 * 
	 */
	List<AdAnswerRecord> getAnswerCouponByUserId(String userId);
	
	/**
	 * 根据userId获取五一答题是否完成
	 * 
	 */
	List<AdAnswerRecord> getAnswerRecord(String userId);
	
	/**
	 * 删除答题记录
	 * @param userId
	 * 			会员id
	 */
	void deleteRecordByUserId(String userId);

}
