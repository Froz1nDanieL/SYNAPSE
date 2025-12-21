package com.mushan.msenbackend.model.vo;

import com.mushan.msenbackend.model.enums.WordTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 词书信息VO
 */
@Data
public class WordBookVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 词书类型标识
     */
    private String type;

    /**
     * 词书名称
     */
    private String name;

    /**
     * 词书描述
     */
    private String description;

    /**
     * 单词总数
     */
    private Long totalWordCount;

    /**
     * 用户已学单词数（未登录为0）
     */
    private Long learnedWordCount;

    /**
     * 学习进度百分比（0-100）
     */
    private Double progressPercent;

    /**
     * 用户是否正在学习该词书
     */
    private Boolean isCurrentBook;

    /**
     * 从枚举创建基础信息
     */
    public static WordBookVO fromEnum(WordTypeEnum wordTypeEnum) {
        WordBookVO vo = new WordBookVO();
        vo.setType(wordTypeEnum.getType());
        vo.setName(wordTypeEnum.getName());
        vo.setDescription(wordTypeEnum.getDescription());
        vo.setTotalWordCount((long) wordTypeEnum.getEstimatedWordCount());
        vo.setLearnedWordCount(0L);
        vo.setProgressPercent(0.0);
        vo.setIsCurrentBook(false);
        return vo;
    }
}
