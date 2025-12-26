package com.mushan.msenbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mushan.msenbackend.model.dto.engdict.EngdictQueryRequest;
import com.mushan.msenbackend.model.entity.Engdict;
import com.mushan.msenbackend.model.vo.WordBookVO;
import com.mushan.msenbackend.model.vo.WordCardVO;

import java.util.List;

/**
 * @author mushan
 * @description 针对表【engdict(英语单词表)】的数据库操作Service
 * @createDate 2025-12-11 16:36:56
 */
public interface EngdictService extends IService<Engdict> {

    /**
     * 获取查询条件
     *
     * @param engdictQueryRequest 查询请求参数
     * @return 查询条件
     */
    QueryWrapper<Engdict> getQueryWrapper(EngdictQueryRequest engdictQueryRequest);

    /**
     * 根据词书类型统计单词数量
     *
     * @param wordType 词书类型
     * @return 单词数量
     */
    Long countByWordType(String wordType);

    /**
     * 获取所有词书信息列表
     *
     * @param userId 用户ID（用于获取用户学习进度，可为null）
     * @return 词书信息列表
     */
    List<WordBookVO> getWordBookList(Long userId);

    /**
     * 获取今日待学新词列表
     *
     * @param userId   用户ID
     * @param wordType 词书类型
     * @param limit    限制数量
     * @return 新词卡片列表
     */
    List<WordCardVO> getNewWordList(Long userId, String wordType, int limit);

    /**
     * 预览词书单词（随机获取几个单词样例）
     *
     * @param wordType 词书类型
     * @param limit    限制数量
     * @return 单词卡片列表
     */
    List<WordCardVO> previewWordBook(String wordType, int limit);

    /**
     * 为单词生成选词测试的干扰项（3个）
     *
     * @param wordId   当前单词ID
     * @param wordType 词书类型
     * @return 3个干扰项的中文释义
     */
    List<String> generateDistractors(Integer wordId, String wordType);

    /**
     * 划词翻译（用于文章阅读等场景）
     * 按优先级搜索各词库，返回第一个匹配的单词信息及收藏状态
     *
     * @param word   单词
     * @param userId 用户ID（可为null，用于查询收藏状态）
     * @return 单词卡片VO
     */
    WordCardVO translateWord(String word, Long userId);
}