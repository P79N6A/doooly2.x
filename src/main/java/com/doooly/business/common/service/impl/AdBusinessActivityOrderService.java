package com.doooly.business.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doooly.business.common.service.AdBusinessActivityOrderServiceI;
import com.doooly.dao.reachad.AdBusinessActivityOrderDao;
import com.doooly.entity.reachad.AdBusinessActivityOrder;

/**
 * 
 * @author lxl
 */
@Service
public class AdBusinessActivityOrderService
		implements AdBusinessActivityOrderServiceI {

	/** 兑换券DAO */
	@Autowired
	private AdBusinessActivityOrderDao adBusinessActivityOrderDao;
	
	/**
	 * 保存商家活动订单信息
	 */
	public void insert(AdBusinessActivityOrder adBusinessActivityOrder){
		adBusinessActivityOrderDao.insert(adBusinessActivityOrder);
	}
}
