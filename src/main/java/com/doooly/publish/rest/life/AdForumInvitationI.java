package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 论坛邀请
 */
public interface AdForumInvitationI {

    /**
     * 注册报名
     * @param paramJSON
     */
    public String registerForum(JSONObject paramJSON);

    /**
     * 批量发送短信
     * @return
     */
    public String batchSendMsg();
}
