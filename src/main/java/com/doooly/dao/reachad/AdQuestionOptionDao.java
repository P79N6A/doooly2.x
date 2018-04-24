package com.doooly.dao.reachad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdQuestionOption;

/**
 * 选项DAO
 * 
 * @author yuelou.zhang
 * @version 2017年2月10日
 */
public interface AdQuestionOptionDao extends BaseDaoI<AdQuestionOption> {

	/**
	 * 根据今天的答题记录查找题目及其选项
	 * 
	 * @param userId
	 *            会员id
	 * @param date
	 *            答题记录日期
	 * 
	 */
	List<AdQuestionOption> getTopicAndOptionsByRecord(@Param(value = "userId") String userId,
			@Param(value = "date") Date date);

	/**
	 * 随机获取题目及其选项
	 * 
	 * 
	 */
	List<AdQuestionOption> getTopicAndOptionsRandom();

	/**
	 * 获取第一题的题目及其选项
	 * 
	 * @param questionOrder
	 *            题目序号(1)
	 * 
	 */
	List<AdQuestionOption> getTopicAndOptionsFirst(Map<String,Object> map);
	
	/**
	 * 获取下一题的题目及其选项
	 * 
	 * @param questionOrder
	 *            题目序号(2,3....8)
	 * @param optionType
	 *            选项类型(A/B/C)
	 * 
	 */
	List<AdQuestionOption> getTopicAndOptionsNext(Map<String,Object> map);
	
	/**
	 * 获取答题信息
	 * 
	 * @param questionOrder
	 *            题目序号(2,3....8)
	 * @param optionType
	 *            选项类型(A/B/C)
	 * 
	 */
	AdQuestionOption getRecordByOrderAndType(Map<String,Object> map);

}
