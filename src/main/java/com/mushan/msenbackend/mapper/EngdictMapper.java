package com.mushan.msenbackend.mapper;

import com.mushan.msenbackend.model.entity.Engdict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
* @author mushan
* @description 针对表【engdict(英语单词表)】的数据库操作Mapper
* @createDate 2025-12-11 16:36:56
* @Entity com.mushan.msenbackend.model.entity.Engdict
*/
public interface EngdictMapper extends BaseMapper<Engdict> {

    /**
     * 直接查询指定物理表，手动拼接表名
     * @param tableName 物理表名，如 "engdict_cet4"
     * @param word 单词
     * @return 单词实体
     */
    @Select("SELECT * FROM ${tableName} WHERE word = #{word} LIMIT 1")
    Engdict selectFromPhysicalTable(@Param("tableName") String tableName, 
                                    @Param("word") String word);

    /**
     * 统计指定物理表中tag包含指定类型的单词数量
     * @param tableName 物理表名
     * @param tag 标签类型
     * @return 单词数量
     */
    @Select("SELECT COUNT(*) FROM ${tableName} WHERE FIND_IN_SET(#{tag}, REPLACE(tag, ' ', ',')) > 0")
    Long countByTagInTable(@Param("tableName") String tableName, 
                          @Param("tag") String tag);

    /**
     * 查询指定物理表中的单词列表（复杂SQL移至XML）
     * @param tableName 物理表名
     * @param tag 标签类型
     * @param excludeIds 排除的单词ID列表
     * @param limit 限制数量
     * @return 单词列表
     */
    List<Engdict> selectNewWordList(@Param("tableName") String tableName,
                                    @Param("tag") String tag,
                                    @Param("excludeIds") Set<Integer> excludeIds,
                                    @Param("limit") int limit);

    /**
     * 随机获取指定物理表中的单词样例
     */
    @Select("SELECT * FROM ${tableName} " +
            "WHERE FIND_IN_SET(#{tag}, REPLACE(tag, ' ', ',')) > 0 " +
            "ORDER BY RAND() LIMIT #{limit}")
    List<Engdict> selectRandomWords(@Param("tableName") String tableName,
                                    @Param("tag") String tag,
                                    @Param("limit") int limit);

    /**
     * 获取干扰项
     */
    @Select("SELECT translation FROM ${tableName} " +
            "WHERE FIND_IN_SET(#{tag}, REPLACE(tag, ' ', ',')) > 0 " +
            "AND id != #{excludeId} " +
            "AND translation IS NOT NULL AND translation != '' " +
            "ORDER BY RAND() LIMIT 3")
    List<String> selectDistractors(@Param("tableName") String tableName,
                                   @Param("tag") String tag,
                                   @Param("excludeId") Integer excludeId);

    /**
     * 查询词形变化匹配（复杂SQL移至XML）
     */
    Engdict selectByExchange(@Param("tableName") String tableName,
                            @Param("word") String word);
}