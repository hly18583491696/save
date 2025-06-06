<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6" v-for="stat in stats" :key="stat.title">
          <div class="stat-card" :class="stat.type">
            <div class="stat-icon">
              <el-icon :size="40"><component :is="stat.icon" /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-title">{{ stat.title }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 入住率统计 -->
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>各楼栋入住率</h3>
            </div>
            <div ref="occupancyChartRef" class="chart-container"></div>
          </div>
        </el-col>
        
        <!-- 用户类型分布 -->
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>用户类型分布</h3>
            </div>
            <div ref="userTypeChartRef" class="chart-container"></div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <!-- 最近动态 -->
    <div class="recent-activities">
      <div class="content-card">
        <div class="card-header">
          <h3>最近动态</h3>
          <el-button type="text" @click="refreshActivities">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
        
        <el-timeline>
          <el-timeline-item
            v-for="activity in activities"
            :key="activity.id"
            :timestamp="activity.time"
            :type="activity.type"
          >
            <div class="activity-content">
              <span class="activity-user">{{ activity.user }}</span>
              <span class="activity-action">{{ activity.action }}</span>
              <span class="activity-target">{{ activity.target }}</span>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 统计数据
const stats = ref([
  {
    title: '总用户数',
    value: '1,234',
    icon: 'User',
    type: 'primary'
  },
  {
    title: '宿舍楼栋',
    value: '12',
    icon: 'OfficeBuilding',
    type: 'success'
  },
  {
    title: '宿舍房间',
    value: '456',
    icon: 'House',
    type: 'warning'
  },
  {
    title: '入住率',
    value: '85%',
    icon: 'PieChart',
    type: 'danger'
  }
])

// 最近动态
const activities = ref([
  {
    id: 1,
    user: '张三',
    action: '申请入住',
    target: 'A栋201房间',
    time: '2024-01-15 14:30',
    type: 'primary'
  },
  {
    id: 2,
    user: '李四',
    action: '退宿申请',
    target: 'B栋305房间',
    time: '2024-01-15 13:20',
    type: 'warning'
  },
  {
    id: 3,
    user: '王五',
    action: '完成入住',
    target: 'C栋102房间',
    time: '2024-01-15 12:10',
    type: 'success'
  },
  {
    id: 4,
    user: '赵六',
    action: '房间调换',
    target: 'A栋301房间',
    time: '2024-01-15 11:00',
    type: 'info'
  }
])

// 图表引用
const occupancyChartRef = ref()
const userTypeChartRef = ref()

// 初始化入住率图表
const initOccupancyChart = () => {
  const chart = echarts.init(occupancyChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['A栋', 'B栋', 'C栋', 'D栋', 'E栋', 'F栋']
    },
    yAxis: {
      type: 'value',
      max: 100,
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [
      {
        name: '入住率',
        type: 'bar',
        data: [85, 92, 78, 88, 95, 82],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  }
  
  chart.setOption(option)
  
  // 响应式
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 初始化用户类型分布图表
const initUserTypeChart = () => {
  const chart = echarts.init(userTypeChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '用户类型',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 1000, name: '学生' },
          { value: 200, name: '宿管员' },
          { value: 34, name: '管理员' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
  
  // 响应式
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 刷新动态
const refreshActivities = () => {
  // 这里可以调用API刷新数据
  console.log('刷新最近动态')
}

onMounted(() => {
  nextTick(() => {
    initOccupancyChart()
    initUserTypeChart()
  })
})
</script>

<style lang="scss" scoped>
.dashboard {
  padding: 20px;
  
  .stats-cards {
    margin-bottom: 20px;
    
    .stat-card {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      transition: transform 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
      }
      
      .stat-icon {
        margin-right: 20px;
        
        .el-icon {
          color: #409EFF;
        }
      }
      
      .stat-content {
        flex: 1;
        
        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #333;
          margin-bottom: 5px;
        }
        
        .stat-title {
          font-size: 14px;
          color: #666;
        }
      }
      
      &.primary .stat-icon .el-icon {
        color: #409EFF;
      }
      
      &.success .stat-icon .el-icon {
        color: #67C23A;
      }
      
      &.warning .stat-icon .el-icon {
        color: #E6A23C;
      }
      
      &.danger .stat-icon .el-icon {
        color: #F56C6C;
      }
    }
  }
  
  .charts-section {
    margin-bottom: 20px;
    
    .chart-card {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      
      .chart-header {
        margin-bottom: 20px;
        
        h3 {
          margin: 0;
          font-size: 16px;
          color: #333;
        }
      }
      
      .chart-container {
        height: 300px;
      }
    }
  }
  
  .recent-activities {
    .content-card {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        
        h3 {
          margin: 0;
          font-size: 16px;
          color: #333;
        }
      }
      
      .activity-content {
        .activity-user {
          font-weight: bold;
          color: #409EFF;
        }
        
        .activity-action {
          margin: 0 5px;
          color: #666;
        }
        
        .activity-target {
          color: #333;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .dashboard {
    padding: 10px;
    
    .stat-card {
      margin-bottom: 10px;
    }
    
    .chart-container {
      height: 250px !important;
    }
  }
}
</style>