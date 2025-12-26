package com.mushan.msenbackend.model.dto.article;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新文章请求
 */
@Data
public class ArticleUpdateRequest implements Serializable {

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
     * 来源
     */
    private String source;

    /**
     * 发布时间
     */
    private Date publishTime;

    private static final long serialVersionUID = 1L;
}
