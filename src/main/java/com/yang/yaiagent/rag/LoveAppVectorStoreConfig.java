package com.yang.yaiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
/**
 * 恋爱大师配置类(初始化基于内存的向量数据库Bean)
 */
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Bean
    // EmbeddingModel是springAI的EmbeddingModel
    VectorStore loveAppVectorStore(EmbeddingModel dashScopeEmbeddingModel) {
        // 创建一个基于内存的向量数据库
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        // 加载文档
        List<Document> documents = loveAppDocumentLoader.loadMardowns();

        //用自定义Token的分割器分割文档(不建议使用)
//        List<Document> documents1 = myTokenTextSplitter.splitDocuments(documents);

        //用自定义AI关键词增强器增强文档
        List<Document> documents1 = myKeywordEnricher.enrichDocuments(documents);
        // 将文档添加到向量数据库
        simpleVectorStore.add(documents1);
        // 返回向量数据库
        return simpleVectorStore;
    }
}
