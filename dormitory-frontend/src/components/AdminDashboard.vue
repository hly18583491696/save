<template>
  <div class="admin-dashboard">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <i class="fas fa-university"></i>
          <span v-if="!sidebarCollapsed" class="logo-text">宿舍管理系统</span>
        </div>
        <button class="sidebar-toggle" @click="toggleSidebar">
          <i class="fas fa-bars"></i>
        </button>
      </div>
      
      <nav class="sidebar-nav">
        <div class="nav-section">
          <div class="nav-title" v-if="!sidebarCollapsed">主要功能</div>
          <div class="nav-item">
            <router-link to="/dashboard" class="nav-link">
              <i class="fas fa-tachometer-alt"></i>
              <span v-if="!sidebarCollapsed">仪表盘</span>
            </router-link>
          </div>
          <div class="nav-item">
            <router-link to="/dashboard/dormitories" class="nav-link">
              <i class="fas fa-building"></i>
              <span v-if="!sidebarCollapsed">宿舍管理</span>
            </router-link>
          </div>
          <div class="nav-item">
            <router-link to="/dashboard/accommodations" class="nav-link">
              <i class="fas fa-bed"></i>
              <span v-if="!sidebarCollapsed">住宿记录管理</span>
            </router-link>
          </div>
          <div class="nav-item">
            <router-link to="/dashboard/allocations" class="nav-link">
              <i class="fas fa-bed"></i>
              <span v-if="!sidebarCollapsed">住宿分配</span>
            </router-link>
          </div>
          <div class="nav-item">
            <router-link to="/dashboard/visitors" class="nav-link">
              <i class="fas fa-users"></i>
              <span v-if="!sidebarCollapsed">访客管理</span>
            </router-link>
          </div>
        </div>
        
        <div class="nav-section">
          <div class="nav-title" v-if="!sidebarCollapsed">系统管理</div>
          <div class="nav-item">
            <router-link to="/dashboard/maintenance" class="nav-link">
              <i class="fas fa-wrench"></i>
              <span v-if="!sidebarCollapsed">维修管理</span>
            </router-link>
          </div>
          <div class="nav-item">
            <router-link to="/dashboard/reports" class="nav-link">
              <i class="fas fa-chart-bar"></i>
              <span v-if="!sidebarCollapsed">统计报表</span>
            </router-link>
          </div>
          <div class="nav-item">
            <router-link to="/dashboard/settings" class="nav-link">
              <i class="fas fa-cog"></i>
              <span v-if="!sidebarCollapsed">系统设置</span>
            </router-link>
          </div>
          <div class="nav-item">
            <router-link to="/dashboard/diagnostic" class="nav-link">
              <i class="fas fa-bug"></i>
              <span v-if="!sidebarCollapsed">删除诊断</span>
            </router-link>
          </div>
        </div>
      </nav>
    </aside>
    
    <!-- 主内容区域 -->
    <div class="main-wrapper">
      <!-- 顶部导航栏 -->
      <header class="top-header">
        <div class="header-left">
          <h1 class="page-title">{{ getPageTitle() }}</h1>
          <div class="breadcrumb">
            <span class="breadcrumb-item">首页</span>
            <i class="fas fa-chevron-right"></i>
            <span class="breadcrumb-item active">{{ getPageTitle() }}</span>
          </div>
        </div>
        
        <div class="header-right">
          <div class="header-actions">
            <button class="action-btn" title="通知">
              <i class="fas fa-bell"></i>
              <span class="badge">3</span>
            </button>
            <button class="action-btn" title="消息">
              <i class="fas fa-envelope"></i>
              <span class="badge">5</span>
            </button>
          </div>
          
          <div class="user-menu">
            <div class="user-avatar">
              <i class="fas fa-user-circle"></i>
            </div>
            <div class="user-info">
              <div class="user-name">{{ userInfo.name }}</div>
              <div class="user-role">{{ getRoleText(userInfo.role) }}</div>
            </div>
            <div class="dropdown">
              <button class="dropdown-toggle" @click="toggleUserMenu">
                <i class="fas fa-chevron-down"></i>
              </button>
              <div class="dropdown-menu" v-if="showUserMenu">
                <a href="#" class="dropdown-item">
                  <i class="fas fa-user"></i>
                  个人资料
                </a>
                <a href="#" class="dropdown-item">
                  <i class="fas fa-cog"></i>
                  账户设置
                </a>
                <div class="dropdown-divider"></div>
                <a href="#" class="dropdown-item" @click="logout">
                  <i class="fas fa-sign-out-alt"></i>
                  退出登录
                </a>
              </div>
            </div>
          </div>
        </div>
      </header>
      
      <!-- 页面内容 -->
      <main class="page-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import DormitoryManagement from './DormitoryManagement.vue'
