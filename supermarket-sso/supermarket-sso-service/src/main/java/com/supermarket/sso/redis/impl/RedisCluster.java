package com.supermarket.sso.redis.impl;
import com.supermarket.sso.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

public class RedisCluster implements RedisUtils {
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 保存
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        this.jedisCluster.set(key,value);
    }

    /**
     * 根据key查询
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        return this.jedisCluster.get(key);
    }

    /**
     * 删除
     *
     * @param key
     */
    @Override
    public void del(String key) {
this.jedisCluster.del(key);
    }

    /**
     * 根据key设置生存时间
     *
     * @param key
     * @param seconds
     */
    @Override
    public void expire(String key, Integer seconds) {
this.jedisCluster.expire(key,seconds);
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
        this.jedisCluster.set(key, value);
        this.jedisCluster.expire(key, seconds);

    }

    /**
     * value加一
     *
     * @param key
     * @return
     */
    @Override
    public Long incr(String key) {
        Long count = this.jedisCluster.incr(key);
        return count;

    }
}
