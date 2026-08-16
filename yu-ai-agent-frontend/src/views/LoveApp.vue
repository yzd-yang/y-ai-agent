<template>
  <div class="chat-container">
    <div class="chat-header">
      <button class="back-btn" @click="goBack">← 返回</button>
      <h1>AI 恋爱大师</h1>
      <div class="chat-id">会话ID: {{ chatId }}</div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="(msg, index) in messages" 
        :key="index" 
        :class="['message', msg.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="message-avatar">
          {{ msg.role === 'user' ? '👤' : '💕' }}
        </div>
        <div class="message-content">
          <div class="message-text" v-html="msg.content"></div>
        </div>
      </div>
      
      <div v-if="loading" class="message ai-message">
        <div class="message-avatar">💕</div>
        <div class="message-content">
          <div class="message-text typing">正在思考中...</div>
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
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { chatWithLoveAppSSE } from '../api'

interface Message {
  role: 'user' | 'ai'
  content: string
}

const router = useRouter()
const messagesContainer = ref<HTMLElement>()
const inputMessage = ref('')
const loading = ref(false)
const messages = ref<Message[]>([])

// 生成唯一的聊天室ID
const chatId = ref(`chat_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`)

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
  
  // 创建 AI 消息占位
  const aiMessageIndex = messages.value.length
  messages.value.push({
    role: 'ai',
    content: ''
  })
  
  // 关闭之前的连接
  if (currentEventSource) {
    currentEventSource.close()
  }
  
  // 建立 SSE 连接
  currentEventSource = chatWithLoveAppSSE(message, chatId.value)
  
  currentEventSource.onmessage = (event) => {
    const data = event.data
    if (data === '[DONE]') {
      currentEventSource?.close()
      loading.value = false
      return
    }
    messages.value[aiMessageIndex].content += data
    scrollToBottom()
  }
  
  currentEventSource.onerror = (error) => {
    console.error('SSE Error:', error)
    currentEventSource?.close()
    if (messages.value[aiMessageIndex].content === '') {
      messages.value[aiMessageIndex].content = '抱歉，连接出现问题，请重试。'
    }
    loading.value = false
  }
}

onMounted(() => {
  // 添加欢迎消息
  messages.value.push({
    role: 'ai',
    content: '你好！我是 AI 恋爱大师，很高兴为你解答情感方面的困惑。请告诉我你的问题吧！'
  })
})
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.chat-header {
  background: linear-gradient(135deg, #ff6b9d 0%, #c44569 100%);
  color: white;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
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

.chat-id {
  font-size: 0.8rem;
  opacity: 0.8;
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  border-color: #ff6b9d;
}

.chat-input button {
  padding: 12px 24px;
  background: linear-gradient(135deg, #ff6b9d 0%, #c44569 100%);
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
