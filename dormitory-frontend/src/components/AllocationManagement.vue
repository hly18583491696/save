<template>
  <div class="page-content">
    <div class="page-header">
      <h1>住宿分配管理</h1>
      <p>管理学生住宿分配和调换</p>
    </div>
    
    <!-- 搜索和操作栏 -->
    <div class="search-bar">
      <div class="search-inputs">
        <div class="input-group">
          <i class="fas fa-search"></i>
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="搜索学生姓名、学号或房间号..."
            @input="handleSearch"
          >
        </div>
        
        <select v-model="statusFilter" @change="handleSearch" class="filter-select">
          <option value="">全部状态</option>
          <option value="ACTIVE">已入住</option>
          <option value="CHECKED_OUT">已退宿</option>
        </select>
        
        <select v-model="buildingFilter" @change="handleSearch" class="filter-select">
          <option value="">全部楼栋</option>
          <option v-for="building in buildings" :key="building.id" :value="building.id">
            {{ building.name }}
          </option>
        </select>
      </div>
      
      <button class="btn btn-primary" @click="showAddDialog">
        <i class="fas fa-plus"></i>
        分配住宿
      </button>
    </div>
    
    <!-- 分配列表 -->
    <div class="allocation-list">
      <div class="list-header">
        <div class="header-item">学生信息</div>
        <div class="header-item">宿舍信息</div>
        <div class="header-item">入住时间</div>
        <div class="header-item">状态</div>
        <div class="header-item">操作</div>
      </div>
      
      <div v-if="loading" class="loading">
        <i class="fas fa-spinner fa-spin"></i>
        <span>加载中...</span>
      </div>
      
      <div v-else-if="allocations.length === 0" class="empty-state">
        <i class="fas fa-bed"></i>
        <h3>暂无住宿分配记录</h3>
        <p>点击上方按钮开始分配住宿</p>
      </div>
      
      <div v-else class="list-content">
        <div 
          v-for="allocation in allocations" 
          :key="allocation.id" 
          class="allocation-item"
        >
          <div class="item-content">
            <div class="student-info">
              <div class="student-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="student-details">
                <div class="student-name">{{ allocation.studentName }}</div>
                <div class="student-code">{{ allocation.studentNumber }}</div>
                <div class="student-class">{{ allocation.className || '未填写班级' }}</div>
                <div class="student-id">{{ allocation.idCard || '未填写身份证' }}</div>
              </div>
            </div>
            
            <div class="room-info">
              <div class="room-details">
                <div class="room-number">{{ allocation.buildingName }} - {{ allocation.roomNumber }}室</div>
                <div class="bed-number">{{ allocation.bedNumber }}号床</div>
              </div>
            </div>
            
            <div class="check-in-date">
              {{ formatDate(allocation.checkInDate) }}
            </div>
            
            <div class="status">
              <span :class="['status-badge', allocation.status === 'ACTIVE' ? 'status-active' : 'status-inactive']">
                {{ allocation.status === 'ACTIVE' ? '已入住' : '已退宿' }}
              </span>
            </div>
            
            <div class="actions">
              <button class="btn-icon" @click="showEditDialog(allocation)" title="编辑">
                <i class="fas fa-edit"></i>
              </button>
              <button class="btn-icon" @click="showDetailDialog(allocation)" title="详情">
                <i class="fas fa-eye"></i>
              </button>
              <button 
                v-if="allocation.status === 'ACTIVE'"
                class="btn-icon btn-danger" 
                @click="handleCheckOut(allocation)" 
                title="退宿"
              >
                <i class="fas fa-sign-out-alt"></i>
              </button>
              <button 
                class="btn-icon btn-danger" 
                @click="handleDelete(allocation)" 
                title="删除"
              >
                <i class="fas fa-trash"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <div v-if="totalPages > 1" class="pagination">
      <button 
        class="btn btn-outline" 
        :disabled="currentPage === 1" 
        @click="changePage(currentPage - 1)"
      >
        上一页
      </button>
      
      <span class="page-info">
        第 {{ currentPage }} 页，共 {{ totalPages }} 页
      </span>
      
      <button 
        class="btn btn-outline" 
        :disabled="currentPage === totalPages" 
        @click="changePage(currentPage + 1)"
      >
        下一页
      </button>
    </div>
    
    <!-- 添加/编辑对话框 -->
    <div v-if="showDialog" class="dialog-overlay" @click="closeDialog">
      <div class="dialog" @click.stop>
        <div class="dialog-header">
          <h3>{{ isEditing ? '编辑住宿分配' : '分配住宿' }}</h3>
          <button class="btn-close" @click="closeDialog">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="dialog-content">
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label>学生</label>
              <select v-model="formData.studentId" @change="onStudentChange" required :disabled="isEditing">
                <option value="">请选择学生</option>
                <option v-for="student in availableStudents" :key="student.id" :value="student.id">
                  {{ student.studentName }} ({{ student.studentNumber }})
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>楼栋</label>
              <select v-model="formData.buildingId" @change="onBuildingChange" required>
                <option value="">请选择楼栋</option>
                <option v-for="building in buildings" :key="building.id" :value="building.id">
                  {{ building.name }}
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>房间</label>
              <select v-model="formData.roomId" @change="onRoomChange" required :disabled="!formData.buildingId">
                <option value="">请选择房间</option>
                <option v-for="room in availableRooms" :key="room.id" :value="room.id">
                  {{ room.roomNumber }}室 ({{ room.roomType }})
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>床位</label>
              <select v-model="formData.bedNumber" required :disabled="!formData.roomId">
                <option value="">请选择床位</option>
                <option v-for="bed in availableBeds" :key="bed" :value="bed">
                  {{ bed }}号床
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>入住时间</label>
              <input 
                v-model="formData.checkInDate" 
                type="date" 
                required
              >
            </div>
            
            <div class="form-group">
              <label>备注</label>
              <textarea 
                v-model="formData.remarks" 
                placeholder="请输入备注信息"
                rows="3"
              ></textarea>
            </div>
            
            <div class="form-actions">
              <button type="button" class="btn btn-outline" @click="closeDialog">
                取消
              </button>
              <button type="submit" class="btn btn-primary" :disabled="submitting">
                <i v-if="submitting" class="fas fa-spinner fa-spin"></i>
                {{ submitting ? '保存中...' : (isEditing ? '更新' : '分配') }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <!-- 详情对话框 -->
    <div v-if="showDetailDialogVisible" class="dialog-overlay" @click="closeDetailDialog">
      <div class="dialog" @click.stop>
        <div class="dialog-header">
          <h3>住宿分配详情</h3>
          <button class="btn-close" @click="closeDetailDialog">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="dialog-content">
          <div class="detail-grid">
            <div class="detail-item">
              <label>学生姓名</label>
              <span>{{ selectedAllocation?.studentName }}</span>
            </div>
            <div class="detail-item">
              <label>学号</label>
              <span>{{ selectedAllocation?.studentNumber }}</span>
            </div>
            <div class="detail-item">
              <label>班级</label>
              <span>{{ selectedAllocation?.className || '未填写' }}</span>
            </div>
            <div class="detail-item">
              <label>身份证号</label>
              <span>{{ selectedAllocation?.idCard || '未填写' }}</span>
            </div>
            <div class="detail-item">
              <label>楼栋</label>
              <span>{{ selectedAllocation?.buildingName }}</span>
            </div>
            <div class="detail-item">
              <label>房间</label>
              <span>{{ selectedAllocation?.roomNumber }}室</span>
            </div>
            <div class="detail-item">
              <label>床位</label>
              <span>{{ selectedAllocation?.bedNumber }}号床</span>
            </div>
            <div class="detail-item">
              <label>入住时间</label>
              <span>{{ formatDate(selectedAllocation?.checkInDate) }}</span>
            </div>
            <div class="detail-item">
              <label>状态</label>
              <span :class="['status-badge', selectedAllocation?.status === 'ACTIVE' ? 'status-active' : 'status-inactive']">
                {{ selectedAllocation?.status === 'ACTIVE' ? '已入住' : '已退宿' }}
              </span>
            </div>
            <div class="detail-item full-width">
              <label>备注</label>
              <span>{{ selectedAllocation?.remarks || '无' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'AllocationManagement',
  setup() {
    const loading = ref(false)
    const submitting = ref(false)
    const showDialog = ref(false)
    const showDetailDialogVisible = ref(false)
    const isEditing = ref(false)
    const searchQuery = ref('')
    const statusFilter = ref('')
    const buildingFilter = ref('')
    const currentPage = ref(1)
    const totalPages = ref(1)
    const pageSize = 10
    
    const allocations = ref([])
    const buildings = ref([])
    const availableStudents = ref([])
    const availableRooms = ref([])
    const availableBeds = ref([])
    const selectedAllocation = ref(null)
    
    const formData = reactive({
      id: null,
      studentId: '',
      studentName: '',
      studentNumber: '',
      className: '',
      idCard: '',
      buildingId: '',
      buildingName: '',
      roomId: '',
      roomNumber: '',
      bedNumber: '',
      checkInDate: '',
      status: 'ACTIVE',
      remarks: ''
    })
    
    const loadAllocations = async () => {
      loading.value = true
      try {
        const response = await fetch('http://localhost:8082/api/accommodations')
        const result = await response.json()
        
        console.log('API响应:', result)
        console.log('住宿分配数据:', result.data)
        
        if (result.success) {
          allocations.value = result.data
          console.log('allocations.value已更新:', allocations.value)
        } else {
          console.error(result.message || '加载住宿分配数据失败')
          allocations.value = []
        }
        
        totalPages.value = Math.ceil(allocations.value.length / pageSize)
      } catch (error) {
        console.error('加载住宿分配失败:', error)
        allocations.value = []
      } finally {
        loading.value = false
      }
    }
    
    const loadBuildings = async () => {
      try {
        const response = await fetch('http://localhost:8082/api/dorm/buildings')
        const result = await response.json()
        
        if (result.success) {
          buildings.value = result.data
        } else {
          console.error(result.message || '加载楼栋数据失败')
          buildings.value = []
        }
      } catch (error) {
        console.error('加载楼栋失败:', error)
        buildings.value = []
      }
    }
    
    const loadStudents = async () => {
      try {
        // 模拟API调用 - 获取未分配住宿的学生
        availableStudents.value = [
          { 
            id: 4, 
            studentName: '赵六', 
            studentNumber: '2021004',
            className: '计算机科学与技术1班',
            idCard: '123456789012345678'
          },
          { 
            id: 5, 
            studentName: '钱七', 
            studentNumber: '2021005',
            className: '软件工程1班',
            idCard: '123456789012345679'
          },
          { 
            id: 6, 
            studentName: '孙八', 
            studentNumber: '2021006',
            className: '网络工程1班',
            idCard: '123456789012345680'
          }
        ]
      } catch (error) {
        console.error('加载学生失败:', error)
      }
    }
    
    const onStudentChange = () => {
      if (formData.studentId) {
        const selectedStudent = availableStudents.value.find(s => s.id == formData.studentId)
        if (selectedStudent) {
          formData.studentName = selectedStudent.studentName
          formData.studentNumber = selectedStudent.studentNumber
          formData.className = selectedStudent.className
          formData.idCard = selectedStudent.idCard
        }
      }
    }
    
    const onBuildingChange = () => {
      if (formData.buildingId) {
        const selectedBuilding = buildings.value.find(b => b.id == formData.buildingId)
        if (selectedBuilding) {
          formData.buildingName = selectedBuilding.name
        }
      }
      loadRooms()
      formData.roomId = ''
      formData.roomNumber = ''
      formData.bedNumber = ''
      availableBeds.value = []
    }
    
    const onRoomChange = () => {
      if (formData.roomId) {
        const selectedRoom = availableRooms.value.find(r => r.id == formData.roomId)
        if (selectedRoom) {
          formData.roomNumber = selectedRoom.roomNumber
        }
      }
      loadBeds()
      formData.bedNumber = ''
    }
    
    const loadRooms = async () => {
      if (!formData.buildingId) {
        availableRooms.value = []
        return
      }
      
      try {
        const response = await fetch(`http://localhost:8082/api/dorm/buildings/${formData.buildingId}/rooms`)
        const result = await response.json()
        
        if (result.success) {
          availableRooms.value = result.data
        } else {
          console.error(result.message || '加载房间数据失败')
          availableRooms.value = []
        }
      } catch (error) {
        console.error('加载房间失败:', error)
        availableRooms.value = []
      }
    }
    
    const loadBeds = async () => {
      if (!formData.roomId) {
        availableBeds.value = []
        return
      }
      
      try {
        const response = await fetch(`http://localhost:8082/api/dorm/rooms/${formData.roomId}/available-beds`)
        const result = await response.json()
        
        if (result.success) {
          availableBeds.value = result.data
        } else {
          console.error(result.message || '加载床位数据失败')
          availableBeds.value = []
        }
      } catch (error) {
        console.error('加载床位失败:', error)
        availableBeds.value = []
      }
    }
    
    const handleSearch = () => {
      // 实现搜索逻辑
      console.log('搜索:', searchQuery.value, statusFilter.value, buildingFilter.value)
      loadAllocations()
    }
    
    const showAddDialog = () => {
      isEditing.value = false
      resetForm()
      loadStudents()
      showDialog.value = true
    }
    
    const showEditDialog = (allocation) => {
      isEditing.value = true
      Object.assign(formData, {
        id: allocation.id,
        studentId: allocation.studentId,
        studentName: allocation.studentName,
        studentNumber: allocation.studentNumber,
        className: allocation.className,
        idCard: allocation.idCard,
        buildingId: allocation.buildingId,
        buildingName: allocation.buildingName,
        roomId: allocation.roomId,
        roomNumber: allocation.roomNumber,
        bedNumber: allocation.bedNumber,
        checkInDate: allocation.checkInDate,
        status: allocation.status || 'ACTIVE',
        remarks: allocation.remarks || ''
      })
      loadStudents()
      loadBuildings()
      loadRooms()
      loadBeds()
      showDialog.value = true
    }
    
    const showDetailDialog = (allocation) => {
      selectedAllocation.value = allocation
      showDetailDialogVisible.value = true
    }
    
    const closeDialog = () => {
      showDialog.value = false
      resetForm()
    }
    
    const closeDetailDialog = () => {
      showDetailDialogVisible.value = false
      selectedAllocation.value = null
    }
    
    const resetForm = () => {
      Object.assign(formData, {
        id: null,
        studentId: '',
        studentName: '',
        studentNumber: '',
        className: '',
        idCard: '',
        buildingId: '',
        buildingName: '',
        roomId: '',
        roomNumber: '',
        bedNumber: '',
        checkInDate: '',
        status: 'ACTIVE',
        remarks: ''
      })
      availableRooms.value = []
      availableBeds.value = []
    }
    
    const handleSubmit = async () => {
      submitting.value = true
      try {
        let response
        if (isEditing.value) {
          // 更新住宿分配
          response = await fetch(`http://localhost:8082/api/accommodations/${formData.id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
          })
        } else {
          // 添加住宿分配
          response = await fetch('http://localhost:8082/api/accommodations', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
          })
        }
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        const result = await response.json()
        if (result.success) {
          console.log(isEditing.value ? '更新成功' : '添加成功')
          closeDialog()
          loadAllocations()
        } else {
          throw new Error(result.message || '操作失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        alert('保存失败: ' + error.message)
      } finally {
        submitting.value = false
      }
    }
    
    const handleCheckOut = async (allocation) => {
      if (confirm(`确定要为学生 ${allocation.studentName} 办理退宿吗？`)) {
        try {
          const response = await fetch(`http://localhost:8082/api/accommodations/checkout/student/${allocation.studentId}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            }
          })
          
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
          }
          
          const result = await response.json()
          if (result.success) {
            console.log('退宿成功')
            loadAllocations()
          } else {
            throw new Error(result.message || '退宿失败')
          }
        } catch (error) {
          console.error('退宿失败:', error)
          alert('退宿失败: ' + error.message)
        }
      }
    }
    
    const handleDelete = async (allocation) => {
      if (confirm(`确定要删除学生 ${allocation.studentName} 的住宿分配记录吗？`)) {
        try {
          const response = await fetch(`http://localhost:8082/api/accommodations/${allocation.id}`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json'
            }
          })
          
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
          }
          
          const result = await response.json()
          if (result.success) {
            console.log('删除成功')
            loadAllocations()
          } else {
            throw new Error(result.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          alert('删除失败: ' + error.message)
        }
      }
    }
    
    const changePage = (page) => {
      currentPage.value = page
      loadAllocations()
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }
    
    onMounted(() => {
      loadAllocations()
      loadBuildings()
    })
    
    return {
      loading,
      submitting,
      showDialog,
      showDetailDialogVisible,
      isEditing,
      searchQuery,
      statusFilter,
      buildingFilter,
      currentPage,
      totalPages,
      allocations,
      buildings,
      availableStudents,
      availableRooms,
      availableBeds,
      selectedAllocation,
      formData,
      handleSearch,
      showAddDialog,
      showEditDialog,
      showDetailDialogVisible,
      closeDialog,
      closeDetailDialog,
      handleSubmit,
      handleCheckOut,
      handleDelete,
      changePage,
      formatDate,
      onStudentChange,
      onBuildingChange,
      onRoomChange,
      loadRooms,
      loadBeds
    }
  }
}
</script>

