package com.mushan.msenbackend.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 文章导入主程序
 * 可独立运行的文章导入工具
 */
@Slf4j
public class ArticleImportMain {

    public static void main(String[] args) {
        // 数据库连接信息
        String dbUrl = "jdbc:mysql://localhost:3306/msen?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        
        // JSON文件路径
        String jsonFilePath = "E:/JavaYuPi/MS-EN/MS-EN-backend/src/main/resources/bbc_articles.json";

        // 初始化JDBC驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("找不到MySQL JDBC驱动", e);
            return;
        }

        // 创建工具类实例并导入数据
        ArticleImportUtil articleImportUtil = new ArticleImportUtil();
        
        // 设置数据库连接信息
        articleImportUtil.setDbUrl(dbUrl);
        articleImportUtil.setUsername(username);
        articleImportUtil.setPassword(password);

        log.info("开始导入文章数据...");
        long startTime = System.currentTimeMillis();
        
        articleImportUtil.importArticlesFromJson(jsonFilePath);
        
        long endTime = System.currentTimeMillis();
        log.info("文章数据导入完成，耗时: {} ms", (endTime - startTime));
    }
}
