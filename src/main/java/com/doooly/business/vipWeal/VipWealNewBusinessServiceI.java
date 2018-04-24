package com.doooly.business.vipWeal;

import com.doooly.dto.common.MessageDataBean;

/**
 * 答题活动业务Service
 * 
 * @author yuelou.zhang
 * @version 2017年4月25日
 */
public interface VipWealNewBusinessServiceI {

	MessageDataBean getBenefitsData(Integer userId, String address);

	MessageDataBean getIntegralAndBusinessList(Long userId);
	}
