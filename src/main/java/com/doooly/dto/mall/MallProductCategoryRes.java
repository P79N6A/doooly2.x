package com.doooly.dto.mall;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.dto.BaseRes;
import com.doooly.entity.reachad.AdProductCategory;

/**
 * 会员数量传输DTO
 * 
 * @author 杨汶蔚
 * @date 2016年9月20日
 * @version 1.0
 */
public class MallProductCategoryRes extends BaseRes {
	/**
	* 
	*/
	private static final long serialVersionUID = 1182021164596774962L;

	private List<AdProductCategory> adProductCategorys;

	@Override
	public String toJsonString() {
		JSONObject json = new JSONObject();
		json.put("code", this.getCode());
		json.put("msg", this.getMsg());
		json.put("adProductCategorys", adProductCategorys);
		return json.toJSONString();
	}
}
