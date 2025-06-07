# 学生宿舍管理系统

基于 Spring Boot 3 + MySQL 8 + Vue 3 + Vite 的前后端分离学生宿舍管理系统。

## 项目简介

本系统是一个完整的学生宿舍管理解决方案，包含用户管理、宿舍楼管理、房间管理、住宿记录、维修申请、访客管理等核心功能。

## 技术栈

### 后端技术
- **Java 21** - 编程语言
- **Spring Boot 3.5.0** - 应用框架
- **Spring Security** - 安全框架
- **MyBatis-Plus 3.5.5** - ORM框架
- **MySQL 8.0.41** - 数据库
- **Redis** - 缓存
- **JWT** - 身份认证
- **Swagger 3** - API文档
- **Maven 3.8.8** - 项目管理

### 前端技术
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Node.js 22.14.0** - 运行环境
- **npm 11.3.0** - 包管理器

## 功能模块

### 1. 用户管理
- 用户注册/登录
- 用户信息管理
- 角色权限控制（管理员/学生/教师）
- 密码修改/重置

### 2. 宿舍楼管理
- 宿舍楼信息维护
- 楼栋类型管理（男生/女生宿舍）
- 管理员分配

### 3. 房间管理
- 房间信息管理
- 床位管理
- 房间状态控制
- 费用设置

### 4. 住宿管理
- 学生入住/退宿
- 住宿记录查询
- 缴费状态管理

### 5. 维修管理
- 维修申请提交
- 维修工单处理
- 维修进度跟踪
- 满意度评价

### 6. 访客管理
- 访客预约申请
- 访客审核流程
- 访客进出记录

## 环境要求

### 开发环境
- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- Node.js 18+
- npm 8+

## 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd mycs-dormitory-management
```

### 2. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE dormitory_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 执行初始化脚本
```bash
mysql -u root -p dormitory_management < src/main/resources/sql/init.sql
```

#### 修改数据库配置
编辑 `src/main/resources/application.properties`：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dormitory_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Redis配置
确保Redis服务已启动，默认配置：
```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### 4. 启动后端服务
```bash
# 安装依赖
mvn clean install

# 启动应用
mvn spring-boot:run
```

服务启动后访问：
- 应用地址：http://localhost:8080
- API文档：http://localhost:8080/swagger-ui.html

### 5. 前端开发（可选）
```bash
# 创建Vue 3项目
npm create vite@latest dormitory-frontend -- --template vue
cd dormitory-frontend
npm install
npm run dev
```

## API文档

启动应用后，访问 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) 查看完整的API文档。

### 主要API端点

#### 用户管理
- `POST /api/users/login` - 用户登录
- `POST /api/users/register` - 用户注册
- `GET /api/users/{id}` - 获取用户信息
- `POST /api/users/list` - 分页查询用户
- `PUT /api/users/{id}` - 更新用户信息

#### 宿舍楼管理
- `GET /api/buildings` - 获取宿舍楼列表
- `POST /api/buildings` - 创建宿舍楼
- `PUT /api/buildings/{id}` - 更新宿舍楼信息

#### 房间管理
- `GET /api/rooms` - 获取房间列表
- `POST /api/rooms` - 创建房间
- `GET /api/rooms/available` - 获取可用房间

## 默认账户

系统初始化后会创建一个管理员账户：
- 用户名：`admin`
- 密码：`admin123`

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── edu/mycc/xhd/mycsdormitorymanagement/
│   │       ├── common/          # 通用类
│   │       ├── config/          # 配置类
│   │       ├── controller/      # 控制器
│   │       ├── entity/          # 实体类
│   │       ├── exception/       # 异常处理
│   │       ├── mapper/          # 数据访问层
│   │       ├── service/         # 业务逻辑层
│   │       └── utils/           # 工具类
│   └── resources/
│       ├── sql/                 # SQL脚本
│       └── application.properties # 配置文件
└── test/                        # 测试代码
```

## 开发指南

### 代码规范
- 使用Lombok简化代码
- 统一异常处理
- RESTful API设计
- 统一响应格式

### 数据库设计
- 使用逻辑删除
- 自动填充创建/更新时间
- 合理的索引设计
- 外键约束

### 安全考虑
- JWT身份认证
- 密码加密存储
- CORS跨域配置
- 参数校验

## 部署说明

### 生产环境配置
1. 修改数据库连接配置
2. 配置Redis连接
3. 设置JWT密钥
4. 配置日志级别
5. 设置文件上传路径

### Docker部署（可选）
```dockerfile
# Dockerfile示例
FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/mycs-dormitory-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目维护者：开发团队
- 邮箱：admin@mycc.edu
- 项目地址：https://github.com/your-username/mycs-dormitory-management

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 完成基础功能模块
- 集成Swagger API文档
- 添加JWT身份认证