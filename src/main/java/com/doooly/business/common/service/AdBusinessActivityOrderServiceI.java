package com.doooly.business.common.service;

import com.doooly.entity.reachad.AdBusinessActivityOrder;

/**
 * @author lxl
 * @name 商家活动验证通过后业务处理
 */
public interface AdBusinessActivityOrderServiceI {
	/**
	 * 保存商家活动订单信息
	 */
	public void insert(AdBusinessActivityOrder adBusinessActivityOrder);

}
