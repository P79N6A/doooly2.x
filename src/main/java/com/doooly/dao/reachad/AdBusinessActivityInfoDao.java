package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdBusinessActivityInfo;

public interface AdBusinessActivityInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AdBusinessActivityInfo record);

    int insertSelective(AdBusinessActivityInfo record);

    AdBusinessActivityInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdBusinessActivityInfo record);

    int updateByPrimaryKey(AdBusinessActivityInfo record);
    
    
    AdBusinessActivityInfo getActivityRule(@Param("businessId") String businessId);
}