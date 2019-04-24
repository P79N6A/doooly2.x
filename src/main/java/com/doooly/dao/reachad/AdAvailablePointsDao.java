package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdAvailablePoints;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    /**
     * 获得用户当前月的到账积分
     * @param userid
     * @return
     */
    BigDecimal getUserMonthArrivalIntegral(@Param("userId") String userid);

    /**
     * 获得用户最大到账时间
     * type in (0 2 3 4 6 7 8 10 12)
     * @param userId
     * @return
     */
    Date maxArrivalAccountUpdateDate(@Param("userId") String userId);
}
