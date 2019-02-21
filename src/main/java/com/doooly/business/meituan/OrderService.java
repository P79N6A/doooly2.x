package com.doooly.business.meituan;

import com.doooly.common.meituan.OrderStatusEnum;
import com.doooly.entity.meituan.Order;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19.
 */
public interface OrderService {

    Order queryOrderByOrderSN(String orderSN) throws Exception;

    List<Order> queryOrderByTimeRange(long fromTime, long toTime, OrderStatusEnum orderStatusEnum) throws Exception;

    Order queryOrderByChannelOrderId(String channelOrderId) throws Exception;

}
