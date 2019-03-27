package com.doooly.business.myorder.service;

import com.doooly.business.myorder.dto.*;
import com.doooly.business.myorder.po.OrderPoReq;
import com.doooly.business.myorder.po.OrderPoResp;
import com.doooly.entity.reachad.AdOrderDetail;
import com.doooly.entity.reachad.AdReturnFlow;

import java.util.List;
import java.util.Map;

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
	public void  cannelHint(OrderHintReq req);

	/**
	 * 取消用户提醒
	 */
	public void  cannelUserFlag(String userId, String flags);

	/**
	 * 获取订单提醒
	 * @param req
	 * @return
	 */
	public HintResp getHint(HintReq req);

	/**
	 * 个人中心提醒
	 * @param req
	 * @return
	 */
	public HintResp getUserFlag(HintReq req);

	/**
	 * 删除订单
	 * @param req
	 * @return
	 */
	public boolean deleteOrder(OrderDeleteReq req);


	List<AdReturnFlow> getOrderList(List<Long> orderList);

	List<AdOrderDetail> finDetailByOrder(List<Long> list);

}
