package com.doooly.business.pay.service.impl;

import com.doooly.business.pay.service.ReturnFlowService;
import com.doooly.dao.reachad.AdReturnDetailDao;
import com.doooly.dao.reachad.AdReturnFlowDao;
import com.doooly.entity.reachad.AdReturnDetail;
import com.doooly.entity.reachad.AdReturnFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生成退货单
 * 
 * @author 2017-11-13 17:44:20 WANG
 *
 */
@Service
public class ReturnFlowServiceImpl implements ReturnFlowService {

	protected Logger logger = LoggerFactory.getLogger(ReturnFlowServiceImpl.class);

	@Autowired
	private AdReturnFlowDao adReturnFlowDao;
	@Autowired
	private AdReturnDetailDao adReturnDetailDao;

	public long insert(AdReturnFlow adReturnFlow) {
		try {
			long i = adReturnFlowDao.insert(adReturnFlow);
			logger.info("adReturnFlowDao.insert i = {},id = {}", i, adReturnFlow.getId());
			List<AdReturnDetail> list = adReturnFlow.getDetails();
			for (AdReturnDetail adReturnDetail : list) {
				adReturnDetail.setReturnFlowId(adReturnFlow.getId());
				adReturnDetailDao.insert(adReturnDetail);
			}
			return adReturnFlow.getId();
		} catch (Exception e) {
			logger.error("ReturnFlowServiceImpl.insert  e = {}", e);
		}
		return 0l;
	}
	
	public AdReturnFlow getByOrderId(long orderId, String returnFlowNumber, String payType){
		return adReturnFlowDao.getByOrderId(orderId,returnFlowNumber,payType);
	}
	
	public int updateByPrimaryKeySelective(AdReturnFlow adReturnFlow){
		return adReturnFlowDao.updateByPrimaryKeySelective(adReturnFlow);
	}

    @Override
    public int updateByOrderId(AdReturnFlow adReturnFlow) {
        return adReturnFlowDao.updateByOrderId(adReturnFlow);
    }

}
