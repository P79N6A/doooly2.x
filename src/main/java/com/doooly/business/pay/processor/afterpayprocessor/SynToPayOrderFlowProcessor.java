package com.doooly.business.pay.processor.afterpayprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.AdOrderFlow;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.dao.reachad.AdOrderFlowDao;
import com.doooly.dto.common.PayMsg;

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
	
	@Override
	public PayMsg process(OrderVo order,PayFlow payFlow) {
		AdOrderFlow adOrderFlow = new AdOrderFlow();
		try {
			logger.info("同步ad_pay_fow到ad_order_flow开始. ==> order={}, ==> payFlow={}", order, payFlow);
			adOrderFlow.setOrderReportId(order.getId());
			adOrderFlow.setSerialNumber(payFlow.getTransNo());
			short payType = getPayType(payFlow);
			adOrderFlow.setPayType(payType);
			adOrderFlow.setAmount(payFlow.getAmount());
			adOrderFlow.setCreateBy(String.valueOf(order.getUserId()));
			adOrderFlow.setType("1");
			adOrderFlow.setDelFlag("0");
			adOrderFlow.setRemarks(String.valueOf(payFlow.getId()));
			adOrderFlow.setUpdateDate(null);
			adOrderFlow.setUpdateBy(null);
			adOrderFlow.setCreateDate(payFlow.getPaySumbitTime());
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

	private short getPayType(PayFlow payFlow) {
		short payType = 9;//其他
		if(PayFlowService.PAYTYPE_WEIXIN.equals(payFlow.getPayType()) || PayFlowService.PAYTYPE_WEIXIN_JSAPI.equals(payFlow.getPayType())){
			payType = 3;
		}else if(PayFlowService.PAYTYPE_DOOOLY.equals(payFlow.getPayType())){
			payType = 0;
		}
		return payType;
	}
}
