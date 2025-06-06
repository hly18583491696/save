<template>
  <div class="room-management">
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="房间号">
          <el-input
            v-model="searchForm.roomNumber"
            placeholder="请输入房间号"
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
          >
            <el-option
              v-for="building in buildings"
              :key="building.id"
              :label="building.buildingName"
              :value="building.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="楼层">
          <el-input-number
            v-model="searchForm.floor"
            :min="1"
            :max="50"
            placeholder="请输入楼层"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select
            v-model="searchForm.roomType"
            placeholder="请选择房间类型"
            clearable
            style="width: 150px"
          >
            <el-option label="4人间" value="4人间" />
            <el-option label="6人间" value="6人间" />
            <el-option label="8人间" value="8人间" />
          </el-select>
        </el-form-item>
        <el-form-item label="房间状态">
          <el-select
            v-model="searchForm.roomStatus"
            placeholder="请选择房间状态"
            clearable
            style="width: 150px"
          >
            <el-option label="可入住" value="可入住" />
            <el-option label="已满员" value="已满员" />
            <el-option label="维修中" value="维修中" />
            <el-option label="停用" value="停用" />
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
        新增房间
      </el-button>
      <el-button type="success" @click="handleBatchGenerate">
        <el-icon><Magic /></el-icon>
        批量生成
      </el-button>
      <el-button
        type="danger"
        :disabled="!selectedRooms.length"
        @click="handleBatchDelete"
      >
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
    </div>
    
    <!-- 房间表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="roomList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roomNumber" label="房间号" width="120" />
        <el-table-column prop="buildingName" label="所属楼栋" width="150" />
        <el-table-column prop="floor" label="楼层" width="80" />
        <el-table-column prop="roomType" label="房间类型" width="100">
          <template #default="{ row }">
            <el-tag size="small" type="primary">
              {{ row.roomType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalBeds" label="床位总数" width="100" />
        <el-table-column prop="occupiedBeds" label="已入住" width="100" />
        <el-table-column prop="availableBeds" label="剩余床位" width="100">
          <template #default="{ row }">
            <span :class="{ 'text-danger': row.availableBeds === 0 }">
              {{ row.availableBeds }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="roomStatus" label="房间状态" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getStatusType(row.roomStatus)"
              size="small"
            >
              {{ row.roomStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="info" size="small" @click="handleViewStudents(row)">
              住宿
            </el-button>
            <el-button
              :type="row.roomStatus === '停用' ? 'success' : 'warning'"
              size="small"
              @click="handleToggleStatus(row)"
            >
              {{ row.roomStatus === '停用' ? '启用' : '停用' }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
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
    
    <!-- 房间表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleCloseDialog"
    >
      <el-form
        ref="roomFormRef"
        :model="roomForm"
        :rules="roomRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房间号" prop="roomNumber">
              <el-input v-model="roomForm.roomNumber" placeholder="请输入房间号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属楼栋" prop="buildingId">
              <el-select v-model="roomForm.buildingId" placeholder="请选择楼栋">
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
            <el-form-item label="楼层" prop="floor">
              <el-input-number
                v-model="roomForm.floor"
                :min="1"
                :max="50"
                placeholder="请输入楼层"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间类型" prop="roomType">
              <el-select v-model="roomForm.roomType" placeholder="请选择房间类型" @change="handleRoomTypeChange">
                <el-option label="4人间" value="4人间" />
                <el-option label="6人间" value="6人间" />
                <el-option label="8人间" value="8人间" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="床位总数" prop="totalBeds">
              <el-input-number
                v-model="roomForm.totalBeds"
                :min="1"
                :max="20"
                placeholder="请输入床位总数"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间状态" prop="roomStatus">
              <el-select v-model="roomForm.roomStatus" placeholder="请选择房间状态">
                <el-option label="可入住" value="可入住" />
                <el-option label="维修中" value="维修中" />
                <el-option label="停用" value="停用" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="roomForm.remark"
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
    
    <!-- 批量生成对话框 -->
    <el-dialog
      v-model="generateDialogVisible"
      title="批量生成房间"
      width="500px"
    >
      <el-form
        ref="generateFormRef"
        :model="generateForm"
        :rules="generateRules"
        label-width="100px"
      >
        <el-form-item label="所属楼栋" prop="buildingId">
          <el-select v-model="generateForm.buildingId" placeholder="请选择楼栋">
            <el-option
              v-for="building in buildings"
              :key="building.id"
              :label="building.buildingName"
              :value="building.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="起始楼层" prop="startFloor">
          <el-input-number
            v-model="generateForm.startFloor"
            :min="1"
            :max="50"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="结束楼层" prop="endFloor">
          <el-input-number
            v-model="generateForm.endFloor"
            :min="1"
            :max="50"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="每层房间数" prop="roomsPerFloor">
          <el-input-number
            v-model="generateForm.roomsPerFloor"
            :min="1"
            :max="100"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="房间类型" prop="roomType">
          <el-select v-model="generateForm.roomType" placeholder="请选择房间类型">
            <el-option label="4人间" value="4人间" />
            <el-option label="6人间" value="6人间" />
            <el-option label="8人间" value="8人间" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="房间号格式">
          <el-input
            v-model="generateForm.roomNumberFormat"
            placeholder="例如：{floor}{room:02d}，生成101、102等"
            readonly
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="generateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleGenerate" :loading="generateLoading">
            生成
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const generateLoading = ref(false)
const dialogVisible = ref(false)
const generateDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const roomFormRef = ref()
const generateFormRef = ref()
const selectedRooms = ref([])

// 搜索表单
const searchForm = reactive({
  roomNumber: '',
  buildingId: null,
  floor: null,
  roomType: '',
  roomStatus: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 房间列表
const roomList = ref([])

// 楼栋列表
const buildings = ref([
  { id: 1, buildingName: 'A栋学生宿舍' },
  { id: 2, buildingName: 'B栋学生宿舍' },
  { id: 3, buildingName: 'C栋学生宿舍' }
])

// 房间表单
const roomForm = reactive({
  id: null,
  roomNumber: '',
  buildingId: null,
  floor: null,
  roomType: '',
  totalBeds: null,
  roomStatus: '可入住',
  remark: ''
})

// 批量生成表单
const generateForm = reactive({
  buildingId: null,
  startFloor: 1,
  endFloor: 6,
  roomsPerFloor: 20,
  roomType: '4人间',
  roomNumberFormat: '{floor}{room:02d}'
})

// 表单验证规则
const roomRules = {
  roomNumber: [
    { required: true, message: '请输入房间号', trigger: 'blur' }
  ],
  buildingId: [
    { required: true, message: '请选择楼栋', trigger: 'change' }
  ],
  floor: [
    { required: true, message: '请输入楼层', trigger: 'blur' }
  ],
  roomType: [
    { required: true, message: '请选择房间类型', trigger: 'change' }
  ],
  totalBeds: [
    { required: true, message: '请输入床位总数', trigger: 'blur' }
  ]
}

const generateRules = {
  buildingId: [
    { required: true, message: '请选择楼栋', trigger: 'change' }
  ],
  startFloor: [
    { required: true, message: '请输入起始楼层', trigger: 'blur' }
  ],
  endFloor: [
    { required: true, message: '请输入结束楼层', trigger: 'blur' }
  ],
  roomsPerFloor: [
    { required: true, message: '请输入每层房间数', trigger: 'blur' }
  ],
  roomType: [
    { required: true, message: '请选择房间类型', trigger: 'change' }
  ]
}

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    '可入住': 'success',
    '已满员': 'warning',
    '维修中': 'danger',
    '停用': 'info'
  }
  return statusMap[status] || 'info'
}

// 房间类型变化时自动设置床位数
const handleRoomTypeChange = (value) => {
  const bedMap = {
    '4人间': 4,
    '6人间': 6,
    '8人间': 8
  }
  roomForm.totalBeds = bedMap[value] || null
}

// 获取房间列表
const fetchRoomList = async () => {
  try {
    loading.value = true
    
    // 模拟数据
    const mockData = {
      code: 200,
      data: {
        records: [
          {
            id: 1,
            roomNumber: '101',
            buildingId: 1,
            buildingName: 'A栋学生宿舍',
            floor: 1,
            roomType: '4人间',
            totalBeds: 4,
            occupiedBeds: 3,
            availableBeds: 1,
            roomStatus: '可入住',
            createTime: '2024-01-01 10:00:00',
            remark: ''
          },
          {
            id: 2,
            roomNumber: '102',
            buildingId: 1,
            buildingName: 'A栋学生宿舍',
            floor: 1,
            roomType: '4人间',
            totalBeds: 4,
            occupiedBeds: 4,
            availableBeds: 0,
            roomStatus: '已满员',
            createTime: '2024-01-01 10:00:00',
            remark: ''
          },
          {
            id: 3,
            roomNumber: '201',
            buildingId: 2,
            buildingName: 'B栋学生宿舍',
            floor: 2,
            roomType: '6人间',
            totalBeds: 6,
            occupiedBeds: 2,
            availableBeds: 4,
            roomStatus: '可入住',
            createTime: '2024-01-01 10:00:00',
            remark: ''
          },
          {
            id: 4,
            roomNumber: '301',
            buildingId: 3,
            buildingName: 'C栋学生宿舍',
            floor: 3,
            roomType: '8人间',
            totalBeds: 8,
            occupiedBeds: 0,
            availableBeds: 8,
            roomStatus: '维修中',
            createTime: '2024-01-01 10:00:00',
            remark: '正在维修水管'
          }
        ],
        total: 4,
        current: pagination.current,
        size: pagination.size
      }
    }
    
    roomList.value = mockData.data.records
    pagination.total = mockData.data.total
  } catch (error) {
    console.error('获取房间列表失败:', error)
    ElMessage.error('获取房间列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchRoomList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    roomNumber: '',
    buildingId: null,
    floor: null,
    roomType: '',
    roomStatus: ''
  })
  pagination.current = 1
  fetchRoomList()
}

// 新增房间
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增房间'
  resetRoomForm()
  dialogVisible.value = true
}

// 编辑房间
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑房间'
  Object.assign(roomForm, row)
  dialogVisible.value = true
}

// 删除房间
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除房间 "${row.roomNumber}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      ElMessage.success('删除成功')
      fetchRoomList()
    } catch (error) {
      console.error('删除房间失败:', error)
    }
  })
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRooms.value.length} 个房间吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      ElMessage.success('批量删除成功')
      fetchRoomList()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

