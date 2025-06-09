-- 重构后的数据库设计方案
-- 移除住宿记录表中的冗余字段，通过关联查询获取学生信息
-- 这是推荐的长期解决方案，可以从根本上避免数据不一致问题

-- 备份原有的住宿记录表
CREATE TABLE `dorm_accommodation_backup` AS SELECT * FROM `dorm_accommodation`;

-- 删除原有的住宿记录表
DROP TABLE IF EXISTS `dorm_accommodation`;

-- 创建重构后的住宿记录表（移除冗余字段）
CREATE TABLE `dorm_accommodation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `check_out_date` date DEFAULT NULL COMMENT '退房日期',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE-在住,CHECKED_OUT-已退房,SUSPENDED-暂停)',
  `bed_number` varchar(10) DEFAULT NULL COMMENT '床位号',
  `remarks` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '删除标志(0-未删除,1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_room_id` (`room_id`),
  CONSTRAINT `fk_accommodation_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_accommodation_room` FOREIGN KEY (`room_id`) REFERENCES `dorm_room` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='住宿记录表（重构版）';

-- 从备份表中迁移数据（只保留核心字段）
INSERT INTO `dorm_accommodation` 
(`student_id`, `room_id`, `check_in_date`, `check_out_date`, `status`, `bed_number`, `remarks`, `create_time`, `update_time`, `deleted`)
SELECT 
`student_id`, `room_id`, `check_in_date`, `check_out_date`, `status`, `bed_number`, `remarks`, `create_time`, `update_time`, `deleted`
FROM `dorm_accommodation_backup`
WHERE `student_id` IN (SELECT `id` FROM `student`);

-- 创建视图：住宿记录详细信息视图（包含学生和房间信息）
CREATE OR REPLACE VIEW `v_accommodation_detail` AS
SELECT 
    a.`id` AS `accommodation_id`,
    a.`student_id`,
    s.`student_name`,
    s.`student_number`,
    s.`class_name`,
    s.`id_card`,
    s.`phone`,
    s.`email`,
    a.`room_id`,
    r.`room_number`,
    r.`building_id`,
    b.`building_name`,
    a.`check_in_date`,
    a.`check_out_date`,
    a.`status`,
    a.`bed_number`,
    a.`remarks`,
    a.`create_time`,
    a.`update_time`,
    a.`deleted`
FROM `dorm_accommodation` a
LEFT JOIN `student` s ON a.`student_id` = s.`id`
LEFT JOIN `dorm_room` r ON a.`room_id` = r.`id`
LEFT JOIN `dorm_building` b ON r.`building_id` = b.`id`
WHERE a.`deleted` = 0;

-- 创建索引优化查询性能
CREATE INDEX `idx_accommodation_status` ON `dorm_accommodation` (`status`);
CREATE INDEX `idx_accommodation_check_in_date` ON `dorm_accommodation` (`check_in_date`);
CREATE INDEX `idx_accommodation_student_status` ON `dorm_accommodation` (`student_id`, `status`);

-- 创建存储过程：获取学生住宿详情
DELIMITER //
CREATE PROCEDURE `sp_get_student_accommodation_detail`(
    IN p_student_id BIGINT
)
BEGIN
    SELECT * FROM `v_accommodation_detail` 
    WHERE `student_id` = p_student_id 
    AND `deleted` = 0
    ORDER BY `check_in_date` DESC;
END//

-- 创建存储过程：获取房间住宿详情
CREATE PROCEDURE `sp_get_room_accommodation_detail`(
    IN p_room_id BIGINT
)
BEGIN
    SELECT * FROM `v_accommodation_detail` 
    WHERE `room_id` = p_room_id 
    AND `deleted` = 0
    ORDER BY `check_in_date` DESC;
END//

-- 创建存储过程：获取楼栋住宿统计
CREATE PROCEDURE `sp_get_building_accommodation_stats`(
    IN p_building_id BIGINT
)
BEGIN
    SELECT 
        b.`building_name`,
        COUNT(DISTINCT a.`room_id`) AS `occupied_rooms`,
        COUNT(a.`id`) AS `total_students`,
        COUNT(CASE WHEN a.`status` = 'ACTIVE' THEN 1 END) AS `active_students`,
        COUNT(CASE WHEN a.`status` = 'CHECKED_OUT' THEN 1 END) AS `checked_out_students`
    FROM `dorm_accommodation` a
    LEFT JOIN `dorm_room` r ON a.`room_id` = r.`id`
    LEFT JOIN `dorm_building` b ON r.`building_id` = b.`id`
    WHERE r.`building_id` = p_building_id 
    AND a.`deleted` = 0
    GROUP BY b.`id`, b.`building_name`;
END//

DELIMITER ;

-- 数据迁移验证查询
-- 验证数据迁移是否成功
SELECT 
    '原表记录数' AS `description`,
    COUNT(*) AS `count`
FROM `dorm_accommodation_backup`
UNION ALL
SELECT 
    '新表记录数' AS `description`,
    COUNT(*) AS `count`
FROM `dorm_accommodation`
UNION ALL
SELECT 
    '视图记录数' AS `description`,
    COUNT(*) AS `count`
FROM `v_accommodation_detail`;

-- 性能测试查询
-- 测试关联查询性能
EXPLAIN SELECT * FROM `v_accommodation_detail` WHERE `student_id` = 1;
EXPLAIN SELECT * FROM `v_accommodation_detail` WHERE `room_id` = 1;
EXPLAIN SELECT * FROM `v_accommodation_detail` WHERE `building_id` = 1 AND `status` = 'ACTIVE';

-- 清理备份表（可选，建议保留一段时间后再删除）
-- DROP TABLE IF EXISTS `dorm_accommodation_backup`;