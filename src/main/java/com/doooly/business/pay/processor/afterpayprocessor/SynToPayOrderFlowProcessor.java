package com.doooly.business.pay.processor.afterpayprocessor;

import com.doooly.business.order.service.OrderService;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.AdOrderFlow;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.business.utils.DateUtils;
import com.doooly.dao.reachad.AdOrderFlowDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/***
 * 同步支付记录到ad_order_flow
 * 
 * @author 2017-10-11 17:34:21 WANG
 *
 */
@Component
public class SynToPayOrderFlowProcessor implements AfterPayProcessor{
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AdOrderFlowDao adOrderFlowDao;
    @Autowired
    private OrderDao orderDao;
	
	@Override
	public PayMsg process(OrderVo order, Map<String, Object> resultMap, String realPayType) {
		AdOrderFlow adOrderFlow = new AdOrderFlow();
		try {
			logger.info("同步ad_pay_fow到ad_order_flow开始. ==> order={}, ==> resultMap={}", order, resultMap);
            BigDecimal amount = new BigDecimal("0");
            if(realPayType.equals("2")){
                //混合支付需要重新计算实付金额,先查询微信支付的用总金额减去
                Order o = new Order();
                o.setOrderNumber(order.getOrderNumber());
                o.setPayType(PayFlowService.PayType.getCodeByName("weixin"));
                o.setState(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
                o.setType(OrderService.OrderStatus.HAD_FINISHED_ORDER.getCode());
                Order order1 = orderDao.getSyncOrder(o);
                amount = order1.getAmount();
            }
			adOrderFlow.setOrderReportId(order.getId());
			adOrderFlow.setSerialNumber(String.valueOf(resultMap.get("outTradeNo")));
			short payType = getPayType(realPayType);
			adOrderFlow.setPayType(payType);
			adOrderFlow.setAmount(order.getTotalMount().subtract(amount));
			adOrderFlow.setCreateBy(String.valueOf(order.getUserId()));
			adOrderFlow.setType("1");
			adOrderFlow.setDelFlag("0");
			adOrderFlow.setRemarks(String.valueOf(resultMap.get("outTradeNo")));
			adOrderFlow.setUpdateDate(null);
			adOrderFlow.setUpdateBy(null);
			adOrderFlow.setCreateDate(DateUtils.parse(String.valueOf(resultMap.get("payEndTime")),DateUtils.DATE_yyyy_MM_dd_HH_mm_ss));
			int rows = adOrderFlowDao.insert(adOrderFlow);
			logger.info("SynToPayOrderFlowProcessor成功. rows = {}", rows);
			if (rows <= 0) {
				logger.error("同步ad_pay_fow到ad_order_flow失败. adOrderFlow = {}",adOrderFlow);
			}
		} catch (Exception e) {
			logger.error("同步ad_pay_fow到ad_order_flow出现异常. adOrderFlow = {},e = {}", adOrderFlow,e);
		}
		return null;
	}

	private short getPayType(String realPayType) {
		short payType;//其他
		if(realPayType.equals("1")){
            payType = 3;//微信支付
        }else {
            payType = 0;//积分支付
        }
		return payType;
	}
}
