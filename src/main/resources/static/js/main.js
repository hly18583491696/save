// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

// 初始化应用
function initializeApp() {
    setupNavigation();
    loadDashboardData();
    setupEventListeners();
}

// 设置导航功能
function setupNavigation() {
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetSection = this.getAttribute('href').substring(1);
            showSection(targetSection);
            
            // 更新导航状态
            navLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        });
    });
}

// 显示指定的页面部分
function showSection(sectionId) {
    // 隐藏所有内容区域
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.classList.remove('active');
    });
    
    // 显示目标区域
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add('active');
    }
    
    // 更新页面标题
    updatePageTitle(sectionId);
}

// 更新页面标题
function updatePageTitle(sectionId) {
    const titles = {
        'home': '系统概览',
        'buildings': '宿舍楼管理',
        'rooms': '房间管理',
        'students': '学生管理',
        'maintenance': '维修管理',
        'visitors': '访客管理'
    };
    
    document.title = `${titles[sectionId] || '学生宿舍管理系统'} - 学生宿舍管理系统`;
}

// 加载仪表板数据
function loadDashboardData() {
    // 模拟数据加载
    const stats = {
        buildings: 12,
        rooms: 480,
        students: 1856,
        maintenance: 23
    };
    
    // 更新统计数据
    updateStatistics(stats);
    
    // 加载最近活动
    loadRecentActivities();
}

// 更新统计数据
function updateStatistics(stats) {
    const elements = {
        buildingCount: stats.buildings,
        roomCount: stats.rooms,
        studentCount: stats.students.toLocaleString(),
        maintenanceCount: stats.maintenance
    };
    
    Object.keys(elements).forEach(id => {
        const element = document.getElementById(id);
        if (element) {
            animateNumber(element, 0, parseInt(elements[id].toString().replace(/,/g, '')), 1000);
        }
    });
}

// 数字动画效果
function animateNumber(element, start, end, duration) {
    const startTime = performance.now();
    const isComma = element.textContent.includes(',');
    
    function updateNumber(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        const current = Math.floor(start + (end - start) * progress);
        element.textContent = isComma ? current.toLocaleString() : current;
        
        if (progress < 1) {
            requestAnimationFrame(updateNumber);
        }
    }
    
    requestAnimationFrame(updateNumber);
}

// 加载最近活动
function loadRecentActivities() {
    const activities = [
        {
            icon: 'fas fa-user-plus',
            content: '<strong>张三</strong> 入住了 <strong>1号楼101室</strong>',
            time: '2小时前'
        },
        {
            icon: 'fas fa-tools',
            content: '<strong>李四</strong> 提交了维修申请',
            time: '4小时前'
        },
        {
            icon: 'fas fa-user-check',
            content: '<strong>王五</strong> 的访客已登记',
            time: '6小时前'
        }
    ];
    
    const activityList = document.querySelector('.activity-list');
    if (activityList) {
        activityList.innerHTML = activities.map(activity => `
            <div class="activity-item">
                <div class="activity-icon">
                    <i class="${activity.icon}"></i>
                </div>
                <div class="activity-content">
                    <p>${activity.content}</p>
                    <span class="activity-time">${activity.time}</span>
                </div>
            </div>
        `).join('');
    }
}

// 设置事件监听器
function setupEventListeners() {
    // 窗口大小改变时的响应
    window.addEventListener('resize', handleResize);
    
    // 键盘快捷键
    document.addEventListener('keydown', handleKeyboard);
}

// 处理窗口大小改变
function handleResize() {
    // 可以在这里添加响应式处理逻辑
    console.log('Window resized');
}

// 处理键盘事件
function handleKeyboard(e) {
    // ESC键返回首页
    if (e.key === 'Escape') {
        showSection('home');
        document.querySelector('.nav-link[href="#home"]').classList.add('active');
        document.querySelectorAll('.nav-link:not([href="#home"])').forEach(link => {
            link.classList.remove('active');
        });
    }
}

// 登出功能
function logout() {
    if (confirm('确定要退出登录吗？')) {
        // 清除本地存储的用户信息
        localStorage.removeItem('userToken');
        localStorage.removeItem('userInfo');
        
        // 重定向到登录页面
        window.location.href = '/login.html';
    }
}

// 显示通知消息
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <i class="fas fa-${getNotificationIcon(type)}"></i>
        <span>${message}</span>
        <button onclick="this.parentElement.remove()">
            <i class="fas fa-times"></i>
        </button>
    `;
    
    document.body.appendChild(notification);
    
    // 自动移除通知
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}

// 获取通知图标
function getNotificationIcon(type) {
    const icons = {
        'success': 'check-circle',
        'error': 'exclamation-circle',
        'warning': 'exclamation-triangle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

// API 请求封装
class ApiClient {
    constructor(baseURL = '/api') {
        this.baseURL = baseURL;
        this.token = localStorage.getItem('userToken');
    }
    
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...(this.token && { 'Authorization': `Bearer ${this.token}` })
            },
            ...options
        };
        
        try {
            const response = await fetch(url, config);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('API request failed:', error);
            showNotification('网络请求失败，请稍后重试', 'error');
            throw error;
        }
    }
    
    async get(endpoint) {
        return this.request(endpoint);
    }
    
    async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }
    
    async put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }
    
    async delete(endpoint) {
        return this.request(endpoint, {
            method: 'DELETE'
        });
    }
}

// 创建全局API客户端实例
const api = new ApiClient();

// 工具函数
const utils = {
    // 格式化日期
    formatDate(date) {
        return new Date(date).toLocaleString('zh-CN');
    },
    
    // 格式化数字
    formatNumber(num) {
        return num.toLocaleString();
    },
    
    // 防抖函数
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },
    
    // 节流函数
    throttle(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }
};

// 导出到全局作用域
window.showSection = showSection;
window.logout = logout;
window.showNotification = showNotification;
window.api = api;
window.utils = utils;