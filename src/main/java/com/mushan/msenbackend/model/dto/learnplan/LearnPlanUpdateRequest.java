package com.mushan.msenbackend.model.dto.learnplan;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新学习计划请求
 */
@Data
public class LearnPlanUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划ID
     */
    private Long id;

    /**
     * 词书类型（可选，更换词书时使用）
     */
    private String wordType;

    /**
     * 每日新词目标量（范围20-100）
     */
    private Integer dailyNewCount;

    /**
     * 计划状态（0暂停/1启用）
     */
    private Integer planStatus;
}
