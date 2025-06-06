import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// 配置NProgress
NProgress.configure({ showSpinner: false })

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', icon: 'House' }
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: '/building',
        name: 'Building',
        component: () => import('@/views/building/index.vue'),
        meta: { title: '楼栋管理', icon: 'OfficeBuilding' }
      },
      {
        path: '/room',
        name: 'Room',
        component: () => import('@/views/room/index.vue'),
        meta: { title: '房间管理', icon: 'House' }
      },
      {
        path: '/accommodation',
        name: 'Accommodation',
        component: () => import('@/views/accommodation/index.vue'),
        meta: { title: '住宿管理', icon: 'UserFilled' }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User', hidden: true }
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/404.vue'),
    meta: { title: '页面不存在' }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  
  const userStore = useUserStore()
  const token = userStore.token
  
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 绵阳城市学院学生宿舍管理系统` : '绵阳城市学院学生宿舍管理系统'
  
  if (to.meta.requiresAuth === false) {
    // 不需要认证的页面
    if (token && to.path === '/login') {
      // 已登录用户访问登录页，重定向到首页
      next('/')
    } else {
      next()
    }
  } else {
    // 需要认证的页面
    if (token) {
      // 已登录
      if (!userStore.userInfo) {
        // 获取用户信息
        try {
          await userStore.getUserInfo()
          next()
        } catch (error) {
          // 获取用户信息失败，清除token并跳转到登录页
          userStore.logout()
          next('/login')
        }
      } else {
        next()
      }
    } else {
      // 未登录，跳转到登录页
      next('/login')
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router