package com.doooly.business.activity;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.dto.activity.ActivityOrderRes;
import com.doooly.dto.common.MessageDataBean;

/**
 *
 * @author 赵清江
 * @date 2016年12月16日
 * @version 1.0
 */
public interface ActivityServiceI {

	public ActivityOrderRes activityOrderService(ActivityOrderReq req);

	public MessageDataBean getHotActivity(JSONObject results);

	public MessageDataBean getActivityInfo(JSONObject results);

	public MessageDataBean getActivityCategoryList(JSONObject results);

}
