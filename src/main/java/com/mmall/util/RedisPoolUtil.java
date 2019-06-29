package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;

        try{
        jedis = RedisPool.getJedis();
        result = jedis.set(key, value);
        } catch (Exception e) {

        }
    }
}
