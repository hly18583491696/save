-- 宿舍管理系统数据库表结构
-- 创建时间: 2025

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色(ADMIN-管理员,TEACHER-教师,USER-普通用户)',
  `status` tinyint DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_real_name` (`real_name`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ----------------------------
-- 宿舍楼表
-- ----------------------------
CREATE TABLE `dorm_building` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_code` varchar(20) NOT NULL COMMENT '楼栋编号',
  `building_name` varchar(100) NOT NULL COMMENT '楼栋名称',
  `building_type` varchar(20) DEFAULT NULL COMMENT '楼栋类型(男生宿舍/女生宿舍)',
  `floors` int NOT NULL COMMENT '楼层数',
  `rooms_per_floor` int DEFAULT NULL COMMENT '每层房间数',
  `manager_name` varchar(50) DEFAULT NULL COMMENT '管理员姓名',
  `manager_phone` varchar(20) DEFAULT NULL COMMENT '管理员电话',
  `description` text COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_code` (`building_code`),
  KEY `idx_building_name` (`building_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍楼表';

-- ----------------------------
-- 宿舍房间表
-- ----------------------------
DROP TABLE IF EXISTS `dorm_room`;
CREATE TABLE `dorm_room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_number` varchar(20) NOT NULL COMMENT '房间号',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID',
  `floor` int NOT NULL COMMENT '楼层',
  `room_type` varchar(20) NOT NULL COMMENT '房间类型(SINGLE-单人间,DOUBLE-双人间,QUAD-四人间,SIX-六人间)',
  `capacity` int NOT NULL COMMENT '容纳人数',
  `current_count` int NOT NULL DEFAULT '0' COMMENT '当前入住人数',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别限制(MALE-男,FEMALE-女)',
  `status` varchar(20) NOT NULL DEFAULT 'AVAILABLE' COMMENT '房间状态(AVAILABLE-可用,FULL-已满,MAINTENANCE-维修中,DISABLED-禁用)',
  `facilities` text COMMENT '房间设施',
  `rent` decimal(10,2) DEFAULT NULL COMMENT '租金',
  `description` text COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_number_building` (`room_number`, `building_id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_floor` (`floor`),
  KEY `idx_room_type` (`room_type`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_room_building` FOREIGN KEY (`building_id`) REFERENCES `dorm_building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍房间表';

-- ----------------------------
-- 学生表
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `student_name` varchar(50) NOT NULL COMMENT '学生姓名',
  `student_number` varchar(20) NOT NULL COMMENT '学号',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `enrollment_date` date DEFAULT NULL COMMENT '入学时间',
  `graduation_date` date DEFAULT NULL COMMENT '离校时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_number` (`student_number`),
  UNIQUE KEY `uk_student_id` (`student_id`),
  KEY `idx_student_name` (`student_name`),
  KEY `idx_class_name` (`class_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- ----------------------------
-- 住宿记录表
-- ----------------------------
DROP TABLE IF EXISTS `dorm_accommodation`;
CREATE TABLE `dorm_accommodation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint DEFAULT NULL COMMENT '学生ID',
  `student_name` varchar(50) DEFAULT NULL COMMENT '学生姓名',
  `student_number` varchar(20) DEFAULT NULL COMMENT '学号',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `room_number` varchar(20) DEFAULT NULL COMMENT '房间号',
  `building_id` bigint DEFAULT NULL COMMENT '楼栋ID',
  `building_name` varchar(100) DEFAULT NULL COMMENT '楼栋名称',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `check_out_date` date DEFAULT NULL COMMENT '退房日期',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-在住,CHECKED_OUT-已退房,SUSPENDED-暂停)',
  `bed_number` varchar(10) DEFAULT NULL COMMENT '床位号',
  `remarks` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_number` (`student_number`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_status` (`status`),
  KEY `idx_check_in_date` (`check_in_date`),
  KEY `idx_student_name` (`student_name`),
  KEY `idx_class_name` (`class_name`),
  CONSTRAINT `fk_accommodation_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_accommodation_room` FOREIGN KEY (`room_id`) REFERENCES `dorm_room` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_accommodation_building` FOREIGN KEY (`building_id`) REFERENCES `dorm_building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='住宿记录表';

