package com.doooly.dao.reachad;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

/**
 * 积分管理表DAO
 * 
 * @author yangwenwei
 * @date 2018年2月27日
 * @version 1.0
 */
public interface AdIntegralActivityDao {

	int updateIntegralGiveOut(@Param("integralId")Long integralId, @Param("subIntegral")BigDecimal subIntegral);
}