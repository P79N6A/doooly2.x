package com.doooly.business.forum.invitation;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * 论坛邀请service
 * @Author: Mr.Wu
 * @Date: 2018/11/20
 */
public interface AdForumInvitationServiceI {

    /**
     * 注册报名
     * @param jsonReq
     * @return
     */
    public MessageDataBean registerForum(JSONObject jsonReq);

}
