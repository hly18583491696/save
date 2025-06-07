package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 宿舍房间数据访问层
 */
@Mapper
public interface DormRoomMapper extends BaseMapper<DormRoom> {
    
    /**
     * 根据楼栋ID查找房间
     */
    @Select("SELECT * FROM dorm_room WHERE building_id = #{buildingId} AND deleted = 0 ORDER BY floor, room_number")
    List<DormRoom> findByBuildingId(Long buildingId);
    
    /**
     * 根据房间状态查找房间
     */
    @Select("SELECT * FROM dorm_room WHERE status = #{status} AND deleted = 0")
    List<DormRoom> findByStatus(String status);
    
    /**
     * 根据楼栋ID和房间号查找房间
     */
    @Select("SELECT * FROM dorm_room WHERE building_id = #{buildingId} AND room_number = #{roomNumber} AND deleted = 0")
    DormRoom findByBuildingIdAndRoomNumber(Long buildingId, String roomNumber);
    
    /**
     * 查询可用房间（有空床位）
     */
    @Select("SELECT * FROM dorm_room WHERE current_count < capacity AND status = 'AVAILABLE' AND deleted = 0")
    List<DormRoom> findAvailableRooms();
    
    /**
     * 根据性别查找可用房间
     */
    @Select("SELECT dr.* FROM dorm_room dr JOIN dorm_building db ON dr.building_id = db.id WHERE dr.status = 'AVAILABLE' AND dr.current_count < dr.capacity AND db.type = #{gender} AND dr.deleted = 0")
    List<DormRoom> findAvailableRoomsByGender(String gender);
    
    /**
     * 根据楼栋ID查找可用房间
     */
    @Select("SELECT * FROM dorm_room WHERE building_id = #{buildingId} AND status = 'AVAILABLE' AND current_count < capacity AND deleted = 0")
    List<DormRoom> findAvailableRoomsByBuildingId(Long buildingId);
}