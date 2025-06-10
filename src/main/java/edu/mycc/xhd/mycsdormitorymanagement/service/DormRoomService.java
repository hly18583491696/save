package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormRoom;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormRoomMapper;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormAccommodationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 宿舍房间服务类
 */
@Service
public class DormRoomService {
    
    @Autowired
    private DormRoomMapper dormRoomMapper;
    
    @Autowired
    private DormAccommodationMapper accommodationMapper;
    
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
    
    /**
     * 获取房间的可用床位
     */
    public List<String> getAvailableBeds(Long roomId) {
        // 获取房间信息
        DormRoom room = dormRoomMapper.selectById(roomId);
        if (room == null) {
            throw new RuntimeException("房间不存在");
        }
        
        // 获取房间总床位数
        Integer capacity = room.getCapacity();
        if (capacity == null || capacity <= 0) {
            return new ArrayList<>();
        }
        
        // 获取已占用的床位
        List<String> occupiedBeds = accommodationMapper.findOccupiedBedsByRoomId(roomId);
        
        // 生成所有床位号（1到capacity）
        List<String> allBeds = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            allBeds.add(String.valueOf(i));
        }
        
        // 过滤掉已占用的床位，返回可用床位
        return allBeds.stream()
                .filter(bed -> !occupiedBeds.contains(bed))
                .collect(Collectors.toList());
    }
}