package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Visitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 访客记录Mapper接口
 */
@Mapper
public interface VisitorMapper extends BaseMapper<Visitor> {
    
    /**
     * 根据被访问学生ID查询访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE student_id = #{studentId} AND deleted = 0")
    List<Visitor> findByStudentId(Long studentId);
    
    /**
     * 根据房间ID查询访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE room_id = #{roomId} AND deleted = 0")
    List<Visitor> findByRoomId(Long roomId);
    
    /**
     * 根据状态查询访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE status = #{status} AND deleted = 0")
    List<Visitor> findByStatus(String status);
    
    /**
     * 根据访客身份证号查询
     */
    @Select("SELECT * FROM dorm_visitor WHERE id_card = #{idCard} AND deleted = 0")
    List<Visitor> findByIdCard(String idCard);
    
    /**
     * 根据审核人ID查询访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE reviewer_id = #{reviewerId} AND deleted = 0")
    List<Visitor> findByReviewerId(Long reviewerId);
    
    /**
     * 查询待审核的访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE status = 'PENDING' AND deleted = 0")
    List<Visitor> findPendingVisitors();
    
    /**
     * 查询正在访问的访客记录
     */
    @Select("SELECT * FROM dorm_visitor WHERE status = 'IN_VISIT' AND deleted = 0")
    List<Visitor> findActiveVisitors();
}