package com.mushan.msenbackend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学习进度VO
 * 包含今日学习情况和词书总进度
 */
@Data
public class LearnProgressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 词书类型
     */
    private String wordType;

    /**
     * 词书名称
     */
    private String wordTypeName;

    // ===== 今日进度 =====

    /**
     * 今日已学新词数
     */
    private Integer todayNewCount;

    /**
     * 今日计划新词数
     */
    private Integer todayNewTarget;

    /**
     * 今日已复习单词数
     */
    private Integer todayReviewCount;

    /**
     * 今日待复习单词数
     */
    private Integer todayReviewPending;

    // ===== 词书总进度 =====

    /**
     * 已学单词总数
     */
    private Integer learnedCount;

    /**
     * 词书单词总数
     */
    private Long totalWordCount;

    /**
     * 已掌握单词数（memLevel=5）
     */
    private Integer masteredCount;

    /**
     * 收藏单词数
     */
    private Integer collectedCount;

    /**
     * 完成进度百分比（0-100）
     */
    private Double progressPercent;

    /**
     * 预计完成天数
     */
    private Integer estimatedDays;

    /**
     * 计划状态（0暂停/1启用）
     */
    private Integer planStatus;
}
