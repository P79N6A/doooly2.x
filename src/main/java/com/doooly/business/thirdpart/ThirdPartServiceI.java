package com.doooly.business.thirdpart;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 第三方接口
 * @author: qing.zhang
 * @date: 2019-01-15
 */
public interface ThirdPartServiceI {

    MessageDataBean getGroupInfo(JSONObject json, HttpServletRequest request);

    MessageDataBean thirdLogin(JSONObject json, HttpServletRequest request);
}
