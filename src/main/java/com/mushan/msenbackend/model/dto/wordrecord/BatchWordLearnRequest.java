package com.mushan.msenbackend.model.dto.wordrecord;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量提交单词学习请求
 */
@Data
public class BatchWordLearnRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 词书类型
     */
    private String wordType;

    /**
     * 学习记录列表
     */
    private List<WordLearnRequest> learnRecords;
}
