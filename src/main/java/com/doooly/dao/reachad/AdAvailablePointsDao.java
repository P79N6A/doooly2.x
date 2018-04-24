package com.doooly.dao.reachad;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdAvailablePoints;

/**
 * @Description: 可用积分
 * @author: qing.zhang
 * @date: 2017-05-19
 */
public interface AdAvailablePointsDao {

    int getTotalNum(@Param("income") String income, @Param("userId") String userId);

    List<AdAvailablePoints> getAdAvailablePoints(@Param("income") String income, @Param("userId") String userId, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    AdAvailablePoints getAvailablePointDetail(String availablePointsId);
    
    /** 根据userId获取用户消费的积分总和 */
    BigDecimal getConsumerPoints(String userId);

	void insert(AdAvailablePoints adAvailablePoints);
}
