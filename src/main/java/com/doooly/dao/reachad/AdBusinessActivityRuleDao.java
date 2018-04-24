package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdBusinessActivityRule;

public interface AdBusinessActivityRuleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AdBusinessActivityRule record);

    int insertSelective(AdBusinessActivityRule record);

    AdBusinessActivityRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdBusinessActivityRule record);

    int updateByPrimaryKey(AdBusinessActivityRule record);
    
    List<AdBusinessActivityRule> getActivityRule(@Param("businessId") String businessId);
}