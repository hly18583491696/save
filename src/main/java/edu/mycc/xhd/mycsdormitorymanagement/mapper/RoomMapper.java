package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 宿舍房间Mapper接口
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    
    /**
     * 根据房间号查询
     */
    @Select("SELECT * FROM dorm_room WHERE room_number = #{roomNumber} AND deleted = 0")
    Room findByRoomNumber(String roomNumber);
    
    /**
     * 根据楼栋ID查询房间列表
     */
    @Select("SELECT * FROM dorm_room WHERE building_id = #{buildingId} AND deleted = 0")
    List<Room> findByBuildingId(Long buildingId);
    
    /**
     * 根据房间状态查询
     */
    @Select("SELECT * FROM dorm_room WHERE status = #{status} AND deleted = 0")
    List<Room> findByStatus(String status);
    
    /**
     * 查询可用房间（有空床位）
     */
    @Select("SELECT * FROM dorm_room WHERE available_beds > 0 AND status = 'AVAILABLE' AND deleted = 0")
    List<Room> findAvailableRooms();
    
    /**
     * 更新房间床位数
     */
    @Update("UPDATE dorm_room SET occupied_beds = #{occupiedBeds}, available_beds = #{availableBeds} WHERE id = #{id}")
    int updateBedCount(Long id, Integer occupiedBeds, Integer availableBeds);
}