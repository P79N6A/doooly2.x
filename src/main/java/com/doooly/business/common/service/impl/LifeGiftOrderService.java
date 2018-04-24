package com.doooly.business.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doooly.business.common.service.LifeGiftOrderServiceI;
import com.doooly.common.service.impl.BaseServiceImpl;
import com.doooly.dao.reachlife.LifeGiftOrderDao;
import com.doooly.entity.reachlife.LifeGiftOrder;

/**
 * A系统福利表(表xx_gift_order)
 * 
 * @author lxl
 * @date 2016年12月15日
 */
@Service
public class LifeGiftOrderService extends BaseServiceImpl<LifeGiftOrderDao, LifeGiftOrder> implements LifeGiftOrderServiceI {

	@Autowired
	private LifeGiftOrderDao lifeGiftOrderDao;

}
