-- 插入测试数据

-- 插入管理员用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `user_code`, `gender`, `phone`, `email`, `user_type`, `college`, `major`, `class_name`, `status`, `create_time`, `update_time`) VALUES
(1, 'admin', 'admin123', '系统管理员', 'ADMIN001', 1, '13800138000', 'admin@example.com', 1, NULL, NULL, NULL, 1, NOW(), NOW()),
(2, 'manager1', 'manager123', '宿管员1', 'MGR001', 0, '13800138001', 'manager1@example.com', 2, NULL, NULL, NULL, 1, NOW(), NOW()),
(3, 'student1', 'student123', '张三', '2021001', 1, '13800138002', 'student1@example.com', 3, '计算机学院', '软件工程', '软工2101', 1, NOW(), NOW()),
(4, 'student2', 'student123', '李四', '2021002', 0, '13800138003', 'student2@example.com', 3, '计算机学院', '软件工程', '软工2101', 1, NOW(), NOW());

-- 插入宿舍楼数据
INSERT INTO `dorm_building` (`id`, `building_code`, `building_name`, `building_type`, `total_floors`, `rooms_per_floor`, `total_rooms`, `manager_id`, `manager_name`, `status`, `create_time`, `update_time`) VALUES
(1, 'A01', 'A栋宿舍楼', 1, 6, 20, 120, 2, '宿管员1', 1, NOW(), NOW()),
(2, 'B01', 'B栋宿舍楼', 2, 6, 20, 120, 2, '宿管员1', 1, NOW(), NOW());

-- 插入房间数据
INSERT INTO `dorm_room` (`id`, `room_number`, `building_id`, `building_name`, `floor`, `room_type`, `total_beds`, `occupied_beds`, `available_beds`, `status`, `create_time`, `update_time`) VALUES
(1, 'A01-101', 1, 'A栋宿舍楼', 1, 2, 4, 2, 2, 1, NOW(), NOW()),
(2, 'A01-102', 1, 'A栋宿舍楼', 1, 2, 4, 0, 4, 1, NOW(), NOW()),
(3, 'B01-101', 2, 'B栋宿舍楼', 1, 2, 4, 1, 3, 1, NOW(), NOW()),
(4, 'B01-102', 2, 'B栋宿舍楼', 1, 2, 4, 0, 4, 1, NOW(), NOW());

-- 插入住宿记录
INSERT INTO `dorm_accommodation` (`id`, `student_id`, `student_name`, `student_code`, `building_id`, `building_name`, `room_id`, `room_number`, `bed_number`, `check_in_date`, `status`, `create_time`, `update_time`) VALUES
(1, 3, '张三', '2021001', 1, 'A栋宿舍楼', 1, 'A01-101', '1', '2024-09-01', 1, NOW(), NOW()),
(2, 4, '李四', '2021002', 2, 'B栋宿舍楼', 3, 'B01-101', '1', '2024-09-01', 1, NOW(), NOW());