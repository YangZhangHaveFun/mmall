package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RedissonManager {
    private Config config = new Config();

    private Redisson redisson = null;

    private static String redis1Ip = PropertiesUtil.getProperty("redis_1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis_1.port"));
    private static String redis2Ip = PropertiesUtil.getProperty("redis_2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis_2.port"));

    public Redisson getRedisson() {
        return redisson;
    }

    @PostConstruct
    private void init(){
        config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());

        try {
            redisson = (Redisson) Redisson.create(config);
            log.info("Redission init successfully.");
        } catch (Exception e) {
            log.error("Redission init failed.", e);
            e.printStackTrace();
        }
    }
}
