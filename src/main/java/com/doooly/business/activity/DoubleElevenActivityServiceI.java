package com.doooly.business.activity;

import com.doooly.dto.common.MessageDataBean;

/**
 * 
 * @author wenwei.yang
 * @date 2017年10月31日
 * @version 1.0
 */
public interface DoubleElevenActivityServiceI {

	MessageDataBean getActivityIndex(String userId);

	MessageDataBean getActivityIndexForJoiner(String superUserId, String userId);

	MessageDataBean helpInitiator(String superUserId, String userId);
	
	MessageDataBean receiveGift(String userId);
	
	MessageDataBean isReceiveGift(String userId);
	
}
