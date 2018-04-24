package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

public interface ScanDiscountRestServiceI {

	public String getBusinessList(JSONObject data);
	
	public String getScanDiscount(JSONObject data);
}
