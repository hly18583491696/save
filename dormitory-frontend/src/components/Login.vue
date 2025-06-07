<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-left">
        <div class="icon">
          <i class="fas fa-building"></i>
        </div>
        <h1>学生宿舍管理系统</h1>
        <p>为学生提供便捷的宿舍管理服务，让校园生活更加美好。</p>
      </div>
      
      <div class="login-right">
        <form class="login-form" @submit.prevent="handleLogin">
          <h2>欢迎登录</h2>
          
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              id="username"
              v-model="loginForm.username"
              type="text" 
              placeholder="请输入用户名"
              required
            >
            <i class="fas fa-user input-icon"></i>
          </div>
          
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              id="password"
              v-model="loginForm.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入密码"
              required
            >
            <i class="fas fa-lock input-icon"></i>
            <i 
              :class="['fas', showPassword ? 'fa-eye-slash' : 'fa-eye', 'toggle-password']"
              @click="togglePassword"
            ></i>
          </div>
          
          <div class="role-selection">
            <label>选择角色</label>
            <div class="role-options">
              <div 
                v-for="role in roles" 
                :key="role.value"
                :class="['role-option', { active: loginForm.role === role.value }]"
                @click="selectRole(role.value)"
              >
                <i :class="role.icon"></i>
                <span>{{ role.label }}</span>
              </div>
            </div>
          </div>
          
          <div class="form-options">
            <label class="remember-me">
              <input v-model="loginForm.rememberMe" type="checkbox">
              <span class="checkmark"></span>
              记住我
            </label>
            <a href="#" class="forgot-password">忘记密码？</a>
          </div>
          
          <button 
            type="submit" 
            class="login-btn"
            :disabled="loading"
          >
            <span v-if="!loading">登录</span>
            <div v-else class="loading"></div>
          </button>
          
          <div class="register-link">
            <span>还没有账户？</span>
            <a href="#" @click.prevent="showRegister">立即注册</a>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Login',
  emits: ['login-success'],
  setup(props, { emit }) {
    const router = useRouter()
    const loading = ref(false)
    const showPassword = ref(false)
    
    const loginForm = reactive({
      username: '',
      password: '',
      role: 'admin',
      rememberMe: false
    })
    
    const roles = reactive([
      { value: 'admin', label: '管理员', icon: 'fas fa-user-shield' },
      { value: 'teacher', label: '教师', icon: 'fas fa-chalkboard-teacher' }
    ])
    
    const togglePassword = () => {
      showPassword.value = !showPassword.value
    }
    
    const selectRole = (role) => {
      loginForm.role = role
    }
    
    const handleLogin = async () => {
      if (!loginForm.username || !loginForm.password) {
        alert('请填写完整的登录信息')
        return
      }
      
      loading.value = true
      
      try {
        // 调用后端登录API
        const response = await fetch('http://localhost:8082/api/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            username: loginForm.username,
            password: loginForm.password,
            role: loginForm.role
          })
        })
        
        const result = await response.json()
        
        if (response.ok && result.code === 200) {
          // 登录成功
          const { token, user } = result.data
          
          const userInfo = {
            id: user.id,
            name: user.username,
            role: user.role,
            avatar: user.avatar || ''
          }
          
          // 保存登录信息
          if (loginForm.rememberMe) {
            localStorage.setItem('userToken', token)
            localStorage.setItem('userInfo', JSON.stringify(userInfo))
          } else {
            sessionStorage.setItem('userToken', token)
            sessionStorage.setItem('userInfo', JSON.stringify(userInfo))
          }
          
          // 跳转到仪表板页面
          router.push('/dashboard')
          
        } else {
          // 登录失败
          alert(result.message || '登录失败，请检查用户名和密码')
        }
        
      } catch (error) {
        console.error('登录失败:', error)
        alert('网络错误，请检查网络连接或后端服务是否启动')
      } finally {
        loading.value = false
      }
    }
    
    const showRegister = () => {
      alert('注册功能正在开发中...')
    }
    
    return {
      loading,
      showPassword,
      loginForm,
      roles,
      togglePassword,
      selectRole,
      handleLogin,
      showRegister
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 100%;
  max-width: 900px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  min-height: 500px;
}

.login-left {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  padding: 40px;
  text-align: center;
}

.login-left h1 {
  font-size: 2.5rem;
  margin-bottom: 20px;
  font-weight: 300;
}

.login-left p {
  font-size: 1.1rem;
  opacity: 0.9;
  line-height: 1.6;
}

.login-left .icon {
  font-size: 4rem;
  margin-bottom: 30px;
  opacity: 0.8;
}

.login-right {
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-form h2 {
  color: #333;
  margin-bottom: 30px;
  font-size: 2rem;
  font-weight: 300;
}

.form-group {
  margin-bottom: 25px;
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 15px 20px;
  border: 2px solid #e1e5e9;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #f8f9fa;
  padding-left: 50px;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  background: white;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.input-icon {
  position: absolute;
  left: 18px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  cursor: pointer;
}

.toggle-password {
  position: absolute;
  right: 18px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  cursor: pointer;
}

.toggle-password:hover {
  color: #667eea;
}

.role-selection {
  margin-bottom: 25px;
}

.role-selection label {
  display: block;
  margin-bottom: 12px;
  color: #555;
  font-weight: 500;
}

.role-options {
  display: flex;
  gap: 10px;
}

.role-option {
  flex: 1;
  padding: 12px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f8f9fa;
}

.role-option:hover {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.05);
}

.role-option.active {
  border-color: #667eea;
  background: #667eea;
  color: white;
}

.role-option i {
  display: block;
  font-size: 1.2rem;
  margin-bottom: 5px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.remember-me {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #666;
}

.remember-me input {
  display: none;
}

.checkmark {
  width: 18px;
  height: 18px;
  border: 2px solid #ddd;
  border-radius: 3px;
  margin-right: 8px;
  position: relative;
  transition: all 0.3s ease;
}

.remember-me input:checked + .checkmark {
  background: #667eea;
  border-color: #667eea;
}

.remember-me input:checked + .checkmark::after {
  content: '✓';
  position: absolute;
  top: -2px;
  left: 2px;
  color: white;
  font-size: 12px;
}

.forgot-password {
  color: #667eea;
  text-decoration: none;
  font-size: 0.9rem;
}

.forgot-password:hover {
  text-decoration: underline;
}

.login-btn {
  width: 100%;
  padding: 15px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.loading {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.register-link a:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    grid-template-columns: 1fr;
    max-width: 400px;
  }
  
  .login-left {
    padding: 30px 20px;
  }
  
  .login-left h1 {
    font-size: 2rem;
  }
  
  .login-right {
    padding: 40px 30px;
  }
  
  .role-options {
    flex-direction: column;
  }
  
  .form-options {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
}
</style>