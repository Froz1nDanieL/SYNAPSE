package com.mushan.msenbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mushan.msenbackend.common.BaseResponse;
import com.mushan.msenbackend.common.ResultUtils;
import com.mushan.msenbackend.exception.BusinessException;
import com.mushan.msenbackend.exception.ErrorCode;
import com.mushan.msenbackend.exception.ThrowUtils;
import com.mushan.msenbackend.model.dto.wordrecord.BatchWordLearnRequest;
import com.mushan.msenbackend.model.dto.wordrecord.BatchWordReviewRequest;
import com.mushan.msenbackend.model.dto.wordrecord.WordCollectRequest;
import com.mushan.msenbackend.model.dto.wordrecord.WordLearnRequest;
import com.mushan.msenbackend.model.dto.wordrecord.WordReviewRequest;
import com.mushan.msenbackend.model.entity.User;
import com.mushan.msenbackend.model.entity.Userlearnplan;
import com.mushan.msenbackend.model.enums.WordTypeEnum;
import com.mushan.msenbackend.model.vo.LearnProgressVO;
import com.mushan.msenbackend.model.vo.WordBookVO;
import com.mushan.msenbackend.model.vo.WordCardVO;
import com.mushan.msenbackend.model.vo.WordRecordVO;
import com.mushan.msenbackend.service.EngdictService;
import com.mushan.msenbackend.service.UserService;
import com.mushan.msenbackend.service.UserlearnplanService;
import com.mushan.msenbackend.service.UserwordrecordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 背单词核心接口
 */
@RestController
@RequestMapping("/word-learn")
@Slf4j
public class WordLearnController {

    @Resource
    private EngdictService engdictService;

    @Resource
    private UserwordrecordService userwordrecordService;

    @Resource
    private UserlearnplanService userlearnplanService;

    @Resource
    private UserService userService;

    // ==================== 词书相关接口 ====================

    /**
     * 获取所有词书列表
     */
    @GetMapping("/word-books")
    public BaseResponse<List<WordBookVO>> getWordBookList(HttpServletRequest httpRequest) {
        Long userId = null;
        try {
            User loginUser = userService.getLoginUser(httpRequest);
            userId = loginUser.getId();
        } catch (Exception e) {
            // 未登录也可以查看词书列表
        }
        List<WordBookVO> wordBookList = engdictService.getWordBookList(userId);
        return ResultUtils.success(wordBookList);
    }

    /**
     * 预览词书单词（随机展示样例）
     */
    @GetMapping("/preview/{wordType}")
    public BaseResponse<List<WordCardVO>> previewWordBook(@PathVariable String wordType,
                                                          @RequestParam(defaultValue = "10") int limit) {
        ThrowUtils.throwIf(!WordTypeEnum.isValidType(wordType), ErrorCode.PARAMS_ERROR, "词书类型不合法");
        ThrowUtils.throwIf(limit > 20, ErrorCode.PARAMS_ERROR, "预览数量不能超过20");
        List<WordCardVO> previewList = engdictService.previewWordBook(wordType, limit);
        return ResultUtils.success(previewList);
    }

    // ==================== 新词学习接口 ====================

    /**
     * 获取今日待学新词列表
     */
    @GetMapping("/new-words")
    public BaseResponse<List<WordCardVO>> getNewWordList(@RequestParam(required = false) String wordType,
                                                         @RequestParam(required = false) Integer limit,
                                                         HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);

