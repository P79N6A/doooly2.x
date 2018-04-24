package com.doooly.publish.rest.life.impl;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.freeCoupon.service.MyCouponsBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.MyCouponsRestServiceI;

/**
 * 我的卡券Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年3月2日
 */
@Component
@Path("/mycoupons")
public class MyCouponsRestService implements MyCouponsRestServiceI {

	private static Logger logger = Logger.getLogger(MyCouponsRestService.class);

	@Autowired
	private MyCouponsBusinessServiceI myCouponsBusinessService;

	@POST
	@Path(value = "/couponList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getCouponList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 获取查询类型
			String couponType = obj.getString("couponType");
			// 获取卡券类型
			String couponCategory = obj.getString("couponCategory");
			// 获取卡券列表数据
			HashMap<String, Object> map = myCouponsBusinessService.getCouponListByType(userId, couponType, couponCategory);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券列表数据异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	@POST
	@Path(value = "/to_use")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String toUseCoupon(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 获取卡券活动关联id
			String actConnId = obj.getString("actConnId");
			// 获取卡券详情
			HashMap<String, Object> map = myCouponsBusinessService.getCouponDetail(userId, actConnId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券详情异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	
	@POST
	@Path(value = "/getVoucherCouponNum")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getVoucherCouponNum(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取用户id
			String userId = obj.getString("userId");
			// 获取商品金额
			String amount = obj.getString("amount");
			// 获取卡券详情
			HashMap<String, Object> map = myCouponsBusinessService.getVoucherCouponNum(userId, amount);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取可用抵扣券数量异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
