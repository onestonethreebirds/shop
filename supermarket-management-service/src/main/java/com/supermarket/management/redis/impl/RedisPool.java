package com.supermarket.management.redis.impl;

import com.supermarket.management.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisPool implements RedisUtils {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 保存
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        Jedis jedis = this.jedisPool.getResource();

        jedis.set(key, value);
        jedis.close();

    }

    /**
     * 根据key查询
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        Jedis jedis = this.jedisPool.getResource();

        String result = jedis.get(key);
        jedis.close();

        return result;

    }

    /**
     * 删除
     *
     * @param key
     */
    @Override
    public void del(String key) {
        Jedis jedis = this.jedisPool.getResource();

        jedis.del(key);
        jedis.close();

    }

    /**
     * 根据key设置生存时间
     *
     * @param key
     * @param seconds
     */
    @Override
    public void expire(String key, Integer seconds) {
        Jedis jedis = this.jedisPool.getResource();

        jedis.expire(key, seconds);
        jedis.close();

    }

    /**
     * 保存并设置生存时间
     *
     * @param key
     * @param value
     * @param seconds
     */
    @Override
    public void set(String key, String value, Integer seconds) {
        Jedis jedis = this.jedisPool.getResource();

        jedis.set(key, value);
        jedis.expire(key, seconds);
        jedis.close();

    }

    /**
     * value加一
     *
     * @param key
     * @return
     */
    @Override
    public Long incr(String key) {
        Jedis jedis = this.jedisPool.getResource();

        Long count = jedis.incr(key);
        jedis.close();

        return count;

    }
}
