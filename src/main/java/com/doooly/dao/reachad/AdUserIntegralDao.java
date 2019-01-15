package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdUserIntegral;

import java.util.Map;

public interface AdUserIntegralDao {

	AdUserIntegral getDirIntegralByUserId(@Param("userId") Long userId);

    AdUserIntegral getDirIntegral(@Param("paramMap") Map<String, Object> paramMap);
}
