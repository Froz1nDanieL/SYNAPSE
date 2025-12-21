package com.mushan.msenbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学习计划VO
 */
@Data
public class LearnPlanVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 词书类型
     */
    private String wordType;

    /**
     * 词书名称
     */
    private String wordTypeName;

    /**
     * 每日新词目标量
     */
    private Integer dailyNewCount;

    /**
     * 当前学习进度（已学单词数）
     */
    private Integer currentProgress;

    /**
     * 计划状态（0暂停/1启用）
     */
    private Integer planStatus;

    /**
     * 词书总单词数
     */
    private Long totalWordCount;

    /**
     * 学习进度百分比
     */
    private Double progressPercent;

    /**
     * 预计完成天数
     */
    private Integer estimatedDays;

    /**
     * 计划开始日期
     */
    private Date startDate;

    /**
     * 创建时间
     */
    private Date createTime;
}
