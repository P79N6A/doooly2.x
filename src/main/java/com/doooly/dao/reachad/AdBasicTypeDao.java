package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBasicType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 活动类目，类型
 * @author: zhaoyipeng
 * 
 */
@Repository
public interface AdBasicTypeDao extends BaseDaoI<AdBasicType> {

	List<AdBasicType> getActivityCategoryList();

//	@Cacheable(module = "TEMPLATE", event = "getFloors", key = "userId, type, templateType")
	List<AdBasicType> getFloors(@Param("userId") String userId, @Param("type") Integer type, @Param("templateType")Integer templateType);

}
