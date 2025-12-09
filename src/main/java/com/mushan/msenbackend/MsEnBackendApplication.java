package com.mushan.msenbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.mushan.msenbackend.mapper")
public class MsEnBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEnBackendApplication.class, args);
    }

}