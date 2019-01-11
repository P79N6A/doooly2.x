package com.doooly.dao.reachad;



import com.doooly.entity.reachad.UserIntegral;

import java.math.BigDecimal;

public interface UserIntegralMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserIntegral record);

    int insertSelective(UserIntegral record);

    UserIntegral selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserIntegral record);

    int updateByPrimaryKey(UserIntegral record);

    BigDecimal getAvailIntegal(UserIntegral userIntegral);
}