package com.doooly.business.meituan.impl;

import com.doooly.business.meituan.MeituanOrderService;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.MeituanRequest;
import com.doooly.common.meituan.OrderStatusEnum;
import com.doooly.entity.meituan.Order;
import com.doooly.entity.meituan.OrderQuery;
import com.google.gson.reflect.TypeToken;
import com.reach.redis.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/19.
 */
@Service
public class MeituanOrderServiceImpl implements MeituanOrderService {

    private static Logger logger = LoggerFactory.getLogger(MeituanOrderServiceImpl.class);

    @Override
    public Order queryOrderByOrderSN(String orderSN) throws Exception{
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOrderSN(orderSN);
        String content = GsonUtils.son.toJson(orderQuery);
        MeituanRequest meituanRequest = new MeituanRequest();
        meituanRequest.setContent(content);
        String ret = meituanRequest.doPost(MeituanConstants.URL_QUERY_ORDER);
        Map<String,Object> retMap = GsonUtils.son.fromJson(ret,Map.class);
        String data = String.valueOf(retMap.get("data"));
        Order order = GsonUtils.son.fromJson(data,Order.class);
        return order;
    }


    @Override
    public List<Order> queryOrderByTimeRange(long fromTime, long toTime, OrderStatusEnum orderStatusEnum) throws Exception{
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setFromTime(fromTime);
        orderQuery.setToTime(toTime);
        orderQuery.setStatus(orderStatusEnum.getCode());
        String content = GsonUtils.son.toJson(orderQuery);
        MeituanRequest meituanRequest = new MeituanRequest();
        meituanRequest.setContent(content);
        String ret = meituanRequest.doPost(MeituanConstants.URL_QUERY_ORDER_BY_TIME_RANGE);
        Map<String,Object> retMap = GsonUtils.son.fromJson(ret,Map.class);
        String data = String.valueOf(retMap.get("data"));
        List<Order> listOrder = GsonUtils.son.fromJson(data,new TypeToken<List<Order>>(){}.getType());
        return listOrder;
    }


    @Override
    public Order queryOrderByChannelOrderId(String channelOrderId) throws Exception {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setChannelOrderId(channelOrderId);
        String content = GsonUtils.son.toJson(orderQuery);
        MeituanRequest meituanRequest = new MeituanRequest();
        meituanRequest.setContent(content);
        String ret = meituanRequest.doPost(MeituanConstants.URL_QUERY_ORDER_BY_CHANNEL_ORDER_ID);
        Map<String,Object> retMap = GsonUtils.son.fromJson(ret,Map.class);
        String data = String.valueOf(retMap.get("data"));
        Order order = GsonUtils.son.fromJson(data,Order.class);
        return order;
    }




}
