package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormVisitor;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormVisitorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 访客管理服务类
 */
@Service
public class DormVisitorService {
    
    @Autowired
    private DormVisitorMapper dormVisitorMapper;
    
    /**
     * 获取所有访客记录
     */
    public List<DormVisitor> getAllVisitors() {
        // 使用QueryWrapper过滤已删除的记录
        return dormVisitorMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<DormVisitor>()
                .eq("deleted", 0)
                .orderByDesc("visit_time")
        );
    }
    
    /**
     * 根据ID获取访客记录
     */
    public DormVisitor getVisitorById(Long id) {
        return dormVisitorMapper.selectById(id);
    }
    
    /**
     * 根据被访问学生ID获取访客记录
     */
    public List<DormVisitor> getVisitorsByStudentId(Long studentId) {
        return dormVisitorMapper.findByVisitedStudentId(studentId);
    }
    
    /**
     * 根据房间ID获取访客记录
     */
    public List<DormVisitor> getVisitorsByRoomId(Long roomId) {
        return dormVisitorMapper.findByRoomId(roomId);
    }
    
    /**
     * 根据楼栋ID获取访客记录
     */
    public List<DormVisitor> getVisitorsByBuildingId(Long buildingId) {
        return dormVisitorMapper.findByBuildingId(buildingId);
    }
    
    /**
     * 根据状态获取访客记录
     */
    public List<DormVisitor> getVisitorsByStatus(String status) {
        return dormVisitorMapper.findByStatus(status);
    }
    
    /**
     * 根据访客姓名模糊查询
     */
    public List<DormVisitor> searchByVisitorName(String visitorName) {
        return dormVisitorMapper.findByVisitorNameLike(visitorName);
    }
    
    /**
     * 根据被访问学生姓名模糊查询
     */
    public List<DormVisitor> searchByStudentName(String studentName) {
        return dormVisitorMapper.findByVisitedStudentNameLike(studentName);
    }
    
    /**
     * 根据时间范围查询访客记录
     */
    public List<DormVisitor> getVisitorsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return dormVisitorMapper.findByVisitTimeBetween(startTime, endTime);
    }
    
    /**
     * 获取当前正在访问的记录
     */
    public List<DormVisitor> getCurrentVisiting() {
        return dormVisitorMapper.findCurrentVisiting();
    }
    
    /**
     * 获取过期未离开的访客记录
     */
    public List<DormVisitor> getExpiredVisitors() {
        return dormVisitorMapper.findExpiredVisitors();
    }
    
    /**
     * 创建访客记录
     */
    @Transactional
    public boolean createVisitor(DormVisitor visitor) {
        visitor.setCreateTime(LocalDateTime.now());
        visitor.setUpdateTime(LocalDateTime.now());
        visitor.setDeleted(0);
        if (visitor.getStatus() == null) {
            visitor.setStatus("VISITING");
        }
        return dormVisitorMapper.insert(visitor) > 0;
    }
    
    /**
     * 更新访客记录
     */
    @Transactional
    public boolean updateVisitor(DormVisitor visitor) {
        visitor.setUpdateTime(LocalDateTime.now());
        return dormVisitorMapper.updateById(visitor) > 0;
    }
    
    /**
     * 删除访客记录（物理删除）
     */
    @Transactional
    public boolean deleteVisitor(Long id) {
        return dormVisitorMapper.deleteById(id) > 0;
    }
    
    /**
     * 访客离开登记
     */
    @Transactional
    public boolean visitorLeave(Long id) {
        return dormVisitorMapper.updateLeaveStatus(id, LocalDateTime.now()) > 0;
    }
    
    /**
     * 批量更新过期访客状态
     */
    @Transactional
    public int updateExpiredVisitors() {
        return dormVisitorMapper.updateExpiredVisitors();
    }
    
    /**
     * 获取访客统计信息
     */
    public Map<String, Object> getVisitorStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 今日访客数量
        int todayCount = dormVisitorMapper.countTodayVisitors();
        statistics.put("todayCount", todayCount);
        
        // 本月访客数量
        int thisMonthCount = dormVisitorMapper.countThisMonthVisitors();
        statistics.put("thisMonthCount", thisMonthCount);
        
        // 当前访问中的数量
        List<DormVisitor> currentVisiting = getCurrentVisiting();
        statistics.put("currentVisitingCount", currentVisiting.size());
        
        // 过期未离开的数量
        List<DormVisitor> expiredVisitors = getExpiredVisitors();
        statistics.put("expiredCount", expiredVisitors.size());
        
        // 按状态统计
        List<Object> statusCounts = dormVisitorMapper.countByStatus();
        statistics.put("statusCounts", statusCounts);
        
        return statistics;
    }
    
    /**
     * 综合搜索访客记录
     */
    public List<DormVisitor> searchVisitors(String keyword, String status, LocalDateTime startTime, LocalDateTime endTime) {
        // 这里可以根据需要实现更复杂的搜索逻辑
        // 目前先实现基本的按关键词搜索
        if (keyword != null && !keyword.trim().isEmpty()) {
            return searchByVisitorName(keyword.trim());
        } else if (status != null && !status.trim().isEmpty()) {
            return getVisitorsByStatus(status);
        } else if (startTime != null && endTime != null) {
            return getVisitorsByTimeRange(startTime, endTime);
        } else {
            return getAllVisitors();
        }
    }
}