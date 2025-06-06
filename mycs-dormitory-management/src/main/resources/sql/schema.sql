-- H2数据库表结构

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `user_code` varchar(50) DEFAULT NULL COMMENT '学号/工号',
  `gender` tinyint DEFAULT NULL COMMENT '性别（0：女，1：男）',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `user_type` tinyint NOT NULL DEFAULT 3 COMMENT '用户类型（1：管理员，2：宿管员，3：学生）',
  `college` varchar(100) DEFAULT NULL COMMENT '学院',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `class_name` varchar(100) DEFAULT NULL COMMENT '班级',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
);

-- 宿舍楼表
CREATE TABLE IF NOT EXISTS `dorm_building` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_code` varchar(20) NOT NULL COMMENT '楼栋编号',
  `building_name` varchar(100) NOT NULL COMMENT '楼栋名称',
  `building_type` tinyint NOT NULL COMMENT '楼栋类型（1：男生宿舍，2：女生宿舍）',
  `total_floors` int NOT NULL COMMENT '总楼层数',
  `rooms_per_floor` int NOT NULL COMMENT '每层房间数',
  `total_rooms` int NOT NULL COMMENT '总房间数',
  `manager_id` bigint DEFAULT NULL COMMENT '宿管员ID',
  `manager_name` varchar(50) DEFAULT NULL COMMENT '宿管员姓名',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0：停用，1：启用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
);

-- 宿舍房间表
CREATE TABLE IF NOT EXISTS `dorm_room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_number` varchar(20) NOT NULL COMMENT '房间号',
  `building_id` bigint NOT NULL COMMENT '所属楼栋ID',
  `building_name` varchar(100) NOT NULL COMMENT '楼栋名称',
  `floor` int NOT NULL COMMENT '楼层',
  `room_type` tinyint NOT NULL COMMENT '房间类型（1：2人间，2：4人间，3：6人间）',
  `total_beds` int NOT NULL COMMENT '床位总数',
  `occupied_beds` int NOT NULL DEFAULT 0 COMMENT '已入住人数',
  `available_beds` int NOT NULL COMMENT '剩余床位',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '房间状态（1：正常，2：维修中，3：停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
);

-- 住宿记录表
CREATE TABLE IF NOT EXISTS `dorm_accommodation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
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
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1：在住，2：已退宿）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`id`)
);