package com.doooly.dao.activity;

import com.doooly.entity.activity.ActActivityCodeRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 活动码记录明细表(ActActivityCodeRecord)表数据库访问层
 *
 * @author Mr_Wu
 * @since 2018-12-30 11:29:12
 */
public interface ActActivityCodeRecordDao {

    /**
     * 批量插入code
     *
     * @param actActivityCodeRecord 实例对象
     * @return 影响行数
     */
    int insertBatch(ActActivityCodeRecord actActivityCodeRecord);

    /**
     * 查询用户是否已领取券码
     * @param
     * @return 优惠码
     */
    ActActivityCodeRecord findByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);
}