package com.mushan.msenbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mushan.msenbackend.mapper.ArticleMapper;
import com.mushan.msenbackend.model.dto.article.ArticleQueryRequest;
import com.mushan.msenbackend.model.entity.Article;
import com.mushan.msenbackend.model.vo.ArticleVO;
import com.mushan.msenbackend.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 针对表【article(文章表)】的数据库操作Service实现
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Override
    public QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        if (articleQueryRequest == null) {
            return new QueryWrapper<>();
        }

        Long id = articleQueryRequest.getId();
        String title = articleQueryRequest.getTitle();
        Integer difficulty = articleQueryRequest.getDifficulty();
        String category = articleQueryRequest.getCategory();

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(difficulty != null, "difficulty", difficulty);
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category);

        return queryWrapper;
    }

    @Override
    public ArticleVO getArticleVO(Article article) {
        if (article == null) {
            return null;
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        return articleVO;
    }

    @Override
    public List<ArticleVO> getArticleVOList(List<Article> articleList) {
        if (articleList == null || articleList.isEmpty()) {
            return Collections.emptyList();
        }
        return articleList.stream()
                .map(this::getArticleVO)
                .collect(Collectors.toList());
    }
}