// 批量生成
const handleBatchGenerate = () => {
  generateDialogVisible.value = true
}

// 生成房间
const handleGenerate = async () => {
  if (!generateFormRef.value) return
  
  try {
    await generateFormRef.value.validate()
    generateLoading.value = true
    
    const totalRooms = (generateForm.endFloor - generateForm.startFloor + 1) * generateForm.roomsPerFloor
    
    ElMessage.success(`成功生成 ${totalRooms} 个房间`)
    generateDialogVisible.value = false
    fetchRoomList()
  } catch (error) {
    console.error('生成房间失败:', error)
  } finally {
    generateLoading.value = false
  }
}

// 切换房间状态
const handleToggleStatus = async (row) => {
  try {
    const newStatus = row.roomStatus === '停用' ? '可入住' : '停用'
    ElMessage.success(`房间状态已${newStatus === '停用' ? '停用' : '启用'}`)
    fetchRoomList()
  } catch (error) {
    console.error('切换房间状态失败:', error)
  }
}

// 查看住宿学生
const handleViewStudents = (row) => {
  router.push({
    path: '/accommodation',
    query: { roomId: row.id }
  })
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedRooms.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  fetchRoomList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  fetchRoomList()
}

// 提交表单
const handleSubmit = async () => {
  if (!roomFormRef.value) return
  
  try {
    await roomFormRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      ElMessage.success('更新成功')
    } else {
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    fetchRoomList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleCloseDialog = () => {
  resetRoomForm()
  if (roomFormRef.value) {
    roomFormRef.value.clearValidate()
  }
  dialogVisible.value = false
}

// 重置房间表单
const resetRoomForm = () => {
  Object.assign(roomForm, {
    id: null,
    roomNumber: '',
    buildingId: null,
    floor: null,
    roomType: '',
    totalBeds: null,
    roomStatus: '可入住',
    remark: ''
  })
}

// 监听路由参数变化
watch(
  () => route.query.buildingId,
  (buildingId) => {
    if (buildingId) {
      searchForm.buildingId = parseInt(buildingId)
      fetchRoomList()
    }
  },
  { immediate: true }
)

onMounted(() => {
  if (!route.query.buildingId) {
    fetchRoomList()
  }
})
</script>

<style lang="scss" scoped>
.room-management {
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
      
      .text-danger {
        color: #f56c6c;
        font-weight: bold;
      }
    }
  }
}
</style>