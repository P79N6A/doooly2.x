package com.doooly.publish.rest.reachad;

import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.dto.activity.ActivityOrderRes;

/**
 * 
 * @author 赵清江
 * @date 2016年12月16日
 * @version 1.0
 */
public interface ActivityServicePublishI {

	public String activityOrderReceiver(ActivityOrderReq req);
	
}
