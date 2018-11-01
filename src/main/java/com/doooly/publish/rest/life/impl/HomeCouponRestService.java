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
import com.doooly.business.freeCoupon.service.HomeCouponServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.HomeCouponRestServiceI;

/**
 * APP首页优惠券 REST Service实现
 * 
 * @author yuelou.zhang
 * @version 2017年8月11日
 */
@Component
@Path("/homeCoupon")
public class HomeCouponRestService implements HomeCouponRestServiceI {

	private static Logger logger = Logger.getLogger(HomeCouponRestService.class);

	@Autowired
	private HomeCouponServiceI homeCouponService;

	@POST
	@Path(value = "/businessList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getBusinessList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			HashMap<String, Object> map = homeCouponService.getBusinessList();
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取优惠券所属商家列表异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/couponList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String getCouponList(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String businessId = obj.getString("businessId");// 商家表主键
			String categoryType = obj.getString("categoryType");// 券类型
			
			int currentPage = obj.getInteger("currentPage");// 当前页
			int pageSize = obj.getInteger("pageSize");// 每页显示条数
			HashMap<String, Object> map = homeCouponService.getCouponListByBusinessId(businessId,categoryType, currentPage,
					pageSize);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取商家的优惠券列表异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

}
