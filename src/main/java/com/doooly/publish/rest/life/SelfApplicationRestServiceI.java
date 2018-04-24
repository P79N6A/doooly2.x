package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

/**
 * 员工自主申请Service
 * 
 * @author wenwei.yang
 * @version 2017年10月9日
 */
public interface SelfApplicationRestServiceI {
	/**
	 * 获取首页公司（即企业）数据
	 * 
	 */
	String index(JSONObject obj);
	
	/**
	 * ajax获取所选公司部门数据
	 * 
	 */
	String departmentDatas(JSONObject obj);
	
	/**
	 * 提交信息
	 * 
	 */
	String submit(JSONObject obj);
}
