package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormAccommodation;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormRoom;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormAccommodationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 住宿记录服务类
 */
@Service
public class DormAccommodationService {
    
    private static final Logger log = LoggerFactory.getLogger(DormAccommodationService.class);
    
    @Autowired
    private DormAccommodationMapper accommodationMapper;
    
    @Autowired
    private DormRoomService dormRoomService;
    
    /**
     * 获取所有住宿记录
     */
    public List<DormAccommodation> getAllAccommodations() {
        return accommodationMapper.findAllActive();
    }
    
    /**
     * 根据ID获取住宿记录
     */
    public DormAccommodation getAccommodationById(Long id) {
        return accommodationMapper.selectById(id);
    }
    
    /**
     * 根据学生ID获取住宿记录
     */
    public List<DormAccommodation> getAccommodationsByStudentId(Long studentId) {
        return accommodationMapper.findByStudentId(studentId);
    }
    
    /**
     * 根据房间ID获取住宿记录
     */
    public List<DormAccommodation> getAccommodationsByRoomId(Long roomId) {
        return accommodationMapper.findByRoomId(roomId);
    }
    
    /**
     * 根据学生ID获取当前有效的住宿记录
     */
    public DormAccommodation getActiveAccommodationByStudentId(Long studentId) {
        return accommodationMapper.findActiveByStudentId(studentId);
    }
    
    /**
     * 检查床位是否被占用
     */
    public boolean isBedOccupied(Long roomId, String bedNumber) {
        DormAccommodation accommodation = accommodationMapper.findByRoomIdAndBedNumber(roomId, bedNumber);
        return accommodation != null;
    }
    
    /**
     * 分配宿舍
     */
    @Transactional
    public boolean assignAccommodation(DormAccommodation accommodation) {
        // 检查学生是否已有住宿记录
        DormAccommodation existingAccommodation = accommodationMapper.findActiveByStudentId(accommodation.getStudentId());
        if (existingAccommodation != null) {
            throw new RuntimeException("学生已有住宿记录，请先退宿");
        }
        
        // 检查床位是否被占用
        if (isBedOccupied(accommodation.getRoomId(), accommodation.getBedNumber())) {
            throw new RuntimeException("该床位已被占用");
        }
        
        // 检查房间是否存在且有空位
        DormRoom room = dormRoomService.getRoomById(accommodation.getRoomId());
        if (room == null) {
            throw new RuntimeException("房间不存在");
        }
        
        if (room.getCurrentCount() >= room.getCapacity()) {
            throw new RuntimeException("房间已满");
        }
        
        accommodation.setCreateTime(LocalDateTime.now());
        accommodation.setUpdateTime(LocalDateTime.now());
        accommodation.setDeleted(0);
        
        // 设置默认状态为活跃
        if (accommodation.getStatus() == null || accommodation.getStatus().isEmpty()) {
            accommodation.setStatus("ACTIVE");
        }
        
        boolean success = accommodationMapper.insert(accommodation) > 0;
        
        if (success) {
            // 更新房间入住人数
            dormRoomService.updateRoomOccupancy(accommodation.getRoomId(), room.getCurrentCount() + 1);
        }
        
        return success;
    }
    
    /**
     * 更新住宿记录
     */
    public boolean updateAccommodation(DormAccommodation accommodation) {
        accommodation.setUpdateTime(LocalDateTime.now());
        return accommodationMapper.updateById(accommodation) > 0;
    }
    
    /**
     * 退宿
     */
    @Transactional
    public boolean checkOut(Long accommodationId) {
        DormAccommodation accommodation = accommodationMapper.selectById(accommodationId);
        if (accommodation == null) {
            throw new RuntimeException("住宿记录不存在");
        }
        
        // 更新住宿记录状态
        accommodation.setStatus("INACTIVE");
        accommodation.setCheckOutDate(LocalDate.now());
        accommodation.setUpdateTime(LocalDateTime.now());
        
        boolean success = accommodationMapper.updateById(accommodation) > 0;
        
        if (success) {
            // 更新房间入住人数
            DormRoom room = dormRoomService.getRoomById(accommodation.getRoomId());
            if (room != null && room.getCurrentCount() > 0) {
                dormRoomService.updateRoomOccupancy(accommodation.getRoomId(), room.getCurrentCount() - 1);
            }
        }
        
        return success;
    }
    
    /**
     * 根据学生ID退宿
     */
    @Transactional
    public boolean checkOutByStudentId(Long studentId) {
        DormAccommodation accommodation = accommodationMapper.findActiveByStudentId(studentId);
        if (accommodation == null) {
            throw new RuntimeException("学生没有有效的住宿记录");
        }
        
        return checkOut(accommodation.getId());
    }
    
    /**
     * 删除住宿记录（逻辑删除）
     */
    public boolean deleteAccommodation(Long id) {
        log.info("开始删除住宿记录，ID: {}", id);
        
        // 先查询记录是否存在
        DormAccommodation existing = accommodationMapper.selectById(id);
        if (existing == null) {
            log.warn("要删除的住宿记录不存在，ID: {}", id);
            return false;
        }
        
        log.info("找到住宿记录，学生: {}, 当前状态: deleted={}", existing.getStudentName(), existing.getDeleted());
        
        DormAccommodation accommodation = new DormAccommodation();
        accommodation.setId(id);
        accommodation.setDeleted(1);
        accommodation.setUpdateTime(LocalDateTime.now());
        
        int result = accommodationMapper.updateById(accommodation);
        log.info("数据库更新结果: {}, 受影响行数: {}", result > 0 ? "成功" : "失败", result);
        
        return result > 0;
    }
    
    /**
     * 获取已入住房间数量
     */
    public int getOccupiedRoomsCount() {
        return accommodationMapper.countOccupiedRooms();
    }
}