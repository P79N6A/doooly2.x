package com.doooly.dao.reachad;

import java.util.List;
import java.util.Map;

/**
 * @Description: 报名记录
 * @author: qing.zhang
 * @date: 2017-04-26
 */
public interface AdJoinUserDao {

    List<Map> getAllAdJoinUser(Integer activityId);

    int getJoinUserNum(Integer activityId);

    Boolean findAdJoinUserByJoinUser(Integer joinUser);//根据用户id查询记录

    void batchInsert(List<Map> adJoinUsers);//报名

    List<Map> findAllJoinUser(Integer activityId);//查询所有参赛选手详情

    List<Map> findJoinUser(Integer activityId);//查询每个项目参赛人数

    void updateSupportCount(Integer joinRecordId);
}
