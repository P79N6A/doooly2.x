package com.doooly.common.redis;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 */
public interface Cache<K, V> {

    void put(K key, V value);

    void put(K key, V value, long expired);

    V get(K key);

    boolean exists(K key);

    long size();

    void del(K key);

    void del(K... keys);

    void clear();

    long ttl(K key);

    void sadd(K key, V value);

    boolean sismember(K key, V value);

    Set<V> smembers(K key);

    void rpush(K key, V value);

    List<V> lrange(K key, int start, int end);

    long lsize(K key);
}
