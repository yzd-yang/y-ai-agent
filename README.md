# Y-AI-Agent

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green.svg)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.5-brightgreen.svg)](https://vuejs.org/)

一个基于 **Spring Boot 3 + Spring AI + 阿里云 DashScope（通义千问）** 构建的全栈多智能体 AI 应用平台，包含后端服务和前端界面，支持多种 AI 对话场景和自主工具调用能力。

## 功能特性

### AI 恋爱大师
- 基于专业提示词的恋爱顾问对话
- SSE（Server-Sent Events）流式输出，实时打字效果
- 支持同步 / 流式两种调用方式
- 对话记忆持久化（MySQL / 文件 / 内存三种模式）
- 结构化输出（自动生成恋爱报告：标题 + 建议列表）

### AI 超级智能体（YManus）
- 自主规划能力，可分解复杂任务并逐步执行
- 内置工具集自动选择与调用
- 最大 20 步执行循环，支持中途终止
- SSE 流式输出每步执行结果

### RAG 检索增强
- 本地向量存储（SimpleVectorStore）
- 文档加载与智能分块（Markdown Document Reader）
- 查询重写（Query Rewriter）提升检索精度
- 上下文增强（Contextual Query Augmenter）
- 云端 AAG 检索增强

### MCP 协议集成
- 支持 MCP（Model Context Protocol）协议扩展外部工具
- 内置图片搜索 MCP Server（独立模块）

### 前端界面
- Vue 3 + TypeScript + Vite 构建
- 响应式聊天界面，支持 SSE 实时流式展示
- 应用中心首页，一键切换不同 AI 应用

## 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 21 | 运行环境 |
| Spring Boot | 3.4.3 | 应用框架 |
| Spring AI Alibaba | 1.1.2.0 | AI Agent 框架 |
| DashScope SDK | 2.22.28 | 通义千问大模型 |
| MyBatis-Plus | 3.5.12 | ORM 框架 |
| MySQL | 8.0+ | 数据库 |
| Knife4j / SpringDoc | 5.2.3 / 2.6.0 | API 文档 |
| iTextPDF | 9.1.0 | PDF 生成 |
| Jsoup | 1.19.1 | 网页抓取 |
| Hutool | 5.8.46 | 工具库 |
| Kryo | 5.6.2 | 序列化 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.41 | 前端框架 |
| TypeScript | 6.0.2 | 类型系统 |
| Vite | 8.2.0 | 构建工具 |
| Vue Router | 4.6.4 | 路由管理 |
| Axios | 1.19.0 | HTTP 客户端 |

## 项目结构

```
y-ai-agent/
├── src/main/java/com/yang/yaiagent/
│   ├── agent/                        # 多智能体框架
│   │   ├── BaseAgent.java            # 抽象基类：状态管理、执行循环
│   │   ├── ReActAgent.java           # ReAct 模式：思考-行动循环
│   │   ├── ToolCallAgent.java        # 工具调用代理：自动选择并执行工具
│   │   ├── YManus.java               # 通用智能体：开箱即用
│   │   └── model/AgentState.java     # 代理状态枚举
│   ├── app/
│   │   └── LoveApp.java              # 恋爱大师应用（对话 + RAG + 工具 + MCP）
│   ├── controller/
│   │   ├── AiController.java         # AI 接口（恋爱大师 + YManus）
│   │   └── HealthController.java     # 健康检查
│   ├── config/
│   │   └── CorsConfig.java           # 全局跨域配置
│   ├── tools/                        # 内置工具集
│   │   ├── FileOperationTool.java    # 文件读写
│   │   ├── WebSearchTool.java        # 百度搜索（SearchAPI）
│   │   ├── WebScrapingTool.java      # 网页内容抓取
│   │   ├── ResourceDownloadTool.java # 资源下载
│   │   ├── PDFGenerationTool.java    # PDF 生成（支持中文）
│   │   ├── TerminateTool.java        # 终止工具
│   │   └── ToolRegistration.java     # 工具注册
│   ├── rag/                          # RAG 检索增强
│   │   ├── LoveAppDocumentLoader.java
│   │   ├── LoveAppVectorStoreConfig.java
│   │   ├── QueryRewriter.java
│   │   └── ...
│   ├── chatmemory/                   # 对话记忆
│   │   ├── MyBatisPlusChatMemoryRepository.java
│   │   └── FileBasedChatMemory.java
│   ├── advisor/                      # 自定义 Advisor
│   │   └── ReReadingAdvisor.java
│   ├── domain/                       # 实体类
│   ├── mapper/                       # MyBatis Mapper
│   ├── utls/                         # 工具类
│   └── constant/                     # 常量
├── src/main/resources/
│   ├── application.yaml              # 主配置
│   └── application-prod.yaml         # 生产环境配置
├── yu-ai-agent-frontend/             # 前端项目
│   ├── src/
│   │   ├── views/
│   │   │   ├── Home.vue              # 应用中心首页
│   │   │   ├── LoveApp.vue           # 恋爱大师聊天页
│   │   │   └── ManusApp.vue          # 超级智能体聊天页
│   │   ├── api/index.ts              # API 接口封装
│   │   ├── router/index.ts           # 路由配置
│   │   ├── App.vue                   # 根组件
│   │   └── main.ts                   # 入口文件
│   ├── Dockerfile                    # 前端 Docker 镜像
│   └── nginx.conf                    # Nginx 配置
├── yu-image-search-mcp-server/       # 图片搜索 MCP 服务（独立模块）
├── Dockerfile                        # 后端 Docker 镜像
├── pom.xml                           # Maven 配置
└── LICENSE                           # MIT 开源许可证
```

## 架构设计

### 多智能体框架

```
BaseAgent（状态管理 + 执行循环）
  └── ReActAgent（思考-行动抽象）
        └── ToolCallAgent（工具调用实现）
              └── YManus（通用智能体）
```

- **BaseAgent** — 管理代理状态（IDLE → RUNNING → FINISHED/ERROR）、消息上下文、最大步数控制，支持同步和 SSE 流式两种执行模式
- **ReActAgent** — 定义 `think()` + `act()` 循环，先思考再行动
- **ToolCallAgent** — 自主维护工具调用上下文，自动选择工具并执行
- **YManus** — 开箱即用的通用智能体，最大 20 步执行，集成全部内置工具

### LoveApp 架构

```
用户请求 → LoveApp → ChatClient（DashScope）
                        ├── 系统提示词（恋爱专家人设）
                        ├── 对话记忆（MySQL / 文件 / 内存）
                        ├── RAG 检索增强（向量存储 + 查询重写）
                        ├── 工具调用（文件/搜索/下载/PDF）
                        └── MCP 协议扩展（图片搜索等）
```

### 前后端通信

```
Vue 3 前端 ──SSE──→ Spring Boot 后端 ──→ DashScope（通义千问）
   │                    │
   │ EventSource        │ ChatClient
   │ /api/ai/*          │ ToolCallback[]
   └────────────────────┘
```

## 快速开始

### 环境要求

- **JDK 21+**
- **Maven 3.9+**
- **MySQL 8.0+**
- **Node.js 20+**（前端开发）

### 1. 克隆项目

```bash
git clone https://github.com/your-username/y-ai-agent.git
cd y-ai-agent
```

### 2. 配置环境变量

```bash
# 必需：阿里云 DashScope API Key
export AI_DASHSCOPE_API_KEY=your-dashscope-api-key

# 可选：SearchAPI Key（用于网页搜索工具）
export SEARCH_API_KEY=your-searchapi-key
```

### 3. 配置数据库

创建 MySQL 数据库：

```sql
CREATE DATABASE y_ai_agent DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

数据库连接配置在 `src/main/resources/application.yaml` 中，默认连接 `localhost:3306/y_ai_agent`，用户名 `root`，密码 `123456`。

### 4. 启动后端

```bash
# 编译并跳过测试
mvn clean package -DskipTests

# 启动主应用
mvn spring-boot:run

# 或者直接运行 jar
java -jar target/y-ai-agent-0.0.1-SNAPSHOT.jar
```

### 5. 启动前端

```bash
cd yu-ai-agent-frontend

# 安装依赖
npm install

# 启动开发服务器（端口 3000）
npm run dev
```

### 6. 访问应用

| 服务 | 地址 |
|------|------|
| 前端首页 | http://localhost:3000 |
| API 文档 | http://localhost:8080/api/swagger-ui.html |
| 健康检查 | http://localhost:8080/api/health |

### 7. 启动图片搜索 MCP 服务（可选）

```bash
cd yu-image-search-mcp-server
mvn spring-boot:run
```

## API 接口

### AI 恋爱大师

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/ai/love_app/chat/sync?message=xxx&chatId=xxx` | 同步调用 |
| GET | `/api/ai/love_app/chat/sse?message=xxx&chatId=xxx` | SSE 流式调用 |

### AI 超级智能体

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/ai/manus/chat?message=xxx` | SSE 流式调用 |

### 健康检查

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/health` | 服务健康状态 |

## Docker 部署

### 后端

```bash
# 构建镜像
docker build -t y-ai-agent-backend .

# 运行容器
docker run -d -p 8123:8123 \
  -e AI_DASHSCOPE_API_KEY=your-key \
  -e SEARCH_API_KEY=your-key \
  y-ai-agent-backend
```

### 前端

```bash
cd yu-ai-agent-frontend

# 构建镜像
docker build -t y-ai-agent-frontend .

# 运行容器
docker run -d -p 80:80 y-ai-agent-frontend
```

## 内置工具集

| 工具 | 功能 | 说明 |
|------|------|------|
| FileOperationTool | 文件读写 | 本地文件系统操作 |
| WebSearchTool | 网页搜索 | 基于 SearchAPI 的百度搜索 |
| WebScrapingTool | 网页抓取 | 使用 Jsoup 抓取网页内容 |
| ResourceDownloadTool | 资源下载 | 下载网络资源到本地 |
| PDFGenerationTool | PDF 生成 | 支持中文的 PDF 文档生成 |
| TerminateTool | 终止 | 终止智能体执行循环 |

## 许可证

本项目基于 [MIT License](LICENSE) 开源。
