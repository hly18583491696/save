<template>
  <div class="error-page">
    <div class="error-container">
      <div class="error-content">
        <!-- 404图标 -->
        <div class="error-icon">
          <svg viewBox="0 0 1024 1024" width="200" height="200">
            <path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#f5f5f5"/>
            <path d="M623.6 316.7C593.6 290.4 554 276 512 276s-81.6 14.4-111.6 40.7C369.2 344 352 380.7 352 420v7.6c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V420c0-44.1 43.1-80 96-80s96 35.9 96 80c0 31.1-22 59.6-56.1 72.7-21.2 8.1-39.2 22.3-52.1 40.9-13.1 19-19.8 41.3-19.8 64.9V620c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8v-22.7a48.3 48.3 0 0 1 30.9-44.8c59-22.7 97.1-74.7 97.1-132.5 0-39.3-17.2-76-48.4-103.3z" fill="#ccc"/>
            <circle cx="512" cy="708" r="48" fill="#ccc"/>
          </svg>
        </div>
        
        <!-- 错误信息 -->
        <div class="error-info">
          <h1 class="error-title">404</h1>
          <h2 class="error-subtitle">页面不存在</h2>
          <p class="error-description">
            抱歉，您访问的页面不存在或已被删除。
          </p>
        </div>
        
        <!-- 操作按钮 -->
        <div class="error-actions">
          <el-button type="primary" @click="goHome">
            <el-icon><HomeFilled /></el-icon>
            返回首页
          </el-button>
          <el-button @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            返回上页
          </el-button>
        </div>
        
        <!-- 建议链接 -->
        <div class="error-suggestions">
          <h3>您可能想要访问：</h3>
          <ul class="suggestion-list">
            <li>
              <router-link to="/dashboard">
                <el-icon><DataBoard /></el-icon>
                仪表盘
              </router-link>
            </li>
            <li>
              <router-link to="/user">
                <el-icon><User /></el-icon>
                用户管理
              </router-link>
            </li>
            <li>
              <router-link to="/building">
                <el-icon><OfficeBuilding /></el-icon>
                楼栋管理
              </router-link>
            </li>
            <li>
              <router-link to="/room">
                <el-icon><House /></el-icon>
                房间管理
              </router-link>
            </li>
            <li>
              <router-link to="/accommodation">
                <el-icon><Bed /></el-icon>
                住宿管理
              </router-link>
            </li>
          </ul>
        </div>
      </div>
      
      <!-- 装饰元素 -->
      <div class="decoration">
        <div class="floating-element element-1"></div>
        <div class="floating-element element-2"></div>
        <div class="floating-element element-3"></div>
        <div class="floating-element element-4"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 返回首页
const goHome = () => {
  router.push('/dashboard')
}

// 返回上一页
const goBack = () => {
  if (window.history.length > 1) {
    router.go(-1)
  } else {
    router.push('/dashboard')
  }
}
</script>

<style lang="scss" scoped>
.error-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.error-container {
  position: relative;
  z-index: 2;
  text-align: center;
  max-width: 600px;
  padding: 40px 20px;
}

.error-content {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 60px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.error-icon {
  margin-bottom: 30px;
  
  svg {
    filter: drop-shadow(0 10px 20px rgba(0, 0, 0, 0.1));
    animation: float 3s ease-in-out infinite;
  }
}

.error-info {
  margin-bottom: 40px;
  
  .error-title {
    font-size: 120px;
    font-weight: bold;
    color: #667eea;
    margin: 0;
    line-height: 1;
    text-shadow: 0 5px 10px rgba(102, 126, 234, 0.3);
  }
  
  .error-subtitle {
    font-size: 32px;
    color: #333;
    margin: 20px 0 10px;
    font-weight: 600;
  }
  
  .error-description {
    font-size: 16px;
    color: #666;
    margin: 0;
    line-height: 1.6;
  }
}

.error-actions {
  margin-bottom: 40px;
  
  .el-button {
    margin: 0 10px;
    padding: 12px 24px;
    font-size: 16px;
    border-radius: 25px;
    
    &.el-button--primary {
      background: linear-gradient(45deg, #667eea, #764ba2);
      border: none;
      
      &:hover {
        background: linear-gradient(45deg, #5a6fd8, #6a4190);
        transform: translateY(-2px);
        box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
      }
    }
    
    &:not(.el-button--primary) {
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      }
    }
  }
}

.error-suggestions {
  h3 {
    font-size: 18px;
    color: #333;
    margin-bottom: 20px;
  }
  
  .suggestion-list {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 15px;
    
    li {
      a {
        display: flex;
        align-items: center;
        padding: 8px 16px;
        background: #f8f9fa;
        border-radius: 20px;
        text-decoration: none;
        color: #666;
        font-size: 14px;
        transition: all 0.3s ease;
        
        .el-icon {
          margin-right: 6px;
          font-size: 16px;
        }
        
        &:hover {
          background: #667eea;
          color: white;
          transform: translateY(-2px);
          box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
        }
      }
    }
  }
}

.decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.floating-element {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
  
  &.element-1 {
    width: 80px;
    height: 80px;
    top: 10%;
    left: 10%;
    animation-delay: 0s;
  }
  
  &.element-2 {
    width: 120px;
    height: 120px;
    top: 20%;
    right: 15%;
    animation-delay: 1s;
  }
  
  &.element-3 {
    width: 60px;
    height: 60px;
    bottom: 20%;
    left: 20%;
    animation-delay: 2s;
  }
  
  &.element-4 {
    width: 100px;
    height: 100px;
    bottom: 10%;
    right: 10%;
    animation-delay: 3s;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .error-container {
    max-width: 90%;
    padding: 20px 10px;
  }
  
  .error-content {
    padding: 40px 20px;
  }
  
  .error-info {
    .error-title {
      font-size: 80px;
    }
    
    .error-subtitle {
      font-size: 24px;
    }
    
    .error-description {
      font-size: 14px;
    }
  }
  
  .error-actions {
    .el-button {
      margin: 5px;
      padding: 10px 20px;
      font-size: 14px;
    }
  }
  
  .error-suggestions {
    .suggestion-list {
      li {
        a {
          padding: 6px 12px;
          font-size: 12px;
          
          .el-icon {
            font-size: 14px;
          }
        }
      }
    }
  }
  
  .error-icon {
    svg {
      width: 150px;
      height: 150px;
    }
  }
}

@media (max-width: 480px) {
  .error-info {
    .error-title {
      font-size: 60px;
    }
    
    .error-subtitle {
      font-size: 20px;
    }
  }
  
  .error-suggestions {
    .suggestion-list {
      flex-direction: column;
      align-items: center;
    }
  }
}
</style>