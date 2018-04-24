package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdInviteActivity;

/**
 * @Description: 报名活动dao
 * @author: qing.zhang
 * @date: 2017-04-25
 */
public interface AdInviteActivityDao extends BaseDaoI<AdInviteActivity> {

    AdInviteActivity getActivityDetail(Integer activityId);

    void updateSupportCount(Integer activityId);

    void updateBrowserCount(Integer activityId);
}