<style scoped>
/* 搜索栏 */
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  background: var(--surface-color);
  padding: 1.5rem;
  border-radius: 0.75rem;
  box-shadow: var(--shadow);
}

.search-inputs {
  display: flex;
  gap: 1rem;
  flex: 1;
}

.input-group {
  position: relative;
  flex: 1;
  max-width: 300px;
}

.input-group i {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-secondary);
}

.input-group input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.filter-select {
  padding: 0.75rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  font-size: 0.875rem;
  background: white;
}

/* 分配列表 */
.allocation-list {
  background: var(--surface-color);
  border-radius: 0.75rem;
  box-shadow: var(--shadow);
  overflow: hidden;
}

.list-header {
  display: grid;
  grid-template-columns: 2fr 2fr 1.5fr 1fr 1.5fr;
  gap: 1rem;
  padding: 1rem 1.5rem;
  background-color: var(--background-color);
  border-bottom: 1px solid var(--border-color);
  font-weight: 600;
  color: var(--text-secondary);
}

.loading, .empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  color: var(--text-secondary);
}

.loading i, .empty-state i {
  font-size: 2rem;
  margin-bottom: 1rem;
  color: var(--primary-color);
}

.allocation-item {
  border-bottom: 1px solid var(--border-color);
}

.allocation-item:last-child {
  border-bottom: none;
}

