package com.doooly.business.myaccount.service;

import com.doooly.dto.common.MessageDataBean;

public interface AdSystemNoticeServiceI {

    MessageDataBean getSystemNoitceList(String userId, String target, Integer currentPage, Integer pageSize, String token, String versionCode);

    MessageDataBean getSystemNoitceByTypeList(String userId, String target, Integer currentPage, Integer pageSize, String token, String versionCode, String type);

    MessageDataBean sendMessage(String userId, String target, String content);

    MessageDataBean getNoReadNum(String userId, String target);

    MessageDataBean getNoReadNumByType(String userId, String target, String type);

    MessageDataBean updateReadType(String userId, String target);

    MessageDataBean sendMessage(String content);

    MessageDataBean sendUrlMessage(String content, String url, String target);

    MessageDataBean getListByType(String userId, String target);

    MessageDataBean updateReadNoticeType(String userId, String target, String noticeType);
}
