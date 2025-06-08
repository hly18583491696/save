package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormVisitor;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormVisitorService;
import edu.mycc.xhd.mycsdormitorymanagement.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 访客管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/visitor")
@CrossOrigin(origins = "*")
public class DormVisitorController {
    
    @Autowired
    private DormVisitorService dormVisitorService;
    
    /**
     * 获取所有访客记录
     */
    @GetMapping("/list")
    public Result<List<DormVisitor>> getAllVisitors() {
        log.info("获取所有访客记录请求");
        try {
            List<DormVisitor> visitors = dormVisitorService.getAllVisitors();
            log.info("成功获取访客记录列表，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("获取访客记录列表失败: {}", e.getMessage());
            return Result.error("获取访客记录列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取访客记录
     */
    @GetMapping("/{id}")
    public Result<DormVisitor> getVisitorById(@PathVariable Long id) {
        log.info("根据ID获取访客记录请求: {}", id);
        try {
            DormVisitor visitor = dormVisitorService.getVisitorById(id);
            if (visitor != null) {
                log.info("成功获取访客记录: {}", visitor.getVisitorName());
                return Result.success(visitor);
            } else {
                log.warn("访客记录不存在: {}", id);
                return Result.error("访客记录不存在");
            }
        } catch (Exception e) {
            log.error("获取访客记录失败: {}", e.getMessage());
            return Result.error("获取访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据被访问学生ID获取访客记录
     */
    @GetMapping("/student/{studentId}")
    public Result<List<DormVisitor>> getVisitorsByStudentId(@PathVariable Long studentId) {
        log.info("根据学生ID获取访客记录请求: {}", studentId);
        try {
            List<DormVisitor> visitors = dormVisitorService.getVisitorsByStudentId(studentId);
            log.info("成功获取学生访客记录，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("获取学生访客记录失败: {}", e.getMessage());
            return Result.error("获取学生访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据房间ID获取访客记录
     */
    @GetMapping("/room/{roomId}")
    public Result<List<DormVisitor>> getVisitorsByRoomId(@PathVariable Long roomId) {
        log.info("根据房间ID获取访客记录请求: {}", roomId);
        try {
            List<DormVisitor> visitors = dormVisitorService.getVisitorsByRoomId(roomId);
            log.info("成功获取房间访客记录，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("获取房间访客记录失败: {}", e.getMessage());
            return Result.error("获取房间访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据楼栋ID获取访客记录
     */
    @GetMapping("/building/{buildingId}")
    public Result<List<DormVisitor>> getVisitorsByBuildingId(@PathVariable Long buildingId) {
        log.info("根据楼栋ID获取访客记录请求: {}", buildingId);
        try {
            List<DormVisitor> visitors = dormVisitorService.getVisitorsByBuildingId(buildingId);
            log.info("成功获取楼栋访客记录，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("获取楼栋访客记录失败: {}", e.getMessage());
            return Result.error("获取楼栋访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据状态获取访客记录
     */
    @GetMapping("/status/{status}")
    public Result<List<DormVisitor>> getVisitorsByStatus(@PathVariable String status) {
        log.info("根据状态获取访客记录请求: {}", status);
        try {
            List<DormVisitor> visitors = dormVisitorService.getVisitorsByStatus(status);
            log.info("成功获取状态为{}的访客记录，共{}条记录", status, visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("获取访客记录失败: {}", e.getMessage());
            return Result.error("获取访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前正在访问的记录
     */
    @GetMapping("/current")
    public Result<List<DormVisitor>> getCurrentVisiting() {
        log.info("获取当前正在访问的记录请求");
        try {
            List<DormVisitor> visitors = dormVisitorService.getCurrentVisiting();
            log.info("成功获取当前访问记录，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("获取当前访问记录失败: {}", e.getMessage());
            return Result.error("获取当前访问记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取过期未离开的访客记录
     */
    @GetMapping("/expired")
    public Result<List<DormVisitor>> getExpiredVisitors() {
        log.info("获取过期未离开的访客记录请求");
        try {
            List<DormVisitor> visitors = dormVisitorService.getExpiredVisitors();
            log.info("成功获取过期访客记录，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("获取过期访客记录失败: {}", e.getMessage());
            return Result.error("获取过期访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建访客记录
     */
    @PostMapping("/create")
    public Result<String> createVisitor(@RequestBody DormVisitor visitor) {
        log.info("创建访客记录请求: {}", visitor.getVisitorName());
        try {
            boolean success = dormVisitorService.createVisitor(visitor);
            if (success) {
                log.info("成功创建访客记录: {}", visitor.getVisitorName());
                return Result.success("访客记录创建成功");
            } else {
                log.error("创建访客记录失败: {}", visitor.getVisitorName());
                return Result.error("创建访客记录失败");
            }
        } catch (Exception e) {
            log.error("创建访客记录异常: {}", e.getMessage());
            return Result.error("创建访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新访客记录
     */
    @PutMapping("/update")
    public Result<String> updateVisitor(@RequestBody DormVisitor visitor) {
        log.info("更新访客记录请求: {}", visitor.getId());
        try {
            boolean success = dormVisitorService.updateVisitor(visitor);
            if (success) {
                log.info("成功更新访客记录: {}", visitor.getId());
                return Result.success("访客记录更新成功");
            } else {
                log.error("更新访客记录失败: {}", visitor.getId());
                return Result.error("更新访客记录失败");
            }
        } catch (Exception e) {
            log.error("更新访客记录异常: {}", e.getMessage());
            return Result.error("更新访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除访客记录
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteVisitor(@PathVariable Long id) {
        log.info("删除访客记录请求: {}", id);
        try {
            boolean success = dormVisitorService.deleteVisitor(id);
            if (success) {
                log.info("成功删除访客记录: {}", id);
                return Result.success("访客记录删除成功");
            } else {
                log.error("删除访客记录失败: {}", id);
                return Result.error("删除访客记录失败");
            }
        } catch (Exception e) {
            log.error("删除访客记录异常: {}", e.getMessage());
            return Result.error("删除访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 访客离开登记
     */
    @PutMapping("/leave/{id}")
    public Result<String> visitorLeave(@PathVariable Long id) {
        log.info("访客离开登记请求: {}", id);
        try {
            boolean success = dormVisitorService.visitorLeave(id);
            if (success) {
                log.info("成功登记访客离开: {}", id);
                return Result.success("访客离开登记成功");
            } else {
                log.error("访客离开登记失败: {}", id);
                return Result.error("访客离开登记失败");
            }
        } catch (Exception e) {
            log.error("访客离开登记异常: {}", e.getMessage());
            return Result.error("访客离开登记失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量更新过期访客状态
     */
    @PutMapping("/update-expired")
    public Result<String> updateExpiredVisitors() {
        log.info("批量更新过期访客状态请求");
        try {
            int count = dormVisitorService.updateExpiredVisitors();
            log.info("成功更新{}条过期访客记录", count);
            return Result.success("成功更新" + count + "条过期访客记录");
        } catch (Exception e) {
            log.error("更新过期访客状态异常: {}", e.getMessage());
            return Result.error("更新过期访客状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索访客记录
     */
    @GetMapping("/search")
    public Result<List<DormVisitor>> searchVisitors(
            @RequestParam(required = false) String visitorName,
            @RequestParam(required = false) String studentName) {
        log.info("搜索访客记录请求 - 访客姓名: {}, 学生姓名: {}", visitorName, studentName);
        try {
            List<DormVisitor> visitors;
            if (visitorName != null && !visitorName.trim().isEmpty()) {
                visitors = dormVisitorService.searchByVisitorName(visitorName.trim());
            } else if (studentName != null && !studentName.trim().isEmpty()) {
                visitors = dormVisitorService.searchByStudentName(studentName.trim());
            } else {
                visitors = dormVisitorService.getAllVisitors();
            }
            log.info("搜索访客记录成功，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("搜索访客记录失败: {}", e.getMessage());
            return Result.error("搜索访客记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据时间范围查询访客记录
     */
    @GetMapping("/time-range")
    public Result<List<DormVisitor>> getVisitorsByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        log.info("根据时间范围查询访客记录请求: {} - {}", startTime, endTime);
        try {
            List<DormVisitor> visitors = dormVisitorService.getVisitorsByTimeRange(startTime, endTime);
            log.info("成功获取时间范围内访客记录，共{}条记录", visitors.size());
            return Result.success(visitors);
        } catch (Exception e) {
            log.error("根据时间范围查询访客记录失败: {}", e.getMessage());
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取访客统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getVisitorStatistics() {
        log.info("获取访客统计信息请求");
        try {
            Map<String, Object> statistics = dormVisitorService.getVisitorStatistics();
            log.info("成功获取访客统计信息");
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取访客统计信息失败: {}", e.getMessage());
            return Result.error("获取统计信息失败: " + e.getMessage());
        }
    }
}