import StudentManagement from './StudentManagement.vue'
import AllocationManagement from './AllocationManagement.vue'
import SystemSettings from './SystemSettings.vue'

export default {
  name: 'AdminDashboard',
  components: {
    DormitoryManagement,
    StudentManagement,
    AllocationManagement,
    SystemSettings
  },
  emits: ['logout'],
  setup(props, { emit }) {
    // 从存储中获取用户信息
    const getUserInfo = () => {
      const userInfoStr = localStorage.getItem('userInfo') || sessionStorage.getItem('userInfo')
      return userInfoStr ? JSON.parse(userInfoStr) : { username: 'Guest', role: 'guest' }
    }
    
    const userInfo = ref(getUserInfo())
    const route = useRoute()
    const router = useRouter()
    const sidebarCollapsed = ref(false)
    const showUserMenu = ref(false)
    
    const toggleSidebar = () => {
      sidebarCollapsed.value = !sidebarCollapsed.value
    }
    
    const toggleUserMenu = () => {
      showUserMenu.value = !showUserMenu.value
    }
    
    const getRoleText = (role) => {
      const roles = {
        admin: '系统管理员',
        teacher: '宿管老师',
        student: '学生'
      }
      return roles[role] || '未知角色'
    }
    
    const logout = () => {
      // 清除存储的用户信息
      localStorage.removeItem('userToken')
      localStorage.removeItem('userInfo')
      sessionStorage.removeItem('userToken')
      sessionStorage.removeItem('userInfo')
      
      // 跳转到登录页
      router.push('/login')
    }
    
    const getPageTitle = () => {
      const routeNames = {
        'dashboard': '仪表盘',
        'dashboard-students': '学生管理',
        'dashboard-dormitories': '宿舍管理',
        'dashboard-allocations': '住宿分配',
        'dashboard-reports': '统计报表',
        'dashboard-settings': '系统设置',
        'dashboard-maintenance': '维修管理',
        'dashboard-visitors': '访客管理',
        'dormitory': '宿舍管理',
        'student': '学生管理',
        'allocation': '分配管理',
        'visitors': '访客管理',
        'settings': '系统设置'
      }
      return routeNames[route.name] || '仪表盘'
    }
    
    // 点击外部关闭用户菜单
    const handleClickOutside = (event) => {
      if (!event.target.closest('.user-menu')) {
        showUserMenu.value = false
      }
    }
    
    onMounted(() => {
      document.addEventListener('click', handleClickOutside)
    })
    
    return {
      userInfo,
      sidebarCollapsed,
      showUserMenu,
      toggleSidebar,
      toggleUserMenu,
      getRoleText,
      logout,
      getPageTitle
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  display: flex;
  height: 100vh;
  background: var(--bg-secondary);
}

/* 侧边栏样式 */
.sidebar {
  width: 280px;
  background: var(--bg-primary);
  border-right: 1px solid var(--border-color);
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
}

.sidebar.collapsed {
  width: 70px;
}

.sidebar-header {
  padding: 1rem;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: var(--primary-color);
  font-weight: 600;
  font-size: 1.1rem;
}

.logo i {
  font-size: 1.5rem;
}

.logo-text {
  white-space: nowrap;
}

.sidebar-toggle {
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.sidebar-toggle:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.sidebar-nav {
  flex: 1;
  padding: 1rem 0;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 2rem;
}

.nav-title {
  padding: 0 1rem;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 0.5rem;
}

.nav-item {
  margin-bottom: 4px;
  border-radius: 8px;
  transition: all 0.2s;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.nav-link:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.nav-link.router-link-active {
  background: var(--primary-color);
  color: white;
}

.nav-link.router-link-active:hover {
  background: var(--primary-hover);
}

.nav-item i {
  width: 20px;
  text-align: center;
}

/* 主内容区域 */
.main-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.top-header {
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-color);
  padding: 1rem 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.page-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.breadcrumb-item.active {
  color: var(--primary-color);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.header-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  position: relative;
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.action-btn:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.badge {
  position: absolute;
  top: 0;
  right: 0;
  background: var(--danger-color);
  color: white;
  font-size: 0.625rem;
  padding: 0.125rem 0.25rem;
  border-radius: 10px;
  min-width: 16px;
  text-align: center;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  position: relative;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
}

.user-avatar i {
  font-size: 32px;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-weight: 500;
  color: var(--text-primary);
}

.user-role {
  font-size: 0.75rem;
  color: var(--text-secondary);
}

.dropdown {
  position: relative;
}

.dropdown-toggle {
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 0.25rem;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background: var(--bg-primary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  box-shadow: var(--shadow-lg);
  min-width: 180px;
  z-index: 1000;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  color: var(--text-primary);
  text-decoration: none;
  transition: background 0.2s ease;
}

.dropdown-item:hover {
  background: var(--bg-secondary);
}

.dropdown-divider {
  height: 1px;
  background: var(--border-color);
  margin: 0.5rem 0;
}

/* 页面内容 */
.page-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}

/* 仪表盘样式 */
.dashboard-content {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.stat-card {
  background: var(--bg-primary);
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid var(--border-color);
  transition: all 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
}

.stat-icon.building {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.room {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.student {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.occupancy {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.stat-trend.up {
  color: var(--success-color);
}

.stat-trend.down {
  color: var(--danger-color);
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin-bottom: 0.5rem;
}

.stat-description {
  font-size: 0.75rem;
  color: var(--text-secondary);
}

.dashboard-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
}

.dashboard-card {
  background: var(--bg-primary);
  border-radius: 12px;
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.card-header {
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: var(--text-primary);
}

.card-content {
  padding: 1.5rem;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: var(--text-secondary);
}

.chart-placeholder i {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.75rem;
  border-radius: 8px;
  transition: background 0.2s ease;
}

.activity-item:hover {
  background: var(--bg-secondary);
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 0.25rem;
}

.activity-time {
  font-size: 0.75rem;
  color: var(--text-secondary);
}

.activity-status {
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 500;
}

.activity-status.pending {
  background: #fef3c7;
  color: #92400e;
}

.activity-status.completed {
  background: #d1fae5;
  color: #065f46;
}

.activity-status.urgent {
  background: #fee2e2;
  color: #991b1b;
}

.quick-actions {
  margin-top: 1rem;
}

.quick-actions h3 {
  margin-bottom: 1rem;
  color: var(--text-primary);
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.action-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.action-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
  border-color: var(--primary-color);
}

.action-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-title {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 0.25rem;
}

.action-description {
  font-size: 0.75rem;
  color: var(--text-secondary);
}

.placeholder-page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.placeholder-content {
  text-align: center;
  color: var(--text-secondary);
}

.placeholder-content i {
  font-size: 4rem;
  margin-bottom: 1rem;
  color: var(--primary-color);
}

.placeholder-content h3 {
  margin-bottom: 0.5rem;
  color: var(--text-primary);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: -280px;
    z-index: 1000;
    transition: left 0.3s ease;
  }
  
  .sidebar.collapsed {
    left: 0;
    width: 280px;
  }
  
  .main-wrapper {
    margin-left: 0;
  }
  
  .top-header {
    padding: 1rem;
  }
  
  .page-content {
    padding: 1rem;
  }
  
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .actions-grid {
    grid-template-columns: 1fr;
  }
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 6px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-sm {
  padding: 0.375rem 0.75rem;
  font-size: 0.75rem;
}

.btn:hover {
  transform: translateY(-1px);
}
</style>