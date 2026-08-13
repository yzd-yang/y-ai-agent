package com.yang.yaiagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;

        @Test
        void testChat() {
            String chatId = UUID.randomUUID().toString();
            // 第一轮
            String message = "你好，我是程序员鱼皮";
            String answer = loveApp.doChat(message, chatId);
            Assertions.assertNotNull(answer);
            // 第二轮
            message = "我想让另一半（编程导航）更爱我";
            answer = loveApp.doChat(message, chatId);
            Assertions.assertNotNull(answer);
            // 第三轮
            message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
            answer = loveApp.doChat(message, chatId);
            Assertions.assertNotNull(answer);
        }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员大大怪，我想让另一半（编程导航）更爱我，但我不知道该怎么做";
        LoveApp.LoveReport report = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(report);

    }

    /**
     * 测试使用本地RAG
     */
    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer =  loveApp.doChatWithVectorStore(message, chatId);
        Assertions.assertNotNull(answer);
    }

    /**
     * 测试使用阿里云Cloud的RAG
     */
    @Test
    void doChatWithAagCloudAdvice() {
        String chatId = UUID.randomUUID().toString();
        String message = "给我推荐一些好看的女孩子";
        String answer =  loveApp.doChatWithAagCloudAdvice(message, chatId);
        Assertions.assertNotNull(answer);
    }



    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");

        // 测试网页抓取：恋爱案例分析
        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");



        // 测试文件操作：保存用户档案
        testMessage("保存我的恋爱档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTool(message, chatId);
        Assertions.assertNotNull(answer);
    }

}
