package com.doooly.business.dict;

public interface ConfigDictServiceI {
	/**
	 * 通过字典表查询对应配置value
	 * 1.若缓存有直接通过缓存取值
	 * 2.缓存不存在，则查询数据库，并放入缓存
	 * 
	* @author  hutao 
	* @date 创建时间：2018年9月13日 下午9:08:24 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	String getValueByTypeAndKey(String dictType, String dictKey);
}
