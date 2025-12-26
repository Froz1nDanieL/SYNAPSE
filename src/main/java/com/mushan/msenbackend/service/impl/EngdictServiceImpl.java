package com.mushan.msenbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mushan.msenbackend.exception.BusinessException;
import com.mushan.msenbackend.exception.ErrorCode;
import com.mushan.msenbackend.exception.ThrowUtils;
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

    /**
     * 根据词书类型获取物理表名
     */
    private String getPhysicalTableName(String wordType) {
        if (StringUtils.isBlank(wordType)) {
            return "engdict_cet4";
        }
        return switch (wordType) {
            case "cet4" -> "engdict_cet4";
            case "cet6" -> "engdict_cet6";
            case "zk" -> "engdict_zk";
            case "gk" -> "engdict_gk";
            case "ky" -> "engdict_ky";
            case "ielts" -> "engdict_ielts";
            case "toefl" -> "engdict_toefl";
            case "gre" -> "engdict_gre";
            default -> "engdict_cet4";
        };
    }

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
        
        // 直接调用Mapper的手动拼接表名方法
        String tableName = getPhysicalTableName(wordType);
        return baseMapper.countByTagInTable(tableName, wordType);
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

        // 直接使用手动拼接表名的方式查询
        String tableName = getPhysicalTableName(wordType);
        List<Engdict> wordList = baseMapper.selectNewWordList(tableName, wordType, learnedWordIds, limit);
        
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
    }

    @Override
    public List<WordCardVO> previewWordBook(String wordType, int limit) {
        if (StringUtils.isBlank(wordType) || limit <= 0) {
            return Collections.emptyList();
        }

        // 直接使用手动拼接表名的方式查询
        String tableName = getPhysicalTableName(wordType);
        List<Engdict> wordList = baseMapper.selectRandomWords(tableName, wordType, limit);

        return wordList.stream()
                .map(word -> convertToWordCardVO(word, wordType, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> generateDistractors(Integer wordId, String wordType) {
        if (wordId == null || StringUtils.isBlank(wordType)) {
            return Collections.emptyList();
        }

        // 直接使用手动拼接表名的方式查询
        String tableName = getPhysicalTableName(wordType);
        return baseMapper.selectDistractors(tableName, wordType, wordId);
    }

    @Override
    public WordCardVO translateWord(String word, Long userId) {
        if (StringUtils.isBlank(word)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "单词不能为空");
        }

        // 统一转换为小写
        String normalizedWord = word.trim().toLowerCase();

        // 按优先级搜索各词库分表（优先高频词书）
        String[] wordTypes = {"cet4", "cet6", "ky", "gk", "zk", "ielts", "toefl"};
        
        Engdict foundWord = null;
        String foundWordType = null;

        // 第一步：直接查询原词
        for (String wordType : wordTypes) {
            String tableName = getPhysicalTableName(wordType);
            foundWord = baseMapper.selectFromPhysicalTable(tableName, normalizedWord);
            
            if (foundWord != null) {
                foundWordType = wordType;
                break; // 找到第一个匹配即返回
            }
        }

        // 第二步：如果直接查询未找到，尝试通过词形变化反向查找原型
        if (foundWord == null) {
            for (String wordType : wordTypes) {
                String tableName = getPhysicalTableName(wordType);
                foundWord = baseMapper.selectByExchange(tableName, normalizedWord);
                
                if (foundWord != null) {
                    foundWordType = wordType;
                    break; // 找到第一个匹配即返回
                }
            }
        }

        // 第三步：如果所有分表都查询不到，直接查询主表 engdict
        if (foundWord == null) {
            foundWord = baseMapper.selectFromPhysicalTable("engdict", normalizedWord);
            // 如果主表找到了，需要根据tag字段确定wordType
            if (foundWord != null && StringUtils.isNotBlank(foundWord.getTag())) {
                // 从tag中提取第一个有效的词书类型
                String[] tags = foundWord.getTag().split(" ");
                for (String tag : tags) {
                    for (String type : wordTypes) {
                        if (tag.contains(type)) {
                            foundWordType = type;
                            break;
                        }
                    }
                    if (foundWordType != null) {
                        break;
                    }
                }
                // 如果tag中没有匹配到任何已知类型，默认使用第一个tag
                if (foundWordType == null && tags.length > 0) {
                    foundWordType = tags[0];
                }
            }
        }

        // 如果所有词库都没找到（包括词形变化）
        ThrowUtils.throwIf(foundWord == null, ErrorCode.NOT_FOUND_ERROR, "该单词不在词库中");

        // 查询用户是否已收藏
        Userwordrecord record = null;
        if (userId != null && userId > 0) {
            QueryWrapper<Userwordrecord> recordWrapper = new QueryWrapper<>();
            recordWrapper.eq("userId", userId);
            recordWrapper.eq("wordId", foundWord.getId());
            recordWrapper.eq("wordType", foundWordType);
            record = userwordrecordService.getOne(recordWrapper);
        }

        // 转换为WordCardVO（不生成选项，划词翻译不需要选项）
        WordCardVO vo = new WordCardVO();
        vo.setWordId(foundWord.getId());
        vo.setWord(foundWord.getWord());
        vo.setPhonetic(foundWord.getPhonetic());
        vo.setDefinition(foundWord.getDefinition());
        vo.setTranslation(foundWord.getTranslation());
        vo.setPos(foundWord.getPos());
        vo.setCollins(foundWord.getCollins());
        vo.setOxford(foundWord.getOxford());
        vo.setExchange(foundWord.getExchange());
        vo.setExchangeInfo(WordExchangeUtil.parseExchange(foundWord.getExchange()));
        vo.setAudio(foundWord.getAudio());
        vo.setWordType(foundWordType);

        // 设置收藏状态
        if (record != null) {
            vo.setRecordId(record.getId());
            vo.setMemLevel(record.getMemLevel());
            vo.setIsCollect(record.getIsCollect());
            vo.setReviewTimes(record.getReviewTimes());
        } else {
            vo.setIsCollect(0); // 未收藏
        }

        return vo;
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