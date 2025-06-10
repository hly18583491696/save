<template>
  <div class="visitor-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2><i class="fas fa-users"></i> 访客管理</h2>
      <p>管理宿舍访客登记、查询和统计</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-calendar-day"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.todayCount || 0 }}</h3>
          <p>今日访客</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-calendar-alt"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.thisMonthCount || 0 }}</h3>
          <p>本月访客</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-user-clock"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.currentVisitingCount || 0 }}</h3>
          <p>访问中</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-exclamation-triangle"></i>
        </div>
        <div class="stat-content">
          <h3>{{ statistics.expiredCount || 0 }}</h3>
          <p>已过期</p>
        </div>
      </div>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <button class="btn btn-primary" @click="showAddDialog = true">
          <i class="fas fa-plus"></i> 新增访客
        </button>
        <button class="btn btn-warning" @click="updateExpiredVisitors">
          <i class="fas fa-sync"></i> 更新过期状态
        </button>
      </div>
      <div class="toolbar-right">
        <div class="search-box">
          <input 
            type="text" 
            v-model="searchKeyword" 
            placeholder="搜索访客姓名或学生姓名"
            @keyup.enter="searchVisitors"
          >
          <button @click="searchVisitors">
            <i class="fas fa-search"></i>
          </button>
        </div>
        <select v-model="statusFilter" @change="filterByStatus">
          <option value="">全部状态</option>
          <option value="VISITING">访问中</option>
          <option value="LEFT">已离开</option>
          <option value="EXPIRED">已过期</option>
        </select>
      </div>
    </div>

    <!-- 访客列表 -->
    <div class="visitor-table">
      <table>
        <thead>
          <tr>
            <th>访客姓名</th>
            <th>联系电话</th>
            <th>被访学生</th>
            <th>房间号</th>
            <th>楼栋</th>
            <th>来访目的</th>
            <th>来访时间</th>
            <th>预计离开</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="visitor in visitors" :key="visitor.id">
            <td>{{ visitor.visitorName }}</td>
            <td>{{ visitor.phone || '-' }}</td>
            <td>{{ visitor.visitedStudentName }}</td>
            <td>{{ visitor.roomNumber }}</td>
            <td>{{ visitor.buildingName }}</td>
            <td>{{ visitor.visitPurpose || '-' }}</td>
            <td>{{ formatDateTime(visitor.visitTime) }}</td>
            <td>{{ formatDateTime(visitor.expectedLeaveTime) }}</td>
            <td>
              <span class="status-badge" :class="getStatusClass(visitor.status)">
                {{ getStatusText(visitor.status) }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button 
                  v-if="visitor.status === 'VISITING'" 
                  class="btn btn-sm btn-success" 
                  @click="markAsLeft(visitor.id)"
                  title="标记离开"
                >
                  <i class="fas fa-sign-out-alt"></i>
                </button>
                <button 
                  class="btn btn-sm btn-info" 
                  @click="editVisitor(visitor)"
                  title="编辑"
                >
                  <i class="fas fa-edit"></i>
                </button>
                <button 
                  class="btn btn-sm btn-danger" 
                  @click="deleteVisitor(visitor.id)"
                  title="删除"
                >
                  <i class="fas fa-trash"></i>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="visitors.length === 0" class="no-data">
        <i class="fas fa-inbox"></i>
        <p>暂无访客记录</p>
      </div>
    </div>

    <!-- 新增/编辑访客对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="modal-overlay" @click="closeDialog">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>
            <i class="fas fa-user-plus"></i>
            {{ showAddDialog ? '新增访客' : '编辑访客' }}
          </h3>
          <button class="close-btn" @click="closeDialog">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveVisitor">
            <div class="form-row">
              <div class="form-group">
                <label>访客姓名 *</label>
                <input 
                  type="text" 
                  v-model="currentVisitor.visitorName" 
                  required
                  placeholder="请输入访客姓名"
                >
              </div>
              <div class="form-group">
                <label>联系电话</label>
                <input 
                  type="tel" 
                  v-model="currentVisitor.phone" 
                  placeholder="请输入联系电话"
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>身份证号</label>
                <input 
                  type="text" 
                  v-model="currentVisitor.idCard" 
                  placeholder="请输入身份证号"
                >
              </div>
              <div class="form-group">
                <label>被访学生ID *</label>
                <input 
                  type="number" 
                  v-model="currentVisitor.visitedStudentId" 
                  required
                  placeholder="请输入学生ID"
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>被访学生姓名 *</label>
                <input 
                  type="text" 
                  v-model="currentVisitor.visitedStudentName" 
                  required
                  placeholder="请输入学生姓名"
                >
              </div>
              <div class="form-group">
                <label>房间ID *</label>
                <input 
                  type="number" 
                  v-model="currentVisitor.roomId" 
                  required
                  placeholder="请输入房间ID"
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>房间号 *</label>
                <input 
                  type="text" 
                  v-model="currentVisitor.roomNumber" 
                  required
                  placeholder="请输入房间号"
                >
              </div>
              <div class="form-group">
                <label>楼栋ID</label>
                <input 
                  type="number" 
                  v-model="currentVisitor.buildingId" 
                  placeholder="请输入楼栋ID"
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>楼栋名称</label>
                <input 
                  type="text" 
                  v-model="currentVisitor.buildingName" 
                  placeholder="请输入楼栋名称"
                >
              </div>
              <div class="form-group">
                <label>来访目的</label>
                <input 
                  type="text" 
                  v-model="currentVisitor.visitPurpose" 
                  placeholder="请输入来访目的"
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>来访时间 *</label>
                <input 
                  type="datetime-local" 
                  v-model="currentVisitor.visitTime" 
                  required
                >
              </div>
              <div class="form-group">
                <label>预计离开时间</label>
                <input 
                  type="datetime-local" 
                  v-model="currentVisitor.expectedLeaveTime"
                >
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>登记保安</label>
                <input 
                  type="text" 
                  v-model="currentVisitor.guardName" 
                  placeholder="请输入登记保安姓名"
                >
              </div>
              <div class="form-group">
                <label>备注</label>
                <textarea 
                  v-model="currentVisitor.remarks" 
                  placeholder="请输入备注信息"
                  rows="3"
                ></textarea>
              </div>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-secondary" @click="closeDialog">
                取消
              </button>
              <button type="submit" class="btn btn-primary">
                {{ showAddDialog ? '添加' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'VisitorManagement',
  data() {
    return {
      visitors: [],
      statistics: {},
      searchKeyword: '',
      statusFilter: '',
      showAddDialog: false,
      showEditDialog: false,
      currentVisitor: {
        visitorName: '',
        phone: '',
        idCard: '',
        visitedStudentId: null,
        visitedStudentName: '',
        roomId: null,
        roomNumber: '',
        buildingId: null,
        buildingName: '',
        visitPurpose: '',
        visitTime: '',
        expectedLeaveTime: '',
        guardName: '',
        remarks: ''
      }
    }
  },
  mounted() {
    this.loadVisitors()
    this.loadStatistics()
  },
  methods: {
    async loadVisitors() {
      try {
        console.log('开始加载访客数据')
        const response = await axios.get('http://localhost:8082/api/visitor/list')
        console.log('API响应:', response.data)
        if (response.data.code === 200) {
          console.log('加载成功，更新前访客数量:', this.visitors.length)
          this.visitors = response.data.data || []
          console.log('更新后访客数量:', this.visitors.length)
          console.log('访客数据:', this.visitors)
        } else {
          this.visitors = []
          console.error('加载访客数据失败:', response.data.message)
        }
      } catch (error) {
        console.error('加载访客数据失败:', error)
        this.visitors = []
      }
    },
    async loadStatistics() {
      try {
        const response = await axios.get('http://localhost:8082/api/visitor/statistics')
        console.log('统计API响应:', response.data) // 添加调试日志
        if (response.data.code === 200) {
          this.statistics = response.data.data || {}
          console.log('统计数据:', this.statistics) // 添加调试日志
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
      }
    },
    async searchVisitors() {
      if (!this.searchKeyword.trim()) {
        this.loadVisitors()
        return
      }
      try {
        const response = await axios.get('http://localhost:8082/api/visitor/search', {
          params: {
            visitorName: this.searchKeyword
          }
        })
        if (response.data.code === 200) {
          this.visitors = response.data.data || []
        }
      } catch (error) {
        console.error('搜索访客失败:', error)
        alert('搜索访客失败')
      }
    },
    async filterByStatus() {
      if (!this.statusFilter) {
        this.loadVisitors()
        return
      }
      try {
        const response = await axios.get(`http://localhost:8082/api/visitor/status/${this.statusFilter}`)
        if (response.data.code === 200) {
          this.visitors = response.data.data || []
        }
      } catch (error) {
        console.error('筛选访客失败:', error)
        alert('筛选访客失败')
      }
    },
    async saveVisitor() {
      try {
        const url = this.showAddDialog ? 'http://localhost:8082/api/visitor/create' : 'http://localhost:8082/api/visitor/update'
        const method = this.showAddDialog ? 'post' : 'put'
        
        const response = await axios[method](url, this.currentVisitor)
        if (response.data.code === 200) {
          alert(response.data.message || '操作成功')
          this.closeDialog()
          this.loadVisitors()
          this.loadStatistics()
        } else {
          alert(response.data.message || '操作失败')
        }
      } catch (error) {
        console.error('保存访客失败:', error)
        alert('保存访客失败')
      }
    },
    async markAsLeft(id) {
      try {
        const response = await axios.put(`http://localhost:8082/api/visitor/leave/${id}`)
        if (response.data.code === 200) {
          alert('访客离开登记成功')
          this.loadVisitors()
          this.loadStatistics()
        } else {
          alert(response.data.message || '操作失败')
        }
      } catch (error) {
        console.error('标记离开失败:', error)
        alert('标记离开失败')
      }
    },
    async deleteVisitor(id) {
      console.log('开始删除访客，ID:', id)
      if (!confirm('确定要删除这条访客记录吗？')) {
        console.log('用户取消删除操作')
        return
      }
      try {
        console.log('发送删除请求到:', `http://localhost:8082/api/visitor/delete/${id}`)
        const response = await axios.delete(`http://localhost:8082/api/visitor/delete/${id}`)
        console.log('删除请求响应:', response.data)
        if (response.data.code === 200) {
          console.log('删除成功，开始重新加载数据')
          alert('删除成功')
          console.log('调用loadVisitors重新加载访客列表')
          await this.loadVisitors()
          console.log('调用loadStatistics重新加载统计数据')
          await this.loadStatistics()
          console.log('数据重新加载完成')
        } else {
          console.log('删除失败，错误信息:', response.data.message)
          alert(response.data.message || '删除失败')
        }
      } catch (error) {
        console.error('删除访客失败:', error)
        alert('删除访客失败')
      }
    },
    async updateExpiredVisitors() {
      try {
        const response = await axios.put('http://localhost:8082/api/visitor/update-expired')
        if (response.data.code === 200) {
          alert(response.data.message || '更新成功')
          this.loadVisitors()
          this.loadStatistics()
        } else {
          alert(response.data.message || '更新失败')
        }
      } catch (error) {
        console.error('更新过期状态失败:', error)
        alert('更新过期状态失败')
      }
    },
    editVisitor(visitor) {
      this.currentVisitor = { ...visitor }
      // 格式化时间为 datetime-local 格式
      if (this.currentVisitor.visitTime) {
        this.currentVisitor.visitTime = this.formatDateTimeForInput(this.currentVisitor.visitTime)
      }
      if (this.currentVisitor.expectedLeaveTime) {
        this.currentVisitor.expectedLeaveTime = this.formatDateTimeForInput(this.currentVisitor.expectedLeaveTime)
      }
      this.showEditDialog = true
    },
    closeDialog() {
      this.showAddDialog = false
      this.showEditDialog = false
      this.currentVisitor = {
        visitorName: '',
        phone: '',
        idCard: '',
        visitedStudentId: null,
        visitedStudentName: '',
        roomId: null,
        roomNumber: '',
        buildingId: null,
        buildingName: '',
        visitPurpose: '',
        visitTime: '',
        expectedLeaveTime: '',
        guardName: '',
        remarks: ''
      }
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    },
    formatDateTimeForInput(dateTime) {
      if (!dateTime) return ''
      const date = new Date(dateTime)
      return date.toISOString().slice(0, 16)
    },
    getStatusText(status) {
      const statusMap = {
        'VISITING': '访问中',
        'LEFT': '已离开',
        'EXPIRED': '已过期'
      }
      return statusMap[status] || status
    },
    getStatusClass(status) {
      const classMap = {
        'VISITING': 'status-visiting',
        'LEFT': 'status-left',
        'EXPIRED': 'status-expired'
      }
      return classMap[status] || ''
    }
  }
}
</script>

<style scoped>
.visitor-management {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 24px;
}

.page-header p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 20px;
}

.stat-content h3 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #333;
}

.stat-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.toolbar {
  background: white;
  padding: 15px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.toolbar-left {
  display: flex;
  gap: 10px;
}

.toolbar-right {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-box {
  display: flex;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.search-box input {
  border: none;
  padding: 8px 12px;
  outline: none;
  width: 200px;
}

.search-box button {
  border: none;
  background: #007bff;
  color: white;
  padding: 8px 12px;
  cursor: pointer;
}

.search-box button:hover {
  background: #0056b3;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  transition: all 0.3s;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-primary:hover {
  background: #0056b3;
}

.btn-warning {
  background: #ffc107;
  color: #212529;
}

.btn-warning:hover {
  background: #e0a800;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-success:hover {
  background: #1e7e34;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-info:hover {
  background: #138496;
}

.btn-danger {
  background: #dc3545;
  color: white;
}

.btn-danger:hover {
  background: #c82333;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #545b62;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}

select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
}

.visitor-table {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.visitor-table table {
  width: 100%;
  border-collapse: collapse;
}

.visitor-table th,
.visitor-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.visitor-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.visitor-table tbody tr:hover {
  background: #f8f9fa;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-visiting {
  background: #d4edda;
  color: #155724;
}

.status-left {
  background: #d1ecf1;
  color: #0c5460;
}

.status-expired {
  background: #f8d7da;
  color: #721c24;
}

.action-buttons {
  display: flex;
  gap: 5px;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #666;
}

.no-data i {
  font-size: 48px;
  margin-bottom: 10px;
  opacity: 0.5;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #666;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group textarea {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0,123,255,0.25);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

@media (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .toolbar-left,
  .toolbar-right {
    justify-content: center;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .search-box input {
    width: 150px;
  }
}
</style>