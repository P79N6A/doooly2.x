package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBasicType;
import com.doooly.entity.reachad.AdVoteRecord;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 活动类目，类型
 * @author: zhaoyipeng
 * 
 */
public interface AdBasicTypeDao extends BaseDaoI<AdBasicType> {

	List<AdBasicType> getActivityCategoryList();
	
	
 }
