package com.jing;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName:JedisPoolUtils
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/5/30 21:47
 * @Version 1.0
 */
public class JedisPoolUtils {
    private static JedisPool jedisPool;
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100); // 最大的连接数
        jedisPoolConfig.setMinIdle(25);  // 最小的闲时的数量
        jedisPoolConfig.setMaxIdle(50);  // 最大的闲时的数量
        jedisPool=new JedisPool(jedisPoolConfig,"192.168.72.142",6379);
    }
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}
