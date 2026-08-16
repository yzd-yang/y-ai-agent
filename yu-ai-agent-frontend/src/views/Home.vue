<template>
  <div class="home-container">
    <h1 class="home-title">AI 应用中心</h1>
    <p class="home-subtitle">选择一个 AI 应用开始体验</p>
    
    <div class="app-cards">
      <div class="app-card" @click="goToLove">
        <div class="card-icon">💕</div>
        <h2>AI 恋爱大师</h2>
        <p>专业的 AI 恋爱顾问，为你解答情感困惑</p>
      </div>
      
      <div class="app-card" @click="goToManus">
        <div class="card-icon">🤖</div>
        <h2>AI 超级智能体</h2>
        <p>强大的 AI 智能助手，帮你解决各种问题</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLove = () => {
  router.push('/love')
}

const goToManus = () => {
  router.push('/manus')
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.home-title {
  font-size: 3rem;
  color: white;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.home-subtitle {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 40px;
}

.app-cards {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
  justify-content: center;
}

.app-card {
  background: white;
  border-radius: 16px;
  padding: 40px;
  width: 280px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.app-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.card-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.app-card h2 {
  color: #333;
  margin-bottom: 10px;
  font-size: 1.5rem;
}

.app-card p {
  color: #666;
  font-size: 0.95rem;
  line-height: 1.5;
}
</style>
