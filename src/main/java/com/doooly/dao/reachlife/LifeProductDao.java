package com.doooly.dao.reachlife;

import com.doooly.entity.reachlife.LifeProduct;

public interface LifeProductDao {
	/**
	 * 根据ID获取对象信息
	 */
	public LifeProduct get(String id);

	/**
	 * 根据ID更新商品信息
	 */
	public int update(LifeProduct lifeProduct);
}