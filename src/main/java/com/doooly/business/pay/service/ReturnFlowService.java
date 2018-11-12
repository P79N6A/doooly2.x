package com.doooly.business.pay.service;

import com.doooly.entity.reachad.AdReturnFlow;

public interface ReturnFlowService {
	
	public long insert(AdReturnFlow adReturnFlow);
	
	public AdReturnFlow getByOrderId(long orderId, String returnFlowNumber);
	
	public int updateByPrimaryKeySelective(AdReturnFlow adReturnFlow);


	
}
