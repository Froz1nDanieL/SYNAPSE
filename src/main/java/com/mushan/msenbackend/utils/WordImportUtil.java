package com.mushan.msenbackend.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 单词导入工具类
 * 用于将CSV文件中的单词数据导入到MySQL数据库中
 */
@Slf4j
@Component
public class WordImportUtil {

    private String dbUrl = "jdbc:mysql://localhost:3306/msen?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    
    private String username = "root";
    
    private String password = "123456";

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 将CSV文件中的数据导入到数据库中
     *
     * @param csvFilePath CSV文件路径
     */
    public void importWordsFromCsv(String csvFilePath) {
        Connection connection = null;
        PreparedStatement preparedStatementCET4 = null;
        PreparedStatement preparedStatementCET6 = null;
        PreparedStatement preparedStatementIELTS = null;
        PreparedStatement preparedStatementTOEFL = null;
        PreparedStatement preparedStatementZK = null;
        PreparedStatement preparedStatementGK = null;
        PreparedStatement preparedStatementKY = null;
        PreparedStatement preparedStatementGRE = null;
        BufferedReader bufferedReader = null;

        try {
            // 建立数据库连接
            connection = DriverManager.getConnection(dbUrl, username, password);

            // 准备插入语句
            String insertSQL = "INSERT INTO {tableName} (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE word=word";
            
            preparedStatementCET4 = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_cet4"));
            preparedStatementCET6 = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_cet6"));
            preparedStatementIELTS = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_ielts"));
            preparedStatementTOEFL = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_toefl"));
            preparedStatementZK = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_zk"));
            preparedStatementGK = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_gk"));
            preparedStatementKY = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_ky"));
            preparedStatementGRE = connection.prepareStatement(insertSQL.replace("{tableName}", "engdict_gre"));

            // 读取CSV文件
            bufferedReader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            boolean isFirstLine = true; // 跳过标题行
            int batchSize = 1000; // 批处理大小
            int count = 0;

            while ((line = bufferedReader.readLine()) != null) {
                // 跳过标题行
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // 解析CSV行
                String[] fields = parseCsvLine(line);
                
                // 根据tag决定插入哪个表
                String tag = fields[7];
                PreparedStatement statementToUse = determinePreparedStatement(tag, preparedStatementCET4, preparedStatementCET6, 
                                                                               preparedStatementIELTS, preparedStatementTOEFL, 
                                                                               preparedStatementZK, preparedStatementGK,
                                                                               preparedStatementKY, preparedStatementGRE);

                // 设置参数
                statementToUse.setString(1, fields[0]);  // word
                statementToUse.setString(2, fields[1]);  // phonetic
                statementToUse.setString(3, fields[2]);  // definition
                statementToUse.setString(4, fields[3]);  // translation
                statementToUse.setString(5, fields[4]);  // pos
                statementToUse.setObject(6, parseInteger(fields[5]));  // collins
                statementToUse.setObject(7, parseInteger(fields[6]));  // oxford
                statementToUse.setString(8, fields[7]);  // tag
                statementToUse.setObject(9, parseInteger(fields[8]));  // bnc
                statementToUse.setObject(10, parseInteger(fields[9])); // frq
                statementToUse.setString(11, fields[10]); // exchange
                statementToUse.setString(12, fields[11]); // detail
                statementToUse.setString(13, fields[12]); // audio

                statementToUse.addBatch();

                count++;
                // 批量执行
                if (count % batchSize == 0) {
                    executeBatches(preparedStatementCET4, preparedStatementCET6, preparedStatementIELTS, 
                                   preparedStatementTOEFL, preparedStatementZK, preparedStatementGK, preparedStatementKY, preparedStatementGRE);
                    log.info("已导入 {} 条记录", count);
                }
            }

            // 执行剩余的批次
            executeBatches(preparedStatementCET4, preparedStatementCET6, preparedStatementIELTS, 
                           preparedStatementTOEFL, preparedStatementZK, preparedStatementGK, preparedStatementKY, preparedStatementGRE);
            log.info("数据导入完成，总共导入 {} 条记录", count);

        } catch (Exception e) {
            log.error("导入单词数据时发生错误", e);
        } finally {
            // 关闭资源
            try {
                if (preparedStatementCET4 != null) {
                    preparedStatementCET4.close();
                }
                if (preparedStatementCET6 != null) {
                    preparedStatementCET6.close();
                }
                if (preparedStatementIELTS != null) {
                    preparedStatementIELTS.close();
                }
                if (preparedStatementTOEFL != null) {
                    preparedStatementTOEFL.close();
                }
                if (preparedStatementZK != null) {
                    preparedStatementZK.close();
                }
                if (preparedStatementGK != null) {
                    preparedStatementGK.close();
                }
                if (preparedStatementKY != null) {
                    preparedStatementKY.close();
                }
                if (preparedStatementGRE != null) {
                    preparedStatementGRE.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                log.error("关闭资源时发生错误", e);
            }
        }
    }

    /**
     * 根据标签确定使用哪个PreparedStatement
     * @param tag 标签
     * @param cet4Statement 四级表Statement
     * @param cet6Statement 六级表Statement
     * @param ieltsStatement 雅思表Statement
     * @param toeflStatement 托福表Statement
     * @param zkStatement 中考表Statement
     * @param gkStatement 高考表Statement
     * @param kyStatement 考研表Statement
     * @param greStatement GRE表Statement
     * @return 对应的PreparedStatement
     */
    private PreparedStatement determinePreparedStatement(String tag, PreparedStatement cet4Statement, 
                                                       PreparedStatement cet6Statement, PreparedStatement ieltsStatement, 
                                                       PreparedStatement toeflStatement, PreparedStatement zkStatement,
                                                       PreparedStatement gkStatement, PreparedStatement kyStatement,
                                                       PreparedStatement greStatement) {
        if (tag == null) {
            return cet4Statement; // 默认使用四级表
        }
        
        String lowerTag = tag.toLowerCase();
        if (lowerTag.contains("cet4") || lowerTag.contains("四级")) {
            return cet4Statement;
        } else if (lowerTag.contains("cet6") || lowerTag.contains("六级")) {
            return cet6Statement;
        } else if (lowerTag.contains("ielts") || lowerTag.contains("雅思")) {
            return ieltsStatement;
        } else if (lowerTag.contains("toefl") || lowerTag.contains("托福")) {
            return toeflStatement;
        } else if (lowerTag.contains("zk") || lowerTag.contains("中考")) {
            return zkStatement;
        } else if (lowerTag.contains("gk") || lowerTag.contains("高考")) {
            return gkStatement;
        } else if (lowerTag.contains("ky") || lowerTag.contains("考研")) {
            return kyStatement;
        } else if (lowerTag.contains("gre")) {
            return greStatement;
        } else {
            return cet4Statement; // 默认使用四级表
        }
    }

    /**
     * 执行所有批处理
     * @param statements PreparedStatement列表
     * @throws SQLException SQL异常
     */
    private void executeBatches(PreparedStatement... statements) throws SQLException {
        for (PreparedStatement statement : statements) {
            statement.executeBatch();
            statement.clearBatch();
        }
    }

    /**
     * 解析CSV行，处理包含逗号的字段（被引号包围）
     *
     * @param line CSV行
     * @return 字段数组
     */
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // 如果是双引号，切换引号状态
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // 如果是逗号且不在引号内，则为字段分隔符
                fields.add(currentField.toString().trim());
                currentField.setLength(0); // 清空当前字段
            } else {
                // 其他字符添加到当前字段
                currentField.append(c);
            }
        }

        // 添加最后一个字段
        fields.add(currentField.toString().trim());

        // 确保有13个字段
        while (fields.size() < 13) {
            fields.add("");
        }

        // 处理引号
        String[] result = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            result[i] = fields.get(i).replaceAll("^\"|\"$", ""); // 去掉首尾的引号
        }

        return result;
    }

    /**
     * 解析整数，处理空字符串
     *
     * @param str 字符串
     * @return 整数值或null
     */
    private Integer parseInteger(String str) {
        if (StrUtil.isBlank(str) || "0".equals(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}