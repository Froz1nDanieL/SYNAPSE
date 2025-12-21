package com.mushan.msenbackend.model.dto.wordrecord;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量提交单词复习请求
 */
@Data
public class BatchWordReviewRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 复习记录列表
     */
    private List<WordReviewRequest> reviewRecords;
}