        // 如果未指定词书类型，使用当前学习计划的词书
        if (StringUtils.isBlank(wordType)) {
            Userlearnplan plan = userlearnplanService.getCurrentPlan(loginUser.getId());
            if (plan == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "请先选择词书创建学习计划");
            }
            wordType = plan.getWordType();
            if (limit == null) {
                limit = plan.getDailyNewCount();
            }
        }

        // 默认每日新词量
        if (limit == null) {
            limit = 50;
        }

        ThrowUtils.throwIf(limit > 100, ErrorCode.PARAMS_ERROR, "单次获取新词数量不能超过100");

        List<WordCardVO> newWordList = engdictService.getNewWordList(loginUser.getId(), wordType, limit);
        return ResultUtils.success(newWordList);
    }

    /**
     * 提交单个单词学习记录
     */
    @PostMapping("/learn")
    public BaseResponse<Long> submitLearnRecord(@RequestBody WordLearnRequest request,
                                                HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        Long recordId = userwordrecordService.saveLearnRecord(loginUser.getId(), request);
        return ResultUtils.success(recordId);
    }

    /**
     * 批量提交单词学习记录
     */
    @PostMapping("/learn/batch")
    public BaseResponse<List<Long>> batchSubmitLearnRecord(@RequestBody BatchWordLearnRequest request,
                                                           HttpServletRequest httpRequest) {
        if (request == null || request.getLearnRecords() == null || request.getLearnRecords().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ThrowUtils.throwIf(request.getLearnRecords().size() > 100, ErrorCode.PARAMS_ERROR, "单次提交不能超过100个单词");

        User loginUser = userService.getLoginUser(httpRequest);
        List<Long> recordIds = new ArrayList<>();

        for (WordLearnRequest learnRequest : request.getLearnRecords()) {
            // 如果单个请求未指定wordType，使用批量请求的wordType
            if (StringUtils.isBlank(learnRequest.getWordType())) {
                learnRequest.setWordType(request.getWordType());
            }
            Long recordId = userwordrecordService.saveLearnRecord(loginUser.getId(), learnRequest);
            recordIds.add(recordId);
        }

        return ResultUtils.success(recordIds);
    }

    // ==================== 智能复习接口 ====================

    /**
     * 获取今日待复习单词列表
     */
    @GetMapping("/review-words")
    public BaseResponse<List<WordCardVO>> getReviewWordList(@RequestParam(required = false) String wordType,
                                                            @RequestParam(defaultValue = "50") int limit,
                                                            HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);

        // 如果未指定词书类型，使用当前学习计划的词书
        if (StringUtils.isBlank(wordType)) {
            Userlearnplan plan = userlearnplanService.getCurrentPlan(loginUser.getId());
            if (plan == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "请先选择词书创建学习计划");
            }
            wordType = plan.getWordType();
        }

        ThrowUtils.throwIf(limit > 100, ErrorCode.PARAMS_ERROR, "单次获取复习单词数量不能超过100");

        List<WordCardVO> reviewWordList = userwordrecordService.getReviewWordList(loginUser.getId(), wordType, limit);
        return ResultUtils.success(reviewWordList);
    }

    /**
     * 提交单个单词复习结果
     */
    @PostMapping("/review")
    public BaseResponse<Boolean> submitReviewRecord(@RequestBody WordReviewRequest request,
                                                    HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        Boolean result = userwordrecordService.updateReviewRecord(loginUser.getId(), request);
        return ResultUtils.success(result);
    }

    /**
     * 批量提交单词复习结果
     */
    @PostMapping("/review/batch")
    public BaseResponse<Boolean> batchSubmitReviewRecord(@RequestBody BatchWordReviewRequest request,
                                                         HttpServletRequest httpRequest) {
        if (request == null || request.getReviewRecords() == null || request.getReviewRecords().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ThrowUtils.throwIf(request.getReviewRecords().size() > 100, ErrorCode.PARAMS_ERROR, "单次提交不能超过100个单词");

        User loginUser = userService.getLoginUser(httpRequest);

        for (WordReviewRequest reviewRequest : request.getReviewRecords()) {
            userwordrecordService.updateReviewRecord(loginUser.getId(), reviewRequest);
        }

        return ResultUtils.success(true);
    }

    // ==================== 进度与统计接口 ====================

    /**
     * 获取学习进度概览
     */
    @GetMapping("/progress")
    public BaseResponse<LearnProgressVO> getLearnProgress(@RequestParam(required = false) String wordType,
                                                          HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);

        // 如果未指定词书类型，使用当前学习计划的词书
        if (StringUtils.isBlank(wordType)) {
            Userlearnplan plan = userlearnplanService.getCurrentPlan(loginUser.getId());
            if (plan == null) {
                return ResultUtils.success(null);
            }
            wordType = plan.getWordType();
        }

        LearnProgressVO progress = userwordrecordService.getLearnProgress(loginUser.getId(), wordType);
        return ResultUtils.success(progress);
    }

    // ==================== 收藏相关接口 ====================

    /**
     * 收藏/取消收藏单词
     */
    @PostMapping("/collect")
    public BaseResponse<Boolean> toggleCollect(@RequestBody WordCollectRequest request,
                                               HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        Boolean result = userwordrecordService.updateCollectStatus(loginUser.getId(), request);
        return ResultUtils.success(result);
    }

    /**
     * 获取收藏列表
     */
    @GetMapping("/collected")
    public BaseResponse<Page<WordRecordVO>> getCollectedWords(@RequestParam(required = false) String wordType,
                                                              @RequestParam(defaultValue = "1") int pageNo,
                                                              @RequestParam(defaultValue = "20") int pageSize,
                                                              HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);
        ThrowUtils.throwIf(pageSize > 50, ErrorCode.PARAMS_ERROR, "每页数量不能超过50");

        Page<WordRecordVO> collectedWords = userwordrecordService.getCollectedWords(
                loginUser.getId(), wordType, pageNo, pageSize);
        return ResultUtils.success(collectedWords);
    }
}
