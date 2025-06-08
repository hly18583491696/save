-- 宿舍管理系统数据库初始化脚本


-- 创建数据库
CREATE DATABASE IF NOT EXISTS `mycs_dormitory` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `mycs_dormitory`;

-- 执行建表脚本
source src/main/resources/sql/schema.sql;

-- 执行数据插入脚本
source src/main/resources/sql/data.sql;

-- 显示所有表
SHOW TABLES;

-- 显示用户表数据
SELECT * FROM sys_user;

SELECT '数据库初始化完成！' AS message;