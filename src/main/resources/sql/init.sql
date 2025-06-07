-- 学生宿舍管理系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS dormitory_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dormitory_management;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    student_id VARCHAR(20) UNIQUE COMMENT '学号/工号',
    gender TINYINT DEFAULT 0 COMMENT '性别 0-女 1-男',
    phone VARCHAR(11) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像URL',
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT' COMMENT '角色 ADMIN-管理员 STUDENT-学生 TEACHER-教师',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    college VARCHAR(100) COMMENT '学院',
    major VARCHAR(100) COMMENT '专业',
    class_name VARCHAR(50) COMMENT '班级',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除'
) COMMENT '用户表';

-- 宿舍楼表
CREATE TABLE IF NOT EXISTS dorm_building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID',
    building_code VARCHAR(20) NOT NULL UNIQUE COMMENT '楼栋编号',
    building_name VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    floors INT NOT NULL COMMENT '楼层数',
    rooms_per_floor INT NOT NULL COMMENT '每层房间数',
    building_type VARCHAR(20) NOT NULL COMMENT '楼栋类型 MALE-男生宿舍 FEMALE-女生宿舍',
    manager_id BIGINT COMMENT '管理员ID',
    manager_name VARCHAR(50) COMMENT '管理员姓名',
    contact_phone VARCHAR(11) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '地址',
    status TINYINT DEFAULT 1 COMMENT '状态 0-停用 1-启用',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除'
) COMMENT '宿舍楼表';

-- 宿舍房间表
CREATE TABLE IF NOT EXISTS dorm_room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '房间ID',
    room_number VARCHAR(20) NOT NULL COMMENT '房间号',
    building_id BIGINT NOT NULL COMMENT '所属楼栋ID',
    building_name VARCHAR(100) COMMENT '楼栋名称',
    floor INT NOT NULL COMMENT '楼层',
    room_type INT NOT NULL COMMENT '房间类型 2-双人间 4-四人间 6-六人间',
    total_beds INT NOT NULL COMMENT '床位总数',
    occupied_beds INT DEFAULT 0 COMMENT '已入住床位数',
    available_beds INT COMMENT '可用床位数',
    area DECIMAL(8,2) COMMENT '房间面积(平方米)',
    facilities TEXT COMMENT '房间设施',
    status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '房间状态 AVAILABLE-可入住 FULL-已满 MAINTENANCE-维修中 CLOSED-关闭',
    fee DECIMAL(10,2) COMMENT '房间费用(元/学期)',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    FOREIGN KEY (building_id) REFERENCES dorm_building(id)
) COMMENT '宿舍房间表';

-- 住宿记录表
CREATE TABLE IF NOT EXISTS dorm_accommodation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '住宿记录ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    student_name VARCHAR(50) COMMENT '学生姓名',
    student_number VARCHAR(20) COMMENT '学号',
    room_id BIGINT NOT NULL COMMENT '房间ID',
    room_number VARCHAR(20) COMMENT '房间号',
    building_id BIGINT COMMENT '楼栋ID',
    building_name VARCHAR(100) COMMENT '楼栋名称',
    bed_number VARCHAR(10) COMMENT '床位号',
    check_in_date DATE COMMENT '入住日期',
    check_out_date DATE COMMENT '退宿日期',
    status VARCHAR(20) DEFAULT 'LIVING' COMMENT '住宿状态 LIVING-在住 CHECKED_OUT-已退宿 SUSPENDED-暂停',
    semester VARCHAR(20) COMMENT '学期',
    academic_year VARCHAR(20) COMMENT '学年',
    fee DECIMAL(10,2) COMMENT '住宿费用',
    payment_status VARCHAR(20) DEFAULT 'UNPAID' COMMENT '缴费状态 UNPAID-未缴费 PAID-已缴费 PARTIAL-部分缴费',
    payment_date DATE COMMENT '缴费日期',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    FOREIGN KEY (student_id) REFERENCES sys_user(id),
    FOREIGN KEY (room_id) REFERENCES dorm_room(id)
) COMMENT '住宿记录表';

-- 维修申请表
CREATE TABLE IF NOT EXISTS dorm_maintenance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '维修申请ID',
    request_number VARCHAR(50) COMMENT '申请单号',
    applicant_id BIGINT NOT NULL COMMENT '申请人ID',
    applicant_name VARCHAR(50) COMMENT '申请人姓名',
    room_id BIGINT NOT NULL COMMENT '房间ID',
    room_number VARCHAR(20) COMMENT '房间号',
    building_id BIGINT COMMENT '楼栋ID',
    building_name VARCHAR(100) COMMENT '楼栋名称',
    maintenance_type VARCHAR(20) COMMENT '维修类型 WATER-水电 FURNITURE-家具 NETWORK-网络 DOOR_WINDOW-门窗 OTHER-其他',
    description TEXT NOT NULL COMMENT '问题描述',
    urgency VARCHAR(20) DEFAULT 'MEDIUM' COMMENT '紧急程度 LOW-低 MEDIUM-中 HIGH-高 URGENT-紧急',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '申请状态 PENDING-待处理 ASSIGNED-已分配 IN_PROGRESS-处理中 COMPLETED-已完成 CANCELLED-已取消',
    repairer_id BIGINT COMMENT '维修人员ID',
    repairer_name VARCHAR(50) COMMENT '维修人员姓名',
    repairer_phone VARCHAR(11) COMMENT '维修人员电话',
    scheduled_time DATETIME COMMENT '预约维修时间',
    start_time DATETIME COMMENT '实际开始时间',
    completed_time DATETIME COMMENT '完成时间',
    cost DECIMAL(10,2) COMMENT '维修费用',
    result TEXT COMMENT '维修结果描述',
    rating TINYINT COMMENT '满意度评分 1-5分',
    feedback TEXT COMMENT '评价内容',
    attachments TEXT COMMENT '图片附件URL',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    FOREIGN KEY (applicant_id) REFERENCES sys_user(id),
    FOREIGN KEY (room_id) REFERENCES dorm_room(id)
) COMMENT '维修申请表';

