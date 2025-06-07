package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Accommodation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 住宿记录Mapper接口
 */
@Mapper
public interface AccommodationMapper extends BaseMapper<Accommodation> {
    
    /**
     * 根据学生ID查询住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE student_id = #{studentId} AND deleted = 0")
    List<Accommodation> findByStudentId(Long studentId);
    
    /**
     * 根据房间ID查询住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE room_id = #{roomId} AND deleted = 0")
    List<Accommodation> findByRoomId(Long roomId);
    
    /**
     * 根据状态查询住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE status = #{status} AND deleted = 0")
    List<Accommodation> findByStatus(String status);
    
    /**
     * 查询学生当前住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE student_id = #{studentId} AND status = 'LIVING' AND deleted = 0")
    Accommodation findCurrentAccommodation(Long studentId);
    
    /**
     * 根据学期查询住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE semester = #{semester} AND academic_year = #{academicYear} AND deleted = 0")
    List<Accommodation> findBySemester(String semester, String academicYear);
}