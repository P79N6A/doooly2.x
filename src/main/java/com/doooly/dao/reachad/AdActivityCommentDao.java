package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdActivityComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 活动评论
 * @author: qing.zhang
 * @date: 2017-04-25
 */
public interface AdActivityCommentDao extends BaseDaoI<AdActivityComment> {


    List<Map> getAllComment(@Param("activityId") Integer activityId, @Param("startIndex") int startIndex, @Param("pageSize") Integer pageSize);

    int getCount(Integer activityId);

    void updateSupportCount(Integer commentId);
}
