package com.doooly.publish.rest.life;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.alibaba.fastjson.JSONObject;

public interface ComplaintServiceI {
	
	String complaintSave(@Context HttpServletRequest request);
	String complaintSaveForAppTwo(@Context HttpServletRequest request);
}
