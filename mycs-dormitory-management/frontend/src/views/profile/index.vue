<template>
  <div class="profile-management">
    <el-row :gutter="20">
      <!-- 个人信息卡片 -->
      <el-col :span="8">
        <el-card class="profile-card">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          
          <div class="profile-info">
            <div class="avatar-section">
              <el-avatar :size="80" :src="userInfo.avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <el-button type="text" @click="handleAvatarUpload" class="upload-btn">
                更换头像
              </el-button>
            </div>
            
            <div class="info-section">
              <div class="info-item">
                <label>用户名：</label>
                <span>{{ userInfo.username }}</span>
              </div>
              <div class="info-item">
                <label>真实姓名：</label>
                <span>{{ userInfo.realName }}</span>
              </div>
              <div class="info-item">
                <label>用户类型：</label>
                <el-tag :type="getUserTypeColor(userInfo.userType)" size="small">
                  {{ getUserTypeName(userInfo.userType) }}
                </el-tag>
              </div>
              <div class="info-item" v-if="userInfo.userType === 'STUDENT'">
                <label>学号：</label>
                <span>{{ userInfo.studentNumber }}</span>
              </div>
              <div class="info-item" v-if="userInfo.userType !== 'STUDENT'">
                <label>工号：</label>
                <span>{{ userInfo.employeeNumber }}</span>
              </div>
              <div class="info-item">
                <label>性别：</label>
                <span>{{ userInfo.gender }}</span>
              </div>
              <div class="info-item">
                <label>手机号：</label>
                <span>{{ userInfo.phone }}</span>
              </div>
              <div class="info-item">
                <label>邮箱：</label>
                <span>{{ userInfo.email }}</span>
              </div>
              <div class="info-item" v-if="userInfo.userType === 'STUDENT'">
                <label>学院：</label>
                <span>{{ userInfo.college }}</span>
              </div>
              <div class="info-item" v-if="userInfo.userType === 'STUDENT'">
                <label>专业：</label>
                <span>{{ userInfo.major }}</span>
              </div>
              <div class="info-item" v-if="userInfo.userType === 'STUDENT'">
                <label>班级：</label>
                <span>{{ userInfo.className }}</span>
              </div>
              <div class="info-item">
                <label>注册时间：</label>
                <span>{{ userInfo.createTime }}</span>
              </div>
            </div>
            
            <div class="action-section">
              <el-button type="primary" @click="handleEditProfile">
                <el-icon><Edit /></el-icon>
                编辑资料
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 住宿信息卡片（仅学生显示） -->
      <el-col :span="8" v-if="userInfo.userType === 'STUDENT'">
        <el-card class="accommodation-card">
          <template #header>
            <div class="card-header">
              <span>住宿信息</span>
            </div>
          </template>
          
          <div class="accommodation-info" v-if="accommodationInfo">
            <div class="info-item">
              <label>楼栋：</label>
              <span>{{ accommodationInfo.buildingName }}</span>
            </div>
            <div class="info-item">
              <label>房间号：</label>
              <span>{{ accommodationInfo.roomNumber }}</span>
            </div>
            <div class="info-item">
              <label>床位号：</label>
              <el-tag type="info" size="small">
                床位{{ accommodationInfo.bedNumber }}
              </el-tag>
            </div>
            <div class="info-item">
              <label>入住时间：</label>
              <span>{{ accommodationInfo.checkInTime }}</span>
            </div>
            <div class="info-item">
              <label>住宿状态：</label>
              <el-tag :type="getAccommodationStatusType(accommodationInfo.status)" size="small">
                {{ accommodationInfo.status }}
              </el-tag>
            </div>
            
            <!-- 室友信息 -->
            <div class="roommates-section" v-if="roommates.length">
              <h4>室友信息</h4>
              <div class="roommate-list">
                <div v-for="roommate in roommates" :key="roommate.id" class="roommate-item">
                  <el-avatar :size="30" :src="roommate.avatar">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <div class="roommate-info">
                    <div class="name">{{ roommate.realName }}</div>
                    <div class="bed">床位{{ roommate.bedNumber }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="no-accommodation" v-else>
            <el-empty description="暂无住宿信息" :image-size="80" />
          </div>
        </el-card>
      </el-col>
      
      <!-- 管理信息卡片（仅宿管员显示） -->
      <el-col :span="8" v-if="userInfo.userType === 'DORMITORY_MANAGER'">
        <el-card class="management-card">
          <template #header>
            <div class="card-header">
              <span>管理信息</span>
            </div>
          </template>
          
          <div class="management-info" v-if="managementInfo">
            <div class="info-item">
              <label>管理楼栋：</label>
              <span>{{ managementInfo.buildingName }}</span>
            </div>
            <div class="info-item">
              <label>楼栋类型：</label>
              <span>{{ managementInfo.buildingType }}</span>
            </div>
            <div class="info-item">
              <label>总楼层：</label>
              <span>{{ managementInfo.totalFloors }}层</span>
            </div>
            <div class="info-item">
              <label>总房间数：</label>
              <span>{{ managementInfo.totalRooms }}间</span>
            </div>
            <div class="info-item">
              <label>入住率：</label>
              <el-progress 
                :percentage="managementInfo.occupancyRate" 
                :color="getOccupancyColor(managementInfo.occupancyRate)"
              />
            </div>
            <div class="info-item">
              <label>管理开始时间：</label>
              <span>{{ managementInfo.startTime }}</span>
            </div>
          </div>
          
          <div class="no-management" v-else>
            <el-empty description="暂无管理信息" :image-size="80" />
          </div>
        </el-card>
      </el-col>
      
      <!-- 统计信息卡片 -->
      <el-col :span="8">
        <el-card class="statistics-card">
          <template #header>
            <div class="card-header">
              <span>统计信息</span>
            </div>
          </template>
          
          <div class="statistics-info">
            <div class="stat-item" v-if="userInfo.userType === 'ADMIN'">
              <div class="stat-number">{{ statistics.totalUsers }}</div>
              <div class="stat-label">总用户数</div>
            </div>
            <div class="stat-item" v-if="userInfo.userType === 'ADMIN'">
              <div class="stat-number">{{ statistics.totalBuildings }}</div>
              <div class="stat-label">楼栋数量</div>
            </div>
            <div class="stat-item" v-if="userInfo.userType === 'ADMIN'">
              <div class="stat-number">{{ statistics.totalRooms }}</div>
              <div class="stat-label">房间数量</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ statistics.loginCount }}</div>
              <div class="stat-label">登录次数</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ statistics.lastLoginDays }}</div>
              <div class="stat-label">天前最后登录</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 编辑资料对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑个人资料"
      width="600px"
      :before-close="handleCloseEditDialog"
    >
      <el-form
        ref="profileFormRef"
        :model="profileForm"
        :rules="profileRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="profileForm.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="profileForm.gender" placeholder="请选择性别">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" v-if="userInfo.userType === 'STUDENT'">
          <el-col :span="12">
            <el-form-item label="学院" prop="college">
              <el-input v-model="profileForm.college" placeholder="请输入学院" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="profileForm.major" placeholder="请输入专业" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="班级" prop="className" v-if="userInfo.userType === 'STUDENT'">
          <el-input v-model="profileForm.className" placeholder="请输入班级" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateProfile" :loading="updateLoading">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 头像上传对话框 -->
    <el-dialog
      v-model="avatarDialogVisible"
      title="更换头像"
      width="400px"
    >
      <div class="avatar-upload">
        <el-upload
          class="avatar-uploader"
          action="#"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload"
          :http-request="handleAvatarChange"
        >
          <img v-if="newAvatar" :src="newAvatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <div class="upload-tips">
          <p>支持 JPG、PNG 格式</p>
          <p>文件大小不超过 2MB</p>
          <p>建议尺寸：200x200 像素</p>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="avatarDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveAvatar" :loading="avatarLoading">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 响应式数据
