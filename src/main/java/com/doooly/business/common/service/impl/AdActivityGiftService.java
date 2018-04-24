package com.doooly.business.common.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doooly.business.common.service.AdActivityGiftServiceI;
import com.doooly.common.service.impl.BaseServiceImpl;
import com.doooly.dao.reachad.AdActivityGiftDao;
import com.doooly.entity.reachad.AdActivityGift;

/**
 * 
 * @author lxl
 */
@Service
public class AdActivityGiftService
		implements AdActivityGiftServiceI {

	/** 日志 */
	private Log logger = LogFactory.getLog(this.getClass());

	/** 活动礼品DAO */
	@Autowired
	private AdActivityGiftDao adActivityGiftDao;

	@Override
	public AdActivityGift getBusinessActivityGift(AdActivityGift adActivityGift) {
		logger.info("adActivityGift:" + adActivityGift);
		try {
			adActivityGift = adActivityGiftDao.getBusinessActivityGift(adActivityGift);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adActivityGift;
	}

}
