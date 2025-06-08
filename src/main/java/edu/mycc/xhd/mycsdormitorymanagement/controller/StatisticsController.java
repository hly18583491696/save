package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.service.DormBuildingService;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormRoomService;
import edu.mycc.xhd.mycsdormitorymanagement.service.StudentService;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormAccommodationService;
import edu.mycc.xhd.mycsdormitorymanagement.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计数据控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {
    
    @Autowired
    private DormBuildingService dormBuildingService;
    
    @Autowired
    private DormRoomService dormRoomService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private DormAccommodationService dormAccommodationService;
    
    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStats() {
        log.info("获取仪表盘统计数据请求");
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 获取楼栋总数
            int totalBuildings = dormBuildingService.getAllBuildings().size();
            
            // 获取房间总数
            int totalRooms = dormRoomService.getAllRooms().size();
            
            // 获取学生总数（统计有住宿记录的学生数量，与学生管理页面保持一致）
            int totalStudents = dormAccommodationService.getAllAccommodations().size();
            
            // 计算入住率
            int occupiedRooms = dormAccommodationService.getOccupiedRoomsCount();
            double occupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100 : 0;
            
            stats.put("totalBuildings", totalBuildings);
            stats.put("totalRooms", totalRooms);
            stats.put("totalStudents", totalStudents);
            stats.put("occupancyRate", Math.round(occupancyRate));
            
            log.info("成功获取仪表盘统计数据: 楼栋{}栋, 房间{}间, 学生{}人, 入住率{}%", 
                    totalBuildings, totalRooms, totalStudents, Math.round(occupancyRate));
            
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取仪表盘统计数据失败: {}", e.getMessage());
            return Result.error("获取统计数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取最近活动记录
     */
    @GetMapping("/recent-activities")
    public Result<Object> getRecentActivities() {
        log.info("获取最近活动记录请求");
        try {
            // TODO: 实现获取最近活动记录的逻辑
            // 这里可以从日志表或操作记录表中获取最近的活动
            
            // 暂时返回空数组，后续可以根据需要实现
            return Result.success(new Object[0]);
        } catch (Exception e) {
            log.error("获取最近活动记录失败: {}", e.getMessage());
            return Result.error("获取最近活动记录失败: " + e.getMessage());
        }
    }
}