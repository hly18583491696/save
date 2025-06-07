<template>
  <div class="user-management">
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input
            v-model="searchForm.realName"
            placeholder="请输入真实姓名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="用户类型">
          <el-select
            v-model="searchForm.userType"
            placeholder="请选择用户类型"
            clearable
            style="width: 150px"
          >
            <el-option label="管理员" :value="1" />
            <el-option label="宿管员" :value="2" />
            <el-option label="学生" :value="3" />
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
            <el-option label="禁用" :value="0" />
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
        新增用户
      </el-button>
      <el-button
        type="danger"
        :disabled="!selectedUsers.length"
        @click="handleBatchDelete"
      >
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
    </div>
    
    <!-- 用户表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="userList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="studentNumber" label="学号/工号" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <span>{{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="userType" label="用户类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="row.userType === 1 ? 'danger' : row.userType === 2 ? 'warning' : 'primary'"
              size="small"
            >
              {{ getUserTypeName(row.userType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="college" label="学院" width="120" />
        <el-table-column prop="major" label="专业" width="120" />
        <el-table-column prop="className" label="班级" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag
              :type="row.status === 1 ? 'success' : 'danger'"
              size="small"
            >
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              size="small"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
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
    
    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleCloseDialog"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="80px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="userForm.username" placeholder="请输入用户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号/工号" prop="studentNumber">
              <el-input v-model="userForm.studentNumber" placeholder="请输入学号或工号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="userForm.gender" placeholder="请选择性别">
                <el-option label="男" :value="1" />
                <el-option label="女" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户类型" prop="userType">
              <el-select v-model="userForm.userType" placeholder="请选择用户类型">
                <el-option label="管理员" :value="1" />
                <el-option label="宿管员" :value="2" />
                <el-option label="学生" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="userForm.status" placeholder="请选择状态">
                <el-option label="正常" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" v-if="userForm.userType === 3">
          <el-col :span="8">
            <el-form-item label="学院" prop="college">
              <el-input v-model="userForm.college" placeholder="请输入学院" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="专业" prop="major">
              <el-input v-model="userForm.major" placeholder="请输入专业" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="班级" prop="className">
              <el-input v-model="userForm.className" placeholder="请输入班级" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="userForm.remark"
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getUserList,
  updateUser,
  deleteUser,
  toggleUserStatus,
  register
} from '@/api/user'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const userFormRef = ref()
const selectedUsers = ref([])

// 搜索表单
const searchForm = reactive({
  username: '',
  realName: '',
  userType: null,
  status: null
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 用户列表
const userList = ref([])

// 用户表单
const userForm = reactive({
  id: null,
  username: '',
  realName: '',
  studentNumber: '',
  gender: null,
  phone: '',
  email: '',
  userType: null,
  college: '',
  major: '',
  className: '',
  status: 1,
  remark: ''
})

// 表单验证规则
const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  studentNumber: [
    { required: true, message: '请输入学号或工号', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  userType: [
    { required: true, message: '请选择用户类型', trigger: 'change' }
  ]
}

// 获取用户类型名称
const getUserTypeName = (type) => {
  const typeMap = {
    1: '管理员',
    2: '宿管员',
    3: '学生'
  }
  return typeMap[type] || '未知'
}

// 获取用户列表
const fetchUserList = async () => {
  try {
    loading.value = true
    const params = {
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    }
    
    // 模拟数据
    const mockData = {
      code: 200,
      data: {
        records: [
          {
            id: 1,
            username: 'admin',
            realName: '系统管理员',
            studentNumber: 'ADMIN001',
            gender: 1,
            phone: '13800138000',
            email: 'admin@mycs.edu.cn',
            userType: 1,
            college: '',
            major: '',
            className: '',
            status: 1,
            createTime: '2024-01-01 10:00:00',
            remark: '系统管理员账号'
          },
          {
            id: 2,
            username: 'dorm001',
            realName: '张宿管',
            studentNumber: 'DORM001',
            gender: 2,
            phone: '13800138001',
            email: 'dorm001@mycs.edu.cn',
            userType: 2,
            college: '',
            major: '',
            className: '',
            status: 1,
            createTime: '2024-01-02 09:00:00',
            remark: 'A栋宿管员'
          },
          {
            id: 3,
            username: '20230001',
            realName: '李小明',
            studentNumber: '20230001',
            gender: 1,
            phone: '13800138002',
            email: '20230001@stu.mycs.edu.cn',
            userType: 3,
            college: '计算机学院',
            major: '软件工程',
            className: '软工2023-1班',
            status: 1,
            createTime: '2024-01-03 08:00:00',
            remark: ''
          }
        ],
        total: 3,
        current: pagination.current,
        size: pagination.size
      }
    }
    
    userList.value = mockData.data.records
    pagination.total = mockData.data.total
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchUserList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    realName: '',
    userType: null,
    status: null
  })
  pagination.current = 1
  fetchUserList()
}

// 新增用户
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  resetUserForm()
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  Object.assign(userForm, row)
  dialogVisible.value = true
}

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除用户 "${row.realName}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      fetchUserList()
    } catch (error) {
      console.error('删除用户失败:', error)
    }
  })
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedUsers.value.length} 个用户吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const promises = selectedUsers.value.map(user => deleteUser(user.id))
      await Promise.all(promises)
      ElMessage.success('批量删除成功')
      fetchUserList()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

// 切换用户状态
const handleToggleStatus = async (row) => {
  try {
    await toggleUserStatus({
      id: row.id,
      status: row.status === 1 ? 0 : 1
    })
    ElMessage.success(`${row.status === 1 ? '禁用' : '启用'}成功`)
    fetchUserList()
  } catch (error) {
    console.error('切换用户状态失败:', error)
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedUsers.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  fetchUserList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  fetchUserList()
}

// 提交表单
const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await updateUser(userForm)
      ElMessage.success('更新成功')
    } else {
      await register({
        ...userForm,
        password: '123456' // 默认密码
      })
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    fetchUserList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleCloseDialog = () => {
  resetUserForm()
  if (userFormRef.value) {
    userFormRef.value.clearValidate()
  }
  dialogVisible.value = false
}

// 重置用户表单
const resetUserForm = () => {
  Object.assign(userForm, {
    id: null,
    username: '',
    realName: '',
    studentNumber: '',
    gender: null,
    phone: '',
    email: '',
    userType: null,
    college: '',
    major: '',
    className: '',
    status: 1,
    remark: ''
  })
}

onMounted(() => {
  fetchUserList()
})
</script>

<style lang="scss" scoped>
.user-management {
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