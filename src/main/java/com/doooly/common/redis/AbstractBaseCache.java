package com.doooly.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis 缓存基类
 * @author 清江
 * @date 2016-7-10
 * @version 1.0
 */
public abstract class AbstractBaseCache<K,V> {

	@Autowired
	protected RedisTemplate<K, V> redisTemplate;
	
	/**
	 * 设置redis模板
	 * @param redisTemplate
	 */
	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate){
		this.redisTemplate = redisTemplate;
	}
	/**
	 * 获取redis 字符串序列化工具
	 * @return
	 */
	public RedisSerializer<String> getStringSerializer(){
		return redisTemplate.getStringSerializer();
	}
	
}
