package com.mushan.msenbackend.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * 文章导入工具类
 * 用于将爬虫爬取的BBC文章JSON数据批量导入数据库
 */
@Slf4j
public class ArticleImportUtil {

    private String dbUrl;
    private String username;
    private String password;

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
     * 从JSON文件导入文章数据到数据库
     *
     * @param jsonFilePath JSON文件路径
     */
    public void importArticlesFromJson(String jsonFilePath) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;

        try {
            // 读取JSON文件
            File file = new File(jsonFilePath);
            if (!file.exists()) {
                log.error("JSON文件不存在: {}", jsonFilePath);
                return;
            }

            String jsonContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            JSONArray jsonArray = JSONUtil.parseArray(jsonContent);

            if (jsonArray.isEmpty()) {
                log.warn("JSON文件为空");
                return;
            }

            // 建立数据库连接
            conn = DriverManager.getConnection(dbUrl, username, password);
            conn.setAutoCommit(false);

            String checkSql = "SELECT COUNT(*) FROM article WHERE title = ?";
            String insertSql = "INSERT INTO article (title, content, difficulty, category, wordCount, readCount, source, publishTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            checkStmt = conn.prepareStatement(checkSql);
            insertStmt = conn.prepareStatement(insertSql);

            int successCount = 0;
            int skipCount = 0;

            // 解析并插入每篇文章
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject articleJson = jsonArray.getJSONObject(i);

                try {
                    String title = articleJson.getStr("title");
                    String content = articleJson.getStr("content");
                    Integer difficulty = articleJson.getInt("difficulty", 2);
                    String category = articleJson.getStr("category");
                    Integer wordCount = articleJson.getInt("wordCount");
                    String source = articleJson.getStr("source");

                    // 解析发布时间
                    Timestamp publishTime = null;
                    String publishTimeStr = articleJson.getStr("publishTime");
                    if (publishTimeStr != null && !publishTimeStr.isEmpty()) {
                        try {
                            ZonedDateTime zonedDateTime = ZonedDateTime.parse(publishTimeStr);
                            publishTime = Timestamp.valueOf(zonedDateTime.toLocalDateTime());
                        } catch (Exception e) {
                            log.warn("解析发布时间失败: {}", publishTimeStr);
                            publishTime = Timestamp.valueOf(LocalDateTime.now());
                        }
                    } else {
                        publishTime = Timestamp.valueOf(LocalDateTime.now());
                    }

                    // 检查是否已存在
                    checkStmt.setString(1, title);
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);
                    rs.close();

                    if (count > 0) {
                        skipCount++;
                        log.info("跳过重复文章: {}", title);
                        continue;
                    }

                    // 插入文章
                    insertStmt.setString(1, title);
                    insertStmt.setString(2, content);
                    insertStmt.setInt(3, difficulty);
                    insertStmt.setString(4, category);
                    insertStmt.setInt(5, wordCount);
                    insertStmt.setInt(6, 0); // readCount初始为0
                    insertStmt.setString(7, source);
                    insertStmt.setTimestamp(8, publishTime);

                    insertStmt.executeUpdate();
                    successCount++;
                    log.info("成功导入文章 [{}/{}]: {}", successCount, jsonArray.size(), title);

                } catch (Exception e) {
                    log.error("插入第 {} 篇文章失败: {}", i + 1, e.getMessage());
                }
            }

            conn.commit();
            log.info("文章导入完成！成功: {} 篇，跳过: {} 篇", successCount, skipCount);

        } catch (Exception e) {
            log.error("导入文章失败", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    log.error("回滚失败", ex);
                }
            }
        } finally {
            // 关闭资源
            try {
                if (insertStmt != null) insertStmt.close();
                if (checkStmt != null) checkStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                log.error("关闭连接失败", e);
            }
        }
    }


}
