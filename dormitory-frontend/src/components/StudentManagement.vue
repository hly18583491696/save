<template>
  <div class="student-management">
    <div class="page-header">
      <h2><i class="fas fa-bed"></i> 住宿记录管理</h2>
      <div class="header-actions">
        <button class="btn btn-primary" @click="showAddDialog = true">
          <i class="fas fa-plus"></i> 添加住宿记录
        </button>
        <button class="btn btn-secondary" @click="exportData">
          <i class="fas fa-download"></i> 导出数据
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
          placeholder="搜索学生姓名、学号、房间号..."
          @input="handleSearch"
        >
      </div>
      <div class="filters">
        <select v-model="filterBuilding" @change="handleFilter">
          <option value="">所有楼栋</option>
          <option v-for="building in buildings" :key="building.id" :value="building.name">
            {{ building.name }}
          </option>
        </select>
        <select v-model="filterStatus" @change="handleFilter">
          <option value="">所有状态</option>
          <option value="ACTIVE">在住</option>
          <option value="CHECKED_OUT">已退宿</option>
          <option value="PENDING">待入住</option>
        </select>
      </div>
    </div>

    <!-- 住宿记录列表 -->
    <div class="students-table-container">
      <table class="students-table">
        <thead>
          <tr>
            <th>学号</th>
            <th>姓名</th>
            <th>班级</th>
            <th>身份证号</th>
            <th>房间号</th>
            <th>楼栋名称</th>
            <th>床位号</th>
            <th>入住日期</th>
            <th>退宿日期</th>
            <th>状态</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="accommodation in filteredStudents" :key="accommodation.id" class="student-row">
            <td class="student-code">{{ accommodation.studentNumber }}</td>
            <td class="student-name">{{ accommodation.studentName }}</td>
            <td>{{ accommodation.className || '未知' }}</td>
            <td>{{ accommodation.idCard || '未填写' }}</td>
            <td>{{ accommodation.roomNumber }}</td>
            <td>{{ accommodation.buildingName }}</td>
            <td>{{ accommodation.bedNumber || '未分配' }}</td>
            <td>{{ formatDate(accommodation.checkInDate) }}</td>
            <td>{{ accommodation.checkOutDate ? formatDate(accommodation.checkOutDate) : '未退宿' }}</td>
            <td>
              <span class="status-badge" :class="accommodation.status?.toLowerCase()">
                {{ accommodation.status || '未知' }}
              </span>
            </td>
            <td>{{ accommodation.remarks || '无' }}</td>
            <td>
              <div class="action-buttons">
                <button 
                  class="btn btn-sm btn-outline" 
                  @click="viewAccommodation(accommodation)"
                  title="查看详情"
                >
                  <i class="fas fa-eye"></i>
                </button>
                <button 
                  class="btn btn-sm btn-outline" 
                  @click="editAccommodation(accommodation)"
                  title="编辑"
                >
                  <i class="fas fa-edit"></i>
                </button>
                <button 
                  class="btn btn-sm btn-outline" 
                  @click="changeRoom(accommodation)"
                  title="调换房间"
                >
                  <i class="fas fa-exchange-alt"></i>
                </button>
                <button 
                  class="btn btn-sm btn-danger" 
                  @click="deleteAccommodation(accommodation)"
                  title="删除"
                >
                  <i class="fas fa-trash"></i>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
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
        第 {{ currentPage }} 页，共 {{ totalPages }} 页 (共 {{ totalStudents }} 名学生)
      </span>
      
      <button 
        class="btn btn-outline" 
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <!-- 添加/编辑住宿记录对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="modal-overlay" @click="closeDialogs">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>{{ showAddDialog ? '添加住宿记录' : '编辑住宿记录' }}</h3>
          <button class="close-btn" @click="closeDialogs">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <form class="modal-body" @submit.prevent="saveAccommodation">
          <div class="form-row">
            <div class="form-group">
              <label>学号 *</label>
              <input v-model="accommodationForm.studentNumber" type="text" required placeholder="学号">
            </div>
            <div class="form-group">
              <label>姓名 *</label>
              <input v-model="accommodationForm.studentName" type="text" required placeholder="姓名">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>班级</label>
              <input v-model="accommodationForm.className" type="text" placeholder="班级">
            </div>
            <div class="form-group">
              <label>身份证号</label>
              <input v-model="accommodationForm.idCard" type="text" placeholder="身份证号">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>楼栋 *</label>
              <select v-model="accommodationForm.buildingId" @change="loadRoomsForBuilding" required>
                <option value="">请选择楼栋</option>
                <option v-for="building in buildings" :key="building.id" :value="building.id">
                  {{ building.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>房间 *</label>
              <select v-model="accommodationForm.roomId" @change="onRoomChange" required>
                <option value="">请选择房间</option>
                <option v-for="room in availableRooms" :key="room.id" :value="room.id">
                  {{ room.roomNumber }} ({{ room.floor }}楼 - {{ room.roomType }} - 剩余床位: {{ room.capacity - room.currentCount }})
                </option>
              </select>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>床位号</label>
              <input v-model="accommodationForm.bedNumber" type="text" placeholder="床位号">
            </div>
            <div class="form-group">
              <label>入住日期</label>
              <input v-model="accommodationForm.checkInDate" type="date">
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>状态</label>
              <select v-model="accommodationForm.status">
                <option value="ACTIVE">在住</option>
                <option value="PENDING">待入住</option>
                <option value="CHECKED_OUT">已退宿</option>
              </select>
            </div>
            <div class="form-group">
              <label>备注</label>
              <input v-model="accommodationForm.remarks" type="text" placeholder="备注">
            </div>
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

    <!-- 住宿记录详情对话框 -->
    <div v-if="showDetailsDialog" class="modal-overlay" @click="closeDialogs">
      <div class="modal modal-large" @click.stop>
        <div class="modal-header">
          <h3>{{ selectedStudent?.studentName }} - 住宿记录详情</h3>
          <button class="close-btn" @click="closeDialogs">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="modal-body">
          <div class="student-details">
            <div class="detail-header">
              <div class="student-avatar-large">
                <img v-if="selectedStudent?.avatar" :src="selectedStudent.avatar" :alt="selectedStudent.studentName">
                <i v-else class="fas fa-user"></i>
              </div>
              <div class="student-basic-info">
                <h3>{{ selectedStudent?.studentName }}</h3>
                <p class="student-code">学号：{{ selectedStudent?.studentNumber }}</p>
                <p class="student-status">
                  <span class="status-badge" :class="getStatusClass(selectedStudent?.status)">
                    {{ getStatusText(selectedStudent?.status) }}
                  </span>
                </p>
              </div>
            </div>
            
            <div class="detail-sections">
              <div class="detail-section">
                <h4><i class="fas fa-user"></i> 基本信息</h4>
                <div class="detail-grid">
                  <div class="detail-item">
                    <label>姓名：</label>
                    <span>{{ selectedStudent?.studentName }}</span>
                  </div>
                  <div class="detail-item">
                    <label>学号：</label>
                    <span>{{ selectedStudent?.studentNumber }}</span>
                  </div>
                  <div class="detail-item">
                    <label>班级：</label>
                    <span>{{ selectedStudent?.className || '未填写' }}</span>
                  </div>
                  <div class="detail-item">
                    <label>身份证号：</label>
                    <span>{{ selectedStudent?.idCard || '未填写' }}</span>
                  </div>
                  <div class="detail-item">
                    <label>创建时间：</label>
                    <span>{{ formatDate(selectedStudent?.createTime) }}</span>
                  </div>
                  <div class="detail-item">
                    <label>更新时间：</label>
                    <span>{{ formatDate(selectedStudent?.updateTime) }}</span>
                  </div>
                </div>
              </div>
              
              <div class="detail-section">
                <h4><i class="fas fa-phone"></i> 联系信息</h4>
                <div class="detail-grid">
                  <div class="detail-item">
                    <label>手机号：</label>
                    <span>{{ selectedStudent?.phone }}</span>
                  </div>
                  <div class="detail-item">
                    <label>邮箱：</label>
                    <span>{{ selectedStudent?.email || '未填写' }}</span>
                  </div>
                </div>
              </div>
              
              <div class="detail-section" v-if="selectedStudent?.dormInfo">
                <h4><i class="fas fa-bed"></i> 宿舍信息</h4>
                <div class="dorm-detail">
                  <div class="dorm-card">
                    <div class="dorm-header">
                      <h5>{{ selectedStudent.dormInfo.buildingName }}</h5>
                      <span class="room-number">{{ selectedStudent.dormInfo.roomNumber }}室</span>
                    </div>
                    <div class="dorm-info-grid">
                      <div class="dorm-info-item">
                        <label>床位号：</label>
                        <span>{{ selectedStudent.dormInfo.bedNumber }}</span>
                      </div>
                      <div class="dorm-info-item">
                        <label>入住时间：</label>
                        <span>{{ selectedStudent.dormInfo.checkInDate }}</span>
                      </div>
                      <div class="dorm-info-item">
                        <label>楼层：</label>
                        <span>{{ selectedStudent.dormInfo.floor }}楼</span>
                      </div>
                      <div class="dorm-info-item">
                        <label>房间类型：</label>
                        <span>{{ selectedStudent.dormInfo.roomType }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              

            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分配宿舍对话框 -->
    <div v-if="showAssignDialog" class="modal-overlay" @click="closeDialogs">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>为 {{ assignStudent?.studentName }} 分配宿舍</h3>
          <button class="close-btn" @click="closeDialogs">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="modal-body">
          <div class="assign-form">
            <div class="form-group">
              <label>选择宿舍楼</label>
              <select v-model="assignForm.buildingId" @change="loadAvailableRooms">
                <option value="">请选择宿舍楼</option>
                <option v-for="building in buildings" :key="building.id" :value="building.id">
                  {{ building.name }}
                </option>
              </select>
            </div>
            
            <div class="form-group" v-if="availableRooms.length > 0">
              <label>选择房间</label>
              <div class="rooms-grid">
                <div 
                  v-for="room in availableRooms" 
                  :key="room.id"
                  class="room-option"
                  :class="{ active: assignForm.roomId === room.id }"
                  @click="selectRoom(room)"
                >
                  <div class="room-number">{{ room.roomNumber }}</div>
                  <div class="room-info">
                    <span>{{ room.floor }}楼</span>
                    <span>{{ room.availableBeds }}/{{ room.totalBeds }} 床位</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="form-group" v-if="assignForm.roomId && availableBeds.length > 0">
              <label>选择床位</label>
              <div class="beds-grid">
                <div 
                  v-for="bed in availableBeds" 
                  :key="bed"
                  class="bed-option"
                  :class="{ active: assignForm.bedNumber === bed }"
                  @click="assignForm.bedNumber = bed"
                >
                  {{ bed }}号床
                </div>
              </div>
            </div>
            
            <div class="modal-actions">
              <button type="button" class="btn btn-secondary" @click="closeDialogs">
                取消
              </button>
              <button 
                type="button" 
                class="btn btn-primary" 
                @click="confirmAssign"
                :disabled="!assignForm.roomId || !assignForm.bedNumber"
              >
                确认分配
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'

export default {
  name: 'StudentManagement',
  setup() {
    const searchQuery = ref('')
    const filterBuilding = ref('')
    const filterStatus = ref('')
    const currentPage = ref(1)
    const pageSize = ref(10)
    const showAddDialog = ref(false)
    const showEditDialog = ref(false)
    const showDetailsDialog = ref(false)
    const showAssignDialog = ref(false)
    const selectedStudent = ref(null)
    const assignStudent = ref(null)
    
    const accommodations = ref([])
    const buildings = ref([])
    const availableRooms = ref([])
    const availableBeds = ref([])
    
    const accommodationForm = reactive({
      id: null,
      studentId: '',
      studentNumber: '',
      studentName: '',
      phone: '',
      email: '',
      className: '',
      idCard: '',
      roomId: '',
      roomNumber: '',
      buildingId: '',
      buildingName: '',
      bedNumber: '',
      checkInDate: '',
      checkOutDate: '',
      status: 'ACTIVE',
      remarks: ''
    })
    
    const assignForm = reactive({
      buildingId: '',
      roomId: '',
      bedNumber: ''
    })
    
    // 计算属性 - 优化性能
    const getFilteredAccommodationsBase = computed(() => {
      let filtered = accommodations.value
      
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(accommodation => 
          (accommodation.studentName && accommodation.studentName.toLowerCase().includes(query)) ||
        (accommodation.studentNumber && accommodation.studentNumber.toLowerCase().includes(query)) ||
          (accommodation.roomNumber && accommodation.roomNumber.toLowerCase().includes(query))
        )
      }
      
      if (filterBuilding.value !== '') {
        filtered = filtered.filter(accommodation => accommodation.buildingName == filterBuilding.value)
      }
      
      if (filterStatus.value !== '') {
        filtered = filtered.filter(accommodation => accommodation.status == filterStatus.value)
      }
      
      return filtered
    })
    
    const filteredStudents = computed(() => {
      const filtered = getFilteredAccommodationsBase.value
      const start = (currentPage.value - 1) * pageSize.value
      const end = start + pageSize.value
      return filtered.slice(start, end)
    })
    
    const totalStudents = computed(() => {
      return getFilteredAccommodationsBase.value.length
    })
    
    const totalPages = computed(() => {
      return Math.ceil(totalStudents.value / pageSize.value)
    })
    
    // 方法
    const getStatusClass = (status) => {
      const statusMap = {
        'ACTIVE': 'active',
        'CHECKED_OUT': 'graduated',
        'PENDING': 'suspended'
      }
      return statusMap[status] || 'active'
    }
    
    const getStatusText = (status) => {
      const statusMap = {
        'ACTIVE': '在住',
        'CHECKED_OUT': '已退宿',
        'PENDING': '待入住'
      }
      return statusMap[status] || '未知'
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return '未设置'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
    
    const loadAccommodations = async () => {
      try {
        const response = await fetch('http://localhost:8082/api/dorm/accommodations')
        const result = await response.json()
        
        console.log('API响应:', result)
        console.log('住宿记录数据:', result.data)
        
        if (result.code === 200) {
          accommodations.value = result.data
          console.log('accommodations.value已更新:', accommodations.value)
        } else {
          console.error(result.message || '加载住宿记录数据失败')
        }
      } catch (error) {
        console.error('加载住宿记录数据失败:', error)
      }
    }
    
    const loadBuildings = async () => {
      try {
        console.log('开始加载楼栋数据...')
        const response = await fetch('http://localhost:8082/api/dorm/buildings')
        console.log('API响应状态:', response.status)
        const result = await response.json()
        console.log('API响应结果:', result)
        
        if (result.code === 200) {
          buildings.value = result.data.map(building => ({
            id: building.id,
            name: building.buildingName || building.name
          }))
          console.log('楼栋数据已更新:', buildings.value)
        } else {
          console.error('API返回错误:', result.message || '加载楼栋数据失败')
          buildings.value = []
        }
      } catch (error) {
        console.error('加载楼栋数据失败:', error)
        buildings.value = []
      }
    }
    
    const loadAvailableRooms = async (buildingId) => {
      const targetBuildingId = buildingId || assignForm.buildingId
      if (!targetBuildingId) {
        availableRooms.value = []
        return
      }
      
      try {
        console.log('开始加载房间数据，楼栋ID:', targetBuildingId)
        const response = await fetch(`http://localhost:8082/api/dorm/rooms/available/${targetBuildingId}`)
        console.log('房间API响应状态:', response.status)
        const result = await response.json()
        console.log('房间API响应结果:', result)
        
        if (result.code === 200) {
          availableRooms.value = result.data
          console.log('房间数据已更新:', availableRooms.value)
        } else {
          console.error('房间API返回错误:', result.message || '加载房间数据失败')
          availableRooms.value = []
        }
      } catch (error) {
        console.error('加载房间数据失败:', error)
        availableRooms.value = []
      }
    }
    
    const loadRoomsForBuilding = async () => {
      if (accommodationForm.buildingId) {
        try {
          console.log('开始加载房间数据，楼栋ID:', accommodationForm.buildingId)
          const response = await fetch(`http://localhost:8082/api/dorm/buildings/${accommodationForm.buildingId}/rooms`)
          console.log('房间API响应状态:', response.status)
          const result = await response.json()
          console.log('房间API响应结果:', result)
          
          if (result.code === 200) {
            availableRooms.value = result.data
            console.log('房间数据已更新:', availableRooms.value)
            
            // 更新表单中的楼栋名称
            const selectedBuilding = buildings.value.find(b => b.id == accommodationForm.buildingId)
            if (selectedBuilding) {
              accommodationForm.buildingName = selectedBuilding.name
            }
          } else {
            console.error('房间API返回错误:', result.message || '加载房间数据失败')
            availableRooms.value = []
          }
        } catch (error) {
          console.error('加载房间数据失败:', error)
          availableRooms.value = []
        }
        
        // 清空之前选择的房间
        accommodationForm.roomId = ''
        accommodationForm.roomNumber = ''
      } else {
        availableRooms.value = []
        accommodationForm.roomId = ''
        accommodationForm.roomNumber = ''
        accommodationForm.buildingName = ''
      }
    }
    
    const onRoomChange = () => {
      if (accommodationForm.roomId) {
        const selectedRoom = availableRooms.value.find(room => room.id == accommodationForm.roomId)
        if (selectedRoom) {
          accommodationForm.roomNumber = selectedRoom.roomNumber
          console.log('选择的房间:', selectedRoom)
        }
      } else {
        accommodationForm.roomNumber = ''
      }
    }
    
    const selectRoom = (room) => {
      assignForm.roomId = room.id
      assignForm.bedNumber = ''
      
      // 模拟可用床位
      availableBeds.value = []
      for (let i = 1; i <= room.totalBeds; i++) {
        if (i <= room.availableBeds) {
          availableBeds.value.push(i)
        }
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
      loadAccommodations()
      loadBuildings()
    }
    
    const resetForm = () => {
      Object.assign(accommodationForm, {
        id: null,
        studentId: '',
        studentNumber: '',
        studentName: '',
        phone: '',
        email: '',
        className: '',
        idCard: '',
        roomId: '',
        roomNumber: '',
        buildingId: '',
        buildingName: '',
        bedNumber: '',
        checkInDate: new Date().toISOString().split('T')[0],
        checkOutDate: '',
        status: 'ACTIVE',
        remarks: ''
      })
      // 清空可用房间列表
      availableRooms.value = []
    }
    
    const resetAssignForm = () => {
      Object.assign(assignForm, {
        buildingId: '',
        roomId: '',
        bedNumber: ''
      })
      availableRooms.value = []
      availableBeds.value = []
    }
    
    const viewAccommodation = async (accommodation) => {
      try {
        console.log('正在获取学生详细信息，学生ID:', accommodation.studentId)
        // 调用学生详细信息API
        const response = await fetch(`http://localhost:8082/api/dorm/accommodations/${accommodation.id}`)
        const result = await response.json()
        
        console.log('API响应:', result)
        
        if (result.code === 200 && result.data) {
          // 合并学生信息和住宿信息
          selectedStudent.value = {
            ...result.data, // 学生完整信息（包含phone和email）
            // 保留住宿记录中的基本信息，以防学生表中缺少这些字段
            studentName: result.data.studentName || accommodation.studentName,
            studentNumber: result.data.studentNumber || accommodation.studentNumber,
            className: result.data.className || accommodation.className,
            idCard: result.data.idCard || accommodation.idCard,
            checkInDate: accommodation.checkInDate ? formatDate(accommodation.checkInDate) : '',
            checkOutDate: accommodation.checkOutDate ? formatDate(accommodation.checkOutDate) : '',
            createTime: accommodation.createTime ? formatDate(accommodation.createTime) : '',
            updateTime: accommodation.updateTime ? formatDate(accommodation.updateTime) : '',
            dormInfo: {
              buildingName: accommodation.buildingName,
              roomNumber: accommodation.roomNumber,
              bedNumber: accommodation.bedNumber,
              checkInDate: accommodation.checkInDate,
              floor: Math.floor(parseInt(accommodation.roomNumber) / 100), // 根据房间号计算楼层
              roomType: '标准间' // 默认房间类型
            }
          }
          console.log('合并后的学生信息:', selectedStudent.value)
        } else {
          // 如果获取学生详细信息失败，创建一个包含基本信息的对象
          selectedStudent.value = {
            ...accommodation,
            phone: '未填写',
            email: '未填写',
            checkInDate: accommodation.checkInDate ? formatDate(accommodation.checkInDate) : '',
            checkOutDate: accommodation.checkOutDate ? formatDate(accommodation.checkOutDate) : '',
            createTime: accommodation.createTime ? formatDate(accommodation.createTime) : '',
            updateTime: accommodation.updateTime ? formatDate(accommodation.updateTime) : '',
            dormInfo: {
              buildingName: accommodation.buildingName,
              roomNumber: accommodation.roomNumber,
              bedNumber: accommodation.bedNumber,
              checkInDate: accommodation.checkInDate,
              floor: Math.floor(parseInt(accommodation.roomNumber) / 100),
              roomType: '标准间'
            }
          }
          console.warn('获取学生详细信息失败，使用住宿记录信息')
        }
        
        showDetailsDialog.value = true
      } catch (error) {
        console.error('获取学生信息失败:', error)
        // 出错时创建一个包含基本信息的对象
        selectedStudent.value = {
          ...accommodation,
          phone: '未填写',
          email: '未填写',
          checkInDate: accommodation.checkInDate ? formatDate(accommodation.checkInDate) : '',
          checkOutDate: accommodation.checkOutDate ? formatDate(accommodation.checkOutDate) : '',
          createTime: accommodation.createTime ? formatDate(accommodation.createTime) : '',
          updateTime: accommodation.updateTime ? formatDate(accommodation.updateTime) : '',
          dormInfo: {
            buildingName: accommodation.buildingName,
            roomNumber: accommodation.roomNumber,
            bedNumber: accommodation.bedNumber,
            checkInDate: accommodation.checkInDate,
            floor: Math.floor(parseInt(accommodation.roomNumber) / 100),
            roomType: '标准间'
          }
        }
        showDetailsDialog.value = true
      }
    }
    
    const editAccommodation = (accommodation) => {
      Object.assign(accommodationForm, {
        id: accommodation.id,
        studentId: accommodation.studentId,
        studentNumber: accommodation.studentNumber,
        studentName: accommodation.studentName,
        phone: accommodation.phone || '',
        email: accommodation.email || '',
        className: accommodation.className || '',
        idCard: accommodation.idCard || '',
        roomId: accommodation.roomId,
        roomNumber: accommodation.roomNumber,
        buildingId: accommodation.buildingId,
        buildingName: accommodation.buildingName,
        bedNumber: accommodation.bedNumber,
        checkInDate: accommodation.checkInDate ? accommodation.checkInDate.split('T')[0] : '',
        checkOutDate: accommodation.checkOutDate ? accommodation.checkOutDate.split('T')[0] : '',
        status: accommodation.status,
        remarks: accommodation.remarks || ''
      })
      showEditDialog.value = true
    }
    
    const saveAccommodation = async () => {
      try {
        let response
        if (showAddDialog.value) {
          // 添加新住宿记录 - 构造正确的住宿记录数据结构
          const accommodationData = {
            studentId: accommodationForm.studentId,
            studentName: accommodationForm.studentName,
            studentNumber: accommodationForm.studentNumber,
            className: accommodationForm.className,
            idCard: accommodationForm.idCard,
            roomId: accommodationForm.roomId,
            roomNumber: accommodationForm.roomNumber,
            buildingId: accommodationForm.buildingId,
            buildingName: accommodationForm.buildingName,
            bedNumber: accommodationForm.bedNumber,
            checkInDate: accommodationForm.checkInDate || new Date().toISOString().split('T')[0],
            status: accommodationForm.status || 'ACTIVE',
            remarks: accommodationForm.remarks
          }
          
          response = await fetch('http://localhost:8082/api/dorm/accommodations', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(accommodationData)
          })
        } else {
          // 编辑住宿记录 - 构造正确的住宿记录数据结构
          const accommodationData = {
            id: accommodationForm.id,
            studentId: accommodationForm.studentId,
            studentName: accommodationForm.studentName,
            studentNumber: accommodationForm.studentNumber,
            className: accommodationForm.className,
            idCard: accommodationForm.idCard,
            roomId: accommodationForm.roomId,
            roomNumber: accommodationForm.roomNumber,
            buildingId: accommodationForm.buildingId,
            buildingName: accommodationForm.buildingName,
            bedNumber: accommodationForm.bedNumber,
            checkInDate: accommodationForm.checkInDate,
            checkOutDate: accommodationForm.checkOutDate,
            status: accommodationForm.status,
            remarks: accommodationForm.remarks
          }
          
          response = await fetch(`http://localhost:8082/api/dorm/accommodations/${accommodationForm.id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(accommodationData)
          })
        }
        
        const result = await response.json()
        
        // 修复响应格式检查 - 后端返回的是code字段
        if (result.code === 200) {
          alert(result.message || '保存住宿记录成功')
          closeDialogs()
          await loadAccommodations() // 重新加载住宿记录列表
        } else {
          alert(result.message || '保存住宿记录失败')
        }
      } catch (error) {
        console.error('保存住宿记录失败:', error)
        alert('操作失败，请重试')
      }
    }
    
    const deleteAccommodation = async (accommodation) => {
      if (confirm(`确定要删除 ${accommodation.studentName} 的住宿记录吗？`)) {
        try {
          const response = await fetch(`http://localhost:8082/api/dorm/accommodations/${accommodation.id}`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json'
            }
          })
          
          const result = await response.json()
          
          if (result.code === 200) {
            alert('删除成功')
            await loadAccommodations() // 重新加载住宿记录列表
          } else {
            alert(result.message || '删除失败')
          }
        } catch (error) {
          console.error('删除住宿记录失败:', error)
          alert('删除失败，请重试')
        }
      }
    }
    
    const changeRoom = (accommodation) => {
      assignStudent.value = accommodation
      resetAssignForm()
      showAssignDialog.value = true
    }
    
    const confirmAssign = async () => {
      try {
        // 构造住宿记录数据
        const accommodationData = {
          studentId: assignStudent.value.studentId,
          studentName: assignStudent.value.studentName,
          studentNumber: assignStudent.value.studentNumber,
          className: assignStudent.value.className,
          idCard: assignStudent.value.idCard,
          roomId: assignForm.roomId,
          bedNumber: assignForm.bedNumber,
          checkInDate: new Date().toISOString().split('T')[0],
          status: 'ACTIVE'
        }
        
        const response = await fetch('http://localhost:8082/api/dorm/accommodations', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(accommodationData)
        })
        
        const result = await response.json()
        
        if (result.code === 200) {
          const building = buildings.value.find(b => b.id == assignForm.buildingId)
          const room = availableRooms.value.find(r => r.id == assignForm.roomId)
          
          closeDialogs()
          alert(`宿舍分配成功！${assignStudent.value.studentName} 已分配到 ${building.name || building.buildingName} ${room.roomNumber}室 ${assignForm.bedNumber}号床`)
          await loadAccommodations() // 重新加载住宿记录列表
        } else {
          alert(result.message || '分配宿舍失败')
        }
      } catch (error) {
        console.error('分配宿舍失败:', error)
        alert('分配失败，请重试')
      }
    }
    
    const exportData = () => {
      alert('导出功能正在开发中...')
    }
    
    const closeDialogs = () => {
      showAddDialog.value = false
      showEditDialog.value = false
      showDetailsDialog.value = false
      showAssignDialog.value = false
      selectedStudent.value = null
      assignStudent.value = null
      resetForm()
      resetAssignForm()
    }
    
    // 生命周期
    onMounted(() => {
      loadAccommodations()
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
      showAssignDialog,
      selectedStudent,
      assignStudent,
      accommodations,
      buildings,
      availableRooms,
      availableBeds,
      accommodationForm,
      assignForm,
      filteredStudents,
      totalStudents,
      totalPages,
      getStatusClass,
      getStatusText,
      formatDate,
      handleSearch,
      handleFilter,
      changePage,
      refreshData,
      viewAccommodation,
      editAccommodation,
      saveAccommodation,
      deleteAccommodation,
      changeRoom,
      confirmAssign,
      exportData,
      closeDialogs,
      loadAvailableRooms,
      loadRoomsForBuilding,
      onRoomChange,
      selectRoom
    }
  }
}
</script>

<style scoped>
.student-management {
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

.students-table-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin-bottom: 30px;
}

.students-table {
  width: 100%;
  border-collapse: collapse;
}

.students-table th {
  background: #f8f9fa;
  padding: 15px 12px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 2px solid #e1e5e9;
  font-size: 0.9rem;
}

.students-table td {
  padding: 15px 12px;
  border-bottom: 1px solid #f0f0f0;
  vertical-align: middle;
}

.student-row:hover {
  background: #f8f9fa;
}

.student-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #667eea;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
  overflow: hidden;
}

