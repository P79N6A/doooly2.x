package com.doooly.business.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doooly.business.common.service.LifeProductServiceI;
import com.doooly.dao.reachlife.LifeProductDao;
import com.doooly.entity.reachlife.LifeProduct;

/**
 * @author lxl
 * @date 2016年12月15日
 * @version 1.0
 */
@Service
public class LifeProductService implements LifeProductServiceI {

	@Autowired
	private LifeProductDao lifeProductDao;

	/**
	 * 根据ID获取对象信息
	 */
	public LifeProduct get(String id) {
		return lifeProductDao.get(id);
	}
	
	/**
	 * 根据ID更新对象信息
	 */
	public int update(LifeProduct lifeProduct) {
		return lifeProductDao.update(lifeProduct);
	}
}
