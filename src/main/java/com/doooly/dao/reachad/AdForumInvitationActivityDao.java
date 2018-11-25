package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.forum.invitation.AdForumInvitationActivity;
import org.apache.ibatis.annotations.Param;

public interface AdForumInvitationActivityDao extends BaseDaoI<AdForumInvitationActivity> {

    String getByPhone(@Param("phone") String phone);

}
