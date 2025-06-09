import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../components/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../components/AdminDashboard.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'DashboardHome',
        component: () => import('../components/Dashboard.vue')
      },
      {
        path: 'accommodations',
        name: 'AccommodationManagement',
        component: () => import('../components/StudentManagement.vue')
      },
      {
        path: 'dormitories',
        name: 'DormitoryManagement',
        component: () => import('../components/DormitoryManagement.vue')
      },
      {
        path: 'allocations',
        name: 'AllocationManagement',
        component: () => import('../components/AllocationManagement.vue')
      },
      {
        path: 'settings',
        name: 'SystemSettings',
        component: () => import('../components/SystemSettings.vue')
      },
      {
        path: 'reports',
        name: 'Reports',
        component: () => import('../components/Reports.vue')
      },
      {
          path: 'maintenance',
          name: 'Maintenance',
          component: () => import('../components/Maintenance.vue')
        },
      {
        path: 'visitors',
        name: 'Visitors',
        component: () => import('../components/VisitorManagement.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('userToken') || sessionStorage.getItem('userToken')
  
  if (to.meta.requiresAuth && !token) {
    // 需要认证但没有token，跳转到登录页
    next('/login')
  } else if (to.path === '/login' && token) {
    // 已登录用户访问登录页，跳转到仪表板
    next('/dashboard')
  } else {
    next()
  }
})

export default router