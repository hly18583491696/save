<template>
  <div class="accommodation-management">
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="学生姓名">
          <el-input
            v-model="searchForm.studentName"
            placeholder="请输入学生姓名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="学号">
          <el-input
            v-model="searchForm.studentNumber"
            placeholder="请输入学号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="所属楼栋">
          <el-select
            v-model="searchForm.buildingId"
            placeholder="请选择楼栋"
            clearable
            style="width: 200px"
            @change="handleBuildingChange"
          >
            <el-option
              v-for="building in buildings"
              :key="building.id"
              :label="building.buildingName"
              :value="building.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="房间号">
          <el-select
            v-model="searchForm.roomId"
            placeholder="请选择房间"
            clearable
            style="width: 200px"
            filterable
          >
            <el-option
              v-for="room in rooms"
              :key="room.id"
              :label="room.roomNumber"
              :value="room.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="住宿状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 150px"
          >
            <el-option label="在住" value="在住" />
            <el-option label="已退宿" value="已退宿" />
            <el-option label="暂离" value="暂离" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 操作按钮 -->
    <div class="operation-buttons">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        安排住宿
      </el-button>
      <el-button type="warning" @click="handleBatchMove">
        <el-icon><Switch /></el-icon>
        批量调宿
      </el-button>
      <el-button
        type="danger"
        :disabled="!selectedAccommodations.length"
        @click="handleBatchCheckout"
      >
        <el-icon><Remove /></el-icon>
        批量退宿
      </el-button>
    </div>
    
    <!-- 住宿表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="accommodationList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="studentNumber" label="学号" width="150" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === '男' ? 'primary' : 'danger'" size="small">
              {{ row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院" width="150" />
        <el-table-column prop="major" label="专业" width="150" />
        <el-table-column prop="className" label="班级" width="120" />
        <el-table-column prop="buildingName" label="楼栋" width="150" />
        <el-table-column prop="roomNumber" label="房间号" width="100" />
        <el-table-column prop="bedNumber" label="床位号" width="100">
          <template #default="{ row }">
            <el-tag type="info" size="small">
              {{ row.bedNumber }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="住宿状态" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getStatusType(row.status)"
              size="small"
            >
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkInTime" label="入住时间" width="160" />
        <el-table-column prop="checkOutTime" label="退宿时间" width="160">
          <template #default="{ row }">
            <span>{{ row.checkOutTime || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="warning" size="small" @click="handleMove(row)">
              调宿
            </el-button>
            <el-button
              v-if="row.status === '在住'"
              type="danger"
              size="small"
              @click="handleCheckout(row)"
            >
              退宿
            </el-button>
            <el-button
              v-else-if="row.status === '已退宿'"
              type="success"
              size="small"
              @click="handleCheckin(row)"
            >
              重新入住
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 住宿表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleCloseDialog"
    >
      <el-form
        ref="accommodationFormRef"
        :model="accommodationForm"
        :rules="accommodationRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学生" prop="studentId">
              <el-select
                v-model="accommodationForm.studentId"
                placeholder="请选择学生"
                filterable
                remote
                :remote-method="searchStudents"
                :loading="studentLoading"
                style="width: 100%"
              >
                <el-option
                  v-for="student in students"
                  :key="student.id"
                  :label="`${student.realName}(${student.studentNumber})`"
                  :value="student.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼栋" prop="buildingId">
              <el-select
                v-model="accommodationForm.buildingId"
                placeholder="请选择楼栋"
                @change="handleFormBuildingChange"
                style="width: 100%"
              >
                <el-option
                  v-for="building in buildings"
                  :key="building.id"
                  :label="building.buildingName"
                  :value="building.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房间" prop="roomId">
              <el-select
                v-model="accommodationForm.roomId"
                placeholder="请选择房间"
                @change="handleFormRoomChange"
                style="width: 100%"
              >
                <el-option
                  v-for="room in formRooms"
                  :key="room.id"
                  :label="`${room.roomNumber}(${room.availableBeds}个空床位)`"
                  :value="room.id"
                  :disabled="room.availableBeds === 0"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="床位" prop="bedNumber">
              <el-select
                v-model="accommodationForm.bedNumber"
                placeholder="请选择床位"
                style="width: 100%"
              >
                <el-option
                  v-for="bed in availableBeds"
                  :key="bed"
                  :label="`床位${bed}`"
                  :value="bed"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="入住时间" prop="checkInTime">
          <el-date-picker
            v-model="accommodationForm.checkInTime"
            type="datetime"
            placeholder="请选择入住时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="accommodationForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 调宿对话框 -->
    <el-dialog
      v-model="moveDialogVisible"
      title="调宿"
      width="500px"
    >
      <el-form
        ref="moveFormRef"
        :model="moveForm"
        :rules="moveRules"
        label-width="100px"
      >
        <el-form-item label="当前住宿">
          <el-input
            :value="`${currentAccommodation.buildingName} ${currentAccommodation.roomNumber} 床位${currentAccommodation.bedNumber}`"
            readonly
          />
        </el-form-item>
        
        <el-form-item label="目标楼栋" prop="targetBuildingId">
          <el-select
            v-model="moveForm.targetBuildingId"
            placeholder="请选择楼栋"
            @change="handleMoveBuildingChange"
            style="width: 100%"
          >
            <el-option
              v-for="building in buildings"
              :key="building.id"
              :label="building.buildingName"
              :value="building.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="目标房间" prop="targetRoomId">
          <el-select
            v-model="moveForm.targetRoomId"
            placeholder="请选择房间"
            @change="handleMoveRoomChange"
            style="width: 100%"
          >
            <el-option
              v-for="room in moveRooms"
              :key="room.id"
              :label="`${room.roomNumber}(${room.availableBeds}个空床位)`"
              :value="room.id"
              :disabled="room.availableBeds === 0"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="目标床位" prop="targetBedNumber">
          <el-select
            v-model="moveForm.targetBedNumber"
            placeholder="请选择床位"
            style="width: 100%"
          >
            <el-option
              v-for="bed in moveAvailableBeds"
              :key="bed"
              :label="`床位${bed}`"
              :value="bed"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="调宿原因" prop="moveReason">
          <el-input
            v-model="moveForm.moveReason"
            type="textarea"
            :rows="3"
            placeholder="请输入调宿原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="moveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleMoveSubmit" :loading="moveLoading">
            确定调宿
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute } from 'vue-router'

const route = useRoute()

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const moveLoading = ref(false)
const studentLoading = ref(false)
const dialogVisible = ref(false)
const moveDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const accommodationFormRef = ref()
const moveFormRef = ref()
const selectedAccommodations = ref([])

// 搜索表单
const searchForm = reactive({
  studentName: '',
  studentNumber: '',
  buildingId: null,
  roomId: null,
  status: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 住宿列表
const accommodationList = ref([])

// 楼栋列表
const buildings = ref([
  { id: 1, buildingName: 'A栋学生宿舍' },
  { id: 2, buildingName: 'B栋学生宿舍' },
  { id: 3, buildingName: 'C栋学生宿舍' }
])

// 房间列表
const rooms = ref([])
const formRooms = ref([])
const moveRooms = ref([])

// 学生列表
const students = ref([])

// 可用床位
const availableBeds = ref([])
const moveAvailableBeds = ref([])

// 当前住宿信息（用于调宿）
const currentAccommodation = ref({})

// 住宿表单
const accommodationForm = reactive({
  id: null,
  studentId: null,
  buildingId: null,
  roomId: null,
  bedNumber: null,
  checkInTime: '',
  remark: ''
})

// 调宿表单
const moveForm = reactive({
  targetBuildingId: null,
  targetRoomId: null,
  targetBedNumber: null,
  moveReason: ''
})

// 表单验证规则
const accommodationRules = {
  studentId: [
    { required: true, message: '请选择学生', trigger: 'change' }
  ],
  buildingId: [
    { required: true, message: '请选择楼栋', trigger: 'change' }
  ],
  roomId: [
    { required: true, message: '请选择房间', trigger: 'change' }
  ],
  bedNumber: [
    { required: true, message: '请选择床位', trigger: 'change' }
  ],
  checkInTime: [
    { required: true, message: '请选择入住时间', trigger: 'change' }
  ]
}

const moveRules = {
  targetBuildingId: [
    { required: true, message: '请选择目标楼栋', trigger: 'change' }
  ],
  targetRoomId: [
    { required: true, message: '请选择目标房间', trigger: 'change' }
  ],
  targetBedNumber: [
    { required: true, message: '请选择目标床位', trigger: 'change' }
  ],
  moveReason: [
    { required: true, message: '请输入调宿原因', trigger: 'blur' }
  ]
}

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    '在住': 'success',
    '已退宿': 'info',
    '暂离': 'warning'
  }
  return statusMap[status] || 'info'
}

// 获取住宿列表
const fetchAccommodationList = async () => {
  try {
    loading.value = true
    
    // 模拟数据
    const mockData = {
      code: 200,
      data: {
        records: [
          {
            id: 1,
            studentId: 1,
            studentName: '张三',
            studentNumber: '2021001',
            gender: '男',
            college: '计算机学院',
            major: '软件工程',
            className: '软工2101',
            buildingId: 1,
            buildingName: 'A栋学生宿舍',
            roomId: 1,
            roomNumber: '101',
            bedNumber: 1,
            status: '在住',
            checkInTime: '2024-09-01 10:00:00',
            checkOutTime: null,
            remark: ''
          },
          {
            id: 2,
            studentId: 2,
            studentName: '李四',
            studentNumber: '2021002',
            gender: '男',
            college: '计算机学院',
            major: '软件工程',
            className: '软工2101',
            buildingId: 1,
            buildingName: 'A栋学生宿舍',
            roomId: 1,
            roomNumber: '101',
            bedNumber: 2,
            status: '在住',
            checkInTime: '2024-09-01 10:00:00',
            checkOutTime: null,
            remark: ''
          },
          {
            id: 3,
            studentId: 3,
            studentName: '王五',
            studentNumber: '2021003',
            gender: '女',
            college: '外国语学院',
            major: '英语',
            className: '英语2101',
            buildingId: 2,
            buildingName: 'B栋学生宿舍',
            roomId: 3,
            roomNumber: '201',
            bedNumber: 1,
            status: '已退宿',
            checkInTime: '2024-09-01 10:00:00',
            checkOutTime: '2024-12-01 10:00:00',
            remark: '毕业退宿'
          }
        ],
        total: 3,
        current: pagination.current,
        size: pagination.size
      }
    }
    
    accommodationList.value = mockData.data.records
    pagination.total = mockData.data.total
  } catch (error) {
    console.error('获取住宿列表失败:', error)
    ElMessage.error('获取住宿列表失败')
  } finally {
    loading.value = false
  }
}

// 获取房间列表
const fetchRooms = async (buildingId = null) => {
  try {
    // 模拟数据
    const mockRooms = [
      { id: 1, roomNumber: '101', buildingId: 1, availableBeds: 1 },
      { id: 2, roomNumber: '102', buildingId: 1, availableBeds: 0 },
      { id: 3, roomNumber: '201', buildingId: 2, availableBeds: 4 },
      { id: 4, roomNumber: '301', buildingId: 3, availableBeds: 8 }
    ]
    
    if (buildingId) {
      return mockRooms.filter(room => room.buildingId === buildingId)
    }
    return mockRooms
  } catch (error) {
    console.error('获取房间列表失败:', error)
    return []
  }
}

// 搜索学生
const searchStudents = async (query) => {
  if (!query) {
    students.value = []
    return
  }
  
  try {
    studentLoading.value = true
    
    // 模拟数据
    const mockStudents = [
      { id: 4, realName: '赵六', studentNumber: '2021004' },
      { id: 5, realName: '钱七', studentNumber: '2021005' },
      { id: 6, realName: '孙八', studentNumber: '2021006' }
    ]
    
    students.value = mockStudents.filter(student => 
      student.realName.includes(query) || student.studentNumber.includes(query)
    )
  } catch (error) {
    console.error('搜索学生失败:', error)
  } finally {
    studentLoading.value = false
  }
}

// 获取可用床位
const fetchAvailableBeds = async (roomId) => {
  try {
    // 模拟数据
    const mockBeds = {
      1: [3, 4], // 房间1的可用床位
      3: [2, 3, 4, 5], // 房间3的可用床位
      4: [1, 2, 3, 4, 5, 6, 7, 8] // 房间4的可用床位
    }
    
    return mockBeds[roomId] || []
  } catch (error) {
    console.error('获取可用床位失败:', error)
    return []
  }
}

// 楼栋变化
const handleBuildingChange = async (buildingId) => {
  searchForm.roomId = null
  if (buildingId) {
    rooms.value = await fetchRooms(buildingId)
  } else {
    rooms.value = []
  }
}

// 表单楼栋变化
const handleFormBuildingChange = async (buildingId) => {
  accommodationForm.roomId = null
  accommodationForm.bedNumber = null
  availableBeds.value = []
  
  if (buildingId) {
    formRooms.value = await fetchRooms(buildingId)
  } else {
    formRooms.value = []
  }
}

// 表单房间变化
const handleFormRoomChange = async (roomId) => {
  accommodationForm.bedNumber = null
  
  if (roomId) {
    availableBeds.value = await fetchAvailableBeds(roomId)
  } else {
    availableBeds.value = []
  }
}

// 调宿楼栋变化
const handleMoveBuildingChange = async (buildingId) => {
  moveForm.targetRoomId = null
  moveForm.targetBedNumber = null
  moveAvailableBeds.value = []
  
  if (buildingId) {
    moveRooms.value = await fetchRooms(buildingId)
  } else {
    moveRooms.value = []
  }
}

// 调宿房间变化
const handleMoveRoomChange = async (roomId) => {
  moveForm.targetBedNumber = null
  
  if (roomId) {
    moveAvailableBeds.value = await fetchAvailableBeds(roomId)
  } else {
    moveAvailableBeds.value = []
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchAccommodationList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    studentName: '',
    studentNumber: '',
    buildingId: null,
    roomId: null,
    status: ''
  })
  rooms.value = []
  pagination.current = 1
  fetchAccommodationList()
}

// 安排住宿
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '安排住宿'
  resetAccommodationForm()
  dialogVisible.value = true
}

// 编辑住宿
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑住宿'
  Object.assign(accommodationForm, row)
  dialogVisible.value = true
}

// 调宿
const handleMove = (row) => {
  currentAccommodation.value = row
  resetMoveForm()
  moveDialogVisible.value = true
}

// 退宿
const handleCheckout = (row) => {
  ElMessageBox.confirm(
    `确定要为学生 "${row.studentName}" 办理退宿吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      ElMessage.success('退宿成功')
      fetchAccommodationList()
    } catch (error) {
      console.error('退宿失败:', error)
    }
  })
}

// 重新入住
const handleCheckin = (row) => {
  ElMessageBox.confirm(
    `确定要为学生 "${row.studentName}" 重新安排入住吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      ElMessage.success('重新入住成功')
      fetchAccommodationList()
    } catch (error) {
      console.error('重新入住失败:', error)
    }
  })
}

