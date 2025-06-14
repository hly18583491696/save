package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormAccommodation;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormRoom;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormAccommodationMapper;
import edu.mycc.xhd.mycsdormitorymanagement.service.StudentService;
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
    
    @Autowired
    private StudentService studentService;
    
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
        
        // 根据studentId获取学生信息并设置相关字段
        if (accommodation.getStudentId() != null) {
            Student student = studentService.getStudentById(accommodation.getStudentId());
            if (student != null) {
                accommodation.setStudentNumber(student.getStudentNumber());
                accommodation.setStudentName(student.getStudentName());
                accommodation.setClassName(student.getClassName());
                accommodation.setIdCard(student.getIdCard());
            } else {
                throw new RuntimeException("学生信息不存在");
            }
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
     * 同步更新住宿记录中的学生信息
     */
    @Transactional
    public boolean updateStudentInfoInAccommodations(Student student) {
        try {
            log.info("开始同步更新住宿记录中的学生信息，学生ID: {}", student.getId());
            
            // 查找该学生的所有住宿记录
            List<DormAccommodation> accommodations = accommodationMapper.findByStudentId(student.getId());
            
            if (accommodations.isEmpty()) {
                log.info("学生没有住宿记录，无需同步更新，学生ID: {}", student.getId());
                return true;
            }
            
            // 更新每条住宿记录中的学生信息
            for (DormAccommodation accommodation : accommodations) {
                accommodation.setStudentName(student.getStudentName());
                accommodation.setStudentNumber(student.getStudentNumber());
                accommodation.setClassName(student.getClassName());
                accommodation.setIdCard(student.getIdCard());
                accommodation.setUpdateTime(LocalDateTime.now());
                
                int updateResult = accommodationMapper.updateById(accommodation);
                if (updateResult <= 0) {
                    log.error("更新住宿记录失败，住宿记录ID: {}", accommodation.getId());
                    throw new RuntimeException("更新住宿记录失败");
                }
            }
            
            log.info("成功同步更新{}条住宿记录中的学生信息，学生ID: {}", accommodations.size(), student.getId());
            return true;
            
        } catch (Exception e) {
            log.error("同步更新住宿记录中的学生信息失败，学生ID: {}, 错误: {}", student.getId(), e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 删除住宿记录（物理删除）
     */
    @Transactional
    public boolean deleteAccommodation(Long id) {
        log.info("开始物理删除住宿记录，ID: {}", id);
        
        // 先查询记录是否存在
        DormAccommodation existing = accommodationMapper.selectById(id);
        if (existing == null) {
            log.warn("要删除的住宿记录不存在，ID: {}", id);
            return false;
        }
        
        log.info("找到住宿记录，学生: {}, 准备进行物理删除", existing.getStudentName());
        
        // 执行物理删除 - 直接从数据库中删除记录
        boolean result = accommodationMapper.deleteByIdPhysically(id) > 0;
        log.info("数据库物理删除结果: {}", result ? "成功" : "失败");
        
        if (result) {
            log.info("成功物理删除住宿记录，记录ID: {}, 学生: {}", existing.getId(), existing.getStudentName());
        }
        
        return result;
    }
    
    /**
     * 获取已入住房间数量
     */
    public int getOccupiedRoomsCount() {
        return accommodationMapper.countOccupiedRooms();
    }
    
    /**
     * 根据学生ID删除住宿记录（物理删除）
     */
    @Transactional
    public boolean deleteAccommodationsByStudentId(Long studentId) {
        log.info("开始物理删除学生ID为{}的所有住宿记录", studentId);
        
        // 查找该学生的所有住宿记录（用于日志记录）
        List<DormAccommodation> accommodations = accommodationMapper.findByStudentId(studentId);
        
        if (accommodations.isEmpty()) {
            log.info("学生ID为{}的住宿记录不存在", studentId);
            return true; // 没有记录也算成功
        }
        
        // 批量物理删除该学生的所有住宿记录
        int deletedCount = accommodationMapper.deleteByStudentIdPhysically(studentId);
        
        if (deletedCount > 0) {
            log.info("成功物理删除学生ID为{}的所有住宿记录，共{}条", studentId, deletedCount);
            return true;
        } else {
            log.error("物理删除学生ID为{}的住宿记录失败", studentId);
            return false;
        }
    }
}