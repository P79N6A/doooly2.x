package com.doooly.business.pay.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.pay.service.PayFlowService;
import com.doooly.dao.reachad.AdPayFlowDao;

@Service
public class PayFlowServiceImpl implements PayFlowService {
	
	protected static Logger logger = LoggerFactory.getLogger(PayFlowServiceImpl.class);

	@Autowired
	private AdPayFlowDao payFlowDao;
	
	@Override
	public PayFlow getByOrderNum(String orderNum,String payType,String payStatus) {
		return payFlowDao.getByOrderNum(orderNum, payType, payStatus);
	}

	@Override
	public int insert(PayFlow payFlow) {
		return payFlowDao.insert(payFlow);
	}
	
	@Override
	public int update(PayFlow payFlow) {
		return payFlowDao.updateByPrimaryKeySelective(payFlow);
	}

	@Override
	public PayFlow getByTranNo(String transNo, String payType) {
		return payFlowDao.getByTransNo(transNo, payType);
	}
	
	@Override
	public PayFlow getById(String id) {
		return payFlowDao.getById(id);
	}
	
	/***
	 * 修改支付成功状态
	 * 
	 * @param flowId
	 * @param transNo
	 * @return
	 */
	public int updatePaySuccess(String flowId, String transNo) {
		try {
			PayFlow flow = new PayFlow();
			flow.setId(Long.valueOf(flowId));
			flow.setTransNo(transNo);
			flow.setPayStatus(PayFlowService.PAYMENT_SUCCESS);
			flow.setErrorCode(PayFlowService.SUCCESS_CODE);
			flow.setErrorReason(PayFlowService.SUCCESS_MSG);
			flow.setPayCallbackTime(new Date());
			return update(flow);
		} catch (NumberFormatException e) {
			logger.error("updatePaySuccess e = {}", e);
		}
		return 0;
	}

}
