package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBasicType;
import com.reach.redis.annotation.Cacheable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 活动类目，类型
 * @author: zhaoyipeng
 * 
 */
public interface AdBasicTypeDao extends BaseDaoI<AdBasicType> {

	List<AdBasicType> getActivityCategoryList();

	@Cacheable(module = "TEMPLATE", event = "getFloors", key = "userId, type, templateType")
	List<AdBasicType> getFloors(@Param("userId") String userId, @Param("type") Integer type, @Param("templateType")Integer templateType);

}
