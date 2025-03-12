package org.redis;

import org.redis.impl.RedisMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        RedisMap redisMap = new RedisMap(new JedisPool("localhost"));
        redisMap.put("1","2");
        System.out.println(redisMap.size());;
    }
}