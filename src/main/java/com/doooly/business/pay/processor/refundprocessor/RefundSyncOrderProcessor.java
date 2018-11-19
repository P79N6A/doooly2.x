package com.doooly.business.pay.processor.refundprocessor;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.dao.reachad.AdAvailablePointsDao;
import com.doooly.dao.reachad.AdReturnFlowDao;
import com.doooly.dao.reachad.AdReturnPointsDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdReturnFlow;
import com.doooly.entity.reachad.AdReturnPoints;
import com.doooly.entity.reachad.Order;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 退货订单计算返利并同步退货流水
 * @author: qing.zhang
 * @date: 2018-11-13
 */
@Component
public class RefundSyncOrderProcessor implements AfterRefundProcessor {

    private Logger logger = LoggerFactory.getLogger(RefundSyncOrderProcessor.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private AdReturnFlowDao adReturnFlowDao;
    @Autowired
    private AdReturnPointsDao adReturnPointsDao;
    @Autowired
    private AdAvailablePointsDao adAvailablePointsDao;

    @Override
    public PayMsg process(OrderVo order, Order o) {
        if (order.getIsSource() == 3) {
            //自营订单不同步11
            return null;
        }
        logger.info("计算退货返利同步订单到_order开始. orderNum = {}", order.getOrderNumber());
        //查询需要计算的退货订单
        List<Order> list = orderDao.findList(o);
        if(CollectionUtils.isNotEmpty(list)){
            for (Order order2 : list) {
                //插入ad_return_points
                AdReturnPoints adReturnPoints = new AdReturnPoints();
                adReturnPoints.setUserId(String.valueOf(order.getUserId()));
                adReturnPoints.setOrderId(order2.getId());
                AdReturnPoints adReturnPoints1 = adReturnPointsDao.get(adReturnPoints);
                if(adReturnPoints1 != null){
                    //说明已经同步过
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("orderId",order2.getId());
                map.put("orderNumber",order2.getOrderNumber());
                map.put("bussinessId",order2.getBussinessId());
                orderDao.computeRefundRebateAndSyncOrder(map);
                //计算完返利,重新查询order，同步ad_return_flow表
                Order order3 = orderDao.get(String.valueOf(order2.getId()));
                AdReturnFlow adReturnFlow = adReturnFlowDao.getByOrderId(order.getId(), order3.getSerialNumber(), String.valueOf(order3.getPayType()));
                adReturnFlow.setUserRebate(order3.getUserRebate());
                adReturnFlow.setBusinessRebateAmount(order3.getBusinessRebate());
                adReturnFlow.setType(null);//不更新type值
                adReturnFlowDao.updateByPrimaryKeySelective(adReturnFlow);
                adReturnPoints.setReportId(String.valueOf(order.getId()));
                adReturnPoints.setAmount(order3.getUserRebate());
                adReturnPoints.setType(AdReturnPoints.TYPE_INTERCHANGE);
                adReturnPoints.setStatus(AdReturnPoints.STATUS_EXPECTED);
                adReturnPoints.setCreateDate(new Date());
                adReturnPointsDao.insert(adReturnPoints);
            }
        }
        return null;
    }
}
