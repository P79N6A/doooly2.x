package com.doooly.publish.rest.life.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.business.HotBusinessServiceI;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.common.dto.BaseReq;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.life.HotBusinessRestServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	private static Logger logger = Logger.getLogger(HotBusinessRestService.class);
	@Autowired
	private HotBusinessServiceI hotBusinessServiceI;
	@Autowired
	protected StringRedisTemplate redisTemplate;
	@POST
	@Path(value="/index")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String index(JSONObject obj) {
		// 获取用户id
		Integer userId = obj.getInteger("userId");
//		String address = obj.getString("address");
		Integer type = obj.getInteger("type");
//		logger.info("App首页的地域信息为============="+address);
		MessageDataBean messageDataBean = hotBusinessServiceI.getIndexData(userId,type);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value="/hotMerchat")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String hotMerchat(JSONObject obj) {
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		String address = obj.getString("address");
		Integer type = obj.getInteger("type");
        Integer shopType = obj.getInteger("shopType");
		logger.info("热门商家的地域信息为============="+address);
		MessageDataBean messageDataBean = hotBusinessServiceI.getHotMerchatData(userId,address,type,shopType);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	/**
	 * 获取商户数据
	 * 
	 */
	@POST
	@Path(value="/hotDatas")
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
		logger.info("ajax查询商家的地域信息为============="+address);
		MessageDataBean messageDataBean = hotBusinessServiceI.getHotDatas(userId,address,currentPage,pageSize,type,shopType);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}	
	/**
	 * 获取列表数据
	 * 
	 */
	@POST
	@Path(value="/dictDatas")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String dictDatas(JSONObject obj) {
		// 获取用户id
		Integer userId = obj.getInteger("userId");
		String address = obj.getString("address");
		logger.info("ajax查询商家的地域信息为============="+address);
		MessageDataBean messageDataBean = hotBusinessServiceI.getDictDatas(userId, address);
		
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}	
	
	@POST
	@Path(value = "/businessInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getBusinessInfo(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Long adBusinessId = json.getLong("adBusinessId");
			messageDataBean = hotBusinessServiceI.getBusinessInfo(adBusinessId);
//			logger.info(messageDataBean.toJsonString());
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/businessServiceData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String businessServiceData(BaseReq<JSONObject> json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
			logger.info(JSON.toJSONString(json));
			JSONObject jsonObject  = json.getParams();
			Long userId = jsonObject.getLong("userId");
			messageDataBean = hotBusinessServiceI.getBusinessServiceData(userId);
			logger.info(messageDataBean.toJsonString());
			logger.info("商家服务列表时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}


	/***
	 * 保留原来老的接口
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/businessServiceData2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String businessServiceData2(BaseReq<JSONObject> json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
			logger.info(JSON.toJSONString(json));
			JSONObject jsonObject  = json.getParams();
			Long userId = jsonObject.getLong("userId");
			messageDataBean = hotBusinessServiceI.getBusinessServiceData2(userId);
			logger.info(messageDataBean.toJsonString());
			logger.info("商家服务列表时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/businessServiceData1")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String businessServiceData1(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
			logger.info(JSON.toJSONString(json));
			Long userId = json.getLong("userId");
			messageDataBean = hotBusinessServiceI.getBusinessServiceData(userId);
			logger.info(messageDataBean.toJsonString());
			logger.info("商家服务列表时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}
}
