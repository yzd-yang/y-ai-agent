package com.yang.yaiagent.controller;


import com.yang.yaiagent.agent.YManus;
import com.yang.yaiagent.app.LoveApp;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@Slf4j
@Tag(name = "ai", description = "ai 相关接口")
@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    private LoveApp loveApp;



    /**
     * 同步调用Ai恋爱大师接口
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/love_app/chat/sync")
    public String doChatWithLoveAppSync(String message,String chatId) {
        String res = loveApp.doChat(message, chatId);

        return res;
    }
    /**
     * SSE调用Ai恋爱大师接口
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/sse",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
        Flux res = loveApp.doChatSse(message, chatId);

        return res;
    }



    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @Resource
    private ToolCallback[] allTools;
    @Resource
    private ChatModel dashscopeChatModel;

    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        YManus yManus = new YManus(allTools, dashscopeChatModel);
        SseEmitter emitter = yManus.runStream(message);
        return emitter;
    }
}
