-- 创建分片后的单词表结构

CREATE TABLE IF NOT EXISTS `engdict_cet4` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
  `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
  `definition` TEXT COMMENT '英文释义',
  `translation` TEXT COMMENT '中文释义',
  `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
  `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
  `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
  `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
  `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
  `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
  `exchange` TEXT COMMENT '时态复数等变换',
  `detail` TEXT COMMENT '扩展信息',
  `audio` TEXT DEFAULT NULL COMMENT '音频URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`),
  KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='英语四级单词表';

CREATE TABLE IF NOT EXISTS `engdict_cet6` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
  `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
  `definition` TEXT COMMENT '英文释义',
  `translation` TEXT COMMENT '中文释义',
  `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
  `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
  `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
  `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
  `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
  `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
  `exchange` TEXT COMMENT '时态复数等变换',
  `detail` TEXT COMMENT '扩展信息',
  `audio` TEXT DEFAULT NULL COMMENT '音频URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`),
  KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='英语六级单词表';

CREATE TABLE IF NOT EXISTS `engdict_ielts` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
  `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
  `definition` TEXT COMMENT '英文释义',
  `translation` TEXT COMMENT '中文释义',
  `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
  `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
  `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
  `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
  `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
  `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
  `exchange` TEXT COMMENT '时态复数等变换',
  `detail` TEXT COMMENT '扩展信息',
  `audio` TEXT DEFAULT NULL COMMENT '音频URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`),
  KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='雅思单词表';

CREATE TABLE IF NOT EXISTS `engdict_toefl` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
  `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
  `definition` TEXT COMMENT '英文释义',
  `translation` TEXT COMMENT '中文释义',
  `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
  `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
  `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
  `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
  `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
  `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
  `exchange` TEXT COMMENT '时态复数等变换',
  `detail` TEXT COMMENT '扩展信息',
  `audio` TEXT DEFAULT NULL COMMENT '音频URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`),
  KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='托福单词表';

CREATE TABLE IF NOT EXISTS `engdict_zk` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
  `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
  `definition` TEXT COMMENT '英文释义',
  `translation` TEXT COMMENT '中文释义',
  `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
  `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
  `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
  `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
  `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
  `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
  `exchange` TEXT COMMENT '时态复数等变换',
  `detail` TEXT COMMENT '扩展信息',
  `audio` TEXT DEFAULT NULL COMMENT '音频URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`),
  KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='中考单词表';

CREATE TABLE IF NOT EXISTS `engdict_gk` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
  `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
  `definition` TEXT COMMENT '英文释义',
  `translation` TEXT COMMENT '中文释义',
  `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
  `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
  `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
  `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
  `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
  `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
  `exchange` TEXT COMMENT '时态复数等变换',
  `detail` TEXT COMMENT '扩展信息',
  `audio` TEXT DEFAULT NULL COMMENT '音频URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`),
  KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='高考单词表';

CREATE TABLE IF NOT EXISTS `engdict_ky` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
  `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
  `definition` TEXT COMMENT '英文释义',
  `translation` TEXT COMMENT '中文释义',
  `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
  `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
  `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
  `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
  `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
  `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
  `exchange` TEXT COMMENT '时态复数等变换',
  `detail` TEXT COMMENT '扩展信息',
  `audio` TEXT DEFAULT NULL COMMENT '音频URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`),
  KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='考研单词表';

CREATE TABLE IF NOT EXISTS `engdict_gre` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT '主键',
    `word` VARCHAR(128) NOT NULL COMMENT '单词名称',
    `phonetic` VARCHAR(64) DEFAULT NULL COMMENT '音标',
    `definition` TEXT COMMENT '英文释义',
    `translation` TEXT COMMENT '中文释义',
    `pos` VARCHAR(16) DEFAULT NULL COMMENT '词性',
    `collins` INTEGER DEFAULT 0 COMMENT '柯林斯星级',
    `oxford` INTEGER DEFAULT 0 COMMENT '是否是牛津三千核心词汇',
    `tag` VARCHAR(64) DEFAULT NULL COMMENT '标签',
    `bnc` INTEGER DEFAULT NULL COMMENT '英国国家语料库词频顺序',
    `frq` INTEGER DEFAULT NULL COMMENT '当代语料库词频顺序',
    `exchange` TEXT COMMENT '时态复数等变换',
    `detail` TEXT COMMENT '扩展信息',
    `audio` TEXT DEFAULT NULL COMMENT '音频URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_word` (`word`),
    KEY `idx_tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GRE单词表';



-- 插入 cet4 标签的单词到 engdict_cet4 表
INSERT INTO engdict_cet4 (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%cet4%' OR tag LIKE '%四级%';

-- 插入 cet6 标签的单词到 engdict_cet6 表
INSERT INTO engdict_cet6 (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%cet6%' OR tag LIKE '%六级%';

-- 插入 zk 标签的单词到 engdict_zk 表
INSERT INTO engdict_zk (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%zk%' OR tag LIKE '%中考%';

-- 插入 gk 标签的单词到 engdict_gk 表
INSERT INTO engdict_gk (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%gk%' OR tag LIKE '%高考%';

-- 插入 ielts 标签的单词到 engdict_ielts 表
INSERT INTO engdict_ielts (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%ielts%' OR tag LIKE '%雅思%';

-- 插入 toefl 标签的单词到 engdict_toefl 表
INSERT INTO engdict_toefl (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%toefl%' OR tag LIKE '%托福%';

-- 插入 ky 标签的单词到 engdict_ky 表
INSERT INTO engdict_ky (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%ky%' OR tag LIKE '%考研%';

-- 插入 gre 标签的单词到 engdict_gre 表
INSERT INTO engdict_gre (word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
SELECT word, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio
FROM engdict
WHERE tag LIKE '%gre%' OR tag LIKE '%GRE%';