<template>
  <div class="chat-container">
    <div class="chat-header">
      <button class="back-btn" @click="goBack">← 返回</button>
      <h1>AI 超级智能体</h1>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="(msg, index) in messages" 
        :key="index" 
        :class="['message', msg.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="message-avatar">
          {{ msg.role === 'user' ? '👤' : '🤖' }}
        </div>
        <div class="message-content">
          <div class="message-text" v-html="msg.content"></div>
        </div>
      </div>
      
      <div v-if="loading" class="message ai-message">
        <div class="message-avatar">🤖</div>
        <div class="message-content">
          <div class="message-text typing">正在分析中...</div>
        </div>
      </div>
    </div>
    
    <div class="chat-input">
      <input 
        v-model="inputMessage" 
        @keyup.enter="sendMessage"
        placeholder="输入你的问题..."
        :disabled="loading"
      />
      <button @click="sendMessage" :disabled="loading || !inputMessage.trim()">
        发送
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { chatWithManusSSE } from '../api'

interface Message {
  role: 'user' | 'ai'
  content: string
}

const router = useRouter()
const messagesContainer = ref<HTMLElement>()
const inputMessage = ref('')
const loading = ref(false)
const messages = ref<Message[]>([])

let currentEventSource: EventSource | null = null

const goBack = () => {
  if (currentEventSource) {
    currentEventSource.close()
  }
  router.push('/')
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const sendMessage = () => {
  const message = inputMessage.value.trim()
  if (!message || loading.value) return
  
  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: message
  })
  inputMessage.value = ''
  loading.value = true
  scrollToBottom()
  
  // 关闭之前的连接
  if (currentEventSource) {
    currentEventSource.close()
  }
  
  // 建立 SSE 连接
  currentEventSource = chatWithManusSSE(message)
  
  // 标记是否需要创建新的 AI 消息气泡
  let needNewBubble = true
  
  currentEventSource.onmessage = (event) => {
    const data = event.data
    if (data === '[DONE]') {
      currentEventSource?.close()
      loading.value = false
      return
    }
    
    // 每个 Step 创建新的气泡
    if (needNewBubble) {
      messages.value.push({
        role: 'ai',
        content: data
      })
      needNewBubble = false
    } else {
      // 追加到当前气泡
      const lastIndex = messages.value.length - 1
      messages.value[lastIndex].content += data
    }
    
    // 检测是否是新 Step 的开始（可以根据后端返回的格式调整）
    if (data.includes('Step') || data.includes('步骤')) {
      needNewBubble = true
    }
    
    scrollToBottom()
  }
  
  currentEventSource.onerror = (error) => {
    console.error('SSE Error:', error)
    currentEventSource?.close()
    // 如果没有收到任何消息，显示错误提示
    if (messages.value.length === 0 || messages.value[messages.value.length - 1].role !== 'ai') {
      messages.value.push({
        role: 'ai',
        content: '抱歉，连接出现问题，请重试。'
      })
    }
    loading.value = false
  }
}
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.chat-header {
  background: linear-gradient(135deg, #4a90d9 0%, #357abd 100%);
  color: white;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.chat-header h1 {
  font-size: 1.3rem;
  margin: 0;
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 0.9rem;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message {
  display: flex;
  gap: 10px;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.message-content {
  background: white;
  padding: 12px 16px;
  border-radius: 16px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.user-message .message-content {
  background: linear-gradient(135deg, #4a90d9 0%, #357abd 100%);
  color: white;
}

.message-text {
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-word;
}

.typing {
  color: #999;
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.chat-input {
  padding: 15px 20px;
  background: white;
  display: flex;
  gap: 10px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.chat-input input {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s;
}

.chat-input input:focus {
  border-color: #4a90d9;
}

.chat-input button {
  padding: 12px 24px;
  background: linear-gradient(135deg, #4a90d9 0%, #357abd 100%);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  cursor: pointer;
  transition: opacity 0.3s;
}

.chat-input button:hover:not(:disabled) {
  opacity: 0.9;
}

.chat-input button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
