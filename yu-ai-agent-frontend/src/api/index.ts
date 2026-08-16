import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 60000
})

/**
 * AI 恋爱大师 SSE 聊天接口
 * 通过 EventSource 实现 SSE 连接
 */
export function chatWithLoveAppSSE(message: string, chatId: string): EventSource {
  const url = `/api/ai/love_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`
  return new EventSource(url)
}

/**
 * AI 超级智能体 SSE 聊天接口
 * 通过 EventSource 实现 SSE 连接
 */
export function chatWithManusSSE(message: string): EventSource {
  const url = `/api/ai/manus/chat?message=${encodeURIComponent(message)}`
  return new EventSource(url)
}

export default api
