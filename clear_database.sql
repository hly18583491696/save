-- 清空数据库mycs_dormitory所有表的数据并删除所有表
-- 执行前请确保已备份重要数据

USE mycs_dormitory;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 清空所有表的数据
TRUNCATE TABLE dorm_visitor;
TRUNCATE TABLE dorm_maintenance;
TRUNCATE TABLE dorm_accommodation;
TRUNCATE TABLE student;
TRUNCATE TABLE dorm_room;
TRUNCATE TABLE dorm_building;
TRUNCATE TABLE sys_user;

-- 删除所有表
DROP TABLE IF EXISTS dorm_visitor;
DROP TABLE IF EXISTS dorm_maintenance;
DROP TABLE IF EXISTS dorm_accommodation;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS dorm_room;
DROP TABLE IF EXISTS dorm_building;
DROP TABLE IF EXISTS sys_user;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 显示剩余的表（应该为空）
SHOW TABLES;