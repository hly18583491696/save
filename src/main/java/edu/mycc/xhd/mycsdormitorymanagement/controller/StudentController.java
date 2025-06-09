package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.service.StudentService;
import edu.mycc.xhd.mycsdormitorymanagement.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * 获取所有学生
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        log.info("获取所有学生列表请求");
        try {
            List<Student> students = studentService.getAllStudents();
            log.info("成功获取学生列表，共{}条记录", students.size());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", students);
            response.put("message", "获取学生列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取学生列表失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学生列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据ID获取学生
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            Map<String, Object> response = new HashMap<>();
            if (student != null) {
                response.put("success", true);
                response.put("data", student);
                response.put("message", "获取学生信息成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "学生不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学生信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 根据学号获取学生
     */
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<Map<String, Object>> getStudentByNumber(@PathVariable String studentNumber) {
        try {
            Student student = studentService.getStudentByNumber(studentNumber);
            Map<String, Object> response = new HashMap<>();
            if (student != null) {
                response.put("success", true);
                response.put("data", student);
                response.put("message", "获取学生信息成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "学生不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学生信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 搜索学生
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchStudents(@RequestParam String keyword) {
        try {
            List<Student> students = studentService.searchStudents(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", students);
            response.put("message", "搜索学生成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索学生失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 添加学生
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        try {
            boolean success = studentService.addStudent(student);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "添加学生成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "添加学生失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "添加学生失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 更新学生
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            student.setId(id);
            boolean success = studentService.updateStudent(student);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "更新学生成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新学生失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新学生失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        log.info("收到删除学生请求，ID: {}", id);
        try {
            boolean success = studentService.deleteStudent(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "删除学生成功");
                log.info("删除学生成功，ID: {}", id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除学生失败");
                log.error("删除学生失败，ID: {}", id);
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除学生失败: " + e.getMessage());
            log.error("删除学生异常，ID: {}, 错误: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 批量删除学生
     */
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteStudents(@RequestBody List<Long> ids) {
        try {
            boolean success = studentService.deleteStudents(ids);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "批量删除学生成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量删除学生失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量删除学生失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}