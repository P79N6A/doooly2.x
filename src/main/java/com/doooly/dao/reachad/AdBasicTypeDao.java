package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBasicType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 活动类目，类型
 * @author: zhaoyipeng
 * 
 */
public interface AdBasicTypeDao extends BaseDaoI<AdBasicType> {

	List<AdBasicType> getActivityCategoryList();

	List<AdBasicType> getFloors(@Param("userId") String userId, @Param("type") Integer type);

}
