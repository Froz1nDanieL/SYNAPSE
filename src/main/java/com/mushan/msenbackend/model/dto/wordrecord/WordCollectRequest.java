package com.mushan.msenbackend.model.dto.wordrecord;

import lombok.Data;

import java.io.Serializable;

/**
 * 单词收藏请求
 */
@Data
public class WordCollectRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学习记录ID（如果已有学习记录）
     */
    private Long recordId;

    /**
     * 单词ID（如果没有学习记录，通过wordId和wordType创建）
     */
    private Integer wordId;

    /**
     * 词书类型（如果没有学习记录，通过wordId和wordType创建）
     */
    private String wordType;

    /**
     * 是否收藏（0取消收藏/1收藏）
     */
    private Integer isCollect;
}
