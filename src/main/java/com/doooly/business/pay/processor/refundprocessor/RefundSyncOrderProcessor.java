package com.doooly.business.pay.processor.refundprocessor;

import com.alibaba.fastjson.JSONArray;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdReturnFlow;
import com.doooly.entity.reachad.AdReturnPoints;
import com.doooly.entity.reachad.AdReturnPointsLog;
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
    private AdReturnPointsLogDao adReturnPointsLogDao;

    @Override
    public PayMsg process(OrderVo order, Order o) {
        if (order.getIsSource() == 3) {
            //自营订单不同步11
            return null;
        }
        logger.info("计算退货返利同步订单到_order开始. orderNum = {}", order.getOrderNumber());
        //查询需要计算的退货订单
        List<Order> list = orderDao.findList(o);
        logger.info("process orderlist,{},{}",order.getOrderNumber(), JSONArray.toJSONString(list));
        if(CollectionUtils.isNotEmpty(list)){
            for (Order order2 : list) {
                try {

                    AdReturnFlow adReturnFlow = adReturnFlowDao.getByOrderId(order.getId(), order2.getSerialNumber(), String.valueOf(order2.getPayType()));
                    if (adReturnFlow == null) {
                        logger.info("adReturnFlow为空，orderId：{}，serialnumber：{}，paytype：{}",order.getId(),order2.getSerialNumber(),order2.getPayType());
                        continue;
                    }

                    //插入ad_return_points_log
                    AdReturnPointsLog adReturnPointsLog = new AdReturnPointsLog();
                    adReturnPointsLog.setOrderId(order2.getId());
                    adReturnPointsLog.setType(AdReturnPoints.TYPE_INTERCHANGE);
                    adReturnPointsLog.setDelFlag("0");
                    adReturnPointsLog.setCreateDate(new Date());
                    adReturnPointsLog.setUpdateDate(new Date());
                    AdReturnPointsLog adReturnPointsLog1 = adReturnPointsLogDao.getByCondition(adReturnPointsLog);
                    if (adReturnPointsLog1 != null) {
                        logger.info("AdReturnPointsLog已经存在：orderId：{}，type：{}",order2.getId(),AdReturnPoints.TYPE_INTERCHANGE);
                        continue;
                    }

                    //插入ad_return_points
                    AdReturnPoints adReturnPoints = new AdReturnPoints();
                    adReturnPoints.setReportId(adReturnFlow.getOrderReportId()+"");
                    AdReturnPoints adReturnPoints1 = adReturnPointsDao.get(adReturnPoints);
                    if(adReturnPoints1 == null){
                        //插入
                        adReturnPoints.setUserId(String.valueOf(order.getUserId()));
                        adReturnPoints.setReportId(adReturnFlow.getOrderReportId()+"");
                        adReturnPoints.setAmount(order2.getUserRebate());
                        adReturnPoints.setType(AdReturnPoints.TYPE_INTERCHANGE);
                        adReturnPoints.setStatus(AdReturnPoints.STATUS_EXPECTED);
                        adReturnPoints.setCreateDate(new Date());
                        adReturnPointsDao.insert(adReturnPoints);
                    }
                    adReturnPoints1 = adReturnPointsDao.getByCondition(adReturnPoints);

                    adReturnPointsLog.setAdReturnPointsId(Long.parseLong(adReturnPoints1.getId()));
                    adReturnPointsLog.setOperateAmount(order2.getUserRebate());
                    adReturnPointsLog.setOperateType("2");
                    adReturnPointsLogDao.save(adReturnPointsLog);

                    //更新adReturnPoints
                    adReturnPoints1.setAmount(adReturnPoints1.getAmount().subtract(adReturnPointsLog.getOperateAmount()));
                    adReturnPointsDao.update(adReturnPoints1);

                    //计算返利
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId",order2.getId());
                    map.put("orderNumber",order2.getOrderNumber());
                    map.put("bussinessId",order2.getBussinessId());
                    orderDao.computeRefundRebateAndSyncOrder(map);

                    //计算完返利,重新查询order，同步ad_return_flow表
                    Order order3 = orderDao.get(String.valueOf(order2.getId()));
                    adReturnFlow.setUserRebate(order3.getUserRebate());
                    adReturnFlow.setBusinessRebateAmount(order3.getBusinessRebate());
                    adReturnFlow.setType(null);//不更新type值
                    adReturnFlowDao.updateByPrimaryKeySelective(adReturnFlow);
                } catch (Exception e) {
                    logger.error("processor退款回调异常：{} ",order2.getOrderNumber(),e);
                }
            }
        }
        return null;
    }
}
