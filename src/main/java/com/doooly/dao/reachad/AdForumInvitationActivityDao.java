package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdForumInvitationActivity;
import org.apache.ibatis.annotations.Param;

public interface AdForumInvitationActivityDao extends BaseDaoI<AdForumInvitationActivity> {

    /**
     * 通过手机号获得数据
     * @param phone
     * @return
     */
    Integer getByPhone(@Param("phone") String phone);

}
