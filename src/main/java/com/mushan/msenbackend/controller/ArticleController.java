package com.mushan.msenbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mushan.msenbackend.annotation.AuthCheck;
import com.mushan.msenbackend.common.BaseResponse;
import com.mushan.msenbackend.common.DeleteRequest;
import com.mushan.msenbackend.common.ResultUtils;
import com.mushan.msenbackend.constant.UserConstant;
import com.mushan.msenbackend.exception.BusinessException;
import com.mushan.msenbackend.exception.ErrorCode;
import com.mushan.msenbackend.exception.ThrowUtils;
import com.mushan.msenbackend.model.dto.article.ArticleAddRequest;
import com.mushan.msenbackend.model.dto.article.ArticleQueryRequest;
import com.mushan.msenbackend.model.dto.article.ArticleUpdateRequest;
import com.mushan.msenbackend.model.entity.Article;
import com.mushan.msenbackend.model.vo.ArticleVO;
import com.mushan.msenbackend.service.ArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 文章接口
 */
@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 创建文章（仅管理员）
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addArticle(@RequestBody ArticleAddRequest articleAddRequest) {
        if (articleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleAddRequest, article);
        boolean result = articleService.save(article);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(article.getId());
    }

    /**
     * 删除文章（仅管理员）
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteArticle(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = articleService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新文章（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest) {
        if (articleUpdateRequest == null || articleUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateRequest, article);
        boolean result = articleService.updateById(article);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取文章
     */
    @GetMapping("/get")
    public BaseResponse<ArticleVO> getArticleById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article article = articleService.getById(id);
        ThrowUtils.throwIf(article == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(articleService.getArticleVO(article));
    }

    /**
     * 分页获取文章列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ArticleVO>> listArticleByPage(@RequestBody ArticleQueryRequest articleQueryRequest) {
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Article> articlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(articleQueryRequest));
        Page<ArticleVO> articleVOPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        articleVOPage.setRecords(articleService.getArticleVOList(articlePage.getRecords()));
        return ResultUtils.success(articleVOPage);
    }
}
