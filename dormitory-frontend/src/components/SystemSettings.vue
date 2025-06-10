<template>
  <div class="page-content">
    <div class="page-header">
      <h1>系统设置</h1>
      <p>管理系统配置和参数</p>
    </div>
    
    <div class="settings-container">
      <!-- 基本设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h2><i class="fas fa-cog"></i> 基本设置</h2>
        </div>
        
        <div class="settings-grid">
          <div class="setting-item">
            <label>系统名称</label>
            <input v-model="settings.systemName" type="text" placeholder="请输入系统名称">
          </div>
          
          <div class="setting-item">
            <label>学校名称</label>
            <input v-model="settings.schoolName" type="text" placeholder="请输入学校名称">
          </div>
          
          <div class="setting-item">
            <label>管理员邮箱</label>
            <input v-model="settings.adminEmail" type="email" placeholder="请输入管理员邮箱">
          </div>
          
          <div class="setting-item">
            <label>联系电话</label>
            <input v-model="settings.contactPhone" type="tel" placeholder="请输入联系电话">
          </div>
        </div>
      </div>
      
      <!-- 宿舍设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h2><i class="fas fa-building"></i> 宿舍设置</h2>
        </div>
        
        <div class="settings-grid">
          <div class="setting-item">
            <label>默认房间类型</label>
            <select v-model="settings.defaultRoomType">
              <option value="single">单人间</option>
              <option value="double">双人间</option>
              <option value="triple">三人间</option>
              <option value="quad">四人间</option>
              <option value="six">六人间</option>
            </select>
          </div>
          
          <div class="setting-item">
            <label>最大入住人数</label>
            <input v-model.number="settings.maxOccupancy" type="number" min="1" max="10">
          </div>
          
          <div class="setting-item">
            <label>允许性别混住</label>
            <div class="toggle-switch">
              <input 
                id="allowMixedGender" 
                v-model="settings.allowMixedGender" 
                type="checkbox"
              >
              <label for="allowMixedGender" class="toggle-label"></label>
            </div>
          </div>
          
          <div class="setting-item">
            <label>自动分配宿舍</label>
            <div class="toggle-switch">
              <input 
                id="autoAssignment" 
                v-model="settings.autoAssignment" 
                type="checkbox"
              >
              <label for="autoAssignment" class="toggle-label"></label>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 通知设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h2><i class="fas fa-bell"></i> 通知设置</h2>
        </div>
        
        <div class="settings-grid">
          <div class="setting-item">
            <label>邮件通知</label>
            <div class="toggle-switch">
              <input 
                id="emailNotification" 
                v-model="settings.emailNotification" 
                type="checkbox"
              >
              <label for="emailNotification" class="toggle-label"></label>
            </div>
          </div>
          
          <div class="setting-item">
            <label>短信通知</label>
            <div class="toggle-switch">
              <input 
                id="smsNotification" 
                v-model="settings.smsNotification" 
                type="checkbox"
              >
              <label for="smsNotification" class="toggle-label"></label>
            </div>
          </div>
          
          <div class="setting-item">
            <label>系统公告</label>
            <div class="toggle-switch">
              <input 
                id="systemAnnouncement" 
                v-model="settings.systemAnnouncement" 
                type="checkbox"
              >
              <label for="systemAnnouncement" class="toggle-label"></label>
            </div>
          </div>
          
          <div class="setting-item">
            <label>维护提醒</label>
            <div class="toggle-switch">
              <input 
                id="maintenanceReminder" 
                v-model="settings.maintenanceReminder" 
                type="checkbox"
              >
              <label for="maintenanceReminder" class="toggle-label"></label>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 安全设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h2><i class="fas fa-shield-alt"></i> 安全设置</h2>
        </div>
        
        <div class="settings-grid">
          <div class="setting-item">
            <label>密码最小长度</label>
            <input v-model.number="settings.minPasswordLength" type="number" min="6" max="20">
          </div>
          
          <div class="setting-item">
            <label>登录失败锁定次数</label>
            <input v-model.number="settings.maxLoginAttempts" type="number" min="3" max="10">
          </div>
          
          <div class="setting-item">
            <label>会话超时时间（分钟）</label>
            <input v-model.number="settings.sessionTimeout" type="number" min="15" max="480">
          </div>
          
          <div class="setting-item">
            <label>强制密码复杂度</label>
            <div class="toggle-switch">
              <input 
                id="enforcePasswordComplexity" 
                v-model="settings.enforcePasswordComplexity" 
                type="checkbox"
              >
              <label for="enforcePasswordComplexity" class="toggle-label"></label>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 数据备份 -->
      <div class="settings-section">
        <div class="section-header">
          <h2><i class="fas fa-database"></i> 数据备份</h2>
        </div>
        
        <div class="backup-controls">
          <div class="backup-info">
            <div class="info-item">
              <label>最后备份时间</label>
              <span>{{ lastBackupTime || '从未备份' }}</span>
            </div>
            <div class="info-item">
              <label>备份文件大小</label>
              <span>{{ backupSize || '0 MB' }}</span>
            </div>
            <div class="info-item">
              <label>自动备份</label>
              <div class="toggle-switch">
                <input 
                  id="autoBackup" 
                  v-model="settings.autoBackup" 
                  type="checkbox"
                >
                <label for="autoBackup" class="toggle-label"></label>
              </div>
            </div>
            <div class="info-item">
              <label>备份频率</label>
              <select v-model="settings.backupFrequency" :disabled="!settings.autoBackup">
                <option value="daily">每日</option>
                <option value="weekly">每周</option>
                <option value="monthly">每月</option>
              </select>
            </div>
          </div>
          
          <div class="backup-actions">
            <button class="btn btn-outline" @click="createBackup" :disabled="backupLoading">
              <i v-if="backupLoading" class="fas fa-spinner fa-spin"></i>
              <i v-else class="fas fa-download"></i>
              {{ backupLoading ? '备份中...' : '立即备份' }}
            </button>
            
            <button class="btn btn-outline" @click="showRestoreDialog">
              <i class="fas fa-upload"></i>
              恢复备份
            </button>
          </div>
        </div>
      </div>
      
      <!-- 系统信息 -->
      <div class="settings-section">
        <div class="section-header">
          <h2><i class="fas fa-info-circle"></i> 系统信息</h2>
        </div>
        
        <div class="system-info">
          <div class="info-grid">
            <div class="info-item">
              <label>系统版本</label>
              <span>v1.0.0</span>
            </div>
            <div class="info-item">
              <label>数据库版本</label>
              <span>MySQL 8.0</span>
            </div>
            <div class="info-item">
              <label>服务器时间</label>
              <span>{{ currentTime }}</span>
            </div>
            <div class="info-item">
              <label>在线用户数</label>
              <span>{{ onlineUsers }}</span>
            </div>
            <div class="info-item">
              <label>总学生数</label>
              <span>{{ totalStudents }}</span>
            </div>
            <div class="info-item">
              <label>总宿舍数</label>
              <span>{{ totalRooms }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 保存按钮 -->
      <div class="save-section">
        <button class="btn btn-primary" @click="saveSettings" :disabled="saving">
          <i v-if="saving" class="fas fa-spinner fa-spin"></i>
          <i v-else class="fas fa-save"></i>
          {{ saving ? '保存中...' : '保存设置' }}
        </button>
        
        <button class="btn btn-outline" @click="resetSettings">
          <i class="fas fa-undo"></i>
          重置设置
        </button>
      </div>
    </div>
    
    <!-- 恢复备份对话框 -->
    <div v-if="showRestore" class="dialog-overlay" @click="closeRestoreDialog">
      <div class="dialog" @click.stop>
        <div class="dialog-header">
          <h3>恢复备份</h3>
          <button class="btn-close" @click="closeRestoreDialog">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="dialog-content">
          <div class="upload-area" @drop="handleFileDrop" @dragover.prevent @dragenter.prevent>
            <input 
              ref="fileInput" 
              type="file" 
              accept=".sql,.zip" 
              @change="handleFileSelect" 
              style="display: none"
            >
            
            <div class="upload-content">
              <i class="fas fa-cloud-upload-alt"></i>
              <h4>选择备份文件</h4>
              <p>拖拽文件到此处或点击选择文件</p>
              <button class="btn btn-outline" @click="$refs.fileInput.click()">
                选择文件
              </button>
            </div>
            
            <div v-if="selectedFile" class="selected-file">
              <i class="fas fa-file"></i>
              <span>{{ selectedFile.name }}</span>
              <button @click="selectedFile = null">
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>
          
          <div class="dialog-actions">
            <button class="btn btn-outline" @click="closeRestoreDialog">
              取消
            </button>
            <button 
              class="btn btn-primary" 
              @click="restoreBackup" 
              :disabled="!selectedFile || restoreLoading"
            >
              <i v-if="restoreLoading" class="fas fa-spinner fa-spin"></i>
              {{ restoreLoading ? '恢复中...' : '开始恢复' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, onUnmounted } from 'vue'

export default {
  name: 'SystemSettings',
  setup() {
    const saving = ref(false)
    const backupLoading = ref(false)
    const restoreLoading = ref(false)
    const showRestore = ref(false)
    const selectedFile = ref(null)
    const currentTime = ref('')
    const lastBackupTime = ref('')
    const backupSize = ref('')
    const onlineUsers = ref(0)
    const totalStudents = ref(0)
    const totalRooms = ref(0)
    
    const settings = reactive({
      // 基本设置
      systemName: '学生宿舍管理系统',
      schoolName: '某某大学',
      adminEmail: 'admin@university.edu',
      contactPhone: '400-123-4567',
      
      // 宿舍设置
      defaultRoomType: 'quad',
      maxOccupancy: 4,
      allowMixedGender: false,
      autoAssignment: true,
      
      // 通知设置
      emailNotification: true,
      smsNotification: false,
      systemAnnouncement: true,
      maintenanceReminder: true,
      
      // 安全设置
      minPasswordLength: 8,
      maxLoginAttempts: 5,
      sessionTimeout: 120,
      enforcePasswordComplexity: true,
      
      // 备份设置
      autoBackup: true,
      backupFrequency: 'weekly'
    })
    
    let timeInterval = null
    
    const updateTime = () => {
      currentTime.value = new Date().toLocaleString('zh-CN')
    }
    
    const loadSettings = async () => {
      try {
        // 获取系统配置
        const configResponse = await fetch('/api/system-config/grouped')
        if (configResponse.ok) {
          const configData = await configResponse.json()
          if (configData.success) {
            const configs = configData.data
            
            // 基础设置
            if (configs.basic) {
              settings.systemName = configs.basic['system.name'] || settings.systemName
              settings.schoolName = configs.basic['system.school.name'] || settings.schoolName
              settings.adminEmail = configs.basic['system.admin.email'] || settings.adminEmail
              settings.contactPhone = configs.basic['system.contact.phone'] || settings.contactPhone
            }
            
            // 宿舍设置
            if (configs.dormitory) {
              settings.defaultRoomType = configs.dormitory['dorm.default.room.type'] || settings.defaultRoomType
              settings.maxOccupancy = parseInt(configs.dormitory['dorm.max.occupancy']) || settings.maxOccupancy
              settings.allowMixedGender = configs.dormitory['dorm.mixed.gender'] === 'true'
              settings.autoAssignment = configs.dormitory['dorm.auto.assignment'] === 'true'
            }
            
            // 通知设置
            if (configs.notification) {
              settings.emailNotification = configs.notification['notification.email.enabled'] === 'true'
              settings.smsNotification = configs.notification['notification.sms.enabled'] === 'true'
              settings.systemAnnouncement = configs.notification['notification.system.enabled'] === 'true'
              settings.maintenanceReminder = configs.notification['notification.maintenance.enabled'] === 'true'
            }
            
            // 安全设置
            if (configs.security) {
              settings.minPasswordLength = parseInt(configs.security['security.password.min.length']) || settings.minPasswordLength
              settings.maxLoginAttempts = parseInt(configs.security['security.max.login.attempts']) || settings.maxLoginAttempts
              settings.sessionTimeout = parseInt(configs.security['security.session.timeout']) || settings.sessionTimeout
              settings.enforcePasswordComplexity = configs.security['security.password.complexity'] === 'true'
            }
            
            // 备份设置
            if (configs.backup) {
              settings.autoBackup = configs.backup['backup.auto.enabled'] === 'true'
              settings.backupFrequency = configs.backup['backup.frequency'] || settings.backupFrequency
            }
          }
        }
        
        // 获取系统信息
        const infoResponse = await fetch('/api/system-config/system-info')
        if (infoResponse.ok) {
          const infoData = await infoResponse.json()
          if (infoData.success) {
            const info = infoData.data
            lastBackupTime.value = info.lastBackupTime || '未备份'
            backupSize.value = info.backupSize || '0 MB'
            onlineUsers.value = info.onlineUsers || 0
            totalStudents.value = info.totalStudents || 0
            totalRooms.value = info.totalRooms || 0
          }
        }
        
        console.log('设置加载完成')
      } catch (error) {
        console.error('加载设置失败:', error)
        alert('加载设置失败，请检查网络连接！')
      }
    }
    
    const saveSettings = async () => {
      saving.value = true
      try {
        // 构建配置更新数据
        const configUpdates = [
          // 基础设置
          { key: 'system.name', value: settings.systemName, type: 'STRING' },
          { key: 'system.school.name', value: settings.schoolName, type: 'STRING' },
          { key: 'system.admin.email', value: settings.adminEmail, type: 'STRING' },
          { key: 'system.contact.phone', value: settings.contactPhone, type: 'STRING' },
          
          // 宿舍设置
          { key: 'dorm.default.room.type', value: settings.defaultRoomType, type: 'STRING' },
          { key: 'dorm.max.occupancy', value: settings.maxOccupancy.toString(), type: 'NUMBER' },
          { key: 'dorm.mixed.gender', value: settings.allowMixedGender.toString(), type: 'BOOLEAN' },
          { key: 'dorm.auto.assignment', value: settings.autoAssignment.toString(), type: 'BOOLEAN' },
          
          // 通知设置
          { key: 'notification.email.enabled', value: settings.emailNotification.toString(), type: 'BOOLEAN' },
          { key: 'notification.sms.enabled', value: settings.smsNotification.toString(), type: 'BOOLEAN' },
          { key: 'notification.system.enabled', value: settings.systemAnnouncement.toString(), type: 'BOOLEAN' },
          { key: 'notification.maintenance.enabled', value: settings.maintenanceReminder.toString(), type: 'BOOLEAN' },
          
          // 安全设置
          { key: 'security.password.min.length', value: settings.minPasswordLength.toString(), type: 'NUMBER' },
          { key: 'security.max.login.attempts', value: settings.maxLoginAttempts.toString(), type: 'NUMBER' },
          { key: 'security.session.timeout', value: settings.sessionTimeout.toString(), type: 'NUMBER' },
          { key: 'security.password.complexity', value: settings.enforcePasswordComplexity.toString(), type: 'BOOLEAN' },
          
          // 备份设置
          { key: 'backup.auto.enabled', value: settings.autoBackup.toString(), type: 'BOOLEAN' },
          { key: 'backup.frequency', value: settings.backupFrequency, type: 'STRING' }
        ]
        
        // 批量更新配置
        const response = await fetch('/api/system-config/batch', {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(configUpdates)
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            alert('设置保存成功！')
          } else {
            throw new Error(result.message || '保存失败')
          }
        } else {
          throw new Error('网络请求失败')
        }
        
        console.log('保存设置:', settings)
      } catch (error) {
        console.error('保存设置失败:', error)
        alert('保存设置失败：' + error.message)
      } finally {
        saving.value = false
      }
    }
    
    const resetSettings = async () => {
      if (confirm('确定要重置所有设置吗？此操作不可撤销。')) {
        try {
          const response = await fetch('/api/system-config/reset', {
            method: 'POST'
          })
          
          if (response.ok) {
            const result = await response.json()
            if (result.success) {
              alert('设置已重置为默认值！')
              // 重新加载设置
              await loadSettings()
            } else {
              throw new Error(result.message || '重置失败')
            }
          } else {
            throw new Error('网络请求失败')
          }
        } catch (error) {
          console.error('重置设置失败:', error)
          alert('重置设置失败：' + error.message)
        }
      }
    }
    
    const createBackup = async () => {
      backupLoading.value = true
      try {
        const response = await fetch('/api/system-config/backup', {
          method: 'POST'
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            lastBackupTime.value = new Date().toLocaleString('zh-CN')
            alert('备份创建成功！')
            // 重新获取系统信息
            const infoResponse = await fetch('/api/system-config/system-info')
            if (infoResponse.ok) {
              const infoData = await infoResponse.json()
              if (infoData.success) {
                backupSize.value = infoData.data.backupSize || backupSize.value
              }
            }
          } else {
            throw new Error(result.message || '备份失败')
          }
        } else {
          throw new Error('网络请求失败')
        }
      } catch (error) {
        console.error('创建备份失败:', error)
        alert('创建备份失败：' + error.message)
      } finally {
        backupLoading.value = false
      }
    }
    
    const showRestoreDialog = () => {
      showRestore.value = true
    }
    
    const closeRestoreDialog = () => {
      showRestore.value = false
      selectedFile.value = null
    }
    
    const handleFileSelect = (event) => {
      const file = event.target.files[0]
      if (file) {
        selectedFile.value = file
      }
    }
    
    const handleFileDrop = (event) => {
      event.preventDefault()
      const file = event.dataTransfer.files[0]
      if (file) {
        selectedFile.value = file
      }
    }
    
    const restoreBackup = async () => {
      if (!selectedFile.value) return
      
      restoreLoading.value = true
      try {
        const formData = new FormData()
        formData.append('file', selectedFile.value)
        
        const response = await fetch('/api/system-config/restore', {
          method: 'POST',
          body: formData
        })
        
        if (response.ok) {
          const result = await response.json()
          if (result.success) {
            alert('备份恢复成功！系统将重新加载。')
            closeRestoreDialog()
            await loadSettings()
          } else {
            throw new Error(result.message || '恢复失败')
          }
        } else {
          throw new Error('网络请求失败')
        }
      } catch (error) {
        console.error('恢复备份失败:', error)
        alert('恢复备份失败：' + error.message)
      } finally {
        restoreLoading.value = false
      }
    }
    
    onMounted(() => {
      loadSettings()
      updateTime()
      timeInterval = setInterval(updateTime, 1000)
    })
    
    onUnmounted(() => {
      if (timeInterval) {
        clearInterval(timeInterval)
      }
    })
    
    return {
      saving,
      backupLoading,
      restoreLoading,
      showRestore,
      selectedFile,
      currentTime,
      lastBackupTime,
      backupSize,
      onlineUsers,
      totalStudents,
      totalRooms,
      settings,
      saveSettings,
      resetSettings,
      createBackup,
      showRestoreDialog,
      closeRestoreDialog,
      handleFileSelect,
      handleFileDrop,
      restoreBackup
    }
  }
}
</script>

<style scoped>
.settings-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.settings-section {
  background: var(--surface-color);
  border-radius: 0.75rem;
  box-shadow: var(--shadow);
  overflow: hidden;
}

.section-header {
  padding: 1.5rem;
  background-color: var(--background-color);
  border-bottom: 1px solid var(--border-color);
}

.section-header h2 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.section-header i {
  color: var(--primary-color);
}

.settings-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  padding: 1.5rem;
}

.setting-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.setting-item label {
  font-weight: 500;
  color: var(--text-primary);
  font-size: 0.875rem;
}

.setting-item input,
.setting-item select {
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.setting-item input:focus,
.setting-item select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* 切换开关 */
.toggle-switch {
  position: relative;
  display: inline-block;
}

.toggle-switch input[type="checkbox"] {
  display: none;
}

.toggle-label {
  display: block;
  width: 48px;
  height: 24px;
  background-color: #ccc;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
  position: relative;
}

.toggle-label::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 2px;
  width: 20px;
  height: 20px;
  background-color: white;
  border-radius: 50%;
  transition: transform 0.3s;
}

.toggle-switch input[type="checkbox"]:checked + .toggle-label {
  background-color: var(--primary-color);
}

.toggle-switch input[type="checkbox"]:checked + .toggle-label::after {
  transform: translateX(24px);
}

/* 备份控制 */
.backup-controls {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.backup-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.backup-actions {
  display: flex;
  gap: 1rem;
}

/* 系统信息 */
.system-info {
  padding: 1.5rem;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.info-item label {
  font-weight: 500;
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.info-item span {
  color: var(--text-primary);
  font-weight: 500;
}

/* 保存区域 */
.save-section {
  display: flex;
  justify-content: center;
  gap: 1rem;
  padding: 2rem 0;
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

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

/* 上传区域 */
.upload-area {
  border: 2px dashed var(--border-color);
  border-radius: 0.5rem;
  padding: 2rem;
  text-align: center;
  transition: border-color 0.3s;
  position: relative;
}

.upload-area:hover {
  border-color: var(--primary-color);
}

.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.upload-content i {
  font-size: 3rem;
  color: var(--primary-color);
}

.upload-content h4 {
  margin: 0;
  font-size: 1.125rem;
  font-weight: 600;
}

.upload-content p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.selected-file {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem;
  background-color: var(--background-color);
  border-radius: 0.5rem;
  margin-top: 1rem;
}

.selected-file i {
  color: var(--primary-color);
}

.selected-file button {
  margin-left: auto;
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 0.25rem;
}

.selected-file button:hover {
  background-color: var(--border-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .settings-grid {
    grid-template-columns: 1fr;
  }
  
  .backup-info {
    grid-template-columns: 1fr;
  }
  
  .backup-actions {
    flex-direction: column;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .save-section {
    flex-direction: column;
    align-items: center;
  }
}
</style>