-- 宿舍管理系统数据库表结构

-- H2数据库不需要创建数据库和USE语句

-- 楼栋表
CREATE TABLE IF NOT EXISTS dorm_building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('MALE', 'FEMALE', 'MIXED')),
    floors INT NOT NULL,
    rooms_per_floor INT NOT NULL,
    manager_name VARCHAR(50),
    manager_phone VARCHAR(20),
    address VARCHAR(200),
    description TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 房间表
CREATE TABLE IF NOT EXISTS dorm_room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    building_id BIGINT NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    floor INT NOT NULL,
    capacity INT NOT NULL,
    current_count INT DEFAULT 0,
    type VARCHAR(10) DEFAULT 'STANDARD' CHECK (type IN ('STANDARD', 'DELUXE', 'SUITE')),
    facilities TEXT,
    rent DECIMAL(10,2),
    status VARCHAR(15) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'RESERVED')),
    description TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    FOREIGN KEY (building_id) REFERENCES dorm_building(id)
);

-- 学生表
CREATE TABLE IF NOT EXISTS dorm_student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    grade VARCHAR(10),
    major VARCHAR(100),
    class_name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    emergency_contact VARCHAR(50),
    emergency_phone VARCHAR(20),
    id_card VARCHAR(18),
    address TEXT,
    status VARCHAR(15) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'GRADUATED', 'SUSPENDED')),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 住宿记录表
CREATE TABLE IF NOT EXISTS dorm_accommodation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE,
    semester VARCHAR(10) NOT NULL CHECK (semester IN ('SPRING', 'FALL')),
    academic_year VARCHAR(10) NOT NULL,
    status VARCHAR(15) DEFAULT 'LIVING' CHECK (status IN ('LIVING', 'CHECKED_OUT', 'TRANSFERRED')),
    rent_amount DECIMAL(10,2),
    deposit_amount DECIMAL(10,2),
    notes TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    FOREIGN KEY (student_id) REFERENCES dorm_student(id),
    FOREIGN KEY (room_id) REFERENCES dorm_room(id)
);

-- 维修记录表
CREATE TABLE IF NOT EXISTS dorm_maintenance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id BIGINT NOT NULL,
    reporter_id BIGINT,
    type VARCHAR(15) NOT NULL CHECK (type IN ('PLUMBING', 'ELECTRICAL', 'FURNITURE', 'APPLIANCE', 'OTHER')),
    description TEXT NOT NULL,
    status VARCHAR(15) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    priority VARCHAR(10) DEFAULT 'MEDIUM' CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    report_date DATE NOT NULL,
    assign_date DATE,
    complete_date DATE,
    repair_person VARCHAR(50),
    cost DECIMAL(10,2),
    notes TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    FOREIGN KEY (room_id) REFERENCES dorm_room(id),
    FOREIGN KEY (reporter_id) REFERENCES dorm_student(id)
);

-- 访客记录表
CREATE TABLE IF NOT EXISTS dorm_visitor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    visitor_name VARCHAR(50) NOT NULL,
    visitor_phone VARCHAR(20),
    visitor_id_card VARCHAR(18),
    relationship VARCHAR(15) CHECK (relationship IN ('PARENT', 'RELATIVE', 'FRIEND', 'OTHER')),
    visit_purpose VARCHAR(200),
    visit_date DATE NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    status VARCHAR(15) DEFAULT 'REGISTERED' CHECK (status IN ('REGISTERED', 'VISITING', 'COMPLETED', 'CANCELLED')),
    notes TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    FOREIGN KEY (student_id) REFERENCES dorm_student(id)
);

-- 费用记录表
CREATE TABLE IF NOT EXISTS dorm_fee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    type VARCHAR(15) NOT NULL CHECK (type IN ('RENT', 'DEPOSIT', 'UTILITY', 'DAMAGE', 'OTHER')),
    amount DECIMAL(10,2) NOT NULL,
    due_date DATE NOT NULL,
    pay_date DATE,
    status VARCHAR(15) DEFAULT 'UNPAID' CHECK (status IN ('UNPAID', 'PAID', 'OVERDUE', 'CANCELLED')),
    description TEXT,
    semester VARCHAR(10) CHECK (semester IN ('SPRING', 'FALL')),
    academic_year VARCHAR(10),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    FOREIGN KEY (student_id) REFERENCES dorm_student(id)
);

-- 通知公告表
CREATE TABLE IF NOT EXISTS dorm_notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(15) DEFAULT 'GENERAL' CHECK (type IN ('GENERAL', 'URGENT', 'MAINTENANCE', 'EVENT')),
    target_audience VARCHAR(15) DEFAULT 'ALL' CHECK (target_audience IN ('ALL', 'MALE', 'FEMALE', 'BUILDING')),
    building_id BIGINT,
    publish_date DATE NOT NULL,
    expire_date DATE,
    status VARCHAR(15) DEFAULT 'PUBLISHED' CHECK (status IN ('DRAFT', 'PUBLISHED', 'EXPIRED', 'CANCELLED')),
    publisher VARCHAR(50),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    FOREIGN KEY (building_id) REFERENCES dorm_building(id)
);

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('ADMIN', 'MANAGER', 'USER')),
    status TINYINT DEFAULT 1,
    last_login_time TIMESTAMP,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- 创建索引
CREATE INDEX idx_building_type ON dorm_building(type);
CREATE INDEX idx_room_building ON dorm_room(building_id);
CREATE INDEX idx_room_status ON dorm_room(status);
CREATE INDEX idx_student_number ON dorm_student(student_number);
CREATE INDEX idx_student_status ON dorm_student(status);
CREATE INDEX idx_accommodation_student ON dorm_accommodation(student_id);
CREATE INDEX idx_accommodation_room ON dorm_accommodation(room_id);
CREATE INDEX idx_accommodation_status ON dorm_accommodation(status);
CREATE INDEX idx_maintenance_room ON dorm_maintenance(room_id);
CREATE INDEX idx_maintenance_status ON dorm_maintenance(status);
CREATE INDEX idx_visitor_student ON dorm_visitor(student_id);
CREATE INDEX idx_visitor_date ON dorm_visitor(visit_date);
CREATE INDEX idx_announcement_status ON dorm_notice(status);
CREATE INDEX idx_announcement_date ON dorm_notice(publish_date);
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_role ON sys_user(role);