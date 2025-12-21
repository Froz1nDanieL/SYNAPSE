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
import com.mushan.msenbackend.model.dto.engdict.EngdictAddRequest;
import com.mushan.msenbackend.model.dto.engdict.EngdictQueryRequest;
import com.mushan.msenbackend.model.dto.engdict.EngdictUpdateRequest;
import com.mushan.msenbackend.model.entity.Engdict;
import com.mushan.msenbackend.service.EngdictService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单词接口
 */
@RestController
@RequestMapping("/engdict")
@Slf4j
public class EngdictController {

    @Resource
    private EngdictService engdictService;

    /**
     * 创建单词（仅管理员）
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Integer> addEngdict(@RequestBody EngdictAddRequest engdictAddRequest) {
        if (engdictAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Engdict engdict = new Engdict();
        BeanUtils.copyProperties(engdictAddRequest, engdict);
        boolean result = engdictService.save(engdict);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(engdict.getId());
    }

    /**
     * 删除单词（仅管理员）
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteEngdict(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = engdictService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新单词（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateEngdict(@RequestBody EngdictUpdateRequest engdictUpdateRequest) {
        if (engdictUpdateRequest == null || engdictUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Engdict engdict = new Engdict();
        BeanUtils.copyProperties(engdictUpdateRequest, engdict);
        boolean result = engdictService.updateById(engdict);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取单词
     */
    @GetMapping("/get")
    public BaseResponse<Engdict> getEngdictById(int id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Engdict engdict = engdictService.getById(id);
        ThrowUtils.throwIf(engdict == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(engdict);
    }

    /**
     * 分页获取单词列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Engdict>> listEngdictByPage(@RequestBody EngdictQueryRequest engdictQueryRequest) {
        long current = engdictQueryRequest.getCurrent();
        long size = engdictQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Engdict> engdictPage = engdictService.page(new Page<>(current, size),
                engdictService.getQueryWrapper(engdictQueryRequest));
        return ResultUtils.success(engdictPage);
    }

    /**
     * 获取单词列表
     */
    @PostMapping("/list")
    public BaseResponse<List<Engdict>> listEngdict(@RequestBody EngdictQueryRequest engdictQueryRequest) {
        List<Engdict> engdictList = engdictService.list(engdictService.getQueryWrapper(engdictQueryRequest));
        return ResultUtils.success(engdictList);
    }
}