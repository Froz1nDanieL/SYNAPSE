package com.mushan.msenbackend.config;

import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * 基于Hint的分表算法
 * 支持通过HintManager强制路由到指定表
 */
@Component
public class TagHintShardingAlgorithm implements HintShardingAlgorithm<String> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<String> shardingValue) {
        // 获取Hint指定的分片值
        Collection<String> values = shardingValue.getValues();
        if (values == null || values.isEmpty()) {
            // 如果没有指定Hint值，返回所有表（全表扫描）
            return availableTargetNames;
        }

        // 取第一个Hint值作为wordType
        String wordType = values.iterator().next();
        
        // 根据wordType路由到对应的物理表
        String targetTable = getTableNameByWordType(wordType);
        
        // 检查目标表是否在可用表中
        if (availableTargetNames.contains(targetTable)) {
            return Collections.singleton(targetTable);
        }
        
        // 如果目标表不存在，返回第一个可用表
        return Collections.singleton(availableTargetNames.iterator().next());
    }

    @Override
    public String getType() {
        return "TAG_HINT";
    }

    /**
     * 根据词书类型获取物理表名
     */
    private String getTableNameByWordType(String wordType) {
        if (wordType == null || wordType.isEmpty()) {
            return "engdict_cet4";
        }

        return switch (wordType) {
            case "cet4" -> "engdict_cet4";
            case "cet6" -> "engdict_cet6";
            case "zk" -> "engdict_zk";
            case "gk" -> "engdict_gk";
            case "ky" -> "engdict_ky";
            case "ielts" -> "engdict_ielts";
            case "toefl" -> "engdict_toefl";
            case "gre" -> "engdict_gre";
            default -> "engdict_cet4";
        };
    }
}
