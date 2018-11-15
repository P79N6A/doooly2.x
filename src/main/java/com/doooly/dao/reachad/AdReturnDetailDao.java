/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.entity.reachad.AdReturnDetail;
import com.doooly.entity.reachad.AdReturnFlow;

import java.util.List;

/**
 * 商品退货订单 退货明细 DAO
 * 
 * @author yuelou.zhang
 * @version 2017年11月13日
 */
public interface AdReturnDetailDao {
	
	public int insert(AdReturnDetail adReturnDetail);

    List<OrderItemVo> getList(AdReturnFlow adReturnFlow);
}