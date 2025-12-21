package com.mushan.msenbackend.utils;

import com.mushan.msenbackend.model.vo.WordCardVO;
import org.apache.commons.lang3.StringUtils;

/**
 * 单词词形变化解析工具类
 */
public class WordExchangeUtil {

    /**
     * 解析单词变形字符串
     * 格式：s:sorts/d:sorted/i:sorting/p:sorted/3:sorts
     * 
     * @param exchange 原始exchange字符串
     * @return 解析后的ExchangeInfo对象，如果为空则返回null
     */
    public static WordCardVO.ExchangeInfo parseExchange(String exchange) {
        if (StringUtils.isBlank(exchange)) {
            return null;
        }

        WordCardVO.ExchangeInfo info = new WordCardVO.ExchangeInfo();
        String[] parts = exchange.split("/");
        
        for (String part : parts) {
            if (StringUtils.isBlank(part) || !part.contains(":")) {
                continue;
            }
            
            String[] kv = part.split(":", 2);
            if (kv.length < 2) {
                continue;
            }
            
            String key = kv[0].trim();
            String value = kv[1].trim();
            
            switch (key) {
                case "s":
                    info.setPlurals(value);
                    break;
                case "d":
                    info.setPastTense(value);
                    break;
                case "i":
                    info.setPresentParticiple(value);
                    break;
                case "p":
                    info.setPastParticiple(value);
                    break;
                case "3":
                    info.setThirdPersonSingular(value);
                    break;
                case "r":
                    info.setComparative(value);
                    break;
                case "t":
                    info.setSuperlative(value);
                    break;
                case "0":
                    info.setLemma(value);
                    break;
                default:
                    // 忽略未知的类型
                    break;
            }
        }
        
        return info;
    }
}
