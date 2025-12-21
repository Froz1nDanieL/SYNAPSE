package com.mushan.msenbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户学习计划表
 * @TableName userlearnplan
 */
@TableName(value ="user_learn_plan")
@Data
public class Userlearnplan {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 词书类型（cet4/cet6/zk/gk/ky/ielts/toefl）
     */
    private String wordType;

    /**
     * 每日新词目标量（默认50）
     */
    private Integer dailyNewCount;

    /**
     * 当前学习进度（已学单词数）
     */
    private Integer currentProgress;

    /**
     * 计划状态（0暂停/1启用）
     */
    private Integer planStatus;

    /**
     * 每日复习提醒时间
     */
    private Date remindTime;

    /**
     * 计划开始日期
     */
    private Date startDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}