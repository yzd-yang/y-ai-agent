import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('../views/Home.vue')
    },
    {
      path: '/love',
      name: 'LoveApp',
      component: () => import('../views/LoveApp.vue')
    },
    {
      path: '/manus',
      name: 'ManusApp',
      component: () => import('../views/ManusApp.vue')
    }
  ]
})

export default router
