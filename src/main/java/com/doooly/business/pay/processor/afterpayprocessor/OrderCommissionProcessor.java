package com.doooly.business.pay.processor.afterpayprocessor;

import com.doooly.business.mall.service.Impl.MallBusinessService;
import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.dao.reachad.AdOrderReportDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 计算佣金
 *
 */
@Component
public class OrderCommissionProcessor implements AfterPayProcessor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AdOrderReportDao adOrderReportDao;
    @Autowired
    private MallBusinessService mallBusinessService;

    @Override
    public PayMsg process(OrderVo order, Map<String, Object> payFlow, String realPayType) {
        try {
            logger.info("OrderCommissionProcessor orderNum = {}",order.getOrderNumber());
            //旅游卡计算返佣返利
            if(order.getProductType() == OrderService.ProductType.TOURIST_CARD_RECHARGE.getCode()){
                OrderVo o = new OrderVo();
                o.setId(order.getId());
                AdBusiness business = mallBusinessService.getById(String.valueOf(order.getBussinessId()));
                BigDecimal businessRebateAmount = null;
                if (!StringUtils.isEmpty(business.getBussinessRebate())) {
                    BigDecimal businessRebate = new BigDecimal(business.getBussinessRebate()).divide(new BigDecimal("100"));
                    businessRebateAmount = order.getTotalMount().multiply(businessRebate);
                    o.setIsBusinessRebate('1');
                    o.setBusinessRebateAmount(businessRebateAmount);
                }
                logger.info("OrderCommissionProcessor bussinessRebate = {}，businessRebateAmount={}", business.getBussinessRebate(), businessRebateAmount);
                //用户返利
                //BigDecimal userReturnAmount = null;
                //if(businessRebateAmount != null && !StringUtils.isEmpty(business.getUserRebate())) {
                //	order.setIsUserRebate('1');
                //	order.setUserRebate(new BigDecimal(business.getUserRebate()).intValue());
                //	userReturnAmount = businessRebateAmount.multiply(new BigDecimal(business.getUserRebate()).divide(new BigDecimal("100")));
                //	order.setUserReturnAmount(userReturnAmount);
                //  logger.info("userRebate = {}，userReturnAmount={}",  business.getUserRebate(), userReturnAmount);
                //}
                o.setIsUserRebate('0');
                o.setUserReturnAmount(new BigDecimal("0"));
                int i = adOrderReportDao.update(o);
                logger.error("orderDao.updateById i = {}", i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderCommissionProcessor ,e = {}", order, e);
        }
        return null;
    }

}
