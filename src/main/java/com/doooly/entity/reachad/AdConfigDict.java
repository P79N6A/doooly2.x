package com.doooly.entity.reachad;


/**
 * 卡券活动表Entity
 * 
 * @author lxl
 * @version 2016-12-14
 */
public class AdConfigDict {

	private String dictType;//配置类型，如活动Activity,链接URL等
	private String dictKey;//配置key
	private String dictValue;//配置value
	public String getDictType() {
		return dictType;
	}
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
	public String getDictKey() {
		return dictKey;
	}
	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}
	public String getDictValue() {
		return dictValue;
	}
	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}
	
	
}