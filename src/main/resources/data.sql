-- 宿舍管理系统初始化数据

-- 插入楼栋信息
INSERT INTO dorm_building (id, name, type, floors, rooms_per_floor, manager_name, manager_phone, address, description, created_time, updated_time, deleted) VALUES
(1, '学生公寓1号楼', 'MALE', 6, 20, '张建国', '13800138001', '校园东区', '男生宿舍楼，设施齐全', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(2, '学生公寓2号楼', 'FEMALE', 6, 20, '李美华', '13800138002', '校园东区', '女生宿舍楼，环境优美', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(3, '学生公寓3号楼', 'MALE', 8, 25, '王志强', '13800138003', '校园西区', '新建男生宿舍楼', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(4, '学生公寓4号楼', 'FEMALE', 8, 25, '赵丽娟', '13800138004', '校园西区', '新建女生宿舍楼', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 插入房间信息
INSERT INTO dorm_room (id, building_id, room_number, floor, capacity, current_count, type, facilities, rent, status, description, created_time, updated_time, deleted) VALUES
-- 1号楼房间
(101, 1, '101', 1, 4, 4, 'STANDARD', '空调,热水器,独立卫生间,阳台', 800.00, 'OCCUPIED', '标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(102, 1, '102', 1, 4, 3, 'STANDARD', '空调,热水器,独立卫生间,阳台', 800.00, 'AVAILABLE', '标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(103, 1, '103', 1, 4, 2, 'STANDARD', '空调,热水器,独立卫生间,阳台', 800.00, 'AVAILABLE', '标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(201, 1, '201', 2, 4, 4, 'STANDARD', '空调,热水器,独立卫生间,阳台', 800.00, 'OCCUPIED', '标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(202, 1, '202', 2, 4, 1, 'STANDARD', '空调,热水器,独立卫生间,阳台', 800.00, 'AVAILABLE', '标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
-- 2号楼房间
(301, 2, '101', 1, 4, 4, 'STANDARD', '空调,热水器,独立卫生间,阳台,化妆镜', 850.00, 'OCCUPIED', '女生标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(302, 2, '102', 1, 4, 3, 'STANDARD', '空调,热水器,独立卫生间,阳台,化妆镜', 850.00, 'AVAILABLE', '女生标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(303, 2, '103', 1, 4, 2, 'STANDARD', '空调,热水器,独立卫生间,阳台,化妆镜', 850.00, 'AVAILABLE', '女生标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(401, 2, '201', 2, 4, 4, 'STANDARD', '空调,热水器,独立卫生间,阳台,化妆镜', 850.00, 'OCCUPIED', '女生标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(402, 2, '202', 2, 4, 0, 'STANDARD', '空调,热水器,独立卫生间,阳台,化妆镜', 850.00, 'AVAILABLE', '女生标准四人间', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 插入学生信息
INSERT INTO dorm_student (id, student_number, name, gender, grade, major, class_name, phone, email, emergency_contact, emergency_phone, id_card, address, status, created_time, updated_time, deleted) VALUES
(1, '2021001001', '张三', 'MALE', '2021', '计算机科学与技术', '计科2101', '13900139001', 'zhangsan@student.edu.cn', '张父', '13700137001', '110101200301011234', '北京市朝阳区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(2, '2021001002', '李四', 'MALE', '2021', '计算机科学与技术', '计科2101', '13900139002', 'lisi@student.edu.cn', '李母', '13700137002', '110101200301021234', '北京市海淀区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(3, '2021001003', '王五', 'MALE', '2021', '计算机科学与技术', '计科2101', '13900139003', 'wangwu@student.edu.cn', '王父', '13700137003', '110101200301031234', '北京市西城区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(4, '2021001004', '赵六', 'MALE', '2021', '计算机科学与技术', '计科2101', '13900139004', 'zhaoliu@student.edu.cn', '赵母', '13700137004', '110101200301041234', '北京市东城区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(5, '2021002001', '小红', 'FEMALE', '2021', '软件工程', '软工2101', '13900139005', 'xiaohong@student.edu.cn', '红父', '13700137005', '110101200301051234', '上海市浦东新区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(6, '2021002002', '小美', 'FEMALE', '2021', '软件工程', '软工2101', '13900139006', 'xiaomei@student.edu.cn', '美母', '13700137006', '110101200301061234', '上海市黄浦区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(7, '2021002003', '小丽', 'FEMALE', '2021', '软件工程', '软工2101', '13900139007', 'xiaoli@student.edu.cn', '丽父', '13700137007', '110101200301071234', '广州市天河区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(8, '2021002004', '小芳', 'FEMALE', '2021', '软件工程', '软工2101', '13900139008', 'xiaofang@student.edu.cn', '芳母', '13700137008', '110101200301081234', '深圳市南山区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(9, '2022001001', '陈明', 'MALE', '2022', '计算机科学与技术', '计科2201', '13900139009', 'chenming@student.edu.cn', '陈父', '13700137009', '110101200401011234', '杭州市西湖区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(10, '2022002001', '刘娜', 'FEMALE', '2022', '软件工程', '软工2201', '13900139010', 'liuna@student.edu.cn', '刘母', '13700137010', '110101200401021234', '南京市鼓楼区', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 插入住宿记录
INSERT INTO dorm_accommodation (id, student_id, room_id, check_in_date, check_out_date, semester, academic_year, status, rent_amount, deposit_amount, notes, created_time, updated_time, deleted) VALUES
(1, 1, 101, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 800.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(2, 2, 101, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 800.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(3, 3, 101, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 800.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(4, 4, 101, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 800.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(5, 5, 301, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 850.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(6, 6, 301, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 850.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(7, 7, 301, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 850.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(8, 8, 301, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 850.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(9, 9, 102, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 800.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(10, 10, 302, '2023-09-01', NULL, 'FALL', '2023-2024', 'LIVING', 850.00, 500.00, '正常入住', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 插入维修记录
INSERT INTO dorm_maintenance (id, room_id, reporter_id, type, description, status, priority, report_date, assign_date, complete_date, repair_person, cost, notes, created_time, updated_time, deleted) VALUES
(1, 101, 1, 'PLUMBING', '水龙头漏水', 'COMPLETED', 'MEDIUM', '2024-01-15', '2024-01-15', '2024-01-16', '李师傅', 50.00, '已更换水龙头', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(2, 301, 5, 'ELECTRICAL', '插座不通电', 'IN_PROGRESS', 'HIGH', '2024-01-20', '2024-01-20', NULL, '王师傅', NULL, '正在检修线路', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(3, 102, 9, 'FURNITURE', '桌子松动', 'PENDING', 'LOW', '2024-01-22', NULL, NULL, NULL, NULL, '等待安排维修', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(4, 201, 1, 'APPLIANCE', '空调制冷效果差', 'ASSIGNED', 'MEDIUM', '2024-01-25', '2024-01-25', NULL, '张师傅', NULL, '已安排师傅检修', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 插入访客记录
INSERT INTO dorm_visitor (id, student_id, visitor_name, visitor_phone, visitor_id_card, relationship, visit_purpose, visit_date, check_in_time, check_out_time, status, notes, created_time, updated_time, deleted) VALUES
(1, 1, '张父', '13700137001', '110101197501011234', 'PARENT', '探望学生', '2024-01-10', '2024-01-10 14:00:00', '2024-01-10 18:00:00', 'COMPLETED', '正常探访', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(2, 5, '红母', '13700137005', '110101197502011234', 'PARENT', '送生活用品', '2024-01-15', '2024-01-15 10:00:00', '2024-01-15 12:00:00', 'COMPLETED', '正常探访', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(3, 3, '王朋友', '13800138888', '110101200001011234', 'FRIEND', '同学聚会', '2024-01-20', '2024-01-20 19:00:00', NULL, 'VISITING', '朋友来访', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

-- 插入通知公告
INSERT INTO dorm_notice (id, title, content, type, target_audience, building_id, publish_date, expire_date, status, publisher, created_time, updated_time, deleted) VALUES
(1, '宿舍卫生检查通知', '各位同学：\n\n为了营造良好的宿舍环境，学校将于下周进行宿舍卫生检查。请各位同学做好以下准备：\n1. 保持宿舍整洁\n2. 垃圾及时清理\n3. 个人物品摆放整齐\n\n检查时间：2024年2月1日-2月3日\n\n学生处\n2024年1月25日', 'GENERAL', 'ALL', NULL, '2024-01-25', '2024-02-05', 'PUBLISHED', '学生处', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(2, '春节放假宿舍管理规定', '各位同学：\n\n春节假期即将到来，现就假期宿舍管理规定通知如下：\n1. 离校学生需办理离校手续\n2. 留校学生需登记备案\n3. 假期期间宿舍楼实行封闭管理\n4. 紧急情况联系值班老师\n\n值班电话：010-12345678\n\n祝大家春节快乐！\n\n宿管中心\n2024年1月20日', 'GENERAL', 'ALL', NULL, '2024-01-20', '2024-02-20', 'PUBLISHED', '宿管中心', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
(3, '宿舍网络升级通知', '各位同学：\n\n为提升宿舍网络质量，将于本周末进行网络设备升级：\n\n升级时间：2024年1月27日22:00 - 2024年1月28日06:00\n影响范围：全部宿舍楼\n\n升级期间网络将暂时中断，请同学们提前做好准备。\n\n网络中心\n2024年1月24日', 'MAINTENANCE', 'ALL', NULL, '2024-01-24', '2024-01-30', 'PUBLISHED', '网络中心', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);