/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdReturnFlow;

/**
 * 商品退货订单 退货流水 DAO
 * 
 * @author yuelou.zhang
 * @version 2017年11月13日
 */
public interface AdReturnFlowDao  {
	
	public int insert(AdReturnFlow adReturnFlow);
	
	public AdReturnFlow getByOrderId(long orderId);
	
	public int updateByPrimaryKeySelective(AdReturnFlow adReturnFlow);
	
}