package com.doooly.dao.reachad;

import com.doooly.business.myorder.po.OrderPoReq;
import com.doooly.entity.reachad.AdOrderIntegralRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 积分消费记录DAO接口
 * @author sfc
 * @version 2019-04-02
 */
public interface AdOrderIntegralRecordDao {

    //汇总手续费
    BigDecimal sumIntegralRebateAmount(AdOrderIntegralRecord adOrderIntegralRecord);

    List<Map<String, Object>> findOrderServiceChargeSum(OrderPoReq req);

}