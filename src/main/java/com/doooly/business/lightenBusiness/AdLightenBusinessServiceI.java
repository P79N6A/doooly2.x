package com.doooly.business.lightenBusiness;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

public interface AdLightenBusinessServiceI {

	JSONObject getAllBusiness(JSONObject obj);

	JSONObject lightenBusiness(JSONObject obj);

	JSONObject reservationOrLightenBusiness(JSONObject obj);

	JSONObject getBusinessDeatil(JSONObject obj);
	
	Integer lightenBusinessType(String userId,String businessId);

	MessageDataBean lightSearch(String businessId, String userId);
}
