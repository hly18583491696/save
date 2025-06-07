<template>
  <div class="building-management">
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="楼栋编号">
          <el-input
            v-model="searchForm.buildingCode"
            placeholder="请输入楼栋编号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="楼栋名称">
          <el-input
            v-model="searchForm.buildingName"
            placeholder="请输入楼栋名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="楼栋类型">
          <el-select
            v-model="searchForm.buildingType"
            placeholder="请选择楼栋类型"
            clearable
            style="width: 150px"
          >
            <el-option label="男生宿舍" value="男生宿舍" />
            <el-option label="女生宿舍" value="女生宿舍" />
            <el-option label="混合宿舍" value="混合宿舍" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="正常" :value="1" />
            <el-option label="维修" :value="0" />
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
        新增楼栋
      </el-button>
      <el-button
        type="danger"
        :disabled="!selectedBuildings.length"
        @click="handleBatchDelete"
      >
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
    </div>
    
    <!-- 楼栋表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="buildingList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="buildingCode" label="楼栋编号" width="120" />
        <el-table-column prop="buildingName" label="楼栋名称" width="150" />
        <el-table-column prop="buildingType" label="楼栋类型" width="120">
          <template #default="{ row }">
            <el-tag
              :type="row.buildingType === '男生宿舍' ? 'primary' : row.buildingType === '女生宿舍' ? 'danger' : 'warning'"
              size="small"
            >
              {{ row.buildingType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalFloors" label="总楼层" width="100" />
        <el-table-column prop="roomsPerFloor" label="每层房间数" width="120" />
        <el-table-column prop="totalRooms" label="总房间数" width="120" />
        <el-table-column prop="dormManagerName" label="宿管员" width="120" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag
              :type="row.status === 1 ? 'success' : 'warning'"
              size="small"
            >
              {{ row.status === 1 ? '正常' : '维修' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="info" size="small" @click="handleViewRooms(row)">
              房间
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
    
    <!-- 楼栋表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleCloseDialog"
    >
      <el-form
        ref="buildingFormRef"
        :model="buildingForm"
        :rules="buildingRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="楼栋编号" prop="buildingCode">
              <el-input v-model="buildingForm.buildingCode" placeholder="请输入楼栋编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼栋名称" prop="buildingName">
              <el-input v-model="buildingForm.buildingName" placeholder="请输入楼栋名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="楼栋类型" prop="buildingType">
              <el-select v-model="buildingForm.buildingType" placeholder="请选择楼栋类型">
                <el-option label="男生宿舍" value="男生宿舍" />
                <el-option label="女生宿舍" value="女生宿舍" />
                <el-option label="混合宿舍" value="混合宿舍" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总楼层" prop="totalFloors">
              <el-input-number
                v-model="buildingForm.totalFloors"
                :min="1"
                :max="50"
                placeholder="请输入总楼层"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="每层房间数" prop="roomsPerFloor">
              <el-input-number
                v-model="buildingForm.roomsPerFloor"
                :min="1"
                :max="100"
                placeholder="请输入每层房间数"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宿管员" prop="dormManagerId">
              <el-select
                v-model="buildingForm.dormManagerId"
                placeholder="请选择宿管员"
                filterable
              >
                <el-option
                  v-for="manager in dormManagers"
                  :key="manager.id"
                  :label="manager.realName"
                  :value="manager.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="buildingForm.status" placeholder="请选择状态">
                <el-option label="正常" :value="1" />
                <el-option label="维修" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="buildingForm.remark"
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const buildingFormRef = ref()
const selectedBuildings = ref([])

// 搜索表单
const searchForm = reactive({
  buildingCode: '',
  buildingName: '',
  buildingType: '',
  status: null
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 楼栋列表
const buildingList = ref([])

// 宿管员列表
const dormManagers = ref([
  { id: 2, realName: '张宿管' },
  { id: 4, realName: '李宿管' },
  { id: 5, realName: '王宿管' }
])

// 楼栋表单
const buildingForm = reactive({
  id: null,
  buildingCode: '',
  buildingName: '',
  buildingType: '',
  totalFloors: null,
  roomsPerFloor: null,
  dormManagerId: null,
  status: 1,
  remark: ''
})

// 表单验证规则
const buildingRules = {
  buildingCode: [
    { required: true, message: '请输入楼栋编号', trigger: 'blur' }
  ],
  buildingName: [
    { required: true, message: '请输入楼栋名称', trigger: 'blur' }
  ],
  buildingType: [
    { required: true, message: '请选择楼栋类型', trigger: 'change' }
  ],
  totalFloors: [
    { required: true, message: '请输入总楼层', trigger: 'blur' }
  ],
  roomsPerFloor: [
    { required: true, message: '请输入每层房间数', trigger: 'blur' }
  ],
  dormManagerId: [
    { required: true, message: '请选择宿管员', trigger: 'change' }
  ]
}

// 计算总房间数
const totalRooms = computed(() => {
  if (buildingForm.totalFloors && buildingForm.roomsPerFloor) {
    return buildingForm.totalFloors * buildingForm.roomsPerFloor
  }
  return 0
})

// 获取楼栋列表
const fetchBuildingList = async () => {
  try {
    loading.value = true
    
    // 模拟数据
    const mockData = {
      code: 200,
      data: {
        records: [
          {
            id: 1,
            buildingCode: 'A001',
            buildingName: 'A栋学生宿舍',
            buildingType: '男生宿舍',
            totalFloors: 6,
            roomsPerFloor: 20,
            totalRooms: 120,
            dormManagerId: 2,
            dormManagerName: '张宿管',
            status: 1,
            createTime: '2024-01-01 10:00:00',
            remark: 'A栋男生宿舍楼'
          },
          {
            id: 2,
            buildingCode: 'B001',
            buildingName: 'B栋学生宿舍',
            buildingType: '女生宿舍',
            totalFloors: 6,
            roomsPerFloor: 18,
            totalRooms: 108,
            dormManagerId: 4,
            dormManagerName: '李宿管',
            status: 1,
            createTime: '2024-01-02 09:00:00',
            remark: 'B栋女生宿舍楼'
          },
          {
            id: 3,
            buildingCode: 'C001',
            buildingName: 'C栋学生宿舍',
            buildingType: '混合宿舍',
            totalFloors: 8,
            roomsPerFloor: 16,
            totalRooms: 128,
            dormManagerId: 5,
            dormManagerName: '王宿管',
            status: 0,
            createTime: '2024-01-03 08:00:00',
            remark: 'C栋混合宿舍楼，正在维修'
          }
        ],
        total: 3,
        current: pagination.current,
        size: pagination.size
      }
    }
    
    buildingList.value = mockData.data.records
    pagination.total = mockData.data.total
  } catch (error) {
    console.error('获取楼栋列表失败:', error)
    ElMessage.error('获取楼栋列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchBuildingList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    buildingCode: '',
    buildingName: '',
    buildingType: '',
    status: null
  })
  pagination.current = 1
  fetchBuildingList()
}

// 新增楼栋
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增楼栋'
  resetBuildingForm()
  dialogVisible.value = true
}

// 编辑楼栋
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑楼栋'
  Object.assign(buildingForm, row)
  dialogVisible.value = true
}

// 删除楼栋
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除楼栋 "${row.buildingName}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      ElMessage.success('删除成功')
      fetchBuildingList()
    } catch (error) {
      console.error('删除楼栋失败:', error)
    }
  })
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedBuildings.value.length} 个楼栋吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      ElMessage.success('批量删除成功')
      fetchBuildingList()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

// 查看房间
const handleViewRooms = (row) => {
  router.push({
    path: '/room',
    query: { buildingId: row.id }
  })
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedBuildings.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  fetchBuildingList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  fetchBuildingList()
}

// 提交表单
const handleSubmit = async () => {
  if (!buildingFormRef.value) return
  
  try {
    await buildingFormRef.value.validate()
    submitLoading.value = true
    
    // 计算总房间数
    buildingForm.totalRooms = totalRooms.value
    
    if (isEdit.value) {
      ElMessage.success('更新成功')
    } else {
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    fetchBuildingList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleCloseDialog = () => {
  resetBuildingForm()
  if (buildingFormRef.value) {
    buildingFormRef.value.clearValidate()
  }
  dialogVisible.value = false
}

// 重置楼栋表单
const resetBuildingForm = () => {
  Object.assign(buildingForm, {
    id: null,
    buildingCode: '',
    buildingName: '',
    buildingType: '',
    totalFloors: null,
    roomsPerFloor: null,
    dormManagerId: null,
    status: 1,
    remark: ''
  })
}

onMounted(() => {
  fetchBuildingList()
})
</script>

<style lang="scss" scoped>
.building-management {
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