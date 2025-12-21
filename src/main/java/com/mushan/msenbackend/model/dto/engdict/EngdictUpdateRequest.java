package com.mushan.msenbackend.model.dto.engdict;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新单词请求
 */
@Data
public class EngdictUpdateRequest implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 单词名称
     */
    private String word;

    /**
     * 音标
     */
    private String phonetic;

    /**
     * 英文释义
     */
    private String definition;

    /**
     * 中文释义
     */
    private String translation;

    /**
     * 词性
     */
    private String pos;

    /**
     * 柯林斯星级
     */
    private Integer collins;

    /**
     * 是否是牛津三千核心词汇
     */
    private Integer oxford;

    /**
     * 标签
     */
    private String tag;

    /**
     * 英国国家语料库词频顺序
     */
    private Integer bnc;

    /**
     * 当代语料库词频顺序
     */
    private Integer frq;

    /**
     * 时态复数等变换
     */
    private String exchange;

    /**
     * 扩展信息
     */
    private String detail;

    /**
     * 音频URL
     */
    private String audio;

    private static final long serialVersionUID = 1L;
}