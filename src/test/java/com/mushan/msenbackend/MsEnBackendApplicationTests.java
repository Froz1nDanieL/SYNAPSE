package com.mushan.msenbackend;

import com.mushan.msenbackend.model.entity.Engdict;
import com.mushan.msenbackend.service.EngdictService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MsEnBackendApplicationTests {

    @Resource
    private EngdictService engdictService;

    @Test
    public void testSharding() {
        // 测试ShardingSphere是否能根据tag路由到分表
        
        // 创建不同的测试数据
        List<Engdict> testDataList = new ArrayList<>();
        
        // 测试cet4标签
        Engdict cet4Dict = new Engdict();
        cet4Dict.setWord("test_cet4");
        cet4Dict.setTag("cet4");
        cet4Dict.setTranslation("测试(cet4)");
        testDataList.add(cet4Dict);
        
        // 测试cet6标签
        Engdict cet6Dict = new Engdict();
        cet6Dict.setWord("test_cet6");
        cet6Dict.setTag("cet6");
        cet6Dict.setTranslation("测试(cet6)");
        testDataList.add(cet6Dict);
        
        // 测试zk标签
        Engdict zkDict = new Engdict();
        zkDict.setWord("test_zk");
        zkDict.setTag("zk");
        zkDict.setTranslation("测试(zk)");
        testDataList.add(zkDict);
        
        // 测试gk标签
        Engdict gkDict = new Engdict();
        gkDict.setWord("test_gk");
        gkDict.setTag("gk");
        gkDict.setTranslation("测试(gk)");
        testDataList.add(gkDict);
        
        // 测试ky标签
        Engdict kyDict = new Engdict();
        kyDict.setWord("test_ky");
        kyDict.setTag("ky");
        kyDict.setTranslation("测试(ky)");
        testDataList.add(kyDict);
        
        // 测试ielts标签
        Engdict ieltsDict = new Engdict();
        ieltsDict.setWord("test_ielts");
        ieltsDict.setTag("ielts");
        ieltsDict.setTranslation("测试(ielts)");
        testDataList.add(ieltsDict);
        
        // 测试toefl标签
        Engdict toeflDict = new Engdict();
        toeflDict.setWord("test_toefl");
        toeflDict.setTag("toefl");
        toeflDict.setTranslation("测试(toefl)");
        testDataList.add(toeflDict);
        
        // 测试无标签的情况
        Engdict noTagDict = new Engdict();
        noTagDict.setWord("test_no_tag");
        noTagDict.setTranslation("测试(无标签)");
        testDataList.add(noTagDict);
        
        // 批量保存测试数据
        for (Engdict dict : testDataList) {
            boolean saveResult = engdictService.save(dict);
            System.out.println("保存单词: " + dict.getWord() + ", 标签: " + dict.getTag() + ", 结果: " + saveResult);
        }
        
        // 验证数据是否正确保存
        System.out.println("\n=== 验证数据保存 ===");
        for (Engdict dict : testDataList) {
            // 关键：查询时必须带上tag字段，否则ShardingSphere无法路由
            Engdict queryResult = engdictService.lambdaQuery()
                    .eq(Engdict::getWord, dict.getWord())
                    .eq(Engdict::getTag, dict.getTag())  // 必须带上分片键！
                    .one();
            
            if (queryResult != null) {
                System.out.println("查询到单词: " + queryResult.getWord() + 
                    ", 标签: " + queryResult.getTag() + 
                    ", 翻译: " + queryResult.getTranslation());
            } else {
                System.out.println("未找到单词: " + dict.getWord());
            }
        }
        
        System.out.println("\n测试完成，请检查日志中ShardingSphere是否显示正确的路由SQL");
    }
}