-- ----------------------------
-- 维修申请表
-- ----------------------------
DROP TABLE IF EXISTS `dorm_maintenance`;
CREATE TABLE `dorm_maintenance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `request_number` varchar(50) DEFAULT NULL COMMENT '申请单号',
  `applicant_id` bigint NOT NULL COMMENT '申请人ID',
  `applicant_name` varchar(50) DEFAULT NULL COMMENT '申请人姓名',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `room_number` varchar(20) DEFAULT NULL COMMENT '房间号',
  `building_id` bigint DEFAULT NULL COMMENT '楼栋ID',
  `building_name` varchar(100) DEFAULT NULL COMMENT '楼栋名称',
  `maintenance_type` varchar(50) NOT NULL COMMENT '维修类型',
  `description` text NOT NULL COMMENT '问题描述',
  `urgency_level` varchar(20) NOT NULL DEFAULT 'NORMAL' COMMENT '紧急程度(LOW-低,NORMAL-普通,HIGH-高,URGENT-紧急)',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态(PENDING-待处理,IN_PROGRESS-处理中,COMPLETED-已完成,CANCELLED-已取消)',
  `assigned_to` varchar(50) DEFAULT NULL COMMENT '分配给',
  `estimated_cost` decimal(10,2) DEFAULT NULL COMMENT '预估费用',
  `actual_cost` decimal(10,2) DEFAULT NULL COMMENT '实际费用',
  `scheduled_time` datetime DEFAULT NULL COMMENT '预约时间',
  `completed_time` datetime DEFAULT NULL COMMENT '完成时间',
  `feedback` text COMMENT '反馈',
  `rating` int DEFAULT NULL COMMENT '评分(1-5)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_request_number` (`request_number`),
  KEY `idx_applicant_id` (`applicant_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_status` (`status`),
  KEY `idx_urgency_level` (`urgency_level`),
  CONSTRAINT `fk_maintenance_room` FOREIGN KEY (`room_id`) REFERENCES `dorm_room` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_maintenance_building` FOREIGN KEY (`building_id`) REFERENCES `dorm_building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修申请表';

-- ----------------------------
-- 访客记录表
-- ----------------------------
DROP TABLE IF EXISTS `dorm_visitor`;
CREATE TABLE `dorm_visitor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `visitor_name` varchar(50) NOT NULL COMMENT '访客姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '访客身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '访客电话',
  `visited_student_id` bigint NOT NULL COMMENT '被访问学生ID',
  `visited_student_name` varchar(50) DEFAULT NULL COMMENT '被访问学生姓名',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `room_number` varchar(20) DEFAULT NULL COMMENT '房间号',
  `building_id` bigint DEFAULT NULL COMMENT '楼栋ID',
  `building_name` varchar(100) DEFAULT NULL COMMENT '楼栋名称',
  `visit_purpose` varchar(200) DEFAULT NULL COMMENT '来访目的',
  `visit_time` datetime NOT NULL COMMENT '来访时间',
  `expected_leave_time` datetime DEFAULT NULL COMMENT '预计离开时间',
  `actual_leave_time` datetime DEFAULT NULL COMMENT '实际离开时间',
  `status` varchar(20) NOT NULL DEFAULT 'VISITING' COMMENT '状态(VISITING-访问中,LEFT-已离开,EXPIRED-已过期)',
  `guard_name` varchar(50) DEFAULT NULL COMMENT '登记保安姓名',
  `remarks` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_visited_student_id` (`visited_student_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_visit_time` (`visit_time`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_visitor_room` FOREIGN KEY (`room_id`) REFERENCES `dorm_room` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_visitor_building` FOREIGN KEY (`building_id`) REFERENCES `dorm_building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='访客记录表';

-- ----------------------------
-- 系统配置表
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(50) NOT NULL DEFAULT 'STRING' COMMENT '配置类型(STRING-字符串,NUMBER-数字,BOOLEAN-布尔值,JSON-JSON对象)',
  `description` varchar(500) DEFAULT NULL COMMENT '配置描述',
  `system_flag` tinyint NOT NULL DEFAULT '0' COMMENT '系统配置标识：1-系统配置，0-用户配置',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_config_type` (`config_type`),
  KEY `idx_system_flag` (`system_flag`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入默认系统配置数据
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_type`, `description`, `system_flag`, `status`) VALUES
('system.name', '宿舍管理系统', 'STRING', '系统名称', 1, 1),
('system.school.name', '某某大学', 'STRING', '学校名称', 1, 1),
('system.admin.email', 'admin@example.com', 'STRING', '管理员邮箱', 1, 1),
('system.contact.phone', '400-123-4567', 'STRING', '联系电话', 1, 1),
('dorm.default.room.type', 'QUAD', 'STRING', '默认房间类型', 1, 1),
('dorm.max.occupancy', '4', 'NUMBER', '最大入住人数', 1, 1),
('dorm.mixed.gender', 'false', 'BOOLEAN', '是否允许混合性别', 1, 1),
('dorm.auto.assignment', 'true', 'BOOLEAN', '是否自动分配', 1, 1),
('notification.email.enabled', 'true', 'BOOLEAN', '邮件通知开关', 1, 1),
('notification.sms.enabled', 'false', 'BOOLEAN', '短信通知开关', 1, 1),
('notification.system.enabled', 'true', 'BOOLEAN', '系统通知开关', 1, 1),
('notification.maintenance.enabled', 'true', 'BOOLEAN', '维修提醒开关', 1, 1),
('security.password.min.length', '6', 'NUMBER', '密码最小长度', 1, 1),
('security.max.login.attempts', '5', 'NUMBER', '最大登录尝试次数', 1, 1),
('security.session.timeout', '30', 'NUMBER', '会话超时时间(分钟)', 1, 1),
('security.password.complexity', 'false', 'BOOLEAN', '密码复杂度要求', 1, 1),
('backup.auto.enabled', 'true', 'BOOLEAN', '自动备份开关', 1, 1),
('backup.frequency', 'DAILY', 'STRING', '备份频率', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;