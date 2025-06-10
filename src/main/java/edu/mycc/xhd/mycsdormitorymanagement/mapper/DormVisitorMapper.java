package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormVisitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 访客记录数据访问层
 */
@Mapper
public interface DormVisitorMapper extends BaseMapper<DormVisitor> {
    
    /**
     * 根据被访问学生ID查找访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE visited_student_id = #{studentId} AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findByVisitedStudentId(Long studentId);
    
    /**
     * 根据房间ID查找访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE room_id = #{roomId} AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findByRoomId(Long roomId);
    
    /**
     * 根据楼栋ID查找访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE building_id = #{buildingId} AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findByBuildingId(Long buildingId);
    
    /**
     * 根据状态查找访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE status = #{status} AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findByStatus(String status);
    
    /**
     * 根据访客姓名模糊查询
     */
    @Select("SELECT * FROM dorm_visitor WHERE visitor_name LIKE CONCAT('%', #{visitorName}, '%') AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findByVisitorNameLike(String visitorName);
    
    /**
     * 根据被访问学生姓名模糊查询
     */
    @Select("SELECT * FROM dorm_visitor WHERE visited_student_name LIKE CONCAT('%', #{studentName}, '%') AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findByVisitedStudentNameLike(String studentName);
    
    /**
     * 根据时间范围查询访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE visit_time BETWEEN #{startTime} AND #{endTime} AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findByVisitTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询当前正在访问的记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE status = 'VISITING' AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findCurrentVisiting();
    
    /**
     * 查询过期未离开的访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE status = 'VISITING' AND expected_leave_time < NOW() AND deleted = 0 ORDER BY visit_time DESC")
    List<DormVisitor> findExpiredVisitors();
    
    /**
     * 更新访客离开状态
     */
    @Update("UPDATE dorm_visitor SET status = 'LEFT', actual_leave_time = #{leaveTime}, update_time = NOW() WHERE id = #{id}")
    int updateLeaveStatus(Long id, LocalDateTime leaveTime);
    
    /**
     * 批量更新过期访客状态
     */
    @Update("UPDATE dorm_visitor SET status = 'EXPIRED', update_time = NOW() WHERE status = 'VISITING' AND expected_leave_time < NOW() AND deleted = 0")
    int updateExpiredVisitors();
    
    /**
     * 统计访客数量按状态
     */
    @Select("SELECT status, COUNT(*) as count FROM dorm_visitor WHERE deleted = 0 GROUP BY status")
    List<Object> countByStatus();
    
    /**
     * 统计今日访客数量
     */
    @Select("SELECT COUNT(*) FROM dorm_visitor WHERE DATE(visit_time) = CURDATE() AND deleted = 0")
    int countTodayVisitors();
    
    /**
     * 统计本月访客数量
     */
    @Select("SELECT COUNT(*) FROM dorm_visitor WHERE YEAR(visit_time) = YEAR(NOW()) AND MONTH(visit_time) = MONTH(NOW()) AND deleted = 0")
    int countThisMonthVisitors();
}