.item-content {
  display: grid;
  grid-template-columns: 2fr 2fr 1.5fr 1fr 1.5fr;
  gap: 1rem;
  padding: 1rem 1.5rem;
  align-items: center;
}

.student-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.student-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.student-details {
  display: flex;
  flex-direction: column;
}

.student-name {
  font-weight: 500;
  margin-bottom: 0.25rem;
}

.student-code {
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.student-class {
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin-top: 0.25rem;
}

.student-id {
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin-top: 0.25rem;
}

.student-class {
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin-top: 0.25rem;
}

.student-id {
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin-top: 0.25rem;
}

.room-info {
  display: flex;
  flex-direction: column;
}

.room-number {
  font-weight: 500;
  margin-bottom: 0.25rem;
}

.bed-number {
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.status-active {
  background-color: #dcfce7;
  color: #166534;
}

.status-inactive {
  background-color: #fef2f2;
  color: #991b1b;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-icon {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 0.375rem;
  background-color: var(--background-color);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-icon:hover {
  background-color: var(--primary-color);
  color: white;
}

.btn-icon.btn-danger:hover {
  background-color: var(--danger-color);
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
}

.page-info {
  color: var(--text-secondary);
  font-size: 0.875rem;
}

/* 对话框 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog {
  background: var(--surface-color);
  border-radius: 0.75rem;
  box-shadow: var(--shadow-lg);
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.dialog-header h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
}

.btn-close {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 0.375rem;
  background-color: transparent;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close:hover {
  background-color: var(--background-color);
}

.dialog-content {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: var(--text-primary);
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

.detail-item label {
  font-weight: 500;
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.detail-item span {
  color: var(--text-primary);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-inputs {
    flex-direction: column;
  }
  
  .list-header {
    display: none;
  }
  
  .item-content {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
}
</style>