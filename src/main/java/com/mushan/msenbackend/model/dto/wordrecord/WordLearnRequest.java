package com.mushan.msenbackend.model.dto.wordrecord;

import lombok.Data;

import java.io.Serializable;

/**
 * 单词学习提交请求
 * 用于提交新词学习结果
 */
@Data
public class WordLearnRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单词ID
     */
    private Integer wordId;

    /**
     * 词书类型
     */
    private String wordType;

    /**
     * 第一轮标记：是否认识（0-不认识，1-认识）
     */
    private Integer firstKnow;

    /**
     * 第二轮选词测试：是否答对（必须为true才能提交）
     */
    private Boolean choiceCorrect;

    /**
     * 第二轮选词测试：错误次数（重测累计，0表示一次通过）
     */
    private Integer choiceErrorCount;

    /**
     * 第三轮拼写测试：是否正确（null表示未进行拼写测试）
     */
    private Boolean spellingCorrect;

    /**
     * 是否收藏（0/1）
     */
    private Integer isCollect;
}
