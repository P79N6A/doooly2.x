package com.doooly.common.redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 */
public abstract class BaseRedisMg<K, V> implements Cache<K, V>{


    private static final Logger LOG = LoggerFactory.getLogger(BaseRedisMg.class);

    @Autowired
    protected RedisTemplate<K, V> redisTemplate;

    @Override
    public void put(K key, V value) {
        try {
            BoundValueOperations<K, V> valueOperations = redisTemplate.boundValueOps(key);
            valueOperations.set(value);
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }

    }

    /**
     * Redis 缓存时间单位默认设置为分钟
     *
     * @param key
     * @param value
     * @param expired
     */
    @Override
    public void put(K key, V value, long expired) {
        try {
            BoundValueOperations<K, V> valueOperations = redisTemplate.boundValueOps(key);
            if (expired > 0)
                valueOperations.set(value, expired, TimeUnit.MINUTES);
            else
                valueOperations.set(value);
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }

    }

    @Override
    public V get(final K key) {
        try {
            BoundValueOperations<K, V> valueOperations = redisTemplate.boundValueOps(key);
            return valueOperations.get();
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return null;
    }

    public Set<K> getKeys(K pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public boolean exists(K key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public long size() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    @Override
    public void del(K key) {
        if (exists(key))
            redisTemplate.delete(key);
    }

    @Override
    public void del(K... keys) {
        if (keys.length > 0) {
            for (K key : keys) {
                del(key);
            }
        }
    }

    @Override
    public void clear() {
        //TODO
    }

    @Override
    public long ttl(K key) {
        return redisTemplate.getExpire(key);
    }

    public RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void sadd(K key, V value) {
        try {
            BoundSetOperations<K, V> boundSetOperations = redisTemplate.boundSetOps(key);
            boundSetOperations.add(value);
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
    }

    @Override
    public boolean sismember(K key, V value) {
        try {
            BoundSetOperations<K, V> boundSetOperations = redisTemplate.boundSetOps(key);
            return boundSetOperations.isMember(value);
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return false;
    }

    @Override
    public Set<V> smembers(K key) {
        try {
            BoundSetOperations<K, V> boundSetOperations = redisTemplate.boundSetOps(key);
            return boundSetOperations.members();
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public void rpush(K key, V value) {
        try {
            BoundListOperations<K, V> boundListOperations = redisTemplate.boundListOps(key);
            boundListOperations.rightPush(value);

        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
    }

    @Override
    public List<V> lrange(K key, int start, int end) {
        try {
            BoundListOperations<K, V> boundListOperations = redisTemplate.boundListOps(key);
            return boundListOperations.range(start, end);
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public long lsize(K key) {
        try {
            BoundListOperations<K, V> boundListOperations = redisTemplate.boundListOps(key);
            return boundListOperations.size();
        }catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return 0;
    }
}
