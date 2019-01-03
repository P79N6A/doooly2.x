package com.doooly.dao.activity;

import com.doooly.entity.activity.ActLotteryResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 抽奖结果明细记录表(ActLotteryResult)表数据库访问层
 *
 * @author Mr_Wu
 * @since 2018-12-30 11:29:20
 */
public interface ActLotteryResultDao {

    /**
     * 查询活动的中奖纪录
     * @param activityId：活动id
     * @param rowNum：查询行数
     * @return
     */
    List<ActLotteryResult> getListByActivityIdAndLimit(@Param("activityId") String activityId, @Param("rowNum") int rowNum);

}