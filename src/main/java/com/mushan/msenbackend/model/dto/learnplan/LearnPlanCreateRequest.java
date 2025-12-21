package com.mushan.msenbackend.model.dto.learnplan;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建学习计划请求
 */
@Data
public class LearnPlanCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 词书类型（cet4/cet6/zk/gk/ky/ielts/toefl）
     */
    private String wordType;

    /**
     * 每日新词目标量（默认50，范围20-100）
     */
    private Integer dailyNewCount;
}
