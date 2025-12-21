package com.mushan.msenbackend.config;

import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ShardingSphere配置类
 */
@Configuration
public class ShardingSphereConfig {

    @Bean
    public StandardShardingAlgorithm<String> tagShardingAlgorithm() {
        return new TagShardingAlgorithm();
    }
}