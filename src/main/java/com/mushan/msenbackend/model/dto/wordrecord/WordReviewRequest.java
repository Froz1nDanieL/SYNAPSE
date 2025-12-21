package com.mushan.msenbackend.model.dto.wordrecord;

import lombok.Data;

import java.io.Serializable;

/**
 * 单词复习提交请求
 * 用于提交复习结果
 */
@Data
public class WordReviewRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学习记录ID
     */
    private Long recordId;

    /**
     * 是否正确（true/false）
     */
    private Boolean isCorrect;
}
