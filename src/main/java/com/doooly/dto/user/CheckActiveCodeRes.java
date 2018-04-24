package com.doooly.dto.user;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;

/**
 * 验证激活码响应DTO
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
public class CheckActiveCodeRes extends BaseRes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5457033994535639154L;
	
	private String readyCode;

	public String getReadyCode() {
		return readyCode;
	}

	public void setReadyCode(String readyCode) {
		this.readyCode = readyCode;
	}
	
	@Override
	public String toJsonString() {
		JSONObject data = new JSONObject();
		data.put("readyCode", readyCode == null ? "" : this.readyCode);
		JSONObject json = new JSONObject();
		json.put("code", this.getCode());
		json.put("msg", this.getMsg());
		json.put("data", data);
		return json.toJSONString();
	}
}
