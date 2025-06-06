-- 绵阳城市学院学生宿舍管理系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `mycs_dormitory` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mycs_dormitory`;

-- 用户表
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `user_code` varchar(50) DEFAULT NULL COMMENT '学号/工号',
  `gender` tinyint DEFAULT NULL COMMENT '性别（0：女，1：男）',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `user_type` tinyint NOT NULL DEFAULT '3' COMMENT '用户类型（1：管理员，2：宿管员，3：学生）',
  `college` varchar(100) DEFAULT NULL COMMENT '学院',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `class_name` varchar(100) DEFAULT NULL COMMENT '班级',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0：禁用，1：启用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_user_code` (`user_code`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 宿舍楼表
CREATE TABLE `dorm_building` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `building_code` varchar(20) NOT NULL COMMENT '楼栋编号',
  `building_name` varchar(100) NOT NULL COMMENT '楼栋名称',
  `building_type` tinyint NOT NULL COMMENT '楼栋类型（1：男生宿舍，2：女生宿舍）',
  `total_floors` int NOT NULL COMMENT '总楼层数',
  `rooms_per_floor` int NOT NULL COMMENT '每层房间数',
  `total_rooms` int NOT NULL COMMENT '总房间数',
  `manager_id` bigint DEFAULT NULL COMMENT '宿管员ID',
  `manager_name` varchar(50) DEFAULT NULL COMMENT '宿管员姓名',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0：停用，1：启用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_code` (`building_code`),
  KEY `idx_building_type` (`building_type`),
  KEY `idx_manager_id` (`manager_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍楼表';

-- 宿舍房间表
CREATE TABLE `dorm_room` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `room_number` varchar(20) NOT NULL COMMENT '房间号',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID',
  `building_name` varchar(100) NOT NULL COMMENT '楼栋名称',
  `floor` int NOT NULL COMMENT '楼层',
  `room_type` tinyint NOT NULL COMMENT '房间类型（1：2人间，2：4人间，3：6人间）',
  `total_beds` int NOT NULL COMMENT '床位总数',
  `occupied_beds` int NOT NULL DEFAULT '0' COMMENT '已入住人数',
  `available_beds` int NOT NULL COMMENT '剩余床位',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '房间状态（1：正常，2：维修中，3：停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_number` (`room_number`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_floor` (`floor`),
  KEY `idx_room_type` (`room_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍房间表';

-- 住宿记录表
CREATE TABLE `dorm_accommodation` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `student_name` varchar(50) NOT NULL COMMENT '学生姓名',
  `student_code` varchar(50) NOT NULL COMMENT '学号',
  `building_id` bigint NOT NULL COMMENT '楼栋ID',
  `building_name` varchar(100) NOT NULL COMMENT '楼栋名称',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `room_number` varchar(20) NOT NULL COMMENT '房间号',
  `bed_number` varchar(10) NOT NULL COMMENT '床位号',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `check_out_date` date DEFAULT NULL COMMENT '退宿日期',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '住宿状态（1：在住，2：已退宿，3：请假）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_student_code` (`student_code`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_status` (`status`),
  KEY `idx_check_in_date` (`check_in_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='住宿记录表';

-- 插入初始数据

-- 插入管理员用户（密码：admin123）
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `user_code`, `gender`, `phone`, `email`, `user_type`, `college`, `status`, `remark`, `create_time`, `update_time`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyZFzUNjKUFVWCZ6.XMfOEBYjdG', '系统管理员', 'ADMIN001', 1, '13800138000', 'admin@mycc.edu.cn', 1, '信息工程学院', 1, '系统管理员账号', NOW(), NOW());

-- 插入宿管员用户（密码：manager123）
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `user_code`, `gender`, `phone`, `email`, `user_type`, `college`, `status`, `remark`, `create_time`, `update_time`) VALUES
(2, 'manager01', '$2a$10$8.H8MfPTH5b5q5y5y5y5yuOCQb376NoUnuTJ8iKyZFzUNjKUFVWCZ6.XMfOEBYjdG', '张宿管', 'MGR001', 2, '13800138001', 'manager01@mycc.edu.cn', 2, '后勤管理处', 1, '1号楼宿管员', NOW(), NOW());

-- 插入学生用户（密码：student123）
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `user_code`, `gender`, `phone`, `email`, `user_type`, `college`, `major`, `class_name`, `status`, `remark`, `create_time`, `update_time`) VALUES
(3, 'student01', '$2a$10$9.I9NgQUI6c6q6z6z6z6zuOCQb376NoUnuTJ8iKyZFzUNjKUFVWCZ6.XMfOEBYjdG', '李小明', '2024001', 1, '13800138002', 'student01@mycc.edu.cn', 3, '信息工程学院', '计算机科学与技术', '计科2024-1班', 1, '学生账号', NOW(), NOW());

-- 插入示例宿舍楼
INSERT INTO `dorm_building` (`id`, `building_code`, `building_name`, `building_type`, `total_floors`, `rooms_per_floor`, `total_rooms`, `manager_id`, `manager_name`, `status`, `remark`, `create_time`, `update_time`) VALUES
(1, 'A01', '1号男生宿舍楼', 1, 6, 20, 120, 2, '张宿管', 1, '主要住宿楼栋', NOW(), NOW()),
(2, 'B01', '1号女生宿舍楼', 2, 6, 20, 120, NULL, NULL, 1, '主要住宿楼栋', NOW(), NOW());

-- 插入示例房间
INSERT INTO `dorm_room` (`id`, `room_number`, `building_id`, `building_name`, `floor`, `room_type`, `total_beds`, `occupied_beds`, `available_beds`, `status`, `remark`, `create_time`, `update_time`) VALUES
(1, 'A01-101', 1, '1号男生宿舍楼', 1, 2, 4, 0, 4, 1, '标准4人间', NOW(), NOW()),
(2, 'A01-102', 1, '1号男生宿舍楼', 1, 2, 4, 0, 4, 1, '标准4人间', NOW(), NOW()),
(3, 'B01-101', 2, '1号女生宿舍楼', 1, 2, 4, 0, 4, 1, '标准4人间', NOW(), NOW()),
(4, 'B01-102', 2, '1号女生宿舍楼', 1, 2, 4, 0, 4, 1, '标准4人间', NOW(), NOW());