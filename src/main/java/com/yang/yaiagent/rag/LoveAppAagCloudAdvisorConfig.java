package com.yang.yaiagent.rag;


import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于阿里云RAG（Retrieval-Augmented Generation）的检索增强配置类
 */
@Configuration
@Slf4j
public class LoveAppAagCloudAdvisorConfig {
    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApikey;
    @Bean
    public Advisor loveAppAagCloudAdvice() {
        // 创建DashScopeApi实例(用build模式创建)
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(dashScopeApikey)
                .build();
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi
        , DashScopeDocumentRetrieverOptions.builder()
                .indexName("恋爱大师")// 指定知识库名称
                .build()
        );

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }
}
