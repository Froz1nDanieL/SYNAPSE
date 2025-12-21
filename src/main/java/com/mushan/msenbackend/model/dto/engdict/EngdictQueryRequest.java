package com.mushan.msenbackend.model.dto.engdict;

import com.mushan.msenbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 单词查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EngdictQueryRequest extends PageRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}