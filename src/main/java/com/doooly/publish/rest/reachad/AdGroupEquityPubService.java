package com.doooly.publish.rest.reachad;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author sfc
 * @date 2019.3.18
 */
public interface AdGroupEquityPubService {
	//获取企业权益列表
	String getGroupEquityList(HttpServletRequest request, HttpServletResponse response);
	//根据ID获取权益信息
	String getEquityByEquityId(JSONObject paramJSON);

}
