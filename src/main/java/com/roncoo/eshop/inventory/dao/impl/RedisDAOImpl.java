package com.roncoo.eshop.inventory.dao.impl;

import com.roncoo.eshop.inventory.dao.RedisDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

@Repository("redisDAO")
public class RedisDAOImpl implements RedisDAO {

//    @Resource
//    private JedisCluster jedisCluster;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
//        jedisCluster.set(key,value);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
//        return jedisCluster.get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }


}
