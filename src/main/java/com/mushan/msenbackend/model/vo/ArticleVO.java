package com.mushan.msenbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章VO
 */
@Data
public class ArticleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章正文
     */
    private String content;

    /**
     * 难度等级(1-简单 2-中等 3-困难)
     */
    private Integer difficulty;

    /**
     * 分类(科技/文化/新闻等)
     */
    private String category;

    /**
     * 字数统计
     */
    private Integer wordCount;

    /**
     * 阅读次数
     */
    private Integer readCount;

    /**
     * 来源
     */
    private String source;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
