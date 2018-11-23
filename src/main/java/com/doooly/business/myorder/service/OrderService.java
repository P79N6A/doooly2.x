package com.doooly.business.myorder.service;

import java.util.List;
import java.util.Map;

import com.doooly.business.myorder.dto.HintReq;
import com.doooly.business.myorder.dto.HintResp;
import com.doooly.business.myorder.dto.OrderDeleteReq;
import com.doooly.business.myorder.dto.OrderDetailReq;
import com.doooly.business.myorder.dto.OrderDetailResp;
import com.doooly.business.myorder.dto.OrderReq;
import com.doooly.business.myorder.po.OrderPoReq;
import com.doooly.business.myorder.po.OrderPoResp;

public interface OrderService {
	/**
	 * 订单列表
	 * @param req
	 * @return
	 */
	public List<OrderPoResp> getOrderList(OrderReq req);

	/**
	 * 订单详情
	 * @param req
	 * @return
	 */
	public OrderDetailResp getOrderDetail(OrderDetailReq req);

	/**
	 * 统计订单分组
	 * @param req
	 * @return
	 */
	public List<Map<String,String>> getOrderdDetailSum(OrderPoReq req);

	/**
	 * 统计订单总数
	 * @param orderReq
	 * @return
	 */
	public Long countOrderNum(OrderReq orderReq);

	/**
	 * 取消订单提醒
	 * @param req
	 */
	public void  cannelHint(OrderReq req);

	/**
	 * 获取订单提醒
	 * @param req
	 * @return
	 */
	public HintResp getHint(HintReq req);

	/**
	 * 删除订单
	 * @param req
	 * @return
	 */
	public boolean deleteOrder(OrderDeleteReq req);

}
