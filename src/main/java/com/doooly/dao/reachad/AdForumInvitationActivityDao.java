package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdForumInvitationActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdForumInvitationActivityDao extends BaseDaoI<AdForumInvitationActivity> {

    /**
     * 通过手机号获得数据
     * @param phone
     * @return
     */
    Integer getByPhone(@Param("phone") String phone);

    /**
     * 获得所有未发送短信的记录
     * @return
     */
    List<AdForumInvitationActivity> getAllBySendState();

    /**
     * 更新短信发送状态为已发送
     */
    void updateMsgState(@Param("id") Integer id);

}
