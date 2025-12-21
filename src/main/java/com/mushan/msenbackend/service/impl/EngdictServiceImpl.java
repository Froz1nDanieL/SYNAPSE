package com.mushan.msenbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mushan.msenbackend.mapper.EngdictMapper;
import com.mushan.msenbackend.model.dto.engdict.EngdictQueryRequest;
import com.mushan.msenbackend.model.entity.Engdict;
import com.mushan.msenbackend.model.entity.Userlearnplan;
import com.mushan.msenbackend.model.entity.Userwordrecord;
import com.mushan.msenbackend.model.enums.WordTypeEnum;
import com.mushan.msenbackend.model.vo.WordBookVO;
import com.mushan.msenbackend.model.vo.WordCardVO;
import com.mushan.msenbackend.service.EngdictService;
import com.mushan.msenbackend.service.UserlearnplanService;
import com.mushan.msenbackend.service.UserwordrecordService;
import com.mushan.msenbackend.utils.WordExchangeUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mushan
 * @description 针对表【engdict(英语单词表)】的数据库操作Service实现
 * @createDate 2025-12-11 16:36:56
 */
@Service
public class EngdictServiceImpl extends ServiceImpl<EngdictMapper, Engdict>
        implements EngdictService {

    @Resource
    @Lazy
    private UserlearnplanService userlearnplanService;

    @Resource
    @Lazy
    private UserwordrecordService userwordrecordService;

    @Override
    public QueryWrapper<Engdict> getQueryWrapper(EngdictQueryRequest engdictQueryRequest) {
        if (engdictQueryRequest == null) {
            return new QueryWrapper<>();
        }

        Integer id = engdictQueryRequest.getId();
        String word = engdictQueryRequest.getWord();
        String phonetic = engdictQueryRequest.getPhonetic();
        String definition = engdictQueryRequest.getDefinition();
        String translation = engdictQueryRequest.getTranslation();
        String pos = engdictQueryRequest.getPos();
        Integer collins = engdictQueryRequest.getCollins();
        Integer oxford = engdictQueryRequest.getOxford();
        String tag = engdictQueryRequest.getTag();
        Integer bnc = engdictQueryRequest.getBnc();
        Integer frq = engdictQueryRequest.getFrq();

        QueryWrapper<Engdict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(word), "word", word);
        queryWrapper.like(StringUtils.isNotBlank(phonetic), "phonetic", phonetic);
        queryWrapper.like(StringUtils.isNotBlank(definition), "definition", definition);
        queryWrapper.like(StringUtils.isNotBlank(translation), "translation", translation);
        queryWrapper.eq(StringUtils.isNotBlank(pos), "pos", pos);
        queryWrapper.eq(collins != null, "collins", collins);
        queryWrapper.eq(oxford != null, "oxford", oxford);
        queryWrapper.like(StringUtils.isNotBlank(tag), "tag", tag);
        queryWrapper.eq(bnc != null, "bnc", bnc);
        queryWrapper.eq(frq != null, "frq", frq);

        return queryWrapper;
    }

    @Override
    public Long countByWordType(String wordType) {
        if (StringUtils.isBlank(wordType)) {
            return 0L;
        }
        
        // 使用Hint强制路由，避免全表扫描，同时支持多标签
        HintManager hintManager = HintManager.getInstance();
        try {
            // 强制路由到指定的分片表，如 engdict_cet4
            hintManager.addTableShardingValue("engdict", wordType);
            
            // 查询tag中包含该wordType的单词
            // 使用FIND_IN_SET处理空格分隔的多标签，如 'cet4 cet6'
            QueryWrapper<Engdict> queryWrapper = new QueryWrapper<>();
            queryWrapper.apply("FIND_IN_SET({0}, REPLACE(tag, ' ', ',')) > 0", wordType);
            return this.count(queryWrapper);
        } finally {
            // 必须关闭HintManager，清理ThreadLocal
            hintManager.close();
        }
    }

    @Override
    public List<WordBookVO> getWordBookList(Long userId) {
        List<WordBookVO> result = new ArrayList<>();

        // 获取用户当前学习计划（如果已登录）
        Userlearnplan currentPlan = null;
        if (userId != null && userId > 0) {
            currentPlan = userlearnplanService.getCurrentPlan(userId);
        }

        // 遍历所有词书类型
        for (WordTypeEnum wordTypeEnum : WordTypeEnum.values()) {
            WordBookVO vo = WordBookVO.fromEnum(wordTypeEnum);

            // 查询实际单词数量
            Long actualCount = countByWordType(wordTypeEnum.getType());
            vo.setTotalWordCount(actualCount);

            // 如果用户已登录，获取学习进度
            if (userId != null && userId > 0) {
                Long learnedCount = userwordrecordService.countLearnedWords(userId, wordTypeEnum.getType());
                vo.setLearnedWordCount(learnedCount);

                // 计算进度百分比
                if (actualCount > 0) {
                    double percent = (learnedCount * 100.0) / actualCount;
                    vo.setProgressPercent(Math.round(percent * 100.0) / 100.0);
                }

                // 检查是否为当前学习的词书
                if (currentPlan != null && wordTypeEnum.getType().equals(currentPlan.getWordType())) {
                    vo.setIsCurrentBook(true);
                }
            }

            result.add(vo);
        }

        return result;
    }

    @Override
    public List<WordCardVO> getNewWordList(Long userId, String wordType, int limit) {
        if (userId == null || StringUtils.isBlank(wordType) || limit <= 0) {
            return Collections.emptyList();
        }

        // 获取用户已学过的单词ID列表（不包括仅收藏的）
        Set<Integer> learnedWordIds = userwordrecordService.getLearnedWordIds(userId, wordType);

        // 使用Hint强制路由到指定词书表
        HintManager hintManager = HintManager.getInstance();
        try {
            hintManager.addTableShardingValue("engdict", wordType);
            
            // 查询未学过的单词，只查询必要字段
            QueryWrapper<Engdict> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "word", "phonetic", "definition", "translation", "exchange");
            // 匹配tag中包含该wordType的单词
            queryWrapper.apply("FIND_IN_SET({0}, REPLACE(tag, ' ', ',')) > 0", wordType);
            if (!learnedWordIds.isEmpty()) {
                queryWrapper.notIn("id", learnedWordIds);
            }
            // 按柯林斯星级降序排序，星级高的优先学习
            queryWrapper.orderByDesc("collins");
            queryWrapper.last("LIMIT " + limit);

            List<Engdict> wordList = this.list(queryWrapper);
            
            if (wordList.isEmpty()) {
                return Collections.emptyList();
            }

            // 获取这些单词的学习记录（包括仅收藏的记录）
            Set<Integer> wordIds = wordList.stream()
                    .map(Engdict::getId)
                    .collect(Collectors.toSet());
            
            Map<Integer, Userwordrecord> recordMap = userwordrecordService.lambdaQuery()
                    .eq(Userwordrecord::getUserId, userId)
                    .eq(Userwordrecord::getWordType, wordType)
                    .in(Userwordrecord::getWordId, wordIds)
                    .list()
                    .stream()
                    .collect(Collectors.toMap(Userwordrecord::getWordId, r -> r));

            // 转换为WordCardVO，填充收藏状态
            return wordList.stream()
                    .map(word -> {
                        Userwordrecord record = recordMap.get(word.getId());
                        return convertToWordCardVO(word, wordType, record);
                    })
                    .collect(Collectors.toList());
        } finally {
            hintManager.close();
        }
    }

    @Override
    public List<WordCardVO> previewWordBook(String wordType, int limit) {
        if (StringUtils.isBlank(wordType) || limit <= 0) {
            return Collections.emptyList();
        }

        // 使用Hint强制路由
        HintManager hintManager = HintManager.getInstance();
        try {
            hintManager.addTableShardingValue("engdict", wordType);
            
            // 随机获取单词样例
            QueryWrapper<Engdict> queryWrapper = new QueryWrapper<>();
            queryWrapper.apply("FIND_IN_SET({0}, REPLACE(tag, ' ', ',')) > 0", wordType);
            queryWrapper.orderByAsc("RAND()");
            queryWrapper.last("LIMIT " + limit);

            List<Engdict> wordList = this.list(queryWrapper);

            return wordList.stream()
                    .map(word -> convertToWordCardVO(word, wordType, null))
                    .collect(Collectors.toList());
        } finally {
            hintManager.close();
        }
    }

    @Override
    public List<String> generateDistractors(Integer wordId, String wordType) {
        if (wordId == null || StringUtils.isBlank(wordType)) {
            return Collections.emptyList();
        }

        // 不再创建新的HintManager，直接查询（依赖外层已设置的Hint）
        // 随机获取3个不同的干扰项（排除当前单词）
        QueryWrapper<Engdict> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("translation");
        queryWrapper.apply("FIND_IN_SET({0}, REPLACE(tag, ' ', ',')) > 0", wordType);
        queryWrapper.ne("id", wordId);
        queryWrapper.isNotNull("translation");
        queryWrapper.ne("translation", "");
        queryWrapper.orderByAsc("RAND()");
        queryWrapper.last("LIMIT 3");

        List<Engdict> distractorWords = this.list(queryWrapper);

        return distractorWords.stream()
                .map(Engdict::getTranslation)
                .collect(Collectors.toList());
    }

    /**
     * 将Engdict实体转换为WordCardVO
     */
    private WordCardVO convertToWordCardVO(Engdict word, String wordType, Userwordrecord record) {
        WordCardVO vo = new WordCardVO();
        vo.setWordId(word.getId());
        vo.setWord(word.getWord());
        vo.setPhonetic(word.getPhonetic());
        vo.setDefinition(word.getDefinition());
        vo.setTranslation(word.getTranslation());
        vo.setPos(word.getPos());
        vo.setCollins(word.getCollins());
        vo.setOxford(word.getOxford());
        vo.setExchange(word.getExchange());
        vo.setExchangeInfo(WordExchangeUtil.parseExchange(word.getExchange()));
        vo.setAudio(word.getAudio());
        vo.setWordType(wordType);

        // 生成选词测试选项（1个正确答案 + 3个干扰项）
        List<String> options = new ArrayList<>();
        options.add(word.getTranslation()); // 正确答案
        List<String> distractors = generateDistractors(word.getId(), wordType);
        options.addAll(distractors);
        // 打乱选项顺序
        Collections.shuffle(options);
        vo.setOptions(options);

        // 如果有学习记录，填充复习相关信息
        if (record != null) {
            vo.setRecordId(record.getId());
            vo.setMemLevel(record.getMemLevel());
            vo.setIsCollect(record.getIsCollect());
            vo.setReviewTimes(record.getReviewTimes());
        } else {
            // 没有记录时，设置默认值
            vo.setIsCollect(0);
        }

        return vo;
    }
}