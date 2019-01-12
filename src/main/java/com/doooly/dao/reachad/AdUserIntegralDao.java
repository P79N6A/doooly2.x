package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdUserIntegral;

public interface AdUserIntegralDao {

	AdUserIntegral getDirIntegralByUserId(@Param("userId") Long userId);
}
