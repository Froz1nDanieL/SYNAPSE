package com.mushan.msenbackend.service;

import com.mushan.msenbackend.model.dto.learnplan.LearnPlanCreateRequest;
import com.mushan.msenbackend.model.dto.learnplan.LearnPlanUpdateRequest;
import com.mushan.msenbackend.model.entity.Userlearnplan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mushan.msenbackend.model.vo.LearnPlanVO;

/**
 * @author Danie
 * @description 针对表【userlearnplan(用户学习计划表)】的数据库操作Service
 * @createDate 2025-12-16 14:30:58
 */
public interface UserlearnplanService extends IService<Userlearnplan> {

    /**
     * 创建学习计划（每个用户只能有一个活跃计划）
     *
     * @param userId  用户ID
     * @param request 创建请求
     * @return 计划ID
     */
    Long createLearnPlan(Long userId, LearnPlanCreateRequest request);

    /**
     * 更新学习计划
     *
     * @param userId  用户ID
     * @param request 更新请求
     * @return 是否成功
     */
    Boolean updateLearnPlan(Long userId, LearnPlanUpdateRequest request);

    /**
     * 获取用户当前的学习计划
     *
     * @param userId 用户ID
     * @return 学习计划（无则返回null）
     */
    Userlearnplan getCurrentPlan(Long userId);

    /**
     * 获取用户学习计划VO（包含进度信息）
     *
     * @param userId 用户ID
     * @return 学习计划VO
     */
    LearnPlanVO getLearnPlanVO(Long userId);

    /**
     * 切换词书（更换学习计划的词书类型）
     *
     * @param userId   用户ID
     * @param wordType 新词书类型
     * @return 是否成功
     */
    Boolean switchWordBook(Long userId, String wordType);

    /**
     * 暂停/启用学习计划
     *
     * @param userId     用户ID
     * @param planStatus 状态（0暂停/1启用）
     * @return 是否成功
     */
    Boolean togglePlanStatus(Long userId, Integer planStatus);

    /**
     * 更新学习进度
     *
     * @param userId    用户ID
     * @param increment 增加的单词数
     */
    void incrementProgress(Long userId, int increment);
}