-- 访客记录表
CREATE TABLE IF NOT EXISTS dorm_visitor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '访客记录ID',
    visitor_name VARCHAR(50) NOT NULL COMMENT '访客姓名',
    id_card VARCHAR(18) COMMENT '访客身份证号',
    phone VARCHAR(11) COMMENT '访客电话',
    student_id BIGINT NOT NULL COMMENT '被访问学生ID',
    student_name VARCHAR(50) COMMENT '被访问学生姓名',
    room_id BIGINT NOT NULL COMMENT '房间ID',
    room_number VARCHAR(20) COMMENT '房间号',
    building_id BIGINT COMMENT '楼栋ID',
    building_name VARCHAR(100) COMMENT '楼栋名称',
    purpose VARCHAR(255) COMMENT '访问目的',
    relationship VARCHAR(50) COMMENT '与被访问人关系',
    appointment_time DATETIME COMMENT '预约访问时间',
    check_in_time DATETIME COMMENT '实际进入时间',
    check_out_time DATETIME COMMENT '离开时间',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '访问状态 PENDING-待审核 APPROVED-已批准 REJECTED-已拒绝 IN_VISIT-访问中 COMPLETED-已完成',
    reviewer_id BIGINT COMMENT '审核人ID',
    reviewer_name VARCHAR(50) COMMENT '审核人姓名',
    review_time DATETIME COMMENT '审核时间',
    review_comment TEXT COMMENT '审核意见',
    registrar_id BIGINT COMMENT '登记人ID',
    registrar_name VARCHAR(50) COMMENT '登记人姓名',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    FOREIGN KEY (student_id) REFERENCES sys_user(id),
    FOREIGN KEY (room_id) REFERENCES dorm_room(id)
) COMMENT '访客记录表';

-- 创建索引
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_student_id ON sys_user(student_id);
CREATE INDEX idx_user_role ON sys_user(role);
CREATE INDEX idx_building_code ON dorm_building(building_code);
CREATE INDEX idx_room_number ON dorm_room(room_number);
CREATE INDEX idx_room_building_id ON dorm_room(building_id);
CREATE INDEX idx_accommodation_student_id ON dorm_accommodation(student_id);
CREATE INDEX idx_accommodation_room_id ON dorm_accommodation(room_id);
CREATE INDEX idx_maintenance_applicant_id ON dorm_maintenance(applicant_id);
CREATE INDEX idx_maintenance_room_id ON dorm_maintenance(room_id);
CREATE INDEX idx_visitor_student_id ON dorm_visitor(student_id);
CREATE INDEX idx_visitor_room_id ON dorm_visitor(room_id);

-- 插入初始数据
-- 插入管理员用户
INSERT INTO sys_user (username, password, real_name, role, status) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '系统管理员', 'ADMIN', 1);
-- 密码是: admin123

-- 插入示例宿舍楼
INSERT INTO dorm_building (building_code, building_name, floors, rooms_per_floor, building_type, manager_name, contact_phone, address, status) VALUES 
('A001', 'A栋男生宿舍', 6, 20, 'MALE', '张管理员', '13800138001', '校园东区A栋', 1),
('B001', 'B栋女生宿舍', 6, 20, 'FEMALE', '李管理员', '13800138002', '校园东区B栋', 1);

-- 插入示例房间
INSERT INTO dorm_room (room_number, building_id, building_name, floor, room_type, total_beds, occupied_beds, available_beds, area, facilities, status, fee) VALUES 
('A101', 1, 'A栋男生宿舍', 1, 4, 4, 0, 4, 20.00, '空调,热水器,独立卫浴,书桌,衣柜', 'AVAILABLE', 1200.00),
('A102', 1, 'A栋男生宿舍', 1, 4, 4, 0, 4, 20.00, '空调,热水器,独立卫浴,书桌,衣柜', 'AVAILABLE', 1200.00),
('B101', 2, 'B栋女生宿舍', 1, 4, 4, 0, 4, 20.00, '空调,热水器,独立卫浴,书桌,衣柜', 'AVAILABLE', 1200.00),
('B102', 2, 'B栋女生宿舍', 1, 4, 4, 0, 4, 20.00, '空调,热水器,独立卫浴,书桌,衣柜', 'AVAILABLE', 1200.00);