# Y-AI-Agent

基于 Spring Boot 3 + Spring AI + 阿里云 DashScope（通义千问）构建的多智能体 AI 应用框架。

## 技术栈

- **Java 21** + **Spring Boot 3.4.3**
- **Spring AI Alibaba** + **DashScope**（通义千问大模型）
- **MySQL** + **MyBatis-Plus**（对话记忆持久化）
- **Knife4j** / **SpringDoc**（API 文档）
- **iTextPDF**（PDF 生成）、**Jsoup**（网页抓取）、**Hutool**（工具库）

## 项目结构

```
y-ai-agent/
├── src/main/java/com/yang/yaiagent/
│   ├── agent/                    # 多智能体框架
│   │   ├── BaseAgent.java        # 抽象基类：状态管理、执行循环
│   │   ├── ReActAgent.java       # ReAct 模式：思考-行动循环
│   │   ├── ToolCallAgent.java    # 工具调用代理：自动选择并执行工具
│   │   └── YManus.java           # 通用智能体：开箱即用
│   ├── tools/                    # 内置工具集
│   │   ├── FileOperationTool     # 文件读写
│   │   ├── WebSearchTool         # 百度搜索（SearchAPI）
│   │   ├── WebScrapingTool       # 网页内容抓取
│   │   ├── ResourceDownloadTool  # 资源下载
│   │   ├── PDFGenerationTool     # PDF 生成（支持中文）
│   │   └── TerminateTool         # 终止工具
│   ├── app/
│   │   └── LoveApp.java          # 恋爱大师应用（对话 + RAG + 工具 + MCP）
│   ├── rag/                      # RAG 检索增强
│   ├── chatmemory/               # 对话记忆（MySQL / 文件）
│   └── advisor/                  # 自定义 Advisor
└── yu-image-search-mcp-server/   # 图片搜索 MCP 服务（独立模块）
```

## 架构设计

### 多智能体框架

```
BaseAgent（状态管理 + 执行循环）
  └── ReActAgent（思考-行动抽象）
        └── ToolCallAgent（工具调用实现）
              └── YManus（通用智能体）
```

- **BaseAgent** — 管理代理状态（IDLE → RUNNING → FINISHED/ERROR）、消息上下文、最大步数控制
- **ReActAgent** — 定义 `think()` + `act()` 循环，先思考再行动
- **ToolCallAgent** — 自主维护工具调用上下文，自动选择工具并执行
- **YManus** — 开箱即用的通用智能体，最大 20 步执行

### LoveApp 功能

| 功能 | 说明 |
|------|------|
| 基础对话 | 基于系统提示词的恋爱专家对话 |
| 对话记忆 | 支持 MySQL / 文件 / 内存三种持久化方式 |
| 结构化输出 | 自动生成恋爱报告（标题 + 建议列表） |
| RAG 知识库 | 本地向量存储 + 查询重写 + 云端 AAG 检索增强 |
| 工具调用 | 自动调用文件、搜索、下载、PDF 等工具 |
| MCP 集成 | 支持 MCP 协议扩展外部工具 |

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.9+
- MySQL 8.0+

### 配置

设置环境变量：

```bash
export AI_DASHSCOPE_API_KEY=your-dashscope-api-key
export SEARCH_API_KEY=your-searchapi-key
```

数据库配置在 `application.yaml` 中，默认连接 `localhost:3306/y_ai_agent`。

### 运行

```bash
# 编译
mvn clean package -DskipTests

# 启动主应用
mvn spring-boot:run

# 启动图片搜索 MCP 服务（独立模块）
cd yu-image-search-mcp-server
mvn spring-boot:run
```

服务启动后访问：
- API 文档：`http://localhost:8080/api/swagger-ui.html`
- 健康检查：`http://localhost:8080/api/health`

### MCP 图片搜索服务

`yu-image-search-mcp-server` 是一个独立的 MCP Server，提供图片搜索能力，可通过 MCP 协议被主应用或其他 MCP 客户端调用。
