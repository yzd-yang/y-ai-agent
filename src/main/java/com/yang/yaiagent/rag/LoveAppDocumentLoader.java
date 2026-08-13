package com.yang.yaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LoveAppDocumentLoader {


    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 加载markdown文件并返回文档列表
     * @return
     */
    public List<Document> loadMardowns(){
        List<Document> allDocuments=new ArrayList<>();
        //加载多篇markdown文件
        try {
            // 获取资源文件
            Resource[] resources = resourcePatternResolver.getResources("classpath*:document/*.md");
            // 遍历资源文件
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                // 获取文件状态(单身,恋爱)
                String status = filename.substring(filename.length()-6, filename.length()-4);
                // 创建MarkdownDocumentReaderConfig加载器
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        // 添加元数据
                        .withAdditionalMetadata("filename", filename)
                        .withAdditionalMetadata("status", status)
                        .build();
                MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(resource, config);
                // 获取文档
                List<Document> documents = markdownDocumentReader.get();
                // 添加文档到列表
                allDocuments.addAll(documents);
            }


        } catch (Exception e) {
            log.error("Error loading markdown files", e);
        }
        return allDocuments;
    }
}
