package com.doooly.business.common.service.impl;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.AdAreaServiceI;
import com.doooly.dao.reachad.AdAreaDao;
import com.doooly.entity.reachad.AdArea;
@Service
public class AdAreaService implements AdAreaServiceI {
	/** 日志 */
	private Log logger = LogFactory.getLog(this.getClass());

	/** 活动礼品DAO */
	@Autowired
	private AdAreaDao adAreaDao;
	@Override
	public List<AdArea> getServicedAreaList() {
		
		List<AdArea> list = adAreaDao.findServicedAreaList();
		return list;
	}
	

}