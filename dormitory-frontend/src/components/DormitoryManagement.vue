<template>
  <div class="dormitory-management">
    <div class="page-header">
      <h2><i class="fas fa-building"></i> 宿舍管理</h2>
      <div class="header-actions">
        <button class="btn btn-primary" @click="showAddDialog = true">
          <i class="fas fa-plus"></i> 添加宿舍
        </button>
        <button class="btn btn-secondary" @click="refreshData">
          <i class="fas fa-sync-alt"></i> 刷新
        </button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-filters">
      <div class="search-box">
        <i class="fas fa-search"></i>
        <input 
          v-model="searchQuery" 
          type="text" 
          placeholder="搜索宿舍楼、房间号..."
          @input="handleSearch"
        >
      </div>
      <div class="filters">
        <select v-model="filterBuilding" @change="handleFilter">
          <option value="">所有楼栋</option>
          <option v-for="building in buildings" :key="building.id" :value="building.id">
            {{ building.name }}
          </option>
        </select>
        <select v-model="filterStatus" @change="handleFilter">
          <option value="">所有状态</option>
          <option value="0">可用</option>
          <option value="1">已满</option>
          <option value="2">维修中</option>
        </select>
      </div>
    </div>

    <!-- 宿舍列表 -->
    <div class="dormitory-grid">
      <div 
        v-for="room in filteredRooms" 
        :key="room.id"
        class="dormitory-card"
        :class="getRoomStatusClass(room.status)"
        @click="viewRoomDetails(room)"
      >
        <div class="card-header">
          <h3>{{ room.buildingName }} - {{ room.roomNumber }}</h3>
          <span class="status-badge" :class="getRoomStatusClass(room.status)">
            {{ getRoomStatusText(room.status) }}
          </span>
        </div>
        
        <div class="card-body">
          <div class="room-info">
            <div class="info-item">
              <i class="fas fa-layer-group"></i>
              <span>{{ room.floor }}楼</span>
            </div>
            <div class="info-item">
              <i class="fas fa-bed"></i>
              <span>{{ getRoomTypeText(room.roomType) }}</span>
            </div>
          </div>
          
          <div class="occupancy-info">
            <div class="occupancy-bar">
              <div 
                class="occupancy-fill" 
                :style="{ width: (room.occupiedBeds / room.totalBeds * 100) + '%' }"
              ></div>
            </div>
            <div class="occupancy-text">
              {{ room.occupiedBeds }}/{{ room.totalBeds }} 床位
            </div>
          </div>
        </div>
        
        <div class="card-actions">
          <button 
            class="btn btn-sm btn-outline" 
            @click.stop="editRoom(room)"
          >
            <i class="fas fa-edit"></i> 编辑
          </button>
          <button 
            class="btn btn-sm btn-outline" 
            @click.stop="manageStudents(room)"
          >
            <i class="fas fa-users"></i> 管理学生
          </button>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="totalPages > 1">
      <button 
        class="btn btn-outline" 
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        <i class="fas fa-chevron-left"></i>
      </button>
      
      <span class="page-info">
        第 {{ currentPage }} 页，共 {{ totalPages }} 页
      </span>
      
      <button 
        class="btn btn-outline" 
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <!-- 添加/编辑宿舍对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="modal-overlay" @click="closeDialogs">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>{{ showAddDialog ? '添加宿舍' : '编辑宿舍' }}</h3>
          <button class="close-btn" @click="closeDialogs">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <form class="modal-body" @submit.prevent="saveRoom">
          <div class="form-group">
            <label>宿舍楼</label>
            <select v-model="roomForm.buildingId" required>
              <option value="">请选择宿舍楼</option>
              <option v-for="building in buildings" :key="building.id" :value="building.id">
                {{ building.name }}
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>房间号</label>
            <input v-model="roomForm.roomNumber" type="text" required placeholder="如：101">
          </div>
          
          <div class="form-group">
            <label>楼层</label>
            <input v-model="roomForm.floor" type="number" required min="1" placeholder="楼层">
          </div>
          
          <div class="form-group">
            <label>房间类型</label>
            <select v-model="roomForm.roomType" required>
              <option value="">请选择房间类型</option>
              <option value="0">标准间</option>
              <option value="1">豪华间</option>
              <option value="2">套房</option>
            </select>
          </div>
          
          <div class="form-group">
            <label>总床位数</label>
            <input v-model="roomForm.totalBeds" type="number" required min="1" max="8" placeholder="床位数">
          </div>
          
          <div class="form-group">
            <label>房间状态</label>
            <select v-model="roomForm.status" required>
              <option value="0">可用</option>
              <option value="1">已满</option>
              <option value="2">维修中</option>
            </select>
          </div>
          
          <div class="form-group">
            <label>备注</label>
            <textarea v-model="roomForm.remark" placeholder="房间备注信息"></textarea>
          </div>
          
          <div class="modal-actions">
            <button type="button" class="btn btn-secondary" @click="closeDialogs">
              取消
            </button>
            <button type="submit" class="btn btn-primary">
              {{ showAddDialog ? '添加' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 房间详情对话框 -->
    <div v-if="showDetailsDialog" class="modal-overlay" @click="closeDialogs">
      <div class="modal modal-large" @click.stop>
        <div class="modal-header">
          <h3>{{ selectedRoom?.buildingName }} - {{ selectedRoom?.roomNumber }} 详情</h3>
          <button class="close-btn" @click="closeDialogs">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="modal-body">
          <div class="room-details">
            <div class="detail-section">
              <h4>基本信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <label>宿舍楼：</label>
                  <span>{{ selectedRoom?.buildingName }}</span>
                </div>
                <div class="detail-item">
                  <label>房间号：</label>
                  <span>{{ selectedRoom?.roomNumber }}</span>
                </div>
                <div class="detail-item">
                  <label>楼层：</label>
                  <span>{{ selectedRoom?.floor }}楼</span>
                </div>
                <div class="detail-item">
                  <label>房间类型：</label>
                  <span>{{ getRoomTypeText(selectedRoom?.roomType) }}</span>
                </div>
                <div class="detail-item">
                  <label>总床位：</label>
                  <span>{{ selectedRoom?.totalBeds }}个</span>
                </div>
                <div class="detail-item">
                  <label>已占用：</label>
                  <span>{{ selectedRoom?.occupiedBeds }}个</span>
                </div>
                <div class="detail-item">
                  <label>可用床位：</label>
                  <span>{{ selectedRoom?.availableBeds }}个</span>
                </div>
                <div class="detail-item">
                  <label>房间状态：</label>
                  <span class="status-badge" :class="getRoomStatusClass(selectedRoom?.status)">
                    {{ getRoomStatusText(selectedRoom?.status) }}
                  </span>
                </div>
              </div>
            </div>
            
            <div class="detail-section" v-if="roomStudents.length > 0">
              <h4>入住学生</h4>
              <div class="students-list">
                <div v-for="student in roomStudents" :key="student.id" class="student-item">
                  <div class="student-avatar">
                    <i class="fas fa-user"></i>
                  </div>
                  <div class="student-info">
                    <div class="student-name">{{ student.studentName }}</div>
                    <div class="student-code">学号：{{ student.studentNumber }}</div>
                    <div class="student-bed">床位：{{ student.bedNumber }}</div>
                  </div>
                  <div class="student-actions">
                    <button class="btn btn-sm btn-outline" @click="viewStudent(student)">
                      查看
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 学生管理对话框 -->
    <div v-if="showStudentManageDialog" class="modal-overlay" @click="closeDialogs">
      <div class="modal modal-large" @click.stop>
        <div class="modal-header">
          <h3>{{ selectedRoom?.buildingName }} - {{ selectedRoom?.roomNumber }} 学生管理</h3>
          <button class="close-btn" @click="closeDialogs">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="modal-body">
          <div class="room-info-summary">
            <div class="info-card">
              <div class="info-label">房间信息</div>
              <div class="info-content">
                <span>{{ selectedRoom?.buildingName }} - {{ selectedRoom?.roomNumber }}</span>
                <span>{{ selectedRoom?.floor }}楼 · {{ getRoomTypeText(selectedRoom?.roomType) }}</span>
              </div>
            </div>
            <div class="info-card">
              <div class="info-label">床位情况</div>
              <div class="info-content">
                <span>总床位：{{ selectedRoom?.totalBeds }}个</span>
                <span>已占用：{{ selectedRoom?.occupiedBeds || roomStudents.length }}个</span>
                <span>可用：{{ (selectedRoom?.totalBeds || 0) - (selectedRoom?.occupiedBeds || roomStudents.length) }}个</span>
              </div>
            </div>
          </div>
          
          <div class="students-management">
            <div class="section-header">
              <h4><i class="fas fa-users"></i> 入住学生列表</h4>
              <div class="header-actions">
                <button class="btn btn-sm btn-primary" @click="addStudentToRoom">
                  <i class="fas fa-plus"></i> 添加学生
                </button>
              </div>
            </div>
            
            <div v-if="roomStudents.length === 0" class="empty-state">
              <i class="fas fa-bed"></i>
              <p>该房间暂无入住学生</p>
            </div>
            
            <div v-else class="students-table">
              <table>
                <thead>
                  <tr>
                    <th>学生姓名</th>
                    <th>学号</th>
                    <th>班级</th>
                    <th>床位号</th>
                    <th>入住时间</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="student in roomStudents" :key="student.id">
                    <td>
                      <div class="student-name-cell">
                        <i class="fas fa-user-circle"></i>
                        {{ student.studentName }}
                      </div>
                    </td>
                    <td>{{ student.studentNumber }}</td>
                    <td>{{ student.className || '-' }}</td>
                    <td>
                      <span class="bed-number">{{ student.bedNumber }}</span>
                    </td>
                    <td>{{ formatDate(student.checkInDate) }}</td>
                    <td>
                      <span class="status-badge" :class="getAccommodationStatusClass(student.status)">
                        {{ getAccommodationStatusText(student.status) }}
                      </span>
                    </td>
                    <td>
                      <div class="action-buttons">
                        <button class="btn btn-sm btn-outline" @click="viewStudentDetail(student)" title="查看详情">
                          <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-outline" @click="editStudentAccommodation(student)" title="编辑">
                          <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" @click="removeStudentFromRoom(student)" title="退宿">
                          <i class="fas fa-sign-out-alt"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeDialogs">
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'

export default {
  name: 'DormitoryManagement',
  setup() {
    const searchQuery = ref('')
    const filterBuilding = ref('')
    const filterStatus = ref('')
    const currentPage = ref(1)
    const pageSize = ref(12)
    const showAddDialog = ref(false)
    const showEditDialog = ref(false)
    const showDetailsDialog = ref(false)
    const showStudentManageDialog = ref(false)
    const selectedRoom = ref(null)
    
    const rooms = ref([])
    const buildings = ref([])
    const roomStudents = ref([])
    
    const roomForm = reactive({
      id: null,
      buildingId: '',
      roomNumber: '',
      floor: '',
      roomType: '',
      totalBeds: '',
      status: '0',
      remark: ''
    })
    
    // 计算属性
    const filteredRooms = computed(() => {
      let filtered = rooms.value
      
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(room => 
          room.buildingName.toLowerCase().includes(query) ||
          room.roomNumber.toLowerCase().includes(query)
        )
      }
      
      if (filterBuilding.value) {
        filtered = filtered.filter(room => room.buildingId == filterBuilding.value)
      }
      
      if (filterStatus.value !== '') {
        filtered = filtered.filter(room => room.status == filterStatus.value)
      }
      
      const start = (currentPage.value - 1) * pageSize.value
      const end = start + pageSize.value
      return filtered.slice(start, end)
    })
    
    const totalPages = computed(() => {
      let filtered = rooms.value
      
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(room => 
          room.buildingName.toLowerCase().includes(query) ||
          room.roomNumber.toLowerCase().includes(query)
        )
      }
      
      if (filterBuilding.value) {
        filtered = filtered.filter(room => room.buildingId == filterBuilding.value)
      }
      
      if (filterStatus.value !== '') {
        filtered = filtered.filter(room => room.status == filterStatus.value)
      }
      
      return Math.ceil(filtered.length / pageSize.value)
    })
    
    // 方法
    const getRoomStatusClass = (status) => {
      const statusMap = {
        0: 'available',
        1: 'full',
        2: 'maintenance'
      }
      return statusMap[status] || 'available'
    }
    
    const getRoomStatusText = (status) => {
      const statusMap = {
        0: '可用',
        1: '已满',
        2: '维修中'
      }
      return statusMap[status] || '未知'
    }
    
    const getRoomTypeText = (type) => {
      const typeMap = {
        0: '标准间',
        1: '豪华间',
        2: '套房'
      }
      return typeMap[type] || '未知'
    }
    
    const loadRooms = async () => {
      try {
        // 获取所有楼栋的房间
        const response = await fetch('http://localhost:8082/api/dorm/buildings')
        if (!response.ok) {
          throw new Error('获取楼栋数据失败')
        }
        const buildingsResult = await response.json()
        
        if (buildingsResult.code === 200) {
          const allRooms = []
          
          // 为每个楼栋获取房间列表
          for (const building of buildingsResult.data) {
            const roomsResponse = await fetch(`http://localhost:8082/api/dorm/buildings/${building.id}/rooms`)
            if (roomsResponse.ok) {
              const roomsResult = await roomsResponse.json()
              if (roomsResult.code === 200) {
                const buildingRooms = roomsResult.data.map(room => ({
                  id: room.id,
                  buildingId: room.buildingId,
                  buildingName: building.buildingName,
                  roomNumber: room.roomNumber,
                  floor: room.floor,
                  roomType: room.roomType,
                  totalBeds: room.capacity,
                  occupiedBeds: room.currentCount,
                  availableBeds: room.capacity - room.currentCount,
                  status: room.status === 'AVAILABLE' ? 0 : room.status === 'FULL' ? 1 : 2,
                  remark: room.description || ''
                }))
                allRooms.push(...buildingRooms)
              }
            }
          }
          
          rooms.value = allRooms
        } else {
          console.error('获取楼栋数据失败:', buildingsResult.message)
        }
      } catch (error) {
        console.error('加载宿舍数据失败:', error)
        // 如果API调用失败，显示空数据
        rooms.value = []
      }
    }
    
    const loadBuildings = async () => {
      try {
        const response = await fetch('http://localhost:8082/api/dorm/buildings')
        if (!response.ok) {
          throw new Error('获取楼栋数据失败')
        }
        const result = await response.json()
        
        if (result.code === 200) {
          buildings.value = result.data.map(building => ({
            id: building.id,
            name: building.buildingName
          }))
        } else {
          console.error('获取楼栋数据失败:', result.message)
          buildings.value = []
        }
      } catch (error) {
        console.error('加载楼栋数据失败:', error)
        buildings.value = []
      }
    }
    
    const handleSearch = () => {
      currentPage.value = 1
    }
    
    const handleFilter = () => {
      currentPage.value = 1
    }
    
    const changePage = (page) => {
      currentPage.value = page
    }
    
    const refreshData = () => {
      loadRooms()
      loadBuildings()
    }
    
    const resetForm = () => {
      Object.assign(roomForm, {
        id: null,
        buildingId: '',
        roomNumber: '',
        floor: '',
        roomType: '',
        totalBeds: '',
        status: '0',
        remark: ''
      })
    }
    
    const editRoom = (room) => {
      Object.assign(roomForm, {
        id: room.id,
        buildingId: room.buildingId,
        roomNumber: room.roomNumber,
        floor: room.floor,
        roomType: room.roomType,
        totalBeds: room.totalBeds,
        status: room.status,
        remark: room.remark
      })
      showEditDialog.value = true
    }
    
    const saveRoom = async () => {
      try {
        // 准备API请求数据
        const roomData = {
          buildingId: parseInt(roomForm.buildingId),
          roomNumber: roomForm.roomNumber,
          floor: parseInt(roomForm.floor),
          roomType: roomForm.roomType,
          capacity: parseInt(roomForm.totalBeds),
          currentCount: 0, // 新房间默认无人入住
          gender: 'MALE', // 默认值，可以根据需要修改
          status: roomForm.status === '0' ? 'AVAILABLE' : roomForm.status === '1' ? 'FULL' : 'MAINTENANCE',
          description: roomForm.remark || ''
        }
        
        let response;
        
        if (showAddDialog.value) {
          // 添加新宿舍
          response = await fetch('http://localhost:8082/api/dorm/rooms', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(roomData)
          })
        } else {
          // 编辑宿舍
          roomData.id = roomForm.id // 更新时需要ID
          response = await fetch(`http://localhost:8082/api/dorm/rooms/${roomForm.id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(roomData)
          })
        }
        
        if (!response.ok) {
          throw new Error('API请求失败')
        }
        
        const result = await response.json()
        
        if (result.code === 200) {
          // 操作成功，刷新数据
          refreshData()
          closeDialogs()
          alert(showAddDialog.value ? '宿舍添加成功！' : '宿舍更新成功！')
        } else {
          throw new Error(result.message || '操作失败')
        }
      } catch (error) {
        console.error('保存宿舍失败:', error)
        alert('操作失败，请重试: ' + error.message)
      }
    }
    
    const viewRoomDetails = async (room) => {
      selectedRoom.value = room
      
      // 加载房间学生信息
      try {
        // 调用真实API获取房间学生信息
        const response = await fetch(`http://localhost:8082/api/accommodations/room/${room.id}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('userToken')}`
          }
        })
        
        const result = await response.json()
        
        if (result.success) {
          // 转换数据格式以匹配前端显示
          roomStudents.value = result.data.map(accommodation => ({
            id: accommodation.id,
            studentName: accommodation.studentName,
            studentNumber: accommodation.studentNumber,
            bedNumber: accommodation.bedNumber,
            checkInDate: accommodation.checkInDate,
            status: accommodation.status
          }))
        } else {
          console.error('获取房间学生信息失败:', result.message)
          roomStudents.value = []
        }
      } catch (error) {
        console.error('加载学生信息失败:', error)
        roomStudents.value = []
      }
      
      showDetailsDialog.value = true
    }
    
    const manageStudents = async (room) => {
      try {
        console.log('管理房间学生:', room)
        selectedRoom.value = room
        
        // 获取房间的住宿记录
        const response = await fetch(`http://localhost:8082/api/accommodations/room/${room.id}`)
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        const result = await response.json()
        console.log('房间住宿记录响应:', result)
        
        if (result.code === 200) {
          roomStudents.value = result.data || []
          // 确保selectedRoom包含最新的occupiedBeds信息
          if (selectedRoom.value) {
            selectedRoom.value.occupiedBeds = result.data ? result.data.length : 0
          }
          showStudentManageDialog.value = true
        } else {
          console.error('获取房间学生信息失败:', result.message)
          alert('获取房间学生信息失败: ' + result.message)
        }
      } catch (error) {
        console.error('获取房间学生信息异常:', error)
        alert('获取房间学生信息失败: ' + error.message)
      }
    }
    
    const viewStudent = (student) => {
      alert(`查看学生 ${student.studentName} 的详细信息功能正在开发中...`)
    }
    
    // 学生管理相关方法
    const addStudentToRoom = () => {
      alert('添加学生到房间功能正在开发中...')
    }
    
    const viewStudentDetail = (student) => {
      alert(`查看学生 ${student.studentName} 详细信息功能正在开发中...`)
    }
    
    const editStudentAccommodation = (student) => {
      alert(`编辑学生 ${student.studentName} 住宿信息功能正在开发中...`)
    }
    
    const removeStudentFromRoom = async (student) => {
      if (!confirm(`确定要让学生 ${student.studentName} 退宿吗？`)) {
        return
      }
      
      try {
        const response = await fetch(`http://localhost:8082/api/accommodations/checkout/student/${student.studentId}`, {
          method: 'PUT'
        })
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        
        const result = await response.json()
        
        if (result.code === 200) {
          alert('学生退宿成功')
          // 重新加载房间学生信息
          manageStudents(selectedRoom.value)
          // 刷新房间列表
          loadRooms()
        } else {
          alert('学生退宿失败: ' + result.message)
        }
      } catch (error) {
        console.error('学生退宿异常:', error)
        alert('学生退宿失败: ' + error.message)
      }
    }
    
    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }
    
    // 获取住宿状态样式类
    const getAccommodationStatusClass = (status) => {
      switch (status) {
        case 0: return 'status-active'
        case 1: return 'status-inactive'
        default: return 'status-unknown'
      }
    }
    
    // 获取住宿状态文本
    const getAccommodationStatusText = (status) => {
      switch (status) {
        case 0: return '在住'
        case 1: return '已退宿'
        default: return '未知'
      }
    }
    
    const closeDialogs = () => {
      showAddDialog.value = false
      showEditDialog.value = false
      showDetailsDialog.value = false
      showStudentManageDialog.value = false
      selectedRoom.value = null
      roomStudents.value = []
      resetForm()
    }
    
    // 生命周期
    onMounted(() => {
      loadRooms()
      loadBuildings()
    })
    
    return {
      searchQuery,
      filterBuilding,
      filterStatus,
      currentPage,
      showAddDialog,
      showEditDialog,
      showDetailsDialog,
      showStudentManageDialog,
      selectedRoom,
      rooms,
      buildings,
      roomStudents,
      roomForm,
      filteredRooms,
      totalPages,
      getRoomStatusClass,
      getRoomStatusText,
      getRoomTypeText,
      handleSearch,
      handleFilter,
      changePage,
      refreshData,
      editRoom,
      saveRoom,
      viewRoomDetails,
      manageStudents,
      viewStudent,
      addStudentToRoom,
      viewStudentDetail,
      editStudentAccommodation,
      removeStudentFromRoom,
      formatDate,
      getAccommodationStatusClass,
      getAccommodationStatusText,
      closeDialogs
    }
  }
}
</script>

