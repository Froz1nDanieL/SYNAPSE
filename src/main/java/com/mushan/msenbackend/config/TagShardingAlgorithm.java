package com.mushan.msenbackend.config;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

/**
 * 根据tag标签进行分表的算法
 * 支持的标签包括：cet4, cet6, zk, gk, ky, ielts, toefl, gre
 */
@Component
public class TagShardingAlgorithm implements StandardShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String tag = shardingValue.getValue();
        if (tag == null || tag.isEmpty()) {
            // 如果tag为空，默认使用engdict表
            return getDefaultTableName(availableTargetNames);
        }

        // 检查tag中包含的关键字并路由到对应表
        if (tag.contains("cet4")) {
            return "engdict_cet4";
        } else if (tag.contains("cet6")) {
            return "engdict_cet6";
        } else if (tag.contains("zk")) {
            return "engdict_zk";
        } else if (tag.contains("gk")) {
            return "engdict_gk";
        } else if (tag.contains("ky")) {
            return "engdict_ky";
        } else if (tag.contains("ielts")) {
            return "engdict_ielts";
        } else if (tag.contains("toefl")) {
            return "engdict_toefl";
        } else if (tag.contains("gre")) {
            return "engdict_gre";
        } else {
            // 如果没有匹配的标签，使用默认表
            return getDefaultTableName(availableTargetNames);
        }
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> shardingValue) {
        // 对于范围查询，返回所有可用的表
        return availableTargetNames;
    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }

    /**
     * 获取默认表名
     * @param availableTargetNames 可用的目标表名集合
     * @return 默认表名
     */
    private String getDefaultTableName(Collection<String> availableTargetNames) {
        // 查找是否有原始表engdict
        Optional<String> defaultTable = availableTargetNames.stream()
                .filter(name -> name.equals("engdict"))
                .findFirst();
        
        // 如果有原始表则使用原始表，否则使用第一个表
        return defaultTable.orElseGet(() -> availableTargetNames.iterator().next());
    }
}