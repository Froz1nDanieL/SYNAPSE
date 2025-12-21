package com.mushan.msenbackend.controller;

import com.mushan.msenbackend.common.BaseResponse;
import com.mushan.msenbackend.common.ResultUtils;
import com.mushan.msenbackend.exception.BusinessException;
import com.mushan.msenbackend.exception.ErrorCode;
import com.mushan.msenbackend.exception.ThrowUtils;
import com.mushan.msenbackend.model.dto.learnplan.LearnPlanCreateRequest;
import com.mushan.msenbackend.model.dto.learnplan.LearnPlanUpdateRequest;
import com.mushan.msenbackend.model.entity.User;
import com.mushan.msenbackend.model.vo.LearnPlanVO;
import com.mushan.msenbackend.service.UserService;
import com.mushan.msenbackend.service.UserlearnplanService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 学习计划接口
 */
@RestController
@RequestMapping("/learnplan")
@Slf4j
public class LearnPlanController {

    @Resource
    private UserlearnplanService userlearnplanService;

    @Resource
    private UserService userService;

    /**
     * 创建/更新学习计划
     * 如果用户已有计划则更新，否则创建新计划
     */
    @PostMapping("/create")
    public BaseResponse<Long> createLearnPlan(@RequestBody LearnPlanCreateRequest request,
                                              HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        Long planId = userlearnplanService.createLearnPlan(loginUser.getId(), request);
        return ResultUtils.success(planId);
    }

    /**
     * 更新学习计划
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateLearnPlan(@RequestBody LearnPlanUpdateRequest request,
                                                 HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(httpRequest);
        Boolean result = userlearnplanService.updateLearnPlan(loginUser.getId(), request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前学习计划
     */
    @GetMapping("/current")
    public BaseResponse<LearnPlanVO> getCurrentPlan(HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);
        LearnPlanVO planVO = userlearnplanService.getLearnPlanVO(loginUser.getId());
        return ResultUtils.success(planVO);
    }

    /**
     * 切换词书
     */
    @PostMapping("/switch")
    public BaseResponse<Boolean> switchWordBook(@RequestParam String wordType,
                                                HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);
        Boolean result = userlearnplanService.switchWordBook(loginUser.getId(), wordType);
        return ResultUtils.success(result);
    }

    /**
     * 暂停学习计划
     */
    @PostMapping("/pause")
    public BaseResponse<Boolean> pausePlan(HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);
        Boolean result = userlearnplanService.togglePlanStatus(loginUser.getId(), 0);
        return ResultUtils.success(result);
    }

    /**
     * 启用学习计划
     */
    @PostMapping("/resume")
    public BaseResponse<Boolean> resumePlan(HttpServletRequest httpRequest) {
        User loginUser = userService.getLoginUser(httpRequest);
        Boolean result = userlearnplanService.togglePlanStatus(loginUser.getId(), 1);
        return ResultUtils.success(result);
    }

    /**
     * 修改每日新词目标
     */
    @PostMapping("/daily-count")
    public BaseResponse<Boolean> updateDailyCount(@RequestParam Integer dailyNewCount,
                                                  HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(dailyNewCount == null || dailyNewCount < 20 || dailyNewCount > 100,
                ErrorCode.PARAMS_ERROR, "每日新词数量应在20-100之间");
        User loginUser = userService.getLoginUser(httpRequest);
        LearnPlanUpdateRequest request = new LearnPlanUpdateRequest();
        request.setDailyNewCount(dailyNewCount);
        Boolean result = userlearnplanService.updateLearnPlan(loginUser.getId(), request);
        return ResultUtils.success(result);
    }
}
