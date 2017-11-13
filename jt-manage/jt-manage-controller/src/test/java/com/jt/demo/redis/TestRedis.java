package com.jt.demo.redis;

import org.junit.Test;
import redis.clients.jedis.*;
import redis.clients.util.ShardInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestRedis {
//    @Test
//    public void test01() {
//        //通过ip和端口号进行连接
//        Jedis jedis = new Jedis("192.168.159.134", 6380);
//        //先通过jedis为redis赋值
//        String result = jedis.set("1707", "快要毕业了");
//        System.out.println(result);
//        String data = jedis.get("1707");
//        System.out.println(data);
//    }
//
//    //实现分片的操作
//    @Test
//    public void test02() {
//
//
//        //定义分片的集合
//        List<JedisShardInfo> infoList = new ArrayList<JedisShardInfo>();
//        infoList.add(new JedisShardInfo("192.168.159.134", 6379));
//        infoList.add(new JedisShardInfo("192.168.159.134", 6380));
//        infoList.add(new JedisShardInfo("192.168.159.134", 6381));
//
//
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(100);
//        //定义jedis连接池
//        ShardedJedisPool jedisPool = new ShardedJedisPool(poolConfig, infoList);
//        ShardedJedis jedis = jedisPool.getResource();
//        String result = jedis.get("name");
//        System.out.println("redis分片的结果为:" + result);
//    }
//
//    @Test
//    public void testSen() {
//        Set<String> sentinels = new HashSet<String>();
//
//        sentinels.add(new HostAndPort("192.168.159.134", 26379).toString());
//        sentinels.add(new HostAndPort("192.168.159.134", 26380).toString());
//        sentinels.add(new HostAndPort("192.168.159.134", 26381).toString());
//
//        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
//
//        Jedis jedis = pool.getResource();
//        jedis.set("name", "1707班");
//        System.out.println(jedis.get("name"));
//    }
}
