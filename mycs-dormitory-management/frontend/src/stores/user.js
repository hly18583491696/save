import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getUserInfo } from '@/api/user'
import { removeToken, getToken, setToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const userInfo = ref(null)
  
  // 登录
  const loginAction = async (loginForm) => {
    try {
      const response = await login(loginForm)
      if (response.code === 200) {
        token.value = response.data
        setToken(response.data)
        return Promise.resolve(response)
      } else {
        return Promise.reject(new Error(response.message))
      }
    } catch (error) {
      return Promise.reject(error)
    }
  }
  
  // 获取用户信息
  const getUserInfoAction = async () => {
    try {
      // 从token中解析用户ID（这里简化处理，实际应该调用后端接口）
      // const response = await getUserInfo()
      // userInfo.value = response.data
      
      // 临时模拟用户信息
      userInfo.value = {
        id: 1,
        username: 'admin',
        realName: '系统管理员',
        userType: 1,
        avatar: null
      }
      
      return Promise.resolve(userInfo.value)
    } catch (error) {
      return Promise.reject(error)
    }
  }
  
  // 登出
  const logout = () => {
    token.value = null
    userInfo.value = null
    removeToken()
  }
  
  // 检查登录状态
  const checkLoginStatus = () => {
    const savedToken = getToken()
    if (savedToken) {
      token.value = savedToken
    }
  }
  
  return {
    token,
    userInfo,
    loginAction,
    getUserInfo: getUserInfoAction,
    logout,
    checkLoginStatus
  }
})