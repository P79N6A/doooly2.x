/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdReturnFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品退货订单 退货流水 DAO
 * 
 * @author yuelou.zhang
 * @version 2017年11月13日
 */
public interface AdReturnFlowDao  {
	
	public int insert(AdReturnFlow adReturnFlow);
	
	public AdReturnFlow getByOrderId(@Param("orderId") long orderId, @Param("returnFlowNumber") String returnFlowNumber, @Param("payType") String payType);

	List<AdReturnFlow> getListByOrderId(@Param("orderId") long orderId, @Param("returnFlowNumber") String returnFlowNumber, @Param("payType") String payType);

	public int updateByPrimaryKeySelective(AdReturnFlow adReturnFlow);
	
}