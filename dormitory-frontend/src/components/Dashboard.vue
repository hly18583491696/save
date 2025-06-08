<template>
  <div class="dashboard-content">
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon building">
            <i class="fas fa-building"></i>
          </div>
          <div class="stat-trend up">
            <i class="fas fa-arrow-up"></i>
            <span>+2.5%</span>
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-number">
            <span v-if="loading">加载中...</span>
            <span v-else>{{ stats.totalBuildings }}</span>
          </div>
          <div class="stat-label">宿舍楼栋</div>
          <div class="stat-description">较上月增加1栋</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon room">
            <i class="fas fa-door-open"></i>
          </div>
          <div class="stat-trend up">
            <i class="fas fa-arrow-up"></i>
            <span>+5.2%</span>
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-number">
            <span v-if="loading">加载中...</span>
            <span v-else>{{ stats.totalRooms }}</span>
          </div>
          <div class="stat-label">宿舍房间</div>
          <div class="stat-description">可用房间85间</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon student">
            <i class="fas fa-user-graduate"></i>
          </div>
          <div class="stat-trend up">
            <i class="fas fa-arrow-up"></i>
            <span>+3.1%</span>
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-number">
            <span v-if="loading">加载中...</span>
            <span v-else>{{ stats.totalStudents }}</span>
          </div>
          <div class="stat-label">在校学生</div>
          <div class="stat-description">新增学生15人</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon occupancy">
            <i class="fas fa-chart-pie"></i>
          </div>
          <div class="stat-trend down">
            <i class="fas fa-arrow-down"></i>
            <span>-1.2%</span>
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-number">
            <span v-if="loading">加载中...</span>
            <span v-else>{{ stats.occupancyRate }}%</span>
          </div>
          <div class="stat-label">入住率</div>
          <div class="stat-description">目标入住率90%</div>
        </div>
      </div>
    </div>
    
    <!-- 图表和活动区域 -->
    <div class="dashboard-grid">
      <div class="dashboard-card chart-card">
        <div class="card-header">
          <h3>入住率趋势</h3>
          <div class="card-actions">
            <button class="btn btn-sm">查看详情</button>
          </div>
        </div>
        <div class="card-content">
          <div class="chart-placeholder">
            <i class="fas fa-chart-line"></i>
            <p>图表加载中...</p>
          </div>
        </div>
      </div>
      
      <div class="dashboard-card activity-card">
        <div class="card-header">
          <h3>最近活动</h3>
          <div class="card-actions">
            <button class="btn btn-sm">查看全部</button>
          </div>
        </div>
        <div class="card-content">
          <div class="activity-list">
            <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
              <div class="activity-icon">
                <i :class="activity.icon"></i>
              </div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-time">{{ activity.time }}</div>
              </div>
              <div class="activity-status" :class="activity.status">
                {{ getStatusText(activity.status) }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 快速操作 -->
    <div class="quick-actions">
      <h3>快速操作</h3>
      <div class="actions-grid">
        <div class="action-card" @click="$router.push('/dashboard/dormitories')">
          <div class="action-icon">
            <i class="fas fa-plus"></i>
          </div>
          <div class="action-content">
            <div class="action-title">添加宿舍</div>
            <div class="action-description">新增宿舍房间</div>
          </div>
        </div>
        
        <div class="action-card" @click="$router.push('/dashboard/students')">
          <div class="action-icon">
            <i class="fas fa-user-plus"></i>
          </div>
          <div class="action-content">
            <div class="action-title">添加学生</div>
            <div class="action-description">录入新学生信息</div>
          </div>
        </div>
        
        <div class="action-card" @click="$router.push('/dashboard/allocations')">
          <div class="action-icon">
            <i class="fas fa-bed"></i>
          </div>
          <div class="action-content">
            <div class="action-title">分配宿舍</div>
            <div class="action-description">为学生分配住宿</div>
          </div>
        </div>
        
        <div class="action-card" @click="$router.push('/dashboard/maintenance')">
          <div class="action-icon">
            <i class="fas fa-wrench"></i>
          </div>
          <div class="action-content">
            <div class="action-title">维修申请</div>
            <div class="action-description">处理维修请求</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'Dashboard',
  setup() {
    const stats = reactive({
      totalBuildings: 0,
      totalRooms: 0,
      totalStudents: 0,
      occupancyRate: 0
    })
    
    const loading = ref(true)
    
    const recentActivities = ref([
      {
        id: 1,
        title: '学生张三申请宿舍调换',
        time: '2小时前',
        icon: 'fas fa-exchange-alt',
        status: 'pending'
      },
      {
        id: 2,
        title: '宿舍A101维修完成',
        time: '4小时前',
        icon: 'fas fa-check-circle',
        status: 'completed'
      },
      {
        id: 3,
        title: '新生李四入住登记',
        time: '6小时前',
        icon: 'fas fa-user-plus',
        status: 'completed'
      },
      {
        id: 4,
        title: '宿舍B205报修申请',
        time: '1天前',
        icon: 'fas fa-exclamation-triangle',
        status: 'urgent'
      }
    ])
    
    const getStatusText = (status) => {
      const statuses = {
        pending: '待处理',
        completed: '已完成',
        urgent: '紧急'
      }
      return statuses[status] || '未知状态'
    }
    
    // 获取统计数据
    const fetchDashboardStats = async () => {
      try {
        loading.value = true
        const response = await axios.get('http://localhost:8082/api/statistics/dashboard')
        if (response.data.code === 200) {
          Object.assign(stats, response.data.data)
        } else {
          console.error('获取统计数据失败:', response.data.message)
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 组件挂载时获取数据
    onMounted(() => {
      fetchDashboardStats()
    })
    
    return {
      stats,
      recentActivities,
      loading,
      fetchDashboardStats,
      getStatusText
    }
  }
}
</script>

<style scoped>
.dashboard-content {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
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
  gap: 4px;
  font-size: 12px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 6px;
}

.stat-trend.up {
  color: #10b981;
  background: #ecfdf5;
}

.stat-trend.down {
  color: #ef4444;
  background: #fef2f2;
}

.stat-content {
  text-align: left;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  font-weight: 600;
  color: #6b7280;
  margin-bottom: 4px;
}

.stat-description {
  font-size: 12px;
  color: #9ca3af;
}

/* 仪表盘网格 */
.dashboard-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
  margin-bottom: 32px;
}

.dashboard-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.card-content {
  padding: 24px;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #9ca3af;
}

.chart-placeholder i {
  font-size: 48px;
  margin-bottom: 12px;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  background: #f9fafb;
  transition: background 0.2s;
}

.activity-item:hover {
  background: #f3f4f6;
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6b7280;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 2px;
}

.activity-time {
  font-size: 12px;
  color: #9ca3af;
}

.activity-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.activity-status.pending {
  background: #fef3c7;
  color: #d97706;
}

.activity-status.completed {
  background: #d1fae5;
  color: #059669;
}

.activity-status.urgent {
  background: #fee2e2;
  color: #dc2626;
}

/* 快速操作 */
.quick-actions {
  margin-bottom: 32px;
}

.quick-actions h3 {
  margin: 0 0 20px 0;
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.action-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.2s;
}

.action-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.action-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
}

.action-content {
  flex: 1;
}

.action-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 4px;
}

.action-description {
  font-size: 14px;
  color: #6b7280;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
}

.btn {
  background: #f3f4f6;
  color: #374151;
}

.btn:hover {
  background: #e5e7eb;
}

@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  }
  
  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>