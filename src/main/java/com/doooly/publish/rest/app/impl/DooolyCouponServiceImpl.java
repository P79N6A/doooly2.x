package com.doooly.publish.rest.app.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.home.v2.servcie.HomePageDataServcie;
import com.doooly.common.DooolyResponseStatus;
import com.doooly.dto.base.BaseResponse;
import com.doooly.dto.coupon.FindExclusiveCouponResponse;
import com.doooly.dto.coupon.GetAdCouponActivityInfosResponse;
import com.doooly.dto.coupon.ReceiveExclusiveCouponRequest;
import com.doooly.dto.coupon.ReceiveExclusiveCouponResponse;
import com.doooly.publish.rest.app.DooolyCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @className: DooolyCouponServiceImpl
 * @description: 兜礼优惠券，相关服务
 * @author: wangchenyu
 * @date: 2018-02-27 17:26
 */
@Component
@Path("/dooolyApp/exclusiveCoupon")
@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DooolyCouponServiceImpl implements DooolyCouponService {
	private static Logger log = LoggerFactory.getLogger(DooolyCouponServiceImpl.class);

	@Autowired
	private HomePageDataServcie homePageDataServcie;

	/**
	 * 查询《专属优惠券》列表，同时根据 isReceived 字段区分是否领取过该券
	 */
	@GET
	@Path(value = "/{userId}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response findExclusiveCoupon(@PathParam("userId") String userId) {
		Long startTime = System.currentTimeMillis();
		log.info("findExclusiveCoupon() userId = {}, startTime = {}", userId, startTime);
		FindExclusiveCouponResponse response = new FindExclusiveCouponResponse();
		try {
			response = homePageDataServcie.findExclusiveCoupon(userId, response);
		} catch (Exception e) {
			log.error("查询《专属优惠券》时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("findExclusiveCoupon() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("findExclusiveCoupon() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}

	/**
	 * 用户领取《专属优惠券》
	 */
	@POST
	@Path(value = "/receive")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response receiveExclusiveCoupon(JSONObject json) {
		Long startTime = System.currentTimeMillis();
		log.info("receiveExclusiveCoupon() json = {}, startTime = {}", json, startTime);
		ReceiveExclusiveCouponResponse response = new ReceiveExclusiveCouponResponse();
		try {
			ReceiveExclusiveCouponRequest request = new ReceiveExclusiveCouponRequest(json);
			response = homePageDataServcie.receiveExclusiveCoupon(request, response);
		} catch (Exception e) {
			log.error("用户领取《专属优惠券》时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("receiveExclusiveCoupon() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("receiveExclusiveCoupon() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}

	/**
	 * 在redis中设置专属优惠券活动id
	 */
	@POST
	@Path(value = "/setActivityId")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response setExclusiveCouponActivityId(JSONObject json) {
		Long startTime = System.currentTimeMillis();
		log.info("setExclusiveCouponActivityId() json = {}, startTime = {}", json, startTime);
		BaseResponse response = new BaseResponse();
		try {
			response = homePageDataServcie.setExclusiveCouponActivityId(json, response);
		} catch (Exception e) {
			log.error("用户领取《专属优惠券》时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("setExclusiveCouponActivityId() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("setExclusiveCouponActivityId() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}

	/**
	 * 发现列表页，点击图片跳转后，获取优惠券活动表信息
	 */
	@GET
	@Path(value = "CouponActivity/{activityId}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response getAdCouponActivityInfos(@PathParam("activityId") String activityId) {
		Long startTime = System.currentTimeMillis();
		log.info("getAdCouponActivityInfos() activityId = {}, startTime = {}", activityId, startTime);
		GetAdCouponActivityInfosResponse response = new GetAdCouponActivityInfosResponse();
		try {
			response = homePageDataServcie.getAdCouponActivityInfos(activityId, response);
		} catch (Exception e) {
			log.error("查询《优惠券活动》信息时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("getAdCouponActivityInfos() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("getAdCouponActivityInfos() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}
}