const updateLoading = ref(false)
const avatarLoading = ref(false)
const editDialogVisible = ref(false)
const avatarDialogVisible = ref(false)
const profileFormRef = ref()
const newAvatar = ref('')

// 用户信息
const userInfo = ref({
  id: 1,
  username: 'admin',
  realName: '系统管理员',
  userType: 'ADMIN',
  studentNumber: '',
  employeeNumber: 'EMP001',
  gender: '男',
  phone: '13800138000',
  email: 'admin@example.com',
  college: '',
  major: '',
  className: '',
  avatar: '',
  createTime: '2024-01-01 10:00:00'
})

// 住宿信息（学生）
const accommodationInfo = ref(null)

// 室友信息
const roommates = ref([])

// 管理信息（宿管员）
const managementInfo = ref(null)

// 统计信息
const statistics = ref({
  totalUsers: 1250,
  totalBuildings: 12,
  totalRooms: 480,
  loginCount: 156,
  lastLoginDays: 0
})

// 编辑表单
const profileForm = reactive({
  realName: '',
  gender: '',
  phone: '',
  email: '',
  college: '',
  major: '',
  className: ''
})

// 表单验证规则
const profileRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
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
  ]
}

// 获取用户类型名称
const getUserTypeName = (type) => {
  const typeMap = {
    'ADMIN': '系统管理员',
    'DORMITORY_MANAGER': '宿管员',
    'STUDENT': '学生'
  }
  return typeMap[type] || type
}

