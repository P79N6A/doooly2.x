package com.doooly.business.common.service;

import com.doooly.entity.reachlife.LifeProduct;

/**
 * @author lxl
 * @date 2016年12月15日
 * @version 1.0
 */
public interface LifeProductServiceI {

	/**
	 * 根据ID获取对象信息
	 */
	public LifeProduct get(String id);
	
	/**
	 * 根据ID更新对象信息
	 */
	public int update(LifeProduct lifeProduct);
}
