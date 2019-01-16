package com.doooly.dto.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.ActivityConstants.ActivityEnum;
import com.doooly.common.util.BigDecimalValueFilter;

import java.util.Map;

public class MessageDataBean {

	// 返回码
	public String code;

	// 返回信息
	public String mess;

	// 返回数据
	public Map<String, Object> data;

	// 返回数据
	public JSONObject jsonData;

	public MessageDataBean() {
	}

	public MessageDataBean(String code, String mess, Map<String, Object> data) {
		this.code = code;
		this.mess = mess;
		this.data = data;
	}

	public MessageDataBean(String code, String mess, JSONObject jsonData) {
		this.code = code;
		this.mess = mess;
		this.jsonData = jsonData;
	}

	public MessageDataBean(String success_code, String success_mess) {
		this.code = success_code;
		this.mess = success_mess;
	}
	
	public MessageDataBean(ActivityEnum activityEnum){
		this.code = activityEnum.getCode();
		this.mess = activityEnum.getDesc();
	}

	// 操作成功返回码
	public static String success_code = "1000";
	// 操作成功返回信息
	public static String success_mess = "操作成功";

	// 操作失败返回码
	public static String failure_code = "1001";
	// 操作失败返回信息
	public static String failure_mess = "操作失败";

	// 没有活动返回码
	public static String null_code = "1005";
	// 没有活动返回信息
	public static String null_mess = "";

	// 券码库存不足
	public static String coupon_stock_zero_code = "1015";
	// 券码库存不足
	public static String coupon_stock_zero_msg = "此礼品已领完";

	// 已领取返回码
	public static String already_receive_code = "1002";
	// 已领取返回信息
	public static String already_receive_mess = "已领取";
	// 已使用返回码
	public static String already_used_code = "1003";
	// 已使用返回信息
	public static String already_used_mess = "已使用";
	// 未拥有返回码
	public static String have_not_code = "1004";
	// 未拥有返回信息
	public static String have_not_mess = "未拥有";
	// 时间超出返回码
	public static String time_out_code = "3000";
	// 时间超出返回信息
	public static String time_out_mess = "已超时";

	// 数据为空返回码
	public static String no_data_code = "2000";
	// 数据为空返回信息
	public static String no_data_mess = "用户未登陆";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	public JSONObject getJsonData() {
		return jsonData;
	}

	public void setJsonData(JSONObject jsonData) {
		this.jsonData = jsonData;
	}

	/** data-Map<String, Object> */
	public String toJsonString() {
		JSONObject json = new JSONObject();
		json.put("code", this.getCode());
		json.put("msg", this.getMess());
		json.put("data", data);
		return json.toJSONString();
	}

	/** data-Map<String, Object> 保留2位小数,小数位0不省略*/
	public String toFormatJsonString() {
		JSONObject json = new JSONObject();
		json.put("code", this.getCode());
		json.put("msg", this.getMess());
		json.put("data", data);
		return JSON.toJSONString(json,new BigDecimalValueFilter());
	}

	/** data-JSONObject */
	public String toJSONString() {
		JSONObject json = new JSONObject();
		json.put("code", this.getCode());
		json.put("msg", this.getMess());
		json.put("data", jsonData);
		return json.toJSONString();
	}
}
