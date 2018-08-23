package com.doooly.publish.rest.life;


import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

public interface MURechargeRestI {

	MessageDataBean sendActivityCoupon(JSONObject req);
}