<style scoped>
.dormitory-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f0f0;
}

.page-header h2 {
  color: #333;
  font-size: 1.8rem;
  font-weight: 600;
}

.page-header h2 i {
  margin-right: 10px;
  color: #667eea;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-filters {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  align-items: center;
}

.search-box {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-box i {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

.search-box input {
  width: 100%;
  padding: 12px 15px 12px 45px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.search-box input:focus {
  outline: none;
  border-color: #667eea;
}

.filters {
  display: flex;
  gap: 15px;
}

.filters select {
  padding: 12px 15px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  background: white;
  cursor: pointer;
  transition: border-color 0.3s ease;
}

.filters select:focus {
  outline: none;
  border-color: #667eea;
}

.dormitory-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.dormitory-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  overflow: hidden;
  border-left: 4px solid #667eea;
}

.dormitory-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.dormitory-card.full {
  border-left-color: #ff6b6b;
}

.dormitory-card.maintenance {
  border-left-color: #ffa726;
}

.card-header {
  padding: 20px 20px 0;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.card-header h3 {
  color: #333;
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  text-transform: uppercase;
}

.status-badge.available {
  background: #d4edda;
  color: #155724;
}

.status-badge.full {
  background: #f8d7da;
  color: #721c24;
}

.status-badge.maintenance {
  background: #fff3cd;
  color: #856404;
}

.card-body {
  padding: 15px 20px;
}

.room-info {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 0.9rem;
}

.info-item i {
  color: #667eea;
  width: 16px;
}

.occupancy-info {
  margin-bottom: 15px;
}

.occupancy-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.occupancy-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  transition: width 0.3s ease;
}

.occupancy-text {
  font-size: 0.9rem;
  color: #666;
  text-align: center;
}

.card-actions {
  padding: 0 20px 20px;
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #5a6268;
}

.btn-outline {
  background: transparent;
  color: #667eea;
  border: 1px solid #667eea;
}

.btn-outline:hover {
  background: #667eea;
  color: white;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 0.8rem;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-top: 30px;
}

.page-info {
  color: #666;
  font-size: 0.9rem;
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
  padding: 20px;
}

.modal {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-large {
  max-width: 800px;
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #e1e5e9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.3rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  color: #999;
  cursor: pointer;
  padding: 5px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: #f0f0f0;
  color: #333;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

.room-details {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.detail-section h4 {
  color: #333;
  font-size: 1.2rem;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 2px solid #f0f0f0;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.detail-item label {
  font-weight: 500;
  color: #666;
  font-size: 0.9rem;
}

.detail-item span {
  color: #333;
  font-size: 1rem;
}

.students-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.student-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e1e5e9;
}

.student-avatar {
  width: 50px;
  height: 50px;
  background: #667eea;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

.student-info {
  flex: 1;
}

.student-name {
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.student-code,
.student-bed {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 2px;
}

.student-actions {
  display: flex;
  gap: 10px;
}

/* 学生管理对话框样式 */
.room-info-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.info-card {
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  border-radius: 12px;
  padding: 20px;
  border-left: 4px solid #3498db;
}

.info-label {
  font-size: 12px;
  color: #7f8c8d;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 10px;
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-content span {
  font-size: 14px;
  color: #2c3e50;
  font-weight: 500;
}

.students-management {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
}

.section-header h4 {
  margin: 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #7f8c8d;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 15px;
  opacity: 0.5;
}

.empty-state p {
  margin: 0;
  font-size: 16px;
}

.students-table {
  overflow-x: auto;
}

.students-table table {
  width: 100%;
  border-collapse: collapse;
}

.students-table th,
.students-table td {
  padding: 15px 12px;
  text-align: left;
  border-bottom: 1px solid #e9ecef;
}

.students-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.students-table td {
  font-size: 14px;
  color: #495057;
}

.student-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.student-name-cell i {
  color: #3498db;
  font-size: 16px;
}

.bed-number {
  background: #e3f2fd;
  color: #1976d2;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
}

.action-buttons {
  display: flex;
  gap: 5px;
}

.action-buttons .btn {
  padding: 6px 8px;
  font-size: 12px;
}

.status-active {
  background: #d4edda;
  color: #155724;
}

.status-inactive {
  background: #f8d7da;
  color: #721c24;
}

.status-unknown {
  background: #e2e3e5;
  color: #383d41;
}

.modal-footer {
  padding: 20px;
  background: #f8f9fa;
  border-top: 1px solid #e9ecef;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-danger {
  background: #dc3545;
  color: white;
}

.btn-danger:hover {
  background: #c82333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dormitory-management {
    padding: 15px;
  }
  
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .search-filters {
    flex-direction: column;
    gap: 15px;
  }
  
  .filters {
    flex-direction: column;
    width: 100%;
  }
  
  .dormitory-grid {
    grid-template-columns: 1fr;
  }
  
  .modal {
    margin: 10px;
    max-width: none;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .student-item {
    flex-direction: column;
    text-align: center;
  }
}
</style>