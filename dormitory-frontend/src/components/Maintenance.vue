<template>
  <div class="maintenance-container">
    <div class="page-header">
      <h2>维修管理</h2>
      <p>管理宿舍维修申请和维修记录</p>
    </div>
    
    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-box">
        <i class="fas fa-search"></i>
        <input 
          type="text" 
          placeholder="搜索维修申请..." 
          v-model="searchQuery"
          class="search-input"
        >
      </div>
      <div class="action-buttons">
        <select v-model="statusFilter" class="form-select">
          <option value="">全部状态</option>
          <option value="pending">待处理</option>
          <option value="in_progress">处理中</option>
          <option value="completed">已完成</option>
          <option value="cancelled">已取消</option>
        </select>
        <button class="btn btn-primary" @click="showAddModal = true">
          <i class="fas fa-plus"></i>
          新增维修申请
        </button>
      </div>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon pending">
          <i class="fas fa-clock"></i>
        </div>
        <div class="stat-content">
          <h3>{{ stats.pending }}</h3>
          <p>待处理</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon in-progress">
          <i class="fas fa-tools"></i>
        </div>
        <div class="stat-content">
          <h3>{{ stats.inProgress }}</h3>
          <p>处理中</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon completed">
          <i class="fas fa-check-circle"></i>
        </div>
        <div class="stat-content">
          <h3>{{ stats.completed }}</h3>
          <p>已完成</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon total">
          <i class="fas fa-list"></i>
        </div>
        <div class="stat-content">
          <h3>{{ stats.total }}</h3>
          <p>总申请</p>
        </div>
      </div>
    </div>
    
    <!-- 维修申请列表 -->
    <div class="maintenance-list">
      <div class="list-header">
        <h3>维修申请列表</h3>
      </div>
      
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>申请编号</th>
              <th>宿舍房间</th>
              <th>申请人</th>
              <th>维修类型</th>
              <th>问题描述</th>
              <th>申请时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="request in filteredRequests" :key="request.id">
              <td>{{ request.id }}</td>
              <td>{{ request.roomNumber }}</td>
              <td>{{ request.applicant }}</td>
              <td>{{ request.type }}</td>
              <td class="description">{{ request.description }}</td>
              <td>{{ formatDate(request.createdAt) }}</td>
              <td>
                <span :class="['status-badge', request.status]">
                  {{ getStatusText(request.status) }}
                </span>
              </td>
              <td>
                <div class="action-buttons">
                  <button 
                    class="btn btn-sm btn-outline"
                    @click="viewRequest(request)"
                  >
                    查看
                  </button>
                  <button 
                    v-if="request.status === 'pending'"
                    class="btn btn-sm btn-primary"
                    @click="processRequest(request)"
                  >
                    处理
                  </button>
                  <button 
                    v-if="request.status === 'in_progress'"
                    class="btn btn-sm btn-success"
                    @click="completeRequest(request)"
                  >
                    完成
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
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
import { ref, reactive, computed } from 'vue'

