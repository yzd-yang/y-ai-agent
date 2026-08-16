package com.yang.yaiagent.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * Y的超级智能的题(拥有自主规划能力,可以直接使用)
 */
@Component
public class YManus extends ToolCallAgent{
    public YManus(ToolCallback[] allTools, ChatModel dashscopeChatModel) {
        super(allTools);
        this.setName("yManus");
        String SYSTEM_PROMPT = """  
                You are YManus, an all-in-one Chinese AI assistant designed to handle any 
                task given to you by users. You have a bunch of tools to efficiently complete tricky requests.  
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """  
                Based on user needs, proactively select the most appropriate tool or combination of tools. 
                For complex tasks, you can break down the problem and use different tools step by step to solve it.  
                After using each tool, clearly explain the execution results and suggest the next steps.  
                If you want to stop the interaction at any point, use the `terminate` tool/function call.  
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);

        this.setMaxSteps(20);
        // 初始化客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .build();
        this.setChatClient(chatClient);
    }

}
