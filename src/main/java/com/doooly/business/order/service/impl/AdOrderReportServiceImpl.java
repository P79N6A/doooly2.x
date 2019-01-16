package com.doooly.business.order.service.impl;

import com.doooly.business.order.service.AdOrderReportServiceI;
import com.doooly.business.order.vo.AdOrderBig;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.reach.redis.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 操作ad_order_report
 * @author: qing.zhang
 * @date: 2018-12-26
 */
@Service
public class AdOrderReportServiceImpl implements AdOrderReportServiceI{

    @Autowired
    private AdOrderReportDao adOrderReportDao;

    /**
     * 根据条件查询话费当月积分消费金额
     * @param paramMap
     * @return
     */
    @Override
    public BigDecimal getConsumptionAmountByMap(Map<String, Object> paramMap) {
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();
        paramMap.put("firstDayOfMonth", DateUtils.formatDate(firstDayOfMonth,DateUtils.DATE_yyyy_MM_dd)+DateUtils.DATE_HH_mm_ss_START);
        paramMap.put("lastDayOfMonth", DateUtils.formatDate(lastDayOfMonth,DateUtils.DATE_yyyy_MM_dd)+DateUtils.DATE_HH_mm_ss_END);
        return adOrderReportDao.getConsumptionAmountByMap(paramMap);
    }

    @Override
    public int getBuyNum(Map<String, Object> paramMap) {
        return adOrderReportDao.getBuyNum(paramMap);
    }

    @Override
    public OrderVo getOrderLimt(OrderVo order) {
        return adOrderReportDao.getOrderLimt(order);
    }

    @Override
    public void insertAdBigOrder(AdOrderBig adOrderBig) {
        adOrderReportDao.insertAdBigOrder(adOrderBig);
    }

    @Override
    public List<OrderVo> getOrders(String bigOrderNumber) {
        return adOrderReportDao.getOrders(bigOrderNumber);
    }

    @Override
    public AdOrderBig getAdOrderBig(AdOrderBig adOrderBig) {
        return adOrderReportDao.getAdOrderBig(adOrderBig);
    }
}