export default {
  name: 'Maintenance',
  setup() {
    const searchQuery = ref('')
    const statusFilter = ref('')
    const showAddModal = ref(false)
    
    const stats = reactive({
      pending: 8,
      inProgress: 5,
      completed: 23,
      total: 36
    })
    
    const newRequest = reactive({
      roomNumber: '',
      applicant: '',
      type: '',
      description: ''
    })
    
    const maintenanceRequests = ref([
      {
        id: 'MR001',
        roomNumber: 'A101',
        applicant: '张三',
        type: '水电',
        description: '水龙头漏水，需要更换密封圈',
        createdAt: '2024-01-15T10:30:00',
        status: 'pending'
      },
      {
        id: 'MR002',
        roomNumber: 'B205',
        applicant: '李四',
        type: '家具',
        description: '床铺松动，需要加固',
        createdAt: '2024-01-14T14:20:00',
        status: 'in_progress'
      },
      {
        id: 'MR003',
        roomNumber: 'C303',
        applicant: '王五',
        type: '门窗',
        description: '窗户关不严，需要调整',
        createdAt: '2024-01-13T09:15:00',
        status: 'completed'
      },
      {
        id: 'MR004',
        roomNumber: 'A203',
        applicant: '赵六',
        type: '网络',
        description: '网络端口故障，无法上网',
        createdAt: '2024-01-12T16:45:00',
        status: 'pending'
      },
      {
        id: 'MR005',
        roomNumber: 'B108',
        applicant: '钱七',
        type: '其他',
        description: '空调制冷效果差，需要检修',
        createdAt: '2024-01-11T11:30:00',
        status: 'in_progress'
      }
    ])
    
    const filteredRequests = computed(() => {
      let filtered = maintenanceRequests.value
      
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(request => 
          request.roomNumber.toLowerCase().includes(query) ||
          request.applicant.toLowerCase().includes(query) ||
          request.type.toLowerCase().includes(query) ||
          request.description.toLowerCase().includes(query)
        )
      }
      
      if (statusFilter.value) {
        filtered = filtered.filter(request => request.status === statusFilter.value)
      }
      
      return filtered
    })
    
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN')
    }
    
    const getStatusText = (status) => {
      const statusMap = {
        pending: '待处理',
        in_progress: '处理中',
        completed: '已完成',
        cancelled: '已取消'
      }
      return statusMap[status] || status
    }
    
    const closeModal = () => {
      showAddModal.value = false
      Object.assign(newRequest, {
        roomNumber: '',
        applicant: '',
        type: '',
        description: ''
      })
    }
    
    const submitRequest = () => {
      const request = {
        id: `MR${String(maintenanceRequests.value.length + 1).padStart(3, '0')}`,
        ...newRequest,
        createdAt: new Date().toISOString(),
        status: 'pending'
      }
      
      maintenanceRequests.value.unshift(request)
      stats.pending++
      stats.total++
      
      closeModal()
      alert('维修申请提交成功！')
    }
    
    const viewRequest = (request) => {
      alert(`查看维修申请详情：\n申请编号：${request.id}\n房间：${request.roomNumber}\n申请人：${request.applicant}\n类型：${request.type}\n描述：${request.description}`)
    }
    
    const processRequest = (request) => {
      request.status = 'in_progress'
      stats.pending--
      stats.inProgress++
      alert('维修申请已开始处理！')
    }
    
    const completeRequest = (request) => {
      request.status = 'completed'
      stats.inProgress--
      stats.completed++
      alert('维修申请已完成！')
    }
    
    return {
      searchQuery,
      statusFilter,
      showAddModal,
      stats,
      newRequest,
      maintenanceRequests,
      filteredRequests,
      formatDate,
      getStatusText,
      closeModal,
      submitRequest,
      viewRequest,
      processRequest,
      completeRequest
    }
  }
}
</script>

<style scoped>
.maintenance-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
}

.page-header p {
  margin: 0;
  color: #6b7280;
  font-size: 16px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  gap: 16px;
}

.search-box {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-box i {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
}

.search-input {
  width: 100%;
  padding: 10px 12px 10px 40px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  align-items: center;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 16px;
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

.stat-icon.pending {
  background: #f59e0b;
}

.stat-icon.in-progress {
  background: #3b82f6;
}

.stat-icon.completed {
  background: #10b981;
}

.stat-icon.total {
  background: #6b7280;
}

.stat-content h3 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
}

.stat-content p {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}

.maintenance-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.list-header {
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;
  background: #f9fafb;
}

.list-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #f3f4f6;
}

.data-table th {
  background: #f9fafb;
  font-weight: 600;
  color: #374151;
  font-size: 14px;
}

.data-table td {
  color: #6b7280;
  font-size: 14px;
}

.data-table td.description {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.data-table tbody tr:hover {
  background: #f9fafb;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.pending {
  background: #fef3c7;
  color: #d97706;
}

.status-badge.in_progress {
  background: #dbeafe;
  color: #2563eb;
}

.status-badge.completed {
  background: #d1fae5;
  color: #059669;
}

.status-badge.cancelled {
  background: #fee2e2;
  color: #dc2626;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}

.btn-primary {
  background: #3b82f6;
  color: white;
}

.btn-primary:hover {
  background: #2563eb;
}

.btn-success {
  background: #10b981;
  color: white;
}

.btn-success:hover {
  background: #059669;
}

.btn-outline {
  background: transparent;
  color: #374151;
  border: 1px solid #d1d5db;
}

.btn-outline:hover {
  background: #f3f4f6;
}

.form-select {
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  background: white;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  color: #9ca3af;
  cursor: pointer;
  padding: 4px;
}

.close-btn:hover {
  color: #374151;
}

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #374151;
  font-size: 14px;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
}

@media (max-width: 768px) {
  .action-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-box {
    max-width: none;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .data-table {
    font-size: 12px;
  }
  
  .data-table th,
  .data-table td {
    padding: 8px 12px;
  }
}
</style>