package com.doooly.business.common.service;

import java.util.List;

import com.doooly.entity.reachad.AdArea;
import com.doooly.entity.reachad.AdBusinessActivityOrder;

/**
 * @author yww
 * @name 商家服务区域信息处理
 */
public interface AdAreaServiceI {
	/**
	 * 获取服务区域基础信息
	 */
	public List<AdArea> getServicedAreaList();

}