.student-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.student-code {
  font-family: monospace;
  font-weight: 600;
  color: #667eea;
}

.student-name {
  font-weight: 600;
  color: #333;
}

.gender-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}

.gender-badge.male {
  background: #e3f2fd;
  color: #1976d2;
}

.gender-badge.female {
  background: #fce4ec;
  color: #c2185b;
}

.dorm-info {
  font-size: 0.9rem;
}

.dorm-building {
  font-weight: 500;
  color: #333;
}

.dorm-room {
  color: #666;
  font-size: 0.8rem;
}

.no-dorm {
  color: #999;
  font-style: italic;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  text-transform: uppercase;
}

.status-badge.active {
  background: #d4edda;
  color: #155724;
}

.status-badge.suspended {
  background: #fff3cd;
  color: #856404;
}

.status-badge.graduated {
  background: #d1ecf1;
  color: #0c5460;
}

.action-buttons {
  display: flex;
  gap: 5px;
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

.btn-danger {
  background: transparent;
  color: #dc3545;
  border: 1px solid #dc3545;
}

.btn-danger:hover {
  background: #dc3545;
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
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-large {
  max-width: 900px;
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

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
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

.student-details {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.detail-header {
  display: flex;
  gap: 20px;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.student-avatar-large {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: #667eea;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 2rem;
  overflow: hidden;
}

.student-avatar-large img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.student-basic-info h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 1.5rem;
}

.student-basic-info .student-code {
  margin: 0 0 10px 0;
  color: #666;
  font-family: monospace;
}

.student-basic-info .student-status {
  margin: 0;
}

.detail-sections {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.detail-section h4 {
  color: #333;
  font-size: 1.2rem;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 2px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.detail-section h4 i {
  color: #667eea;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 15px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.detail-item.full-width {
  grid-column: 1 / -1;
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

.dorm-detail {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.dorm-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #e1e5e9;
}

.dorm-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.dorm-header h5 {
  margin: 0;
  color: #333;
  font-size: 1.1rem;
}

.room-number {
  background: #667eea;
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.dorm-info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.dorm-info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.dorm-info-item label {
  font-weight: 500;
  color: #666;
  font-size: 0.9rem;
}

.dorm-info-item span {
  color: #333;
  font-size: 1rem;
}

.remark-content {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  border-left: 4px solid #667eea;
  color: #333;
  line-height: 1.6;
}

.assign-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.rooms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
}

.room-option {
  padding: 15px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f8f9fa;
}

.room-option:hover {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.05);
}

.room-option.active {
  border-color: #667eea;
  background: #667eea;
  color: white;
}

.room-number {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 8px;
}

.room-info {
  font-size: 0.9rem;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.beds-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 10px;
}

.bed-option {
  padding: 12px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f8f9fa;
  font-weight: 500;
}

.bed-option:hover {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.05);
}

.bed-option.active {
  border-color: #667eea;
  background: #667eea;
  color: white;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .student-management {
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
  
  .students-table-container {
    overflow-x: auto;
  }
  
  .students-table {
    min-width: 800px;
  }
  
  .modal {
    margin: 10px;
    max-width: none;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .detail-header {
    flex-direction: column;
    text-align: center;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .dorm-header {
    flex-direction: column;
    gap: 10px;
  }
  
  .dorm-info-grid {
    grid-template-columns: 1fr;
  }
  
  .rooms-grid {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }
  
  .beds-grid {
    grid-template-columns: repeat(auto-fill, minmax(60px, 1fr));
  }
}
</style>