package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.LoveKaiHealthCouponServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.LoveKaiHealthCouponRestServiceI;

/**
 * 爱启健康优惠券活动 rest 接口
 * 
 * @author yuelou.zhang
 * @date 2017年9月18日
 * @version 1.0
 */
@Component
@Path("/loveKaiHealthCoupon")
public class LoveKaiHealthCouponRestService implements LoveKaiHealthCouponRestServiceI {

	private static Logger logger = Logger.getLogger(LoveKaiHealthCouponRestService.class);

	@Autowired
	private LoveKaiHealthCouponServiceI loveKaiHealthCouponService;

	@POST
	@Path(value = "/receive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String receiveCoupon(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String activityId = obj.getString("activityId");// 活动id
			messageDataBean = loveKaiHealthCouponService.receiveCoupon(userId, activityId);
		} catch (Exception e) {
			logger.error("爱启健康优惠券活动|领取优惠券异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
