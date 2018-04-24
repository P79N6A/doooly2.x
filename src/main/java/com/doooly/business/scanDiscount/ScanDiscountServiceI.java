package com.doooly.business.scanDiscount;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

public interface ScanDiscountServiceI {

	HashMap<String, Object> getBusinessList(JSONObject data);

	MessageDataBean getScanDiscount(JSONObject data);

}
