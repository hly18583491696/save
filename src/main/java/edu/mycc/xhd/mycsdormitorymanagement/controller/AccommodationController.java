package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormAccommodation;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormAccommodationService;
import edu.mycc.xhd.mycsdormitorymanagement.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 住宿记录管理控制器
 * 整合了原学生管理功能和住宿记录管理功能
 */
@Slf4j
@RestController
@RequestMapping("/api/accommodations")
@CrossOrigin(origins = "*")
public class AccommodationController {
    
    @Autowired
    private DormAccommodationService accommodationService;
    
    @Autowired
    private StudentService studentService;
    
    // ==================== 住宿记录管理功能 ====================
    
    /**
     * 获取所有住宿记录
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAccommodations() {
        log.info("获取所有住宿记录请求");
        try {
            List<DormAccommodation> accommodations = accommodationService.getAllAccommodations();
            log.info("成功获取住宿记录列表，共{}条记录", accommodations.size());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", accommodations);
            response.put("message", "获取住宿记录列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取住宿记录列表失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取住宿记录列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据ID获取住宿记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAccommodationById(@PathVariable Long id) {
        try {
            DormAccommodation accommodation = accommodationService.getAccommodationById(id);
            Map<String, Object> response = new HashMap<>();
            if (accommodation != null) {
                response.put("success", true);
                response.put("data", accommodation);
                response.put("message", "获取住宿记录成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "住宿记录不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取住宿记录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据学生ID获取住宿记录
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getAccommodationsByStudentId(@PathVariable Long studentId) {
        try {
            List<DormAccommodation> accommodations = accommodationService.getAccommodationsByStudentId(studentId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", accommodations);
            response.put("message", "获取学生住宿记录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学生住宿记录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据房间ID获取住宿记录
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Map<String, Object>> getAccommodationsByRoomId(@PathVariable Long roomId) {
        try {
            List<DormAccommodation> accommodations = accommodationService.getAccommodationsByRoomId(roomId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", accommodations);
            response.put("message", "获取房间住宿记录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取房间住宿记录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 搜索住宿记录（根据学生姓名、学号等）
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAccommodations(@RequestParam String keyword) {
        try {
            // 先搜索学生，然后根据学生信息查找住宿记录
            List<Student> students = studentService.searchStudents(keyword);
            List<DormAccommodation> accommodations = new java.util.ArrayList<>();
            
            for (Student student : students) {
                List<DormAccommodation> studentAccommodations = accommodationService.getAccommodationsByStudentId(student.getId());
                accommodations.addAll(studentAccommodations);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", accommodations);
            response.put("message", "搜索住宿记录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索住宿记录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 分配宿舍
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> assignAccommodation(@RequestBody DormAccommodation accommodation) {
        try {
            boolean success = accommodationService.assignAccommodation(accommodation);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "分配宿舍成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "分配宿舍失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "分配宿舍失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新住宿记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAccommodation(@PathVariable Long id, @RequestBody DormAccommodation accommodation) {
        try {
            accommodation.setId(id);
            boolean success = accommodationService.updateAccommodation(accommodation);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "更新住宿记录成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新住宿记录失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新住宿记录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据学生ID退宿
     */
    @PostMapping("/student/{studentId}/checkout")
    public ResponseEntity<Map<String, Object>> checkOutByStudentId(@PathVariable Long studentId) {
        try {
            boolean success = accommodationService.checkOutByStudentId(studentId);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "学生退宿成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "学生退宿失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "学生退宿失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 删除住宿记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAccommodation(@PathVariable Long id) {
        log.info("收到删除住宿记录请求，ID: {}", id);
        try {
            boolean success = accommodationService.deleteAccommodation(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                // 删除成功后，重新获取最新的住宿记录列表
                List<DormAccommodation> accommodations = accommodationService.getAllAccommodations();
                response.put("success", true);
                response.put("data", accommodations);
                response.put("message", "删除住宿记录成功");
                log.info("删除住宿记录成功，ID: {}，返回最新数据列表", id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除住宿记录失败");
                log.error("删除住宿记录失败，ID: {}", id);
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除住宿记录失败: " + e.getMessage());
            log.error("删除住宿记录异常，ID: {}, 错误: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 批量删除住宿记录
     */
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteAccommodations(@RequestBody List<Long> ids) {
        log.info("收到批量删除住宿记录请求，IDs: {}", ids);
        try {
            boolean allSuccess = true;
            for (Long id : ids) {
                boolean success = accommodationService.deleteAccommodation(id);
                if (!success) {
                    allSuccess = false;
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            if (allSuccess) {
                // 批量删除成功后，重新获取最新的住宿记录列表
                List<DormAccommodation> accommodations = accommodationService.getAllAccommodations();
                response.put("success", true);
                response.put("data", accommodations);
                response.put("message", "批量删除住宿记录成功");
                log.info("批量删除住宿记录成功，IDs: {}，返回最新数据列表", ids);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量删除住宿记录部分失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量删除住宿记录失败: " + e.getMessage());
            log.error("批量删除住宿记录异常，IDs: {}, 错误: {}", ids, e.getMessage(), e);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 退宿
     */
    @PostMapping("/{id}/checkout")
    public ResponseEntity<Map<String, Object>> checkOut(@PathVariable Long id) {
        try {
            boolean success = accommodationService.checkOut(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "退宿成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "退宿失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "退宿失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
}