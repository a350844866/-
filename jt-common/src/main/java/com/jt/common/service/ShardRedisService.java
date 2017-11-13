package com.jt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service //将对象交给spring管理
public class ShardRedisService {
    @Autowired(required = false) //需要才进行注入操作
    private ShardedJedisPool shardedJedisPool;

    public String set(String key, String value) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String result = shardedJedis.set(key, value);
        //将数据返回给连接池
        shardedJedisPool.returnResource(shardedJedis);
        return result;
    }


    public String get(String key) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String value = shardedJedis.get(key);
        //将连接还回连接池
        shardedJedisPool.returnResource(shardedJedis);
        return value;
    }
}
