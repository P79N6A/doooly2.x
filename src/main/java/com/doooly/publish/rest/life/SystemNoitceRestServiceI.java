package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public interface SystemNoitceRestServiceI {
    // 查询消息中心详情
    String getSystemNoitceList(JSONObject json,@Context HttpServletRequest request);

    String sendMessage(JSONObject json);//发送消息接口

    String getNoReadNum(JSONObject json);//获取未读取消息数量

    String updateReadType(JSONObject json);//修改消息读取状态

    String sendMessageToAll(JSONObject json);//发送消息给所有人
}
