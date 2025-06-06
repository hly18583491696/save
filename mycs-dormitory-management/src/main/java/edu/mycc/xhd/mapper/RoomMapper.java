package edu.mycc.xhd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 宿舍房间Mapper接口
 * 
 * @author xhd
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    
    /**
     * 根据房间号查询房间
     * 
     * @param roomNumber 房间号
     * @return 房间信息
     */
    Room selectByRoomNumber(@Param("roomNumber") String roomNumber);
    
    /**
     * 检查房间号是否存在
     * 
     * @param roomNumber 房间号
     * @param id 房间ID（排除自己）
     * @return 数量
     */
    int checkRoomNumberExists(@Param("roomNumber") String roomNumber, @Param("id") Long id);
    
    /**
     * 更新房间入住人数
     * 
     * @param roomId 房间ID
     * @param occupiedBeds 已入住人数
     */
    void updateOccupiedBeds(@Param("roomId") Long roomId, @Param("occupiedBeds") Integer occupiedBeds);
}