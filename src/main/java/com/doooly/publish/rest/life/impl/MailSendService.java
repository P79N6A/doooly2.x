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
import com.doooly.common.util.SendMailUtil;
import com.doooly.publish.rest.life.CouponSendServiceI;
import com.doooly.publish.rest.life.MailSendServiceI;

@Component
@Path("/mailSend")
public class MailSendService implements MailSendServiceI {

	/** 获取二级菜单 商品信息 以及商品分类信息 */
	@POST
	@Path(value = "/forRefundTicket")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMailForRefundTicket(JSONObject json) {
		// TODO Auto-generated method stub
		Integer type = json.getInteger("type");
		String username = json.getString("username");
		String orderNo = json.getString("orderNo");
		String[] address = { "yuming.gu@reach-core.com", "mindy.jiang@reach-core.com" };
		String message = "";
		if (type == 1) {
			message = "您好，非自愿退票的客户" + username + "已申请退票,目前处于申请退票阶段,请及时处理,吉祥订单号为:" + orderNo + ",祝您生活愉快";
			SendMailUtil.sendCommonMail(address, "兜礼退票提醒", message);
		} else if (type == 0) {
			message = "您好，自愿退票的客户" + username + "已申请退票,目前处于已受理待退款阶段,请及时处理,吉祥订单号为:" + orderNo + ",祝您生活愉快";
			SendMailUtil.sendCommonMail(address, "兜礼退票提醒", message);
		} else if (type == 2){
			String[] newAddress = { "wenwei.yang@reach-core.com", "tao.hu@reach-core.com" };
			message = "您好，会员卡号为"+username+"的用户在购买票时,推送B系统时报错";
			SendMailUtil.sendCommonMail(newAddress, "兜礼购票出错提醒", message);
		}
	}

}
