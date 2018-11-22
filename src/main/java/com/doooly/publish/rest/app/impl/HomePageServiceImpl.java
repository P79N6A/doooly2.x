package com.doooly.publish.rest.app.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.home.v2.servcie.HomePageDataServcie;
import com.doooly.common.DooolyResponseStatus;
import com.doooly.dto.base.BaseResponse;
import com.doooly.dto.home.*;
import com.doooly.publish.rest.app.HomePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @className: HomePageService
 * @description: 新版兜礼APP首页，接口处理类
 * @author: wangchenyu
 * @date: 2018-02-12 15:39
 */
@Component
@Path("/dooolyApp/index")
@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HomePageServiceImpl implements HomePageService {
	private static Logger log = LoggerFactory.getLogger(HomePageServiceImpl.class);

	@Autowired
	private HomePageDataServcie homePageDataServcie;

	@POST
	@Path(value = "/user/profile")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response getUserProfile(JSONObject json) {
		Long startTime = System.currentTimeMillis();
		log.info("getUserProfile() json = {}, startTime = {}", json, startTime);
		GetHomePageDataV2Response response = new GetHomePageDataV2Response();
		try {
			GetHomePageDataV2Request request = new GetHomePageDataV2Request(json);
			response = homePageDataServcie.getHomePageDataV2(request, response);
		} catch (Exception e) {
			log.error("获取兜礼APP首页，会员信息概要时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("getUserProfile() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("getUserProfile() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}
<<<<<<< .mine
	@POST
	@Path(value = "/user/profile/v2_2")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response getUserProfileV2_2(JSONObject json) {
		Long startTime = System.currentTimeMillis();
		log.info("getUserProfileV2_2() json = {}, startTime = {}", json, startTime);
		GetHomePageDataV2Response response = new GetHomePageDataV2Response();
		try {
			GetHomePageDataV2Request request = new GetHomePageDataV2Request(json);
			response = homePageDataServcie.getHomePageDataV2_2(request, response);
		} catch (Exception e) {
			log.error("获取兜礼个人中心会员信息概要时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("getUserProfileV2_2() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("getUserProfileV2_2() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}

=======
	
	@POST
	@Path(value = "/user/profile/v2_2")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response getUserProfile2_2(JSONObject json) {
		Long startTime = System.currentTimeMillis();
		log.info("getUserProfile() json = {}, startTime = {}", json, startTime);
		GetHomePageDataV2Response response = new GetHomePageDataV2Response();
		try {
			GetHomePageDataV2Request request = new GetHomePageDataV2Request(json);
			response = homePageDataServcie.getHomePageDataV2(request, response);
		} catch (Exception e) {
			log.error("获取兜礼APP首页，会员信息概要时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("getUserProfile() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("getUserProfile() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}
>>>>>>> .theirs

	@POST
	@Path(value = "/guide/flow")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response getUserAppGuideFlow(JSONObject json) {
		Long startTime = System.currentTimeMillis();
		log.info("getUserAppGuideFlow() json = {}, startTime = {}", json, startTime);
		UserGuideFlowResponse response = new UserGuideFlowResponse();
		try {
			Integer userId = json.getInteger("userId");
			response = homePageDataServcie.getUserAppGuideFlow(userId, response);
		} catch (Exception e) {
			log.error("获取兜礼APP首页，查询用户新手引导完成进度时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("getUserAppGuideFlow() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("getUserAppGuideFlow() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}

	@POST
	@Path(value = "/guide/finish")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response insertOrUpdateUserGuideFinish(JSONObject json) {
		Long startTime = System.currentTimeMillis();
		log.info("insertOrUpdateUserGuideFinish() json = {}, startTime = {}", json, startTime);
		BaseResponse response = new BaseResponse();
		try {
			GuideFinishRequest request = new GuideFinishRequest(json);
			response = homePageDataServcie.insertOrUpdateUserGuideFinish(request, response);
		} catch (Exception e) {
			log.error("获取兜礼APP首页，会员信息概要时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("insertOrUpdateUserGuideFinish() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("insertOrUpdateUserGuideFinish() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}

	/**
	 * 获取企业闪屏（企业app开机启动图片）
	 */
	@GET
	@Path(value = "SplashScreen/{groupId}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Override
	public Response getSplashScreen(@PathParam("groupId") String groupId) {
		Long startTime = System.currentTimeMillis();
		log.info("getSplashScreen() groupId = {}, startTime = {}", groupId, startTime);
		GetSplashScreenResponse response = new GetSplashScreenResponse();
		try {
			response = homePageDataServcie.getSplashScreen(groupId, response);
		} catch (Exception e) {
			log.error("获取兜礼APP开机启动图片链接地址时，程序异常。", e);
			response.setStatus(DooolyResponseStatus.SYSTEM_ERROR);
		} finally {
			log.info("getSplashScreen() response = " + JSONObject.toJSONString(response));
			Long endTime = System.currentTimeMillis();
			log.info("getSplashScreen() endTime = {}, 调用接口总耗时：{}", endTime, (endTime - startTime) + "ms");
		}
		return Response.ok(response).build();
	}
}
