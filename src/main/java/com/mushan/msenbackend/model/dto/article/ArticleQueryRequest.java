package com.mushan.msenbackend.model.dto.article;

import com.mushan.msenbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文章查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryRequest extends PageRequest implements Serializable {

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 难度等级(1-简单 2-中等 3-困难)
     */
    private Integer difficulty;

    /**
     * 分类(科技/文化/新闻等)
     */
    private String category;

    private static final long serialVersionUID = 1L;
}
