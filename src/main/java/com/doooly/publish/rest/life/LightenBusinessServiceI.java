package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 */
public interface LightenBusinessServiceI {

	public String lightenBusiness(JSONObject obj);
	
	public String getAllBusiness(JSONObject obj);
	
	public String reservationOrLightenBusiness(JSONObject obj);
	
	public String getBusinessDeatil(JSONObject obj);

	public String lightSearch(JSONObject obj);

	public String getLightType(JSONObject obj);
}
