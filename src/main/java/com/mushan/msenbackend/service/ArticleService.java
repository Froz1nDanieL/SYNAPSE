package com.mushan.msenbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mushan.msenbackend.model.dto.article.ArticleQueryRequest;
import com.mushan.msenbackend.model.entity.Article;
import com.mushan.msenbackend.model.vo.ArticleVO;

import java.util.List;

/**
 * @description 针对表【article(文章表)】的数据库操作Service
 */
public interface ArticleService extends IService<Article> {

    /**
     * 获取查询条件
     *
     * @param articleQueryRequest 查询请求参数
     * @return 查询条件
     */
    QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest);

    /**
     * 获取文章VO
     *
     * @param article 文章实体
     * @return 文章VO
     */
    ArticleVO getArticleVO(Article article);

    /**
     * 获取文章VO列表
     *
     * @param articleList 文章实体列表
     * @return 文章VO列表
     */
    List<ArticleVO> getArticleVOList(List<Article> articleList);
}
