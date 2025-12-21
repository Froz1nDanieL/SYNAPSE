package com.mushan.msenbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 单词卡片VO
 * 用于展示学习/复习时的单词信息
 */
@Data
public class WordCardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单词ID
     */
    private Integer wordId;

    /**
     * 学习记录ID（复习时使用，新词学习时为null）
     */
    private Long recordId;

    /**
     * 单词
     */
    private String word;

    /**
     * 音标
     */
    private String phonetic;

    /**
     * 英文释义
     */
    private String definition;

    /**
     * 中文释义
     */
    private String translation;

    /**
     * 词性
     */
    private String pos;

    /**
     * 柯林斯星级
     */
    private Integer collins;

    /**
     * 是否牛津核心词汇
     */
    private Integer oxford;

    /**
     * 词形变化（原始字符串）
     */
    private String exchange;

    /**
     * 词形变化（解析后的结构化数据）
     */
    private ExchangeInfo exchangeInfo;

    /**
     * 音频URL
     */
    private String audio;

    /**
     * 词书类型
     */
    private String wordType;

    /**
     * 当前记忆等级（复习时有值）
     */
    private Integer memLevel;

    /**
     * 是否已收藏
     */
    private Integer isCollect;

    /**
     * 复习次数（复习时有值）
     */
    private Integer reviewTimes;

    /**
     * 选词测试选项（4个选项，包含1个正确答案和3个干扰项）
     */
    private List<String> options;

    /**
     * 词形变化信息
     */
    @Data
    public static class ExchangeInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 复数形式 (s:)
         */
        private String plurals;

        /**
         * 过去式 (d:)
         */
        private String pastTense;

        /**
         * 现在分词 (i:)
         */
        private String presentParticiple;

        /**
         * 过去分词 (p:)
         */
        private String pastParticiple;

        /**
         * 第三人称单数 (3:)
         */
        private String thirdPersonSingular;

        /**
         * 比较级 (r:)
         */
        private String comparative;

        /**
         * 最高级 (t:)
         */
        private String superlative;

        /**
         * 原型 (0:)
         */
        private String lemma;
    }
}
