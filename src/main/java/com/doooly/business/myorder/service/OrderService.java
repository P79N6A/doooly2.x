package com.doooly.business.myorder.service;

import java.util.List;
import java.util.Map;

import com.doooly.business.myorder.dto.OrderDetailReq;
import com.doooly.business.myorder.dto.OrderDetailResp;
import com.doooly.business.myorder.dto.OrderReq;
import com.doooly.business.myorder.po.OrderPoReq;
import com.doooly.business.myorder.po.OrderPoResp;

public interface OrderService {

	public List<OrderPoResp> getOrderList(OrderReq req);
	
	public OrderDetailResp getOrderDetail(OrderDetailReq req);
	
	public List<Map<String,String>> getOrderdDetailSum(OrderPoReq req);
	
	public Long countOrderNum(OrderReq orderReq);
	
}
