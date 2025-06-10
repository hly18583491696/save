<template>
  <div class="maintenance-container">
    <div class="page-header">
      <h2>🔧 维修管理</h2>
      <p>智能化宿舍维修申请与跟踪系统</p>
    </div>
    
    <!-- 快速操作栏 -->
    <div class="quick-actions">
      <div class="search-section">
        <div class="search-box">
          <i class="fas fa-search"></i>
          <input 
            type="text" 
            placeholder="搜索申请单号、房间号、申请人..." 
            v-model="searchQuery"
            class="search-input"
            @input="handleSearch"
          >
        </div>
        <div class="filter-group">
          <select v-model="statusFilter" @change="handleFilterChange">
              <option value="">全部状态</option>
              <option value="PENDING">🕐 待处理</option>
              <option value="IN_PROGRESS">🔧 处理中</option>
              <option value="COMPLETED">✅ 已完成</option>
              <option value="CANCELLED">❌ 已取消</option>
            </select>
          <select v-model="typeFilter" @change="handleFilterChange">
              <option value="">全部类型</option>
              <option value="ELECTRICAL">💧 水电维修</option>
              <option value="PLUMBING">🚰 管道维修</option>
              <option value="FURNITURE">🪑 家具维修</option>
              <option value="APPLIANCE">🔌 电器维修</option>
              <option value="OTHER">🔧 其他维修</option>
            </select>
          <select v-model="urgencyFilter" @change="handleFilterChange">
              <option value="">全部紧急度</option>
              <option value="LOW">🟢 低</option>
              <option value="NORMAL">🟡 普通</option>
              <option value="HIGH">🟠 高</option>
              <option value="URGENT">🔴 紧急</option>
            </select>
        </div>
      </div>
      <div class="action-buttons">
        <button class="btn btn-outline" @click="refreshData">
          <i class="fas fa-sync-alt" :class="{spinning: loading}"></i>
          刷新
        </button>
        <button class="btn btn-primary" @click="showAddModal = true">
          <i class="fas fa-plus"></i>
          新增申请
        </button>
      </div>
    </div>
    
    <!-- 数据统计面板 -->
    <div class="stats-panel">
      <div class="stats-grid">
        <div class="stat-card pending" @click="filterByStatus('pending')">
          <div class="stat-icon">
            <i class="fas fa-clock"></i>
          </div>
          <div class="stat-content">
            <h3>{{ stats.pending }}</h3>
            <p>待处理申请</p>
            <span class="stat-trend" v-if="stats.pendingTrend">
              <i class="fas fa-arrow-up" v-if="stats.pendingTrend > 0"></i>
              <i class="fas fa-arrow-down" v-else></i>
              {{ Math.abs(stats.pendingTrend) }}
            </span>
          </div>
        </div>
        <div class="stat-card in-progress" @click="filterByStatus('in_progress')">
          <div class="stat-icon">
            <i class="fas fa-tools"></i>
          </div>
          <div class="stat-content">
            <h3>{{ stats.inProgress }}</h3>
            <p>维修进行中</p>
            <span class="stat-trend" v-if="stats.inProgressTrend">
              <i class="fas fa-arrow-up" v-if="stats.inProgressTrend > 0"></i>
              <i class="fas fa-arrow-down" v-else></i>
              {{ Math.abs(stats.inProgressTrend) }}
            </span>
          </div>
        </div>
        <div class="stat-card completed" @click="filterByStatus('completed')">
          <div class="stat-icon">
            <i class="fas fa-check-circle"></i>
          </div>
          <div class="stat-content">
            <h3>{{ stats.completed }}</h3>
            <p>本月完成</p>
            <span class="stat-trend positive" v-if="stats.completedTrend">
              <i class="fas fa-arrow-up"></i>
              {{ stats.completedTrend }}
            </span>
          </div>
        </div>
        <div class="stat-card urgent" @click="filterByUrgency('URGENT')">
          <div class="stat-icon">
            <i class="fas fa-exclamation-triangle"></i>
          </div>
          <div class="stat-content">
            <h3>{{ stats.urgent }}</h3>
            <p>紧急申请</p>
            <span class="stat-trend negative" v-if="stats.urgent > 0">
              <i class="fas fa-exclamation"></i>
              需优先处理
            </span>
          </div>
        </div>
        <div class="stat-card total">
          <div class="stat-icon">
            <i class="fas fa-chart-bar"></i>
          </div>
          <div class="stat-content">
            <h3>{{ stats.total }}</h3>
            <p>总申请数</p>
            <span class="stat-rate">
              完成率: {{ completionRate }}%
            </span>
          </div>
        </div>
        <div class="stat-card satisfaction">
          <div class="stat-icon">
            <i class="fas fa-star"></i>
          </div>
          <div class="stat-content">
            <h3>{{ stats.avgRating }}</h3>
            <p>平均评分</p>
            <div class="rating-stars">
              <i v-for="n in 5" :key="n" 
                 :class="['fas fa-star', n <= Math.round(stats.avgRating) ? 'active' : '']">
              </i>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 维修申请列表 -->
    <div class="maintenance-list">
      <div class="list-header">
        <div class="header-left">
          <h3><i class="fas fa-list-alt"></i> 维修申请管理</h3>
          <span class="list-count">共 {{ filteredRequests.length }} 条记录</span>
        </div>
        <div class="header-actions">
          <div class="view-toggle">
            <button :class="['btn', 'btn-sm', viewMode === 'table' ? 'btn-primary' : 'btn-outline']" 
                    @click="viewMode = 'table'">
              <i class="fas fa-table"></i> 表格
            </button>
            <button :class="['btn', 'btn-sm', viewMode === 'card' ? 'btn-primary' : 'btn-outline']" 
                    @click="viewMode = 'card'">
              <i class="fas fa-th-large"></i> 卡片
            </button>
          </div>
          <button class="btn btn-success" @click="showAddModal = true">
            <i class="fas fa-plus"></i> 新增申请
          </button>
          <button class="btn btn-outline" @click="exportData">
            <i class="fas fa-download"></i> 导出
          </button>
        </div>
      </div>
      
      <!-- 表格视图 -->
      <div v-if="viewMode === 'table'" class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th class="col-checkbox">
                <input type="checkbox" v-model="selectAll" @change="toggleSelectAll">
              </th>
              <th class="col-request-number sortable" @click="sortBy('id')">
                申请编号 
                <i :class="getSortIcon('id')"></i>
              </th>
              <th class="col-room sortable" @click="sortBy('roomNumber')">
                宿舍房间 
                <i :class="getSortIcon('roomNumber')"></i>
              </th>
              <th class="col-applicant">申请人</th>
              <th class="col-type">维修类型</th>
              <th class="col-urgency">紧急程度</th>
              <th class="col-description">问题描述</th>
              <th class="col-time sortable" @click="sortBy('createdAt')">
                申请时间 
                <i :class="getSortIcon('createdAt')"></i>
              </th>
              <th class="col-progress">处理进度</th>
              <th class="col-status">状态</th>
              <th class="col-actions">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="request in paginatedRequests" :key="request.id" 
                :class="{'selected': selectedItems.includes(request.id), 'urgent': request.urgency === 'URGENT'}">
              <td class="col-checkbox">
                <input type="checkbox" :value="request.id" v-model="selectedItems">
              </td>
              <td class="col-request-number">
                <div class="request-number">
                  <span class="number">{{ request.id }}</span>
                  <span v-if="request.urgency === 'URGENT'" class="urgent-badge">
                    <i class="fas fa-exclamation-triangle"></i>
                  </span>
                </div>
              </td>
              <td class="col-room">
                <div class="room-info">
                  <span class="room-number">{{ request.roomNumber }}</span>
                  <small class="building">{{ request.building }}栋</small>
                </div>
              </td>
              <td class="col-applicant">
                <div class="applicant-info">
                  <div class="user-avatar">
                    <i class="fas fa-user"></i>
                  </div>
                  <div class="user-details">
                    <span class="name">{{ request.applicant }}</span>
                  </div>
                </div>
              </td>
              <td class="col-type">
                <span :class="['type-badge', request.type.toLowerCase()]">
                  <i :class="getTypeIcon(request.type)"></i>
                  {{ request.type }}
                </span>
              </td>
              <td class="col-urgency">
                <span :class="['urgency-badge', request.urgency]">
                  {{ request.urgency }}
                </span>
              </td>
              <td class="col-description">
                <div class="desc-content" :title="request.description">
                  {{ request.description.length > 30 ? request.description.substring(0, 30) + '...' : request.description }}
                </div>
              </td>
              <td class="col-time">
                <div class="time-info">
                  <div class="request-time">{{ formatDate(request.createdAt) }}</div>
                  <small class="relative-time">{{ getRelativeTime(request.createdAt) }}</small>
                </div>
              </td>
              <td class="col-progress">
                <div class="progress-info">
                  <div class="progress-bar">
                    <div class="progress-fill" :style="{width: getProgressWidth(request.status)}"></div>
                  </div>
                  <small class="progress-text">{{ getProgressText(request.status) }}</small>
                </div>
              </td>
              <td class="col-status">
                <span :class="['status-badge', request.status]">
                  <i :class="getStatusIcon(request.status)"></i>
                  {{ getStatusText(request.status) }}
                </span>
              </td>
              <td class="col-actions">
                <div class="action-buttons">
                  <button class="btn btn-sm btn-info" @click="viewRequest(request)" title="查看详情">
                    <i class="fas fa-eye"></i>
                  </button>
                  <button v-if="request.status === 'pending'" 
                          class="btn btn-sm btn-warning" 
                          @click="processRequest(request)"
                          title="开始处理">
                    <i class="fas fa-play"></i>
                  </button>
                  <button v-if="request.status === 'in_progress'" 
                          class="btn btn-sm btn-success" 
                          @click="completeRequest(request)"
                          title="完成维修">
                    <i class="fas fa-check"></i>
                  </button>
                  <button class="btn btn-sm btn-secondary" 
                          @click="editRequest(request)"
                          title="编辑">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button v-if="request.status === 'pending'" 
                          class="btn btn-sm btn-danger" 
                          @click="deleteRequest(request)"
                          title="删除">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        
        <!-- 分页控件 -->
        <div class="pagination-container">
          <div class="pagination-info">
            显示第 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, filteredRequests.length) }} 条，
            共 {{ filteredRequests.length }} 条记录
          </div>
          <div class="pagination">
            <button class="btn btn-sm" :disabled="currentPage === 1" @click="currentPage--">
              <i class="fas fa-chevron-left"></i>
            </button>
            <span v-for="page in visiblePages" :key="page">
              <button v-if="page !== '...'" 
                      :class="['btn', 'btn-sm', page === currentPage ? 'btn-primary' : 'btn-outline']"
                      @click="currentPage = page">
                {{ page }}
              </button>
              <span v-else class="pagination-ellipsis">...</span>
            </span>
            <button class="btn btn-sm" :disabled="currentPage === totalPages" @click="currentPage++">
              <i class="fas fa-chevron-right"></i>
            </button>
          </div>
          <div class="page-size-selector">
            <label>每页显示：</label>
            <select v-model="pageSize" @change="currentPage = 1">
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="50">50</option>
            </select>
          </div>
        </div>
      </div>
      
      <!-- 卡片视图 -->
      <div v-else class="card-container">
        <div class="maintenance-cards">
          <div v-for="request in paginatedRequests" :key="request.id" 
               :class="['maintenance-card', request.status, {'urgent': request.urgency === 'URGENT'}]">
            <div class="card-header">
              <div class="card-title">
                <span class="request-number">{{ request.id }}</span>
                <span v-if="request.urgency === 'URGENT'" class="urgent-indicator">
                  <i class="fas fa-exclamation-triangle"></i>
                </span>
              </div>
              <div class="card-status">
                <span :class="['status-badge', request.status]">
                  {{ getStatusText(request.status) }}
                </span>
              </div>
            </div>
            <div class="card-content">
              <div class="card-info">
                <div class="info-item">
                  <i class="fas fa-home"></i>
                  <span>{{ request.building }}栋 {{ request.roomNumber }}</span>
                </div>
                <div class="info-item">
                  <i class="fas fa-user"></i>
                  <span>{{ request.applicant }}</span>
                </div>
                <div class="info-item">
                  <i :class="getTypeIcon(request.type)"></i>
                  <span>{{ request.type }}</span>
                </div>
                <div class="info-item">
                  <i class="fas fa-clock"></i>
                  <span>{{ formatDate(request.createdAt) }}</span>
                </div>
              </div>
              <div class="card-description">
                {{ request.description }}
              </div>
            </div>
            <div class="card-actions">
              <button class="btn btn-sm btn-info" @click="viewRequest(request)">
                <i class="fas fa-eye"></i> 详情
              </button>
              <button v-if="request.status === 'pending'" 
                      class="btn btn-sm btn-warning" 
                      @click="processRequest(request)">
                <i class="fas fa-play"></i> 处理
              </button>
              <button v-if="request.status === 'in_progress'" 
                      class="btn btn-sm btn-success" 
                      @click="completeRequest(request)">
                <i class="fas fa-check"></i> 完成
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 新增维修申请模态框 -->
    <div v-if="showAddModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增维修申请</h3>
          <button class="close-btn" @click="closeModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitRequest">
            <div class="form-group">
              <label>宿舍房间</label>
              <input 
                type="text" 
                v-model="newRequest.roomNumber" 
                class="form-input"
                placeholder="请输入房间号"
                required
              >
            </div>
            <div class="form-group">
              <label>申请人</label>
              <input 
                type="text" 
                v-model="newRequest.applicant" 
                class="form-input"
                placeholder="请输入申请人姓名"
                required
              >
            </div>
            <div class="form-group">
              <label>维修类型</label>
              <select v-model="newRequest.type" class="form-select" required>
                <option value="">请选择维修类型</option>
                <option value="水电">水电</option>
                <option value="家具">家具</option>
                <option value="门窗">门窗</option>
                <option value="网络">网络</option>
                <option value="其他">其他</option>
              </select>
            </div>
            <div class="form-group">
              <label>问题描述</label>
              <textarea 
                v-model="newRequest.description" 
                class="form-textarea"
                placeholder="请详细描述维修问题"
                rows="4"
                required
              ></textarea>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-outline" @click="closeModal">
                取消
              </button>
              <button type="submit" class="btn btn-primary">
                提交申请
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'Maintenance',
  setup() {
    // 响应式数据
    const searchQuery = ref('')
    const statusFilter = ref('')
    const typeFilter = ref('')
    const urgencyFilter = ref('')
    const showAddModal = ref(false)
    const showDetailModal = ref(false)
    const viewMode = ref('table') // 'table' 或 'card'
    const loading = ref(false)
    
    // 分页相关
    const currentPage = ref(1)
    const pageSize = ref(20)
    
    // 排序相关
    const sortField = ref('')
    const sortOrder = ref('asc') // 'asc' 或 'desc'
    
    // 选择相关
    const selectedItems = ref([])
    const selectAll = ref(false)
    
    // 统计数据
    const stats = ref({
      pending: 0,
      inProgress: 0,
      completed: 0,
      urgent: 0,
      total: 0,
      avgRating: 0,
      pendingTrend: 0,
      inProgressTrend: 0,
      completedTrend: 0
    })
    
    // 新增申请表单数据
    const newRequest = ref({
      roomNumber: '',
      building: '',
      applicant: '',
      contact: '',
      type: '',
      urgency: 'NORMAL',
      description: ''
    })
    
    // 当前查看的申请详情
    const currentRequest = ref(null)
    
    // 维修申请列表
    const maintenanceRequests = ref([])
    
    // API基础URL
    const API_BASE_URL = 'http://localhost:8082/api/maintenance'
    
    // 获取维修申请列表
    const fetchMaintenanceRequests = async () => {
      try {
        loading.value = true
        const response = await axios.get(`${API_BASE_URL}/list`)
        if (response.data.code === 200 && response.data.data) {
          maintenanceRequests.value = response.data.data.map(item => ({
            id: item.requestNumber,
            roomNumber: item.roomNumber,
            building: item.buildingName,
            applicant: item.applicantName,
            contact: '',
            type: item.maintenanceType,
            urgency: item.urgencyLevel,
            description: item.description,
            createdAt: item.createTime,
            status: item.status,
            assignee: item.assignedTo || '',
            completedAt: item.completedTime || '',
            rating: item.rating || 0,
            feedback: item.feedback || ''
          }))
        }
      } catch (error) {
        console.error('获取维修申请列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 获取统计数据
    const fetchStatistics = async () => {
      try {
        const response = await axios.get(`${API_BASE_URL}/statistics`)
        if (response.data.code === 200 && response.data.data) {
          const data = response.data.data
          const statusStats = data.statusStatistics || []
          
          // 从状态统计数组中提取各状态的数量
          const pendingItem = statusStats.find(item => item.status === 'PENDING')
          const inProgressItem = statusStats.find(item => item.status === 'IN_PROGRESS')
          const completedItem = statusStats.find(item => item.status === 'COMPLETED')
          const urgentItem = statusStats.find(item => item.urgency_level === 'URGENT')
          
          stats.value = {
            pending: pendingItem ? pendingItem.count : 0,
            inProgress: inProgressItem ? inProgressItem.count : 0,
            completed: completedItem ? completedItem.count : 0,
            urgent: urgentItem ? urgentItem.count : 0,
            total: data.totalCount || 0,
            avgRating: data.averageRating || 0,
            pendingTrend: 0,
            inProgressTrend: 0,
            completedTrend: 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    }
    
    // 计算属性
    const filteredRequests = computed(() => {
      let filtered = maintenanceRequests.value.filter(item => {
        const matchesSearch = !searchQuery.value || 
          item.id.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          item.roomNumber.includes(searchQuery.value) ||
          item.applicant.includes(searchQuery.value) ||
          item.description.includes(searchQuery.value)
        
        const matchesStatus = !statusFilter.value || item.status === statusFilter.value
        const matchesType = !typeFilter.value || item.type === typeFilter.value
        const matchesUrgency = !urgencyFilter.value || item.urgency === urgencyFilter.value
        
        return matchesSearch && matchesStatus && matchesType && matchesUrgency
      })
      
      // 排序
      if (sortField.value) {
        filtered.sort((a, b) => {
          let aVal = a[sortField.value]
          let bVal = b[sortField.value]
          
          if (sortField.value === 'createdAt') {
            aVal = new Date(aVal)
            bVal = new Date(bVal)
          }
          
          if (sortOrder.value === 'asc') {
            return aVal > bVal ? 1 : -1
          } else {
            return aVal < bVal ? 1 : -1
          }
        })
      }
      
      return filtered
    })
    
    const totalPages = computed(() => {
      return Math.ceil(filteredRequests.value.length / pageSize.value)
    })
    
    const paginatedRequests = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value
      const end = start + pageSize.value
      return filteredRequests.value.slice(start, end)
    })
    
    const visiblePages = computed(() => {
      const pages = []
      const total = totalPages.value
      const current = currentPage.value
      
      if (total <= 7) {
        for (let i = 1; i <= total; i++) {
          pages.push(i)
        }
      } else {
        if (current <= 4) {
          for (let i = 1; i <= 5; i++) {
            pages.push(i)
          }
          pages.push('...')
          pages.push(total)
        } else if (current >= total - 3) {
          pages.push(1)
          pages.push('...')
          for (let i = total - 4; i <= total; i++) {
            pages.push(i)
          }
        } else {
          pages.push(1)
          pages.push('...')
          for (let i = current - 1; i <= current + 1; i++) {
            pages.push(i)
          }
          pages.push('...')
          pages.push(total)
        }
      }
      
      return pages
    })
    
    const completionRate = computed(() => {
      const total = stats.value.total
      const completed = stats.value.completed
      return total > 0 ? Math.round((completed / total) * 100) : 0
    })
    
    // 方法
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
    
    const getRelativeTime = (dateString) => {
      const date = new Date(dateString)
      const now = new Date()
      const diff = now - date
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      
      if (days > 0) {
        return `${days}天前`
      } else if (hours > 0) {
        return `${hours}小时前`
      } else {
        return '刚刚'
      }
    }
    
    const getStatusText = (status) => {
      const statusMap = {
        'PENDING': '待处理',
        'IN_PROGRESS': '处理中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    }
    
    const getStatusIcon = (status) => {
      const iconMap = {
        'PENDING': 'fas fa-clock',
        'IN_PROGRESS': 'fas fa-tools',
        'COMPLETED': 'fas fa-check-circle',
        'CANCELLED': 'fas fa-times-circle'
      }
      return iconMap[status] || 'fas fa-question-circle'
    }
    
    const getTypeIcon = (type) => {
      const iconMap = {
        'ELECTRICAL': 'fas fa-bolt',
        'PLUMBING': 'fas fa-tint',
        'FURNITURE': 'fas fa-couch',
        'APPLIANCE': 'fas fa-plug',
        'OTHER': 'fas fa-tools'
      }
      return iconMap[type] || 'fas fa-wrench'
    }
    
    const getProgressWidth = (status) => {
      const progressMap = {
        'PENDING': '25%',
        'IN_PROGRESS': '75%',
        'COMPLETED': '100%',
        'CANCELLED': '0%'
      }
      return progressMap[status] || '0%'
    }
    
    const getProgressText = (status) => {
      const textMap = {
        'PENDING': '等待处理',
        'IN_PROGRESS': '处理中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return textMap[status] || '未知'
    }
    
    const getSortIcon = (field) => {
      if (sortField.value !== field) {
        return 'fas fa-sort'
      }
      return sortOrder.value === 'asc' ? 'fas fa-sort-up' : 'fas fa-sort-down'
    }
    
    const sortBy = (field) => {
      if (sortField.value === field) {
        sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
      } else {
        sortField.value = field
        sortOrder.value = 'asc'
      }
    }
    
    const toggleSelectAll = () => {
      if (selectAll.value) {
        selectedItems.value = paginatedRequests.value.map(item => item.id)
      } else {
        selectedItems.value = []
      }
    }
    
    const filterByStatus = (status) => {
      statusFilter.value = statusFilter.value === status ? '' : status
      applyFilters()
    }
    
    const filterByUrgency = (urgency) => {
      urgencyFilter.value = urgencyFilter.value === urgency ? '' : urgency
      applyFilters()
    }
    
    const applyFilters = () => {
      currentPage.value = 1
    }
    
    const handleSearch = () => {
      currentPage.value = 1
    }
    
    const closeModal = () => {
      showAddModal.value = false
      newRequest.value = {
        roomNumber: '',
        building: '',
        applicant: '',
        contact: '',
        type: '',
        urgency: 'NORMAL',
        description: ''
      }
    }
    
    // 提交维修申请
    const submitRequest = async () => {
      try {
        loading.value = true
        const requestData = {
          roomNumber: newRequest.value.roomNumber,
          buildingName: newRequest.value.building,
          applicantName: newRequest.value.applicant,
          contactPhone: newRequest.value.contact,
          maintenanceType: newRequest.value.type,
          urgencyLevel: newRequest.value.urgency,
          description: newRequest.value.description
        }
        
        const response = await axios.post(`${API_BASE_URL}/create`, requestData)
        if (response.data.code === 200) {
          alert('维修申请提交成功！')
          closeModal()
          await fetchMaintenanceRequests()
          await fetchStatistics()
        } else {
          alert('提交失败：' + response.data.message)
        }
      } catch (error) {
        console.error('提交维修申请失败:', error)
        alert('提交失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    const viewRequest = (request) => {
      currentRequest.value = request
      showDetailModal.value = true
    }
    
    // 编辑维修申请
    const editRequest = async (request) => {
      // 这里可以实现编辑功能，暂时只是查看
      viewRequest(request)
    }
    
    // 删除维修申请
    const deleteRequest = async (request) => {
      if (confirm('确定要删除这个维修申请吗？')) {
        try {
          loading.value = true
          const response = await axios.delete(`${API_BASE_URL}/delete/${request.id}`)
          if (response.data.code === 200) {
            alert('删除成功！')
            await fetchMaintenanceRequests()
            await fetchStatistics()
          } else {
            alert('删除失败：' + response.data.message)
          }
        } catch (error) {
          console.error('删除维修申请失败:', error)
          alert('删除失败，请稍后重试')
        } finally {
          loading.value = false
        }
      }
    }
    
    // 处理维修申请
    const processRequest = async (request) => {
      try {
        loading.value = true
        const response = await axios.put(`${API_BASE_URL}/update-status/${request.id}`, {
          status: 'in_progress'
        })
        if (response.data.code === 200) {
          alert('申请已开始处理！')
          await fetchMaintenanceRequests()
          await fetchStatistics()
        } else {
          alert('操作失败：' + response.data.message)
        }
      } catch (error) {
        console.error('处理维修申请失败:', error)
        alert('操作失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    // 完成维修申请
    const completeRequest = async (request) => {
      try {
        loading.value = true
        const response = await axios.put(`${API_BASE_URL}/complete/${request.id}`, {
          actualCost: 0 // 可以添加实际费用输入
        })
        if (response.data.code === 200) {
          alert('维修已完成！')
          await fetchMaintenanceRequests()
          await fetchStatistics()
        } else {
          alert('操作失败：' + response.data.message)
        }
      } catch (error) {
        console.error('完成维修申请失败:', error)
        alert('操作失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    const exportData = () => {
      console.log('导出数据')
      // 这里实现数据导出功能
    }
    
    // 刷新数据
    const refreshData = async () => {
      await fetchMaintenanceRequests()
      await fetchStatistics()
    }
    
    onMounted(async () => {
      // 组件挂载时获取数据
      await fetchMaintenanceRequests()
      await fetchStatistics()
    })
    
    return {
      // 响应式数据
      searchQuery,
      statusFilter,
      typeFilter,
      urgencyFilter,
      showAddModal,
      showDetailModal,
      viewMode,
      loading,
      currentPage,
      pageSize,
      sortField,
      sortOrder,
      selectedItems,
      selectAll,
      stats,
      newRequest,
      currentRequest,
      maintenanceRequests,
      
      // 计算属性
      filteredRequests,
      totalPages,
      paginatedRequests,
      visiblePages,
      completionRate,
      
      // 方法
      formatDate,
      getRelativeTime,
      getStatusText,
      getStatusIcon,
      getTypeIcon,
      getProgressWidth,
      getProgressText,
      getSortIcon,
      sortBy,
      toggleSelectAll,
      filterByStatus,
      filterByUrgency,
      applyFilters,
      handleSearch,
      closeModal,
      submitRequest,
      viewRequest,
      editRequest,
      deleteRequest,
      processRequest,
      completeRequest,
      exportData,
      refreshData
    }
  }
}
</script>

<style scoped>
.maintenance-container {
  padding: 20px;
  background-color: #f8fafc;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 32px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.page-header h2 {
  margin: 0 0 8px 0;
  font-size: 32px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-header p {
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

/* 快速操作栏 */
.quick-actions {
  background: white;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  border: 1px solid #e2e8f0;
}

.search-section {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.search-box {
  flex: 1;
  min-width: 250px;
  position: relative;
}

.search-box i {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #64748b;
  z-index: 1;
}

.search-input {
  width: 100%;
  padding: 12px 16px 12px 44px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s;
  background: #f8fafc;
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6;
  background: white;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.filter-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.action-buttons {
  display: flex;
  gap: 12px;
  align-items: center;
}

/* 统计面板 */
.stats-panel {
  margin-bottom: 24px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  border: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  gap: 20px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  transition: width 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.stat-card:hover::before {
  width: 100%;
  opacity: 0.05;
}

.stat-card.PENDING::before {
  background: #f59e0b;
}

.stat-card.IN_PROGRESS::before {
  background: #3b82f6;
}

.stat-card.COMPLETED::before {
  background: #10b981;
}

.stat-card.urgent::before {
  background: #ef4444;
}

.stat-card.total::before {
  background: #6366f1;
}

.stat-card.satisfaction::before {
  background: #f59e0b;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.stat-card.PENDING .stat-icon {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.stat-card.IN_PROGRESS .stat-icon {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
}

.stat-card.COMPLETED .stat-icon {
  background: linear-gradient(135deg, #10b981, #059669);
}

.stat-card.urgent .stat-icon {
  background: linear-gradient(135deg, #ef4444, #dc2626);
}

.stat-card.total .stat-icon {
  background: linear-gradient(135deg, #6366f1, #4f46e5);
}

.stat-card.satisfaction .stat-icon {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.stat-content {
  flex: 1;
}

.stat-content h3 {
  margin: 0 0 4px 0;
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
}

.stat-content p {
  margin: 0 0 8px 0;
  color: #64748b;
  font-size: 14px;
  font-weight: 500;
}

.stat-trend {
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  font-weight: 500;
}

.stat-trend.positive {
  background: #dcfce7;
  color: #166534;
}

.stat-trend.negative {
  background: #fef2f2;
  color: #dc2626;
}

.stat-rate {
  font-size: 12px;
  color: #64748b;
  font-weight: 500;
}

.rating-stars {
  display: flex;
  gap: 2px;
  margin-top: 4px;
}

.rating-stars .fa-star {
  font-size: 12px;
  color: #d1d5db;
}

.rating-stars .fa-star.active {
  color: #f59e0b;
}

/* 维修申请列表 */
.maintenance-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  border: 1px solid #e2e8f0;
  overflow: hidden;
}

.list-header {
  padding: 24px;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f8fafc;
}

.header-left h3 {
  margin: 0 0 4px 0;
  color: #1e293b;
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.list-count {
  font-size: 14px;
  color: #64748b;
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.view-toggle {
  display: flex;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  overflow: hidden;
}

.view-toggle .btn {
  border-radius: 0;
  border: none;
  margin: 0;
}

.view-toggle .btn:first-child {
  border-radius: 8px 0 0 8px;
}

.view-toggle .btn:last-child {
  border-radius: 0 8px 8px 0;
}

/* 表格视图 */
.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.data-table th {
  background: #f8fafc;
  padding: 16px;
  text-align: left;
  font-weight: 600;
  color: #475569;
  border-bottom: 2px solid #e2e8f0;
  font-size: 14px;
  position: sticky;
  top: 0;
  z-index: 10;
}

.data-table th.sortable {
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s;
}

.data-table th.sortable:hover {
  background: #e2e8f0;
}

.data-table td {
  padding: 16px;
  border-bottom: 1px solid #f1f5f9;
  font-size: 14px;
  color: #475569;
  vertical-align: middle;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

/* 列宽度定义 */
.col-checkbox {
  width: 50px;
  text-align: center;
}

.col-request-number {
  width: 120px;
}

.col-room {
  width: 120px;
}

.col-applicant {
  width: 120px;
}

.col-type {
  width: 100px;
}

.col-urgency {
  width: 80px;
}

.col-description {
  width: 200px;
  max-width: 200px;
}

.col-time {
  width: 140px;
}

.col-progress {
  width: 120px;
}

.col-status {
  width: 100px;
}

.col-actions {
  width: 180px;
}

.data-table tr:hover {
  background: #f8fafc;
}

.data-table tr.selected {
  background: #eff6ff;
}

.data-table tr.urgent {
  border-left: 4px solid #ef4444;
}

.request-number {
  display: flex;
  align-items: center;
  gap: 8px;
}

.request-number .number {
  font-weight: 600;
  color: #1e293b;
}

.urgent-badge {
  color: #ef4444;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.room-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  max-width: 100%;
}

.room-number {
  font-weight: 600;
  color: #1e293b;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.building {
  color: #64748b;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.applicant-info {
  display: flex;
  align-items: center;
  gap: 8px;
  max-width: 100%;
}

.user-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
  flex-shrink: 0;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
  flex: 1;
}

.user-details .name {
  font-weight: 500;
  color: #1e293b;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-details .contact {
  color: #64748b;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.type-badge {
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.type-badge.ELECTRICAL {
  background: #dbeafe;
  color: #1e40af;
}

.type-badge.PLUMBING {
  background: #dbeafe;
  color: #1e40af;
}

.type-badge.FURNITURE {
  background: #fef3c7;
  color: #92400e;
}

.type-badge.APPLIANCE {
  background: #e0e7ff;
  color: #3730a3;
}

.type-badge.OTHER {
  background: #f3f4f6;
  color: #374151;
}

.urgency-badge {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
}

.urgency-badge.URGENT {
  background: #fef2f2;
  color: #dc2626;
}

.urgency-badge.NORMAL {
  background: #f0f9ff;
  color: #0369a1;
}

.urgency-badge.HIGH {
  background: #fef3c7;
  color: #92400e;
}

.urgency-badge.LOW {
  background: #f7fee7;
  color: #365314;
}

.desc-content {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  white-space: normal;
  line-height: 1.4;
  cursor: help;
}

.time-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  max-width: 100%;
}

.request-time {
  font-weight: 500;
  color: #1e293b;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.relative-time {
  color: #64748b;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.progress-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 80px;
}

.progress-bar {
  width: 100%;
  height: 6px;
  background: #e2e8f0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #3b82f6, #1d4ed8);
  transition: width 0.3s;
}

.progress-text {
  font-size: 11px;
  color: #64748b;
  text-align: center;
}

.status-badge {
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.status-badge.PENDING {
  background: #fef3c7;
  color: #92400e;
}

.status-badge.IN_PROGRESS {
  background: #dbeafe;
  color: #1e40af;
}

.status-badge.COMPLETED {
  background: #d1fae5;
  color: #065f46;
}

.status-badge.CANCELLED {
  background: #fef2f2;
  color: #dc2626;
}

.action-buttons {
  display: flex;
  gap: 4px;
  align-items: center;
}

.action-buttons .btn {
  padding: 6px 8px;
  min-width: auto;
}

/* 分页控件 */
.pagination-container {
  padding: 20px 24px;
  border-top: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f8fafc;
}

.pagination-info {
  color: #64748b;
  font-size: 14px;
}

.pagination {
  display: flex;
  gap: 4px;
  align-items: center;
}

.pagination-ellipsis {
  padding: 8px 4px;
  color: #64748b;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748b;
}

.page-size-selector select {
  padding: 4px 8px;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  font-size: 14px;
}

/* 卡片视图 */
.card-container {
  padding: 24px;
}

.maintenance-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.maintenance-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  border: 1px solid #e2e8f0;
  overflow: hidden;
  transition: all 0.3s;
  position: relative;
}

.maintenance-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
}

.maintenance-card.PENDING::before {
  background: #f59e0b;
}

.maintenance-card.IN_PROGRESS::before {
  background: #3b82f6;
}

.maintenance-card.COMPLETED::before {
  background: #10b981;
}

.maintenance-card.urgent {
  border-color: #ef4444;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
}

.maintenance-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.card-header {
  padding: 20px 20px 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-title .request-number {
  font-weight: 600;
  color: #1e293b;
  font-size: 16px;
}

.urgent-indicator {
  color: #ef4444;
  animation: pulse 2s infinite;
}

.card-status {
  margin-top: 4px;
}

.card-content {
  padding: 16px 20px;
}

.card-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748b;
}

.info-item i {
  width: 16px;
  color: #94a3b8;
}

.card-description {
  background: #f8fafc;
  padding: 12px;
  border-radius: 8px;
  font-size: 14px;
  color: #475569;
  line-height: 1.5;
  border-left: 3px solid #e2e8f0;
}

.card-actions {
  padding: 16px 20px 20px 20px;
  display: flex;
  gap: 8px;
  border-top: 1px solid #f1f5f9;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  position: relative;
  overflow: hidden;
}

.btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
  transition: left 0.5s;
}

.btn:hover::before {
  left: 100%;
}

.btn-primary {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  color: white;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.btn-success {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.btn-success:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}

.btn-outline {
  background: white;
  color: #475569;
  border: 2px solid #e2e8f0;
}

.btn-outline:hover {
  background: #f8fafc;
  border-color: #cbd5e1;
  transform: translateY(-1px);
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
}

.btn-info {
  background: linear-gradient(135deg, #06b6d4, #0891b2);
  color: white;
}

.btn-warning {
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: white;
}

.btn-danger {
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
}

.btn-secondary {
  background: linear-gradient(135deg, #6b7280, #4b5563);
  color: white;
}

.form-select {
  padding: 10px 14px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
}

.form-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
  animation: modalSlideIn 0.3s ease-out;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-header {
  padding: 24px;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #f8fafc;
}

.modal-header h3 {
  margin: 0;
  color: #1e293b;
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #64748b;
  cursor: pointer;
  padding: 8px;
  border-radius: 6px;
  transition: all 0.2s;
}

.close-btn:hover {
  background: #e2e8f0;
  color: #475569;
}

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #374151;
  font-weight: 500;
  font-size: 14px;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s;
  background: #f8fafc;
  box-sizing: border-box;
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
  outline: none;
  border-color: #3b82f6;
  background: white;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
  font-family: inherit;
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
  padding: 24px;
  border-top: 1px solid #e2e8f0;
  background: #f8fafc;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  }
  
  .maintenance-cards {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 768px) {
  .maintenance-container {
    padding: 16px;
  }
  
  .page-header {
    padding: 24px;
  }
  
  .page-header h2 {
    font-size: 24px;
  }
  
  .search-section {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    min-width: auto;
  }
  
  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
  }
  
  .stat-card {
    padding: 20px;
  }
  
  .stat-icon {
    width: 48px;
    height: 48px;
    font-size: 20px;
  }
  
  .stat-content h3 {
    font-size: 24px;
  }
  
  .list-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .header-actions {
    flex-wrap: wrap;
  }
  
  .data-table {
    font-size: 12px;
  }
  
  .data-table th,
  .data-table td {
    padding: 12px 8px;
  }
  
  .desc-content {
    max-width: 120px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
  
  .pagination-container {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .maintenance-cards {
    grid-template-columns: 1fr;
  }
  
  .modal-content {
    width: 95%;
    margin: 20px;
  }
}

@media (max-width: 480px) {
  .page-header h2 {
    font-size: 20px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .data-table th,
  .data-table td {
    padding: 8px 4px;
  }
  
  .action-buttons .btn {
    padding: 4px 6px;
    font-size: 11px;
  }
}
</style>