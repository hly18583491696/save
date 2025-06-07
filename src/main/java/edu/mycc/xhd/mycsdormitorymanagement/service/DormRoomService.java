package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormRoom;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宿舍房间服务类
 */
@Service
public class DormRoomService {
    
    @Autowired
    private DormRoomMapper dormRoomMapper;
    
    /**
     * 获取所有房间
     */
    public List<DormRoom> getAllRooms() {
        return dormRoomMapper.selectList(null);
    }
    
    /**
     * 根据楼栋ID获取房间列表
     */
    public List<DormRoom> getRoomsByBuildingId(Long buildingId) {
        return dormRoomMapper.findByBuildingId(buildingId);
    }
    
    /**
     * 根据ID获取房间
     */
    public DormRoom getRoomById(Long id) {
        return dormRoomMapper.selectById(id);
    }
    
    /**
     * 根据状态获取房间
     */
    public List<DormRoom> getRoomsByStatus(String status) {
        return dormRoomMapper.findByStatus(status);
    }
    
    /**
     * 获取可用房间
     */
    public List<DormRoom> getAvailableRooms() {
        return dormRoomMapper.findAvailableRooms();
    }
    
    /**
     * 根据性别获取可用房间
     */
    public List<DormRoom> getAvailableRoomsByGender(String gender) {
        return dormRoomMapper.findAvailableRoomsByGender(gender);
    }
    
    /**
     * 根据楼栋ID获取可用房间
     */
    public List<DormRoom> getAvailableRoomsByBuildingId(Long buildingId) {
        return dormRoomMapper.findAvailableRoomsByBuildingId(buildingId);
    }
    
    /**
     * 添加房间
     */
    public boolean addRoom(DormRoom room) {
        // 检查房间号是否已存在
        DormRoom existing = dormRoomMapper.findByBuildingIdAndRoomNumber(room.getBuildingId(), room.getRoomNumber());
        if (existing != null) {
            throw new RuntimeException("该楼栋中房间号已存在");
        }
        
        room.setCreateTime(LocalDateTime.now());
        room.setUpdateTime(LocalDateTime.now());
        room.setDeleted(0);
        
        // 初始化当前入住人数为0
        if (room.getCurrentCount() == null) {
            room.setCurrentCount(0);
        }
        
        return dormRoomMapper.insert(room) > 0;
    }
    
    /**
     * 更新房间
     */
    public boolean updateRoom(DormRoom room) {
        room.setUpdateTime(LocalDateTime.now());
        return dormRoomMapper.updateById(room) > 0;
    }
    
    /**
     * 删除房间（逻辑删除）
     */
    public boolean deleteRoom(Long id) {
        DormRoom room = new DormRoom();
        room.setId(id);
        room.setDeleted(1);
        room.setUpdateTime(LocalDateTime.now());
        return dormRoomMapper.updateById(room) > 0;
    }
    
    /**
     * 更新房间入住人数
     */
    public void updateRoomOccupancy(Long roomId, Integer occupancy) {
        DormRoom room = dormRoomMapper.selectById(roomId);
        if (room != null) {
            room.setCurrentCount(occupancy);
            dormRoomMapper.updateById(room);
        }
    }
}