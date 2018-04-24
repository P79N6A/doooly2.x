package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdVoteOption;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 投票选项
 * @author: qing.zhang
 * @date: 2017-05-31
 */
public interface AdVoteOptionDao extends BaseDaoI<AdVoteOption> {

    List<AdVoteOption> findList(@Param("orderType") Integer orderType);//查询所有投票选项 orderType 排序方式 首页0 列表1

    void updateVoteCount(Integer optionId);

	List<AdVoteOption> findListByActivityId(@Param("orderType") Integer orderType,@Param("activityId") Integer activityId);
}
