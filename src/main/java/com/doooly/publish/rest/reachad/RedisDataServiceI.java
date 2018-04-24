package com.doooly.publish.rest.reachad;

import com.alibaba.fastjson.JSONObject;

public interface RedisDataServiceI {
	/**
	 * 存储redis data
	 * 
	 * @param paraJSON
	 * @return
	 */
	JSONObject pushDataToRedis(JSONObject paramJSON);

	/**
	 * 获取redis data
	 * 
	 * @param paraJSON
	 * @return
	 */
	JSONObject popDataToRedis(JSONObject paramJSON);

	/**
	 * 获取redis data总量
	 * 
	 * @param paraJSON
	 * @return
	 */
	JSONObject totalDataOfRedis(JSONObject paramJSON);

	/**
	 * key删除redis data
	 * 
	 * @param paraJSON
	 * @return
	 */
	JSONObject deleteRedisData(JSONObject paramJSON);

	/**
	 * 持久化redis data 更新数据库
	 * 
	 * @param paraJSON
	 * @return
	 */
	JSONObject persistRedisData(JSONObject paramJSON);
}
