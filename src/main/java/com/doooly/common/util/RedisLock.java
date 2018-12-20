package com.doooly.common.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component("redisLockf")
@Scope(value = "prototype")
public class RedisLock {
    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);
    @Autowired
    private RedisTemplate redisTemplate;
    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;
    private int expireMsecs = '\uea60';
    private int timeoutMsecs = 10*1000;
    private volatile boolean locked = false;

    public RedisLock() {
    }

    public String get(final String key) {
        Object obj = null;
        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = connection.get(serializer.serialize(key));
                    connection.close();
                    return data == null?null:serializer.deserialize(data);
                }
            });
        } catch (Exception var4) {
            logger.error("get redis error, key : {}", key);
        }

        return obj != null?obj.toString():null;
    }

    public boolean setNX(final String key, final String value) {
        return this.setNX(key, value, 100);
    }

    public boolean setNX(final String key, final String value, final long timeout) {
        Object obj = null;
        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                    connection.expire(serializer.serialize(key), timeout);
                    connection.close();
                    return success;
                }
            });
        } catch (Exception var5) {
            logger.error("setNX redis error, key : {}", key);
        }
        return obj != null?((Boolean)obj).booleanValue():false;
    }

    private String getSet(final String key, final String value) {
        Object obj = null;

        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] ret = connection.getSet(serializer.serialize(key), serializer.serialize(value));
                    connection.close();
                    return serializer.deserialize(ret);
                }
            });
        } catch (Exception var5) {
            logger.error("setNX redis error, key : {}", key);
        }

        return obj != null?(String)obj:null;
    }

    public boolean lock(final String lockKey,final long timeOutMills) {
    	Object obj = null;
        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Boolean success = connection.setNX(serializer.serialize(lockKey), serializer.serialize(""));
                    if(success) {
                    	connection.expire(serializer.serialize(lockKey), timeOutMills);
                    }
                    connection.close();
                    return success;
                }
            });
        } catch (Exception var5) {
            logger.error("setNX redis error, key : {}", lockKey);
        }
        return obj != null?((Boolean)obj).booleanValue():false;
    }



    public synchronized boolean lock_v2(String lockKey,long timeOutMills) {
        long timeout = this.timeoutMsecs;
        if (timeOutMills > 0) {
            timeout = timeOutMills;
        }
        while(timeout >= 0) {
            long expires = System.currentTimeMillis() + (long)this.expireMsecs + 1L;
            String expiresStr = String.valueOf(expires);
            if(this.setNX(lockKey, expiresStr)) {
                this.locked = true;
                return true;
            }

            String currentValueStr = this.get(lockKey);
            if(currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                String oldValueStr = this.getSet(lockKey, expiresStr);
                if(oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    this.locked = true;
                    return true;
                }
            }

            timeout -= 100;

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var8) {
                var8.printStackTrace();
            }
        }

        return false;
    }

    public void unlock(final String lockKey) {
       /* if(this.locked) {
            this.redisTemplate.delete(lockKey);
            this.locked = false;
        }*/
    	Object obj = null;

        try {
            obj = this.redisTemplate.execute(new RedisCallback<Object>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Long data=connection.del(serializer.serialize(lockKey));
                    connection.close();
                    return data;
                }
            });
        } catch (Exception var4) {
            logger.error("get redis error, key : {}", lockKey);
        }
    }


    public synchronized void unlock_v2(String lockKey) {
        if(this.locked) {
            this.redisTemplate.delete(lockKey);
            this.locked = false;
        }
    }
}

