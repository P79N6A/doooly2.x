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
import com.doooly.business.activity.BelleCouponServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.BelleCouponRestServiceI;

/**
 * 百丽优惠券活动 rest 接口
 *
 * @author yuelou.zhang
 * @date 2017年9月18日
 * @version 1.0
 */
@Component
@Path("/belleCoupon")
public class BelleCouponRestService implements BelleCouponRestServiceI {

	private static Logger logger = Logger.getLogger(BelleCouponRestService.class);

	@Autowired
	private BelleCouponServiceI belleCouponService;

	/**
	 * 2018-03-19  百丽新领券活动
	 * @param obj
	 * @return
	 */
	@POST
	@Path(value = "/receive2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String receiveCoupon(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String idFlag = obj.getString("idFlag");// 活动id
			messageDataBean = belleCouponService.receiveCoupon2(userId, idFlag);
		} catch (Exception e) {
			logger.error("百丽优惠券活动|领取优惠券异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}


	@POST
	@Path(value = "/receive")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String receiveCoupon2(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String userId = obj.getString("userId");// 会员id
			String activityId = obj.getString("activityId");// 活动id
			messageDataBean = belleCouponService.receiveCoupon(userId, activityId);
		} catch (Exception e) {
			logger.error("百丽优惠券活动|领取优惠券异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}


}