// 获取用户类型颜色
const getUserTypeColor = (type) => {
  const colorMap = {
    'ADMIN': 'danger',
    'DORMITORY_MANAGER': 'warning',
    'STUDENT': 'primary'
  }
  return colorMap[type] || 'info'
}

// 获取住宿状态类型
const getAccommodationStatusType = (status) => {
  const statusMap = {
    '在住': 'success',
    '已退宿': 'info',
    '暂离': 'warning'
  }
  return statusMap[status] || 'info'
}

// 获取入住率颜色
const getOccupancyColor = (rate) => {
  if (rate >= 90) return '#f56c6c'
  if (rate >= 70) return '#e6a23c'
  return '#67c23a'
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    // 模拟数据，实际应该从 userStore 获取
    const mockUserInfo = {
      id: 1,
      username: 'student001',
      realName: '张三',
      userType: 'STUDENT',
      studentNumber: '2021001',
      employeeNumber: '',
      gender: '男',
      phone: '13800138001',
      email: 'zhangsan@example.com',
      college: '计算机学院',
      major: '软件工程',
      className: '软工2101',
      avatar: '',
      createTime: '2024-09-01 10:00:00'
    }
    
    userInfo.value = mockUserInfo
    
    // 如果是学生，获取住宿信息
    if (mockUserInfo.userType === 'STUDENT') {
      await fetchAccommodationInfo()
    }
    
    // 如果是宿管员，获取管理信息
    if (mockUserInfo.userType === 'DORMITORY_MANAGER') {
      await fetchManagementInfo()
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 获取住宿信息
const fetchAccommodationInfo = async () => {
  try {
    // 模拟数据
    const mockAccommodationInfo = {
      buildingName: 'A栋学生宿舍',
      roomNumber: '101',
      bedNumber: 1,
      checkInTime: '2024-09-01 10:00:00',
      status: '在住'
    }
    
    accommodationInfo.value = mockAccommodationInfo
    
    // 获取室友信息
    const mockRoommates = [
      {
        id: 2,
        realName: '李四',
        bedNumber: 2,
        avatar: ''
      },
      {
        id: 3,
        realName: '王五',
        bedNumber: 3,
        avatar: ''
      }
    ]
    
    roommates.value = mockRoommates
  } catch (error) {
    console.error('获取住宿信息失败:', error)
  }
}

// 获取管理信息
const fetchManagementInfo = async () => {
  try {
    // 模拟数据
    const mockManagementInfo = {
      buildingName: 'A栋学生宿舍',
      buildingType: '男生宿舍',
      totalFloors: 6,
      totalRooms: 120,
      occupancyRate: 85,
      startTime: '2024-01-01 10:00:00'
    }
    
    managementInfo.value = mockManagementInfo
  } catch (error) {
    console.error('获取管理信息失败:', error)
  }
}

// 编辑资料
const handleEditProfile = () => {
  Object.assign(profileForm, {
    realName: userInfo.value.realName,
    gender: userInfo.value.gender,
    phone: userInfo.value.phone,
    email: userInfo.value.email,
    college: userInfo.value.college,
    major: userInfo.value.major,
    className: userInfo.value.className
  })
  editDialogVisible.value = true
}

// 更新资料
const handleUpdateProfile = async () => {
  if (!profileFormRef.value) return
  
  try {
    await profileFormRef.value.validate()
    updateLoading.value = true
    
    // 更新用户信息
    Object.assign(userInfo.value, profileForm)
    
    ElMessage.success('更新成功')
    editDialogVisible.value = false
  } catch (error) {
    console.error('更新失败:', error)
  } finally {
    updateLoading.value = false
  }
}

// 关闭编辑对话框
const handleCloseEditDialog = () => {
  if (profileFormRef.value) {
    profileFormRef.value.clearValidate()
  }
  editDialogVisible.value = false
}

// 上传头像
const handleAvatarUpload = () => {
  newAvatar.value = userInfo.value.avatar
  avatarDialogVisible.value = true
}

// 头像上传前验证
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2
  
  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 处理头像变化
const handleAvatarChange = (options) => {
  const file = options.file
  const reader = new FileReader()
  reader.onload = (e) => {
    newAvatar.value = e.target.result
  }
  reader.readAsDataURL(file)
}

// 保存头像
const handleSaveAvatar = async () => {
  try {
    avatarLoading.value = true
    
    // 这里应该上传到服务器
    userInfo.value.avatar = newAvatar.value
    
    ElMessage.success('头像更新成功')
    avatarDialogVisible.value = false
  } catch (error) {
    console.error('头像更新失败:', error)
    ElMessage.error('头像更新失败')
  } finally {
    avatarLoading.value = false
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style lang="scss" scoped>
.profile-management {
  .profile-card,
  .accommodation-card,
  .management-card,
  .statistics-card {
    height: 100%;
    
    .card-header {
      font-weight: bold;
      font-size: 16px;
    }
  }
  
  .profile-info {
    .avatar-section {
      text-align: center;
      margin-bottom: 20px;
      
      .upload-btn {
        display: block;
        margin: 10px auto 0;
        padding: 0;
        font-size: 12px;
      }
    }
    
    .info-section {
      .info-item {
        display: flex;
        align-items: center;
        margin-bottom: 12px;
        
        label {
          width: 80px;
          color: #666;
          font-size: 14px;
        }
        
        span {
          flex: 1;
          font-size: 14px;
        }
      }
    }
    
    .action-section {
      text-align: center;
      margin-top: 20px;
    }
  }
  
  .accommodation-info {
    .info-item {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      
      label {
        width: 80px;
        color: #666;
        font-size: 14px;
      }
      
      span {
        flex: 1;
        font-size: 14px;
      }
    }
    
    .roommates-section {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 10px 0;
        font-size: 14px;
        color: #333;
      }
      
      .roommate-list {
        .roommate-item {
          display: flex;
          align-items: center;
          margin-bottom: 8px;
          
          .roommate-info {
            margin-left: 10px;
            
            .name {
              font-size: 13px;
              color: #333;
            }
            
            .bed {
              font-size: 12px;
              color: #666;
            }
          }
        }
      }
    }
  }
  
  .management-info {
    .info-item {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      
      label {
        width: 80px;
        color: #666;
        font-size: 14px;
      }
      
      span {
        flex: 1;
        font-size: 14px;
      }
      
      .el-progress {
        flex: 1;
      }
    }
  }
  
  .statistics-info {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    
    .stat-item {
      text-align: center;
      flex: 1;
      min-width: 80px;
      
      .stat-number {
        font-size: 24px;
        font-weight: bold;
        color: #409eff;
        margin-bottom: 5px;
      }
      
      .stat-label {
        font-size: 12px;
        color: #666;
      }
    }
  }
  
  .avatar-upload {
    text-align: center;
    
    .avatar-uploader {
      :deep(.el-upload) {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: 0.2s;
        
        &:hover {
          border-color: #409eff;
        }
      }
    }
    
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }
    
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }
    
    .upload-tips {
      margin-top: 10px;
      
      p {
        margin: 5px 0;
        font-size: 12px;
        color: #666;
      }
    }
  }
}
</style>