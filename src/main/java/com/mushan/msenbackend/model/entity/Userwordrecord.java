package com.mushan.msenbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户单词学习记录表
 * @TableName user_word_record
 */
@TableName(value ="user_word_record")
@Data
public class Userwordrecord {
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
     * 单词ID（对应engdict.id）
     */
    private Integer wordId;

    /**
     * 词书类型（便于分表查询）
     */
    private String wordType;

    /**
     * 记忆等级（0-5）
     */
    private Integer memLevel;

    /**
     * 首次学习时间
     */
    private Date learnTime;

    /**
     * 最近复习时间
     */
    private Date lastReviewTime;

    /**
     * 下次复习时间
     */
    private Date nextReviewTime;

    /**
     * 复习次数
     */
    private Integer reviewTimes;

    /**
     * 正确次数
     */
    private Integer correctTimes;

    /**
     * 错误次数
     */
    private Integer errorTimes;

    /**
     * 是否收藏（0/1）
     */
    private Integer isCollect;

    /**
     * 是否已掌握（0/1）
     */
    private Integer isMastered;

    /**
     * 第一轮：是否认识（0-不认识，1-认识）
     */
    private Integer firstKnow;

    /**
     * 第二轮：选词测试是否答对（0/1/null）
     */
    private Integer choiceCorrect;

    /**
     * 第二轮：选词测试错误次数（重测累计）
     */
    private Integer choiceErrorCount;

    /**
     * 第三轮：拼写测试是否正确（0/1/null）
     */
    private Integer spellingCorrect;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}