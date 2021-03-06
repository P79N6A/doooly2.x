package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.business.HotBusinessServiceI;
import com.doooly.business.mypoint.service.MyPointServiceI;
import com.doooly.common.annotation.UserToken;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.common.dto.BaseReq;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.HotBusinessRestServiceI;
import com.easy.mq.client.RocketClient;
import com.easy.mq.result.RocketProducerMessage;
import com.reach.constrant.Group;
import com.reach.constrant.Topic;
import com.reach.dto.UserEventReq;
import com.reach.enums.EventType;
import com.reach.redis.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 商家
 * @author:
 * @date: 2017-05-18
 */
@Component
@Path("/hotBusiness")
public class HotBusinessRestService implements HotBusinessRestServiceI {
	/**
	 * app首页和热门商户页面
	 * 
	 */
	private static Logger logger = LoggerFactory.getLogger(HotBusinessRestService.class);
	@Autowired
	private HotBusinessServiceI hotBusinessServiceI;
	@Autowired
	private MyPointServiceI myPointServiceI;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Resource(name = "rocketClient")
	private RocketClient rocketClient;

	@POST
	@Path(value = "/index")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String index(JSONObject obj, @Context HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		map.put("groupId", request.getHeader("groupId"));
		map.put("userId", obj.getInteger("userId").toString());
		map.put("type", obj.getInteger("type").toString());

		logger.info("index === groupId:" + request.getHeader("groupId"));

		// 没有传这个值得接口
//		map.put("adType", obj.getInteger("adType") != null ? obj.getInteger("adType").toString() : null);
		MessageDataBean messageDataBean = hotBusinessServiceI.getIndexData(map);

		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/hotMerchat")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String hotMerchat(JSONObject obj) {
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		String address = obj.getString("address");
		Integer type = obj.getInteger("type");
		Integer shopType = obj.getInteger("shopType");
		logger.info("热门商家的地域信息为=============" + address);
		MessageDataBean messageDataBean = hotBusinessServiceI.getHotMerchatData(userId, address, type, shopType);

		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	/**
	 * 获取商户数据
	 * 
	 */
	@POST
	@Path(value = "/hotDatas")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String hotDatas(JSONObject obj) {
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		String address = obj.getString("address");
		Integer currentPage = obj.getInteger("currentPage");
		Integer pageSize = obj.getInteger("pageSize");
		String type = obj.getString("type");
		Integer shopType = obj.getInteger("shopType");
		logger.info("ajax查询商家的地域信息为=============" + address);
		MessageDataBean messageDataBean = hotBusinessServiceI.getHotDatas(userId, address, currentPage, pageSize, type,
				shopType);

		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	/**
	 * 获取列表数据
	 * 
	 */
	@POST
	@Path(value = "/dictDatas")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String dictDatas(JSONObject obj) {
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		String address = obj.getString("address");
		logger.info("ajax查询商家的地域信息为=============" + address);
		MessageDataBean messageDataBean = hotBusinessServiceI.getDictDatas(userId, address);

		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/businessInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getBusinessInfo(JSONObject json, @Context HttpServletRequest request) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Long adBusinessId = json.getLong("adBusinessId");
			String token = json.getString(ConstantsLogin.TOKEN);
			if (StringUtils.isBlank(token)) {
			    token = request.getHeader(ConstantsLogin.TOKEN);
            }
			messageDataBean = hotBusinessServiceI.getBusinessInfo(adBusinessId, token);

			String userId = String.valueOf(stringRedisTemplate.boundValueOps(token).get());
			RocketProducerMessage message = new RocketProducerMessage();
			message.setTopic(Topic.ACTION_TOPIC);
			message.setGroup(Group.ACTION_GROUP);
			UserEventReq req = new UserEventReq();
			req.setEvent(EventType.VISIT_BUSI);
			if(request.getHeader("deviceNo") != null) {
				req.setDeviceId(request.getHeader("deviceNo"));
			}else if(request.getHeader("deviceId") != null){
				req.setDeviceId(request.getHeader("deviceId"));
			} else {
			    req.setDeviceId(userId);
            }
			req.setUserId(userId);
			req.setBusinessId(adBusinessId + "");
			req.setChannel(request.getHeader("appSource"));
			message.setBody(SerializationUtils.serialize(req));
			rocketClient.send(message, true);
			logger.info("businessInfo action data:{}", JsonUtil.bean2Json(req));

			// logger.info(messageDataBean.toJsonString());
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	/***
	 * 原来老的2.0可以用积分服务
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/businessServiceData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String businessServiceData(BaseReq<JSONObject> json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
			logger.info(JSON.toJSONString(json));
			JSONObject jsonObject = json.getParams();
			Long userId = jsonObject.getLong("userId");
			messageDataBean = hotBusinessServiceI.getBusinessServiceData(userId);
			logger.info(messageDataBean.toJsonString());
			logger.info("商家服务列表时间" + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode() + "");
			messageDataBean.setMess(SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}

	/**
	 * 新的2.1可以用积分服务
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/businessServiceDataV21")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@UserToken
	public String businessServiceDataV21(JSONObject json, @Context HttpServletRequest request) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
			logger.info(JSON.toJSONString(json));
			Long userId = json.getLong("userId");
			messageDataBean = hotBusinessServiceI.getBusinessServiceDataV21(userId);
			logger.info(messageDataBean.toJsonString());
			logger.info("商家服务列表时间" + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode() + "");
			messageDataBean.setMess(SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}

	/***
	 * 2.1可以用积分服务-商家详情
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/businessInfoService")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String businessInfoService(JSONObject params, @Context HttpServletRequest request) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String channel = request.getHeader(ConstantsLogin.CHANNEL);
			params.put(ConstantsLogin.CHANNEL, channel);
			logger.info("====【businessInfoService】-params:" + params.toJSONString());

			messageDataBean = hotBusinessServiceI.getBusinessInfoService(params);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess(MessageDataBean.failure_mess);
		}
		return messageDataBean.toJSONString();
	}
	
	/***
	 * 用户预返积分
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/userReturnPoints")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String userReturnPoints(JSONObject params, @Context HttpServletRequest request) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String channel = request.getHeader(ConstantsLogin.CHANNEL);
			params.put(ConstantsLogin.CHANNEL, channel);
			logger.info("====【businessInfoService】-params:" + params.toJSONString());
			messageDataBean = myPointServiceI.getUserReturnPoints(params.getLong("userId"));
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess(MessageDataBean.failure_mess);
		}
		return messageDataBean.toJsonString();
	}
	
	/***
	 * 领取预返积分
	 * 
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/receiveUserReturnPoints")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String receiveUserReturnPoints(JSONObject params, @Context HttpServletRequest request) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			String channel = request.getHeader(ConstantsLogin.CHANNEL);
			params.put(ConstantsLogin.CHANNEL, channel);
			logger.info("====【businessInfoService】-params:" + params.toJSONString());
			messageDataBean = myPointServiceI.receiveUserReturnPoints(params.getLong("userId"));
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess(MessageDataBean.failure_mess);
		}
		return messageDataBean.toJsonString();
	}
}
