package com.mushan.msenbackend.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 词书类型枚举
 */
@Getter
public enum WordTypeEnum {

    CET4("cet4", "四级词书", "CET-4核心词汇", 4500),
    CET6("cet6", "六级词书", "CET-6核心词汇", 5500),
    ZK("zk", "中考词书", "中考必备词汇", 1600),
    GK("gk", "高考词书", "高考核心词汇", 3500),
    KY("ky", "考研词书", "考研核心词汇", 5500),
    IELTS("ielts", "雅思词书", "雅思高频词汇", 8000),
    TOEFL("toefl", "托福词书", "托福核心词汇", 8000),
    GRE("gre", "GRE词书", "GRE核心词汇", 12000);

    /**
     * 类型标识
     */
    private final String type;

    /**
     * 词书名称
     */
    private final String name;

    /**
     * 词书描述
     */
    private final String description;

    /**
     * 预估单词数量
     */
    private final int estimatedWordCount;

    WordTypeEnum(String type, String name, String description, int estimatedWordCount) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.estimatedWordCount = estimatedWordCount;
    }

    /**
     * 根据类型获取枚举
     */
    public static WordTypeEnum getEnumByType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        for (WordTypeEnum wordTypeEnum : WordTypeEnum.values()) {
            if (wordTypeEnum.getType().equals(type)) {
                return wordTypeEnum;
            }
        }
        return null;
    }

    /**
     * 获取所有词书类型列表
     */
    public static List<String> getAllTypes() {
        return Arrays.stream(WordTypeEnum.values())
                .map(WordTypeEnum::getType)
                .collect(Collectors.toList());
    }

    /**
     * 判断类型是否有效
     */
    public static boolean isValidType(String type) {
        return getEnumByType(type) != null;
    }
}