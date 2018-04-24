package com.doooly.business.redisUtil;

import com.doooly.common.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 2017-12-20 16:51:16 WANG
 */
public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    protected StringRedisTemplate redisTemplate = (StringRedisTemplate)SpringContextHolder.getBean(StringRedisTemplate.class);

    private String lockName;

    private long ACQUIRET_TIMEOUT = 15 * 1000;
    private long EXPIRE_TIME = 30 * 1000;

    private boolean isLocked = false;

    public final static String CREATE_ORDER_LOCKNAME = "CREATE_ORDER";

    public RedisLock(String lockName){
        this.lockName = lockName;
    }

    public boolean tryLock() {
        return tryLock(ACQUIRET_TIMEOUT);
    }


    public boolean tryLock(long acquireTimeout) {
        long end = System.currentTimeMillis() + acquireTimeout;
        String key = "lock:" + lockName;
        while (System.currentTimeMillis() < end) {
            if (redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(end))) {
                isLocked = true;
                logger.info(lockName + " - tryLock success.");
                // 设置锁的有效期，防止因异常情况无法释放锁而造成死锁情况的发生
                redisTemplate.expire(key, EXPIRE_TIME, TimeUnit.MILLISECONDS);
                return isLocked;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info(lockName + " - tryLock failed.");
        return false;
    }

    public void unlock() {
        if(isLocked) {
            isLocked = false;
            redisTemplate.delete("lock:" + lockName);
        }
    }
}
