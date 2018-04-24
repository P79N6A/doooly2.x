package com.doooly.dao.reachad;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdAwardsRecord;

public interface AdAwardsRecordDao extends BaseDaoI<AdAwardsRecord> {

	AdAwardsRecord getByUserId(Integer userId);

	Integer getGroupCountByGroupId(@Param("groupNum") Long groupNum,@Param("weekNum")Integer weekNum);

	List<AdAwardsRecord> findList();
	
}
