package com.doooly.publish.rest.reachad.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.redisUtil.RedisUtilService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.publish.rest.reachad.RedisDataServiceI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/redis")
public class RedisDataService implements RedisDataServiceI {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private RedisUtilService redisUtilService;

	/** 存储缓存code数据 */
	@SuppressWarnings("unchecked")
	@POST
	@Path(value = "/pushDataToRedis")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject pushDataToRedis(JSONObject paramJSON) {
		JSONObject result = new JSONObject();
		try {
			// redis 兑换码key
			String codeKey = paramJSON.getString("codeKey").toString();
			// redis 兑换码key
			List<String> codeList = (List<String>) JSONObject.parse(paramJSON.getString("codeList").toString());
			if (redisUtilService.PushDataToRedis(codeKey, codeList)) {
				result.put("code", MessageDataBean.success_code);
				log.info("pushDataToRedis success=" + result.toJSONString());
			} else {
				result.put("code", MessageDataBean.failure_code);
				log.info("pushDataToRedis success=" + result.toJSONString());
			}
		} catch (Exception e) {
			log.error(e);
			result.put("code", MessageDataBean.failure_code);
			result.put("info", "pushDataToRedis=" + e.getMessage());
		}
		return result;
	}

	/** 读取code缓存数据 */
	@POST
	@Path(value = "/popDataFromRedis")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject popDataToRedis(JSONObject paramJSON) {
		JSONObject result = new JSONObject();
		try {
			log.info("popDataFromRedis=paramJSON:{" + paramJSON.toJSONString() + "}");
			// redis 兑换码key
			String codeKey = paramJSON.getString("codeKey").toString();
			// redis 兑换码数量
			Integer codeCount = Integer.valueOf(paramJSON.getInteger("codeCount"));
			List<String> codeList = redisUtilService.PopDataFromRedis(codeKey, codeCount);
			result.put("code", MessageDataBean.success_code);
			result.put("codeList", JSONObject.toJSONString(codeList));
			log.info("popDataFromRedis success=" + result.toJSONString());
		} catch (Exception e) {
			log.error(e);
			result.put("code", MessageDataBean.failure_code);
			result.put("info", "popDataFromRedis=" + e.getMessage());
		}
		return result;
	}

	/** 获取code缓存数据总数 */
	@POST
	@Path(value = "/totalDataOfRedis")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject totalDataOfRedis(JSONObject paramJSON) {
		JSONObject result = new JSONObject();
		try {
			// redis 兑换码key
			String codeKey = paramJSON.getString("codeKey").toString();
			Long codeTotalCount = redisUtilService.totalDataOfRedis(codeKey);
			result.put("code", MessageDataBean.success_code);
			result.put("codeTotalCount", codeTotalCount);
			log.info("totalDataOfRedis success=" + result.toJSONString());
		} catch (Exception e) {
			log.error(e);
			result.put("code", MessageDataBean.failure_code);
			result.put("info", "totalDataOfRedis=" + e.getMessage());
		}
		return result;
	}

	/** 删除key缓存数据 */
	@POST
	@Path(value = "/deleteRedisData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject deleteRedisData(JSONObject paramJSON) {
		JSONObject result = new JSONObject();
		try {
			// redis 兑换码key
			String codeKey = paramJSON.getString("codeKey").toString();
			redisUtilService.deleteRedisData(codeKey);
			result.put("code", MessageDataBean.success_code);
			log.info("deleteRedisData success=" + result.toJSONString());
		} catch (Exception e) {
			log.error(e);
			result.put("code", MessageDataBean.failure_code);
			result.put("info", "deleteRedisData=" + e.getMessage());
		}
		return result;
	}

	@Override
	public JSONObject persistRedisData(JSONObject paramJSON) {
		return null;
	}

}
