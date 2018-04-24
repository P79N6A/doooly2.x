package com.doooly.business.myaccount.service;

import com.doooly.dto.common.MessageDataBean;

public interface AdSystemNoticeServiceI {

    MessageDataBean getSystemNoitceList(String userId, String target, Integer currentPage, Integer pageSize, String token, String versionCode);

    MessageDataBean sendMessage(String userId, String target, String content);

    MessageDataBean getNoReadNum(String userId, String target);

    MessageDataBean updateReadType(String userId, String target);

    MessageDataBean sendMessage(String content);
}
