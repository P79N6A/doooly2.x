package com.doooly.publish.rest.reachad;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface CpsServiceI {
	/**
	 * 更新一条cps记录
	 * 
	 * @param cpsJson
	 * @return
	 */
	JSONObject updateCps(String cpsJson);

	/**
	 * 批量更新cps记录
	 *
	 * @param cpsJsonArray
	 * @return
	 */
	JSONObject batchUpdateCps(JSONArray cpsJsonArray);
}
