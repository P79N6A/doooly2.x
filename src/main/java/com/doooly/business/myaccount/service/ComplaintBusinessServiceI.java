package com.doooly.business.myaccount.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

public interface ComplaintBusinessServiceI {

	void complaintSave(HttpServletRequest request);

	void complaintSaveForAppTwo(HttpServletRequest request, JSONObject json);

}
