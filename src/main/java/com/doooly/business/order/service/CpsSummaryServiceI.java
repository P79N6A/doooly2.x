package com.doooly.business.order.service;

/**
 * 商家营销补贴汇总接口
 * 
 * @author tangzhiyuan
 * @date 2016年12月14日
 * @version 1.0
 *
 */
public interface CpsSummaryServiceI {
	/**
	 * 更新订单营销补贴
	 * 
	 * @param order
	 */
	void updateCpsFee(String orderJson);
}
