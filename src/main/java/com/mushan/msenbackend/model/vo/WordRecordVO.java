package com.mushan.msenbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户单词学习记录VO
 */
@Data
public class WordRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 单词ID
     */
    private Integer wordId;

    /**
     * 单词
     */
    private String word;

    /**
     * 音标
     */
    private String phonetic;

    /**
     * 中文释义
     */
    private String translation;

    /**
     * 音频URL
     */
    private String audio;

    /**
     * 词书类型
     */
    private String wordType;

    /**
     * 记忆等级（0-5）
     */
    private Integer memLevel;

    /**
     * 首次学习时间
     */
    private Date learnTime;

    /**
     * 下次复习时间
     */
    private Date nextReviewTime;

    /**
     * 复习次数
     */
    private Integer reviewTimes;

    /**
     * 是否收藏
     */
    private Integer isCollect;

    /**
     * 是否已掌握
     */
    private Integer isMastered;
}
