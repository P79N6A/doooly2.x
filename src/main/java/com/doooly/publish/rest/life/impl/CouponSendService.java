package com.doooly.publish.rest.life.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dailymoney.service.DailyMoneyBusinessServiceI;
import com.doooly.business.freeCoupon.service.task.CouponSendTask;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.CouponSendServiceI;

@Component
@Path("/couponSend")
public class CouponSendService implements CouponSendServiceI {
	@Autowired
	private CouponSendTask couponSendTask;
	@Autowired
	private DailyMoneyBusinessServiceI dailyMoneyBusinessServiceI;

	@POST
	@Path(value = "/send")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendCoupon() {
		couponSendTask.luckySendCoupon();
	}

	@POST
	@Path(value = "/updateWealth")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateWealth() {
		dailyMoneyBusinessServiceI.updateUserWealth();

	}
	@POST
	@Path(value = "/getUserCode")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUserCode(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		String telephone = obj.getString("telephone");
		messageDataBean = dailyMoneyBusinessServiceI.getUserCode(telephone);
		return messageDataBean.toJsonString();
		
	}

}
