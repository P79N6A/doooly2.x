package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.common.persistence.BaseDao;
import com.doooly.entity.reachad.AdBusinessLighten;
public interface AdLightenBusinessDao extends BaseDaoI<AdBusinessLighten>{

	void lightenBusiness(@Param("userId")String userId, @Param("businessIdAllList")List<String> businessIdAllList);

	List<AdBusinessLighten> getByUserId(@Param("userId")String userId);

	AdBusinessLighten lightenBusinessType(@Param("userId")String userId, @Param("businessId")String businessId);

	List<AdBusinessLighten> getByUserIdIsLingten(@Param("userId")String userId);

}
