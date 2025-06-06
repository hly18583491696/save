package edu.mycc.xhd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.entity.Accommodation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 住宿记录Mapper接口
 * 
 * @author xhd
 */
@Mapper
public interface AccommodationMapper extends BaseMapper<Accommodation> {
    
    /**
     * 根据学生ID查询当前住宿记录
     * 
     * @param studentId 学生ID
     * @return 住宿记录
     */
    Accommodation selectCurrentByStudentId(@Param("studentId") Long studentId);
    
    /**
     * 根据房间ID统计在住人数
     * 
     * @param roomId 房间ID
     * @return 在住人数
     */
    int countByRoomId(@Param("roomId") Long roomId);
    
    /**
     * 检查床位是否被占用
     * 
     * @param roomId 房间ID
     * @param bedNumber 床位号
     * @param id 住宿记录ID（排除自己）
     * @return 数量
     */
    int checkBedOccupied(@Param("roomId") Long roomId, @Param("bedNumber") String bedNumber, @Param("id") Long id);
}