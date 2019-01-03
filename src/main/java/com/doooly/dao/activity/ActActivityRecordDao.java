package com.doooly.dao.activity;

import com.doooly.entity.activity.ActActivityRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 运营活动表(ActActivityRecord)表数据库访问层
 *
 * @author Mr_Wu
 * @since 2018-12-30 11:29:19
 */
public interface ActActivityRecordDao {

    /**
     * 通过活动唯一标识查询单条数据
     *
     * @param id
     * @return
     */
    ActActivityRecord queryById(String id);

    /**
     * 查询企业活动
     * @param id
     * @param groupId
     * @return
     */
    ActActivityRecord queryByActKeyAndGroup(@Param("id")String id, @Param("groupId") String groupId);
}