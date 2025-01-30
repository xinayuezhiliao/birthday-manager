import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '生日管家 - 首页' }
  },
  {
    path: '/add',
    name: 'AddContact',
    component: () => import('../views/AddContact.vue'),
    meta: { title: '添加联系人' }
  },
  {
    path: '/calendar',
    name: 'Calendar',
    component: () => import('../views/Calendar.vue'),
    meta: { title: '生日日历' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '生日管家'
  next()
})

export default router
