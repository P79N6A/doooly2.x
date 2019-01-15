package com.doooly.business.order.service;

import com.doooly.business.order.vo.OrderVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 操作ad_order_report
 * @author: qing.zhang
 * @date: 2018-12-26
 */
public interface AdOrderReportServiceI {

    BigDecimal getConsumptionAmountByMap(Map<String, Object> paramMap);

    int getBuyNum(Map<String, Object> paramMap);

    OrderVo getOrderLimt(OrderVo order);
}
