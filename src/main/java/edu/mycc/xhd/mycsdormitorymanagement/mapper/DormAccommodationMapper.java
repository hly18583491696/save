package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormAccommodation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 住宿记录数据访问层
 */
@Mapper
public interface DormAccommodationMapper extends BaseMapper<DormAccommodation> {
    
    /**
     * 根据学生ID查找住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE student_id = #{studentId} AND deleted = 0")
    List<DormAccommodation> findByStudentId(Long studentId);
    
    /**
     * 根据房间ID查找住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE room_id = #{roomId} AND deleted = 0")
    List<DormAccommodation> findByRoomId(Long roomId);
    
    /**
     * 根据学生ID查找当前有效的住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE student_id = #{studentId} AND status = 'ACTIVE' AND deleted = 0")
    DormAccommodation findActiveByStudentId(Long studentId);
    
    /**
     * 根据房间ID和床位号查找住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE room_id = #{roomId} AND bed_number = #{bedNumber} AND status = 'ACTIVE' AND deleted = 0")
    DormAccommodation findByRoomIdAndBedNumber(Long roomId, String bedNumber);
    
    /**
     * 根据状态查找住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE status = #{status} AND deleted = 0")
    List<DormAccommodation> findByStatus(String status);
    
    /**
     * 根据ID查找住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE id = #{id} AND deleted = 0")
    DormAccommodation selectById(Long id);
    
    /**
     * 查询所有有效的住宿记录
     */
    @Select("SELECT * FROM dorm_accommodation WHERE deleted = 0 ORDER BY check_in_date DESC")
    List<DormAccommodation> findAllActive();
    
    /**
     * 统计已入住房间数量
     */
    @Select("SELECT COUNT(DISTINCT room_id) FROM dorm_accommodation WHERE status = 'ACTIVE' AND deleted = 0")
    int countOccupiedRooms();
}