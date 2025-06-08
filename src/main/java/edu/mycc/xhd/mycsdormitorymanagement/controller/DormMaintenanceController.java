package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormMaintenance;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormMaintenanceService;
import edu.mycc.xhd.mycsdormitorymanagement.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 维修管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
public class DormMaintenanceController {
    
    @Autowired
    private DormMaintenanceService maintenanceService;
    
    /**
     * 获取所有维修记录
     */
    @GetMapping("/list")
    public Result<List<DormMaintenance>> getAllMaintenances() {
        log.info("获取所有维修记录请求");
        try {
            List<DormMaintenance> maintenances = maintenanceService.getAllMaintenances();
            log.info("成功获取维修记录列表，共{}条记录", maintenances.size());
            return Result.success(maintenances);
        } catch (Exception e) {
            log.error("获取维修记录列表失败: {}", e.getMessage());
            return Result.error("获取维修记录列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取维修记录
     */
    @GetMapping("/{id}")
    public Result<DormMaintenance> getMaintenanceById(@PathVariable Long id) {
        log.info("获取维修记录详情请求，ID: {}", id);
        try {
            DormMaintenance maintenance = maintenanceService.getMaintenanceById(id);
            if (maintenance != null) {
                log.info("成功获取维修记录详情，ID: {}", id);
                return Result.success(maintenance);
            } else {
                log.warn("维修记录不存在，ID: {}", id);
                return Result.error("维修记录不存在");
            }
        } catch (Exception e) {
            log.error("获取维修记录详情失败，ID: {}, 错误: {}", id, e.getMessage());
            return Result.error("获取维修记录详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据申请单号获取维修记录
     */
    @GetMapping("/request/{requestNumber}")
    public Result<DormMaintenance> getMaintenanceByRequestNumber(@PathVariable String requestNumber) {
        log.info("根据申请单号获取维修记录请求，申请单号: {}", requestNumber);
        try {
            DormMaintenance maintenance = maintenanceService.getMaintenanceByRequestNumber(requestNumber);
            if (maintenance != null) {
                log.info("成功获取维修记录，申请单号: {}", requestNumber);
                return Result.success(maintenance);
            } else {
                log.warn("维修记录不存在，申请单号: {}", requestNumber);
                return Result.error("维修记录不存在");
            }
        } catch (Exception e) {
            log.error("获取维修记录失败，申请单号: {}, 错误: {}", requestNumber, e.getMessage());
            return Result.error("获取维修记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据申请人ID获取维修记录
     */
    @GetMapping("/applicant/{applicantId}")
    public Result<List<DormMaintenance>> getMaintenancesByApplicantId(@PathVariable Long applicantId) {
        log.info("根据申请人ID获取维修记录请求，申请人ID: {}", applicantId);
        try {
            List<DormMaintenance> maintenances = maintenanceService.getMaintenancesByApplicantId(applicantId);
            log.info("成功获取申请人维修记录，申请人ID: {}, 共{}条记录", applicantId, maintenances.size());
            return Result.success(maintenances);
        } catch (Exception e) {
            log.error("获取申请人维修记录失败，申请人ID: {}, 错误: {}", applicantId, e.getMessage());
            return Result.error("获取申请人维修记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据房间ID获取维修记录
     */
    @GetMapping("/room/{roomId}")
    public Result<List<DormMaintenance>> getMaintenancesByRoomId(@PathVariable Long roomId) {
        log.info("根据房间ID获取维修记录请求，房间ID: {}", roomId);
        try {
            List<DormMaintenance> maintenances = maintenanceService.getMaintenancesByRoomId(roomId);
            log.info("成功获取房间维修记录，房间ID: {}, 共{}条记录", roomId, maintenances.size());
            return Result.success(maintenances);
        } catch (Exception e) {
            log.error("获取房间维修记录失败，房间ID: {}, 错误: {}", roomId, e.getMessage());
            return Result.error("获取房间维修记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据状态获取维修记录
     */
    @GetMapping("/status/{status}")
    public Result<List<DormMaintenance>> getMaintenancesByStatus(@PathVariable String status) {
        log.info("根据状态获取维修记录请求，状态: {}", status);
        try {
            List<DormMaintenance> maintenances = maintenanceService.getMaintenancesByStatus(status);
            log.info("成功获取状态维修记录，状态: {}, 共{}条记录", status, maintenances.size());
            return Result.success(maintenances);
        } catch (Exception e) {
            log.error("获取状态维修记录失败，状态: {}, 错误: {}", status, e.getMessage());
            return Result.error("获取状态维修记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据维修类型获取维修记录
     */
    @GetMapping("/type/{maintenanceType}")
    public Result<List<DormMaintenance>> getMaintenancesByType(@PathVariable String maintenanceType) {
        log.info("根据维修类型获取维修记录请求，维修类型: {}", maintenanceType);
        try {
            List<DormMaintenance> maintenances = maintenanceService.getMaintenancesByType(maintenanceType);
            log.info("成功获取维修类型记录，维修类型: {}, 共{}条记录", maintenanceType, maintenances.size());
            return Result.success(maintenances);
        } catch (Exception e) {
            log.error("获取维修类型记录失败，维修类型: {}, 错误: {}", maintenanceType, e.getMessage());
            return Result.error("获取维修类型记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建维修申请
     */
    @PostMapping("/create")
    public Result<DormMaintenance> createMaintenance(@RequestBody DormMaintenance maintenance) {
        log.info("创建维修申请请求，申请人: {}, 房间号: {}", maintenance.getApplicantName(), maintenance.getRoomNumber());
        try {
            // 基本参数验证
            if (maintenance.getApplicantId() == null || maintenance.getRoomId() == null || 
                maintenance.getMaintenanceType() == null || maintenance.getDescription() == null) {
                return Result.badRequest("必填参数不能为空");
            }
            
            DormMaintenance createdMaintenance = maintenanceService.createMaintenance(maintenance);
            log.info("成功创建维修申请，申请单号: {}", createdMaintenance.getRequestNumber());
            return Result.success(createdMaintenance, "维修申请创建成功");
        } catch (Exception e) {
            log.error("创建维修申请失败: {}", e.getMessage());
            return Result.error("创建维修申请失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新维修记录
     */
    @PutMapping("/update")
    public Result<DormMaintenance> updateMaintenance(@RequestBody DormMaintenance maintenance) {
        log.info("更新维修记录请求，ID: {}", maintenance.getId());
        try {
            if (maintenance.getId() == null) {
                return Result.badRequest("维修记录ID不能为空");
            }
            
            DormMaintenance updatedMaintenance = maintenanceService.updateMaintenance(maintenance);
            log.info("成功更新维修记录，ID: {}", maintenance.getId());
            return Result.success(updatedMaintenance, "维修记录更新成功");
        } catch (Exception e) {
            log.error("更新维修记录失败，ID: {}, 错误: {}", maintenance.getId(), e.getMessage());
            return Result.error("更新维修记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除维修记录
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteMaintenance(@PathVariable Long id) {
        log.info("删除维修记录请求，ID: {}", id);
        try {
            boolean success = maintenanceService.deleteMaintenance(id);
            if (success) {
                log.info("成功删除维修记录，ID: {}", id);
                return Result.success(null, "维修记录删除成功");
            } else {
                log.warn("维修记录不存在或删除失败，ID: {}", id);
                return Result.error("维修记录不存在或删除失败");
            }
        } catch (Exception e) {
            log.error("删除维修记录失败，ID: {}, 错误: {}", id, e.getMessage());
            return Result.error("删除维修记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新维修状态
     */
    @PutMapping("/status/{id}")
    public Result<Void> updateMaintenanceStatus(@PathVariable Long id, @RequestParam String status) {
        log.info("更新维修状态请求，ID: {}, 状态: {}", id, status);
        try {
            boolean success = maintenanceService.updateMaintenanceStatus(id, status);
            if (success) {
                log.info("成功更新维修状态，ID: {}, 状态: {}", id, status);
                return Result.success(null, "维修状态更新成功");
            } else {
                log.warn("维修记录不存在或状态更新失败，ID: {}", id);
                return Result.error("维修记录不存在或状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新维修状态失败，ID: {}, 错误: {}", id, e.getMessage());
            return Result.error("更新维修状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 分配维修任务
     */
    @PutMapping("/assign/{id}")
    public Result<Void> assignMaintenance(@PathVariable Long id, @RequestParam String assignedTo) {
        log.info("分配维修任务请求，ID: {}, 分配给: {}", id, assignedTo);
        try {
            boolean success = maintenanceService.assignMaintenance(id, assignedTo);
            if (success) {
                log.info("成功分配维修任务，ID: {}, 分配给: {}", id, assignedTo);
                return Result.success(null, "维修任务分配成功");
            } else {
                log.warn("维修记录不存在或任务分配失败，ID: {}", id);
                return Result.error("维修记录不存在或任务分配失败");
            }
        } catch (Exception e) {
            log.error("分配维修任务失败，ID: {}, 错误: {}", id, e.getMessage());
            return Result.error("分配维修任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 完成维修任务
     */
    @PutMapping("/complete/{id}")
    public Result<Void> completeMaintenance(@PathVariable Long id, @RequestParam BigDecimal actualCost) {
        log.info("完成维修任务请求，ID: {}, 实际费用: {}", id, actualCost);
        try {
            boolean success = maintenanceService.completeMaintenance(id, actualCost);
            if (success) {
                log.info("成功完成维修任务，ID: {}", id);
                return Result.success(null, "维修任务完成成功");
            } else {
                log.warn("维修记录不存在或任务完成失败，ID: {}", id);
                return Result.error("维修记录不存在或任务完成失败");
            }
        } catch (Exception e) {
            log.error("完成维修任务失败，ID: {}, 错误: {}", id, e.getMessage());
            return Result.error("完成维修任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加反馈和评分
     */
    @PutMapping("/feedback/{id}")
    public Result<Void> addFeedback(@PathVariable Long id, @RequestParam String feedback, @RequestParam Integer rating) {
        log.info("添加维修反馈请求，ID: {}, 评分: {}", id, rating);
        try {
            if (rating < 1 || rating > 5) {
                return Result.badRequest("评分必须在1-5之间");
            }
            
            boolean success = maintenanceService.addFeedback(id, feedback, rating);
            if (success) {
                log.info("成功添加维修反馈，ID: {}", id);
                return Result.success(null, "维修反馈添加成功");
            } else {
                log.warn("维修记录不存在或反馈添加失败，ID: {}", id);
                return Result.error("维修记录不存在或反馈添加失败");
            }
        } catch (Exception e) {
            log.error("添加维修反馈失败，ID: {}, 错误: {}", id, e.getMessage());
            return Result.error("添加维修反馈失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取维修统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getMaintenanceStatistics() {
        log.info("获取维修统计信息请求");
        try {
            Map<String, Object> statistics = maintenanceService.getMaintenanceStatistics();
            log.info("成功获取维修统计信息");
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取维修统计信息失败: {}", e.getMessage());
            return Result.error("获取维修统计信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索维修记录
     */
    @GetMapping("/search")
    public Result<List<DormMaintenance>> searchMaintenances(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String maintenanceType,
            @RequestParam(required = false) String urgencyLevel) {
        log.info("搜索维修记录请求，关键词: {}, 状态: {}, 类型: {}, 紧急程度: {}", keyword, status, maintenanceType, urgencyLevel);
        try {
            List<DormMaintenance> maintenances = maintenanceService.searchMaintenances(keyword, status, maintenanceType, urgencyLevel);
            log.info("成功搜索维修记录，共{}条记录", maintenances.size());
            return Result.success(maintenances);
        } catch (Exception e) {
            log.error("搜索维修记录失败: {}", e.getMessage());
            return Result.error("搜索维修记录失败: " + e.getMessage());
        }
    }
}