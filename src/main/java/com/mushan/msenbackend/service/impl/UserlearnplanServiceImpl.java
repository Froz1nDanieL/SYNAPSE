package com.mushan.msenbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mushan.msenbackend.exception.BusinessException;
import com.mushan.msenbackend.exception.ErrorCode;
import com.mushan.msenbackend.mapper.UserlearnplanMapper;
import com.mushan.msenbackend.model.dto.learnplan.LearnPlanCreateRequest;
import com.mushan.msenbackend.model.dto.learnplan.LearnPlanUpdateRequest;
import com.mushan.msenbackend.model.entity.Userlearnplan;
import com.mushan.msenbackend.model.enums.WordTypeEnum;
import com.mushan.msenbackend.model.vo.LearnPlanVO;
import com.mushan.msenbackend.service.EngdictService;
import com.mushan.msenbackend.service.UserlearnplanService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Danie
 * @description 针对表【userlearnplan(用户学习计划表)】的数据库操作Service实现
 * @createDate 2025-12-16 14:30:58
 */
@Service
public class UserlearnplanServiceImpl extends ServiceImpl<UserlearnplanMapper, Userlearnplan>
        implements UserlearnplanService {

    @Resource
    private EngdictService engdictService;

    /**
     * 每日新词量范围
     */
    private static final int MIN_DAILY_COUNT = 20;
    private static final int MAX_DAILY_COUNT = 100;
    private static final int DEFAULT_DAILY_COUNT = 50;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLearnPlan(Long userId, LearnPlanCreateRequest request) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不合法");
        }
        String wordType = request.getWordType();
        if (StringUtils.isBlank(wordType) || !WordTypeEnum.isValidType(wordType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "词书类型不合法");
        }

        // 校验每日新词数量
        Integer dailyNewCount = request.getDailyNewCount();
        if (dailyNewCount == null) {
            dailyNewCount = DEFAULT_DAILY_COUNT;
        } else if (dailyNewCount < MIN_DAILY_COUNT || dailyNewCount > MAX_DAILY_COUNT) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    "每日新词数量应在" + MIN_DAILY_COUNT + "-" + MAX_DAILY_COUNT + "之间");
        }

        // 检查用户是否已有学习计划，每个用户只能有一个计划
        Userlearnplan existingPlan = getCurrentPlan(userId);
        if (existingPlan != null) {
            // 已有计划，进行更新而不是创建新的
            existingPlan.setWordType(wordType);
            existingPlan.setDailyNewCount(dailyNewCount);
            existingPlan.setPlanStatus(1);  // 启用状态
            existingPlan.setStartDate(new Date());
            existingPlan.setCurrentProgress(0);  // 重置进度
            existingPlan.setUpdateTime(new Date());
            this.updateById(existingPlan);
            return existingPlan.getId();
        }

        // 创建新计划
        Userlearnplan plan = new Userlearnplan();
        plan.setUserId(userId);
        plan.setWordType(wordType);
        plan.setDailyNewCount(dailyNewCount);
        plan.setCurrentProgress(0);
        plan.setPlanStatus(1);  // 默认启用
        plan.setStartDate(new Date());

        this.save(plan);
        return plan.getId();
    }

    @Override
    public Boolean updateLearnPlan(Long userId, LearnPlanUpdateRequest request) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不合法");
        }

        Userlearnplan plan = getCurrentPlan(userId);
        if (plan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户暂无学习计划");
        }

        // 更新词书类型
        if (StringUtils.isNotBlank(request.getWordType())) {
            if (!WordTypeEnum.isValidType(request.getWordType())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "词书类型不合法");
            }
            plan.setWordType(request.getWordType());
        }

        // 更新每日新词数量
        if (request.getDailyNewCount() != null) {
            if (request.getDailyNewCount() < MIN_DAILY_COUNT || request.getDailyNewCount() > MAX_DAILY_COUNT) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,
                        "每日新词数量应在" + MIN_DAILY_COUNT + "-" + MAX_DAILY_COUNT + "之间");
            }
            plan.setDailyNewCount(request.getDailyNewCount());
        }

        // 更新计划状态
        if (request.getPlanStatus() != null) {
            if (request.getPlanStatus() != 0 && request.getPlanStatus() != 1) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "计划状态不合法");
            }
            plan.setPlanStatus(request.getPlanStatus());
        }

        plan.setUpdateTime(new Date());
        return this.updateById(plan);
    }

    @Override
    public Userlearnplan getCurrentPlan(Long userId) {
        if (userId == null || userId <= 0) {
            return null;
        }
        QueryWrapper<Userlearnplan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public LearnPlanVO getLearnPlanVO(Long userId) {
        Userlearnplan plan = getCurrentPlan(userId);
        if (plan == null) {
            return null;
        }

        LearnPlanVO vo = new LearnPlanVO();
        vo.setId(plan.getId());
        vo.setUserId(plan.getUserId());
        vo.setWordType(plan.getWordType());
        vo.setDailyNewCount(plan.getDailyNewCount());
        vo.setCurrentProgress(plan.getCurrentProgress());
        vo.setPlanStatus(plan.getPlanStatus());
        vo.setStartDate(plan.getStartDate());
        vo.setCreateTime(plan.getCreateTime());

        // 获取词书名称
        WordTypeEnum wordTypeEnum = WordTypeEnum.getEnumByType(plan.getWordType());
        if (wordTypeEnum != null) {
            vo.setWordTypeName(wordTypeEnum.getName());
        }

        // 获取词书总单词数
        Long totalCount = engdictService.countByWordType(plan.getWordType());
        vo.setTotalWordCount(totalCount);

        // 计算进度百分比
        if (totalCount > 0) {
            double percent = (plan.getCurrentProgress() * 100.0) / totalCount;
            vo.setProgressPercent(Math.round(percent * 100.0) / 100.0);
        } else {
            vo.setProgressPercent(0.0);
        }

        // 计算预计完成天数
        if (plan.getDailyNewCount() > 0 && totalCount > plan.getCurrentProgress()) {
            int remainingWords = (int) (totalCount - plan.getCurrentProgress());
            vo.setEstimatedDays((int) Math.ceil((double) remainingWords / plan.getDailyNewCount()));
        } else {
            vo.setEstimatedDays(0);
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean switchWordBook(Long userId, String wordType) {
        if (!WordTypeEnum.isValidType(wordType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "词书类型不合法");
        }

        Userlearnplan plan = getCurrentPlan(userId);
        if (plan == null) {
            // 无计划则创建新计划
            LearnPlanCreateRequest request = new LearnPlanCreateRequest();
            request.setWordType(wordType);
            request.setDailyNewCount(DEFAULT_DAILY_COUNT);
            createLearnPlan(userId, request);
            return true;
        }

        // 切换词书时重置进度
        plan.setWordType(wordType);
        plan.setCurrentProgress(0);
        plan.setStartDate(new Date());
        plan.setPlanStatus(1);
        plan.setUpdateTime(new Date());
        return this.updateById(plan);
    }

    @Override
    public Boolean togglePlanStatus(Long userId, Integer planStatus) {
        if (planStatus == null || (planStatus != 0 && planStatus != 1)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "计划状态不合法");
        }

        Userlearnplan plan = getCurrentPlan(userId);
        if (plan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户暂无学习计划");
        }

        plan.setPlanStatus(planStatus);
        plan.setUpdateTime(new Date());
        return this.updateById(plan);
    }

    @Override
    public void incrementProgress(Long userId, int increment) {
        if (increment <= 0) {
            return;
        }
        Userlearnplan plan = getCurrentPlan(userId);
        if (plan != null) {
            UpdateWrapper<Userlearnplan> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", plan.getId())
                    .setSql("currentProgress = currentProgress + " + increment)
                    .set("updateTime", new Date());
            this.update(updateWrapper);
        }
    }
}
