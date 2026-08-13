package com.yang.yaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebSearchToolTest {

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool();
        String query="AI";
        String result = webSearchTool.searchWeb(query);
        Assertions.assertNotNull(result);
    }
}