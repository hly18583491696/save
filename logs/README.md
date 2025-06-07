# 宿舍管理系统日志说明

## 日志文件说明

本系统使用Logback作为日志框架，日志文件按照不同类型分别存储：

### 日志文件类型

1. **dormitory-management.log** - 主日志文件
   - 包含所有级别的日志信息
   - 记录系统启动、运行状态等信息

2. **dormitory-management-business.log** - 业务日志文件
   - 专门记录Controller和Service层的业务操作
   - 包含用户登录、数据操作等关键业务日志

3. **dormitory-management-error.log** - 错误日志文件
   - 只记录ERROR级别的日志
   - 用于快速定位系统错误和异常

### 日志滚动策略

- **文件大小限制**: 单个日志文件最大10MB
- **保留天数**: 保留最近30天的日志文件
- **总大小限制**: 所有日志文件总大小不超过1GB
- **文件命名**: 按日期和序号命名，如 `dormitory-management.2025-06-08.1.log`

### 日志级别配置

- **开发环境**: DEBUG级别，输出详细调试信息
- **生产环境**: WARN级别，只输出警告和错误信息
- **业务日志**: INFO级别，记录关键业务操作

### 主要日志记录点

#### 认证相关
- 用户登录尝试和结果
- 用户注册操作
- Token验证
- 用户登出操作

#### 数据操作
- 学生信息查询和管理
- 宿舍信息查询和管理
- 数据库操作异常

#### 系统异常
- 全局异常处理
- 参数校验失败
- 系统内部错误

### 查看日志的方法

#### Windows系统
```bash
# 查看主日志文件
type dormitory-management.log

# 实时监控日志
Get-Content dormitory-management.log -Wait

# 查看最后100行
Get-Content dormitory-management.log -Tail 100
```

#### Linux/Mac系统
```bash
# 查看主日志文件
cat dormitory-management.log

# 实时监控日志
tail -f dormitory-management.log

# 查看最后100行
tail -100 dormitory-management.log

# 搜索特定内容
grep "ERROR" dormitory-management.log
```

### 日志分析建议

1. **定期检查错误日志**: 重点关注 `dormitory-management-error.log`
2. **监控业务日志**: 通过 `dormitory-management-business.log` 跟踪用户操作
3. **性能分析**: 观察数据库操作和API响应时间
4. **异常模式识别**: 寻找重复出现的错误模式

### 常见问题速查表

| 问题现象 | 可能原因 | 解决方案 |
|---------|---------|----------|
| 登录成功但页面不跳转 | 路由跳转代码未执行 | 检查 then 回调是否触发（打印日志） |
| 跳转后页面空白 | 目标路由未定义或组件未正确加载 | 检查 router/index.js 路由配置 |
| 路由守卫拦截跳转 | 未登录状态或token无效 | 检查路由守卫逻辑和token有效性 |
| 后端返回200但无token | 后端未正确生成token | 使用Postman验证后端登录接口 |
| 跨域拦截 | 后端CORS配置错误 | 检查后端CORS允许前端域名 |

### 注意事项

- 日志文件会自动滚动，旧文件会被压缩保存
- 生产环境建议定期备份重要日志
- 可根据需要调整日志级别和输出格式