// 批量调宿
const handleBatchMove = () => {
  if (!selectedAccommodations.value.length) {
    ElMessage.warning('请选择要调宿的学生')
    return
  }
  ElMessage.info('批量调宿功能开发中')
}

// 批量退宿
const handleBatchCheckout = () => {
  ElMessageBox.confirm(
    `确定要为选中的 ${selectedAccommodations.value.length} 名学生办理退宿吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      ElMessage.success('批量退宿成功')
      fetchAccommodationList()
    } catch (error) {
      console.error('批量退宿失败:', error)
    }
  })
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedAccommodations.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  fetchAccommodationList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  fetchAccommodationList()
}

// 提交表单
const handleSubmit = async () => {
  if (!accommodationFormRef.value) return
  
  try {
    await accommodationFormRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      ElMessage.success('更新成功')
    } else {
      ElMessage.success('安排住宿成功')
    }
    
    dialogVisible.value = false
    fetchAccommodationList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 提交调宿
const handleMoveSubmit = async () => {
  if (!moveFormRef.value) return
  
  try {
    await moveFormRef.value.validate()
    moveLoading.value = true
    
    ElMessage.success('调宿成功')
    moveDialogVisible.value = false
    fetchAccommodationList()
  } catch (error) {
    console.error('调宿失败:', error)
  } finally {
    moveLoading.value = false
  }
}

// 关闭对话框
const handleCloseDialog = () => {
  resetAccommodationForm()
  if (accommodationFormRef.value) {
    accommodationFormRef.value.clearValidate()
  }
  dialogVisible.value = false
}

// 重置住宿表单
const resetAccommodationForm = () => {
  Object.assign(accommodationForm, {
    id: null,
    studentId: null,
    buildingId: null,
    roomId: null,
    bedNumber: null,
    checkInTime: '',
    remark: ''
  })
  students.value = []
  formRooms.value = []
  availableBeds.value = []
}

// 重置调宿表单
const resetMoveForm = () => {
  Object.assign(moveForm, {
    targetBuildingId: null,
    targetRoomId: null,
    targetBedNumber: null,
    moveReason: ''
  })
  moveRooms.value = []
  moveAvailableBeds.value = []
}

// 监听路由参数变化
watch(
  () => route.query.roomId,
  (roomId) => {
    if (roomId) {
      searchForm.roomId = parseInt(roomId)
      fetchAccommodationList()
    }
  },
  { immediate: true }
)

onMounted(() => {
  if (!route.query.roomId) {
    fetchAccommodationList()
  }
})
</script>

<style lang="scss" scoped>
.accommodation-management {
  .search-form {
    .el-form-item {
      margin-bottom: 16px;
    }
  }
  
  .table-container {
    .el-table {
      .el-tag {
        font-size: 12px;
      }
    }
  }
}
</style>