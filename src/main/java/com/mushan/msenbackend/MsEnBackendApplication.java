package com.mushan.msenbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableAsync
@MapperScan("com.mushan.msenbackend.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 2592000) // 30天
public class MsEnBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEnBackendApplication.class, args);
    }

}