package com.mushan.msenbackend.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 单词导入主程序
 * 可独立运行的单词导入工具
 */
@Slf4j
public class WordImportMain {

    public static void main(String[] args) {
        // 数据库连接信息
        String dbUrl = "jdbc:mysql://localhost:3306/msen?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        
        // CSV文件路径
        String csvFilePath = "E:/JavaYuPi/MS-EN/MS-EN-backend/src/main/resources/ecdict.csv";

        // 初始化JDBC驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("找不到MySQL JDBC驱动", e);
            return;
        }

        // 创建工具类实例并导入数据
        WordImportUtil wordImportUtil = new WordImportUtil();
        
        // 设置数据库连接信息
        wordImportUtil.setDbUrl(dbUrl);
        wordImportUtil.setUsername(username);
        wordImportUtil.setPassword(password);

        log.info("开始导入单词数据...");
        long startTime = System.currentTimeMillis();
        
        wordImportUtil.importWordsFromCsv(csvFilePath);
        
        long endTime = System.currentTimeMillis();
        log.info("单词数据导入完成，耗时: {} ms", (endTime - startTime));
    }
}