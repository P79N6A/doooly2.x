package com.doooly.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 */

/**
 * RedisCache操作
 */
public class Jiucache extends BaseRedisMg<String, Object> {
    // 日志记录
    private static final Logger LOGGER = LoggerFactory.getLogger(Jiucache.class);

    /**
     * 将数据放到缓存中
     *
     * @param key
     * @param value
     */
    public void putData(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.boundValueOps(key).set(value);
            redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
           LOGGER.error("将数据放到缓存中异常", e);
        }
    }

    /**
     * 根据KEY获取缓存中的数据
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        try {
            Object str = super.get((String) key);
            return str;
        } catch (Exception e) {
           LOGGER.error("将数据放到缓存中异常", e);
        }
        return null;
    }

}