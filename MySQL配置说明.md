# MySQL数据库配置说明

## 前置要求

1. 确保已安装MySQL数据库服务器
2. MySQL服务正在运行
3. 有数据库管理权限

## 配置步骤

### 1. 创建数据库

使用MySQL命令行或图形化工具（如Navicat、phpMyAdmin等）执行以下命令：

```sql
CREATE DATABASE IF NOT EXISTS dormitory_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

或者直接运行项目根目录下的 `mysql-init.sql` 文件：

```bash
mysql -u root -p < mysql-init.sql
```

### 2. 修改数据库连接配置

在 `src/main/resources/application.properties` 文件中，根据你的MySQL配置修改以下参数：

```properties
# MySQL数据库配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/dormitory_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=123456
```

**请根据实际情况修改：**
- `localhost:3306` - MySQL服务器地址和端口
- `dormitory_management` - 数据库名称
- `root` - 数据库用户名
- `123456` - 数据库密码

### 3. 表结构自动创建

项目使用JPA的 `hibernate.ddl-auto=update` 配置，会根据实体类自动创建和更新表结构。首次运行时会自动创建所有必要的表。

### 4. 启动应用

配置完成后，使用以下命令启动应用：

```bash
mvn spring-boot:run
```

## 常见问题

### 1. 连接失败
- 检查MySQL服务是否启动
- 确认用户名和密码是否正确
- 检查数据库是否存在
- 确认MySQL端口是否正确（默认3306）

### 2. 时区问题
如果遇到时区相关错误，可以在MySQL中执行：
```sql
SET GLOBAL time_zone = '+8:00';
```

### 3. 字符编码问题
确保MySQL配置支持utf8mb4字符集，可以在my.cnf中添加：
```ini
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
```

## 数据库表说明

应用会自动创建以下主要表：
- `users` - 用户表
- `buildings` - 宿舍楼表
- `rooms` - 房间表
- `accommodations` - 住宿记录表
- `dorm_notice` - 宿舍公告表
- `maintenance_requests` - 维修申请表

## 默认管理员账户

系统启动后会自动创建一个默认管理员账户：
- 用户名：admin
- 密码：admin

**注意：生产环境中请及时修改默认密码！**