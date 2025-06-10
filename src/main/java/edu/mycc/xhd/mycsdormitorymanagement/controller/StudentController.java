package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器
 * 负责处理学生信息的增删改查操作
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
@Slf4j
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
        log.info("根据ID获取学生信息请求，ID: {}", id);
        try {
            Student student = studentService.getStudentById(id);
            Map<String, Object> response = new HashMap<>();
            if (student != null) {
                response.put("success", true);
                response.put("data", student);
                response.put("message", "获取学生信息成功");
                log.info("成功获取学生信息，ID: {}, 姓名: {}", id, student.getStudentName());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "学生不存在");
                log.warn("学生不存在，ID: {}", id);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            log.error("获取学生信息失败，ID: {}, 错误: {}", id, e.getMessage());
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
        log.info("根据学号获取学生信息请求，学号: {}", studentNumber);
        try {
            Student student = studentService.getStudentByNumber(studentNumber);
            Map<String, Object> response = new HashMap<>();
            if (student != null) {
                response.put("success", true);
                response.put("data", student);
                response.put("message", "获取学生信息成功");
                log.info("成功根据学号获取学生信息，学号: {}, 姓名: {}", studentNumber, student.getStudentName());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "学生不存在");
                log.warn("学生不存在，学号: {}", studentNumber);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            log.error("根据学号获取学生信息失败，学号: {}, 错误: {}", studentNumber, e.getMessage());
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
        log.info("搜索学生请求，关键词: {}", keyword);
        try {
            List<Student> students = studentService.searchStudents(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", students);
            response.put("message", "搜索学生成功");
            log.info("搜索学生成功，关键词: {}, 找到{}条记录", keyword, students.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("搜索学生失败，关键词: {}, 错误: {}", keyword, e.getMessage());
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
        log.info("添加学生请求，学生信息: 姓名={}, 学号={}", student.getStudentName(), student.getStudentNumber());
        try {
            boolean success = studentService.addStudent(student);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "添加学生成功");
                log.info("添加学生成功，姓名: {}, 学号: {}", student.getStudentName(), student.getStudentNumber());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "添加学生失败");
                log.error("添加学生失败，姓名: {}, 学号: {}", student.getStudentName(), student.getStudentNumber());
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            log.error("添加学生异常，姓名: {}, 学号: {}, 错误: {}", student.getStudentName(), student.getStudentNumber(), e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "添加学生失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 更新学生信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        log.info("更新学生信息请求，ID: {}, 姓名: {}", id, student.getStudentName());
        try {
            student.setId(id);
            boolean success = studentService.updateStudent(student);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "更新学生信息成功");
                log.info("更新学生信息成功，ID: {}, 姓名: {}", id, student.getStudentName());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "更新学生信息失败");
                log.error("更新学生信息失败，ID: {}, 姓名: {}", id, student.getStudentName());
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            log.error("更新学生信息异常，ID: {}, 姓名: {}, 错误: {}", id, student.getStudentName(), e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新学生信息失败: " + e.getMessage());
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
                // 删除成功后，重新获取最新的学生列表
                List<Student> students = studentService.getAllStudents();
                response.put("success", true);
                response.put("data", students);
                response.put("message", "删除学生成功");
                log.info("删除学生成功，ID: {}，返回最新数据列表", id);
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
        log.info("收到批量删除学生请求，IDs: {}", ids);
        try {
            boolean success = studentService.deleteStudents(ids);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                // 批量删除成功后，重新获取最新的学生列表
                List<Student> students = studentService.getAllStudents();
                response.put("success", true);
                response.put("data", students);
                response.put("message", "批量删除学生成功");
                log.info("批量删除学生成功，IDs: {}，返回最新数据列表", ids);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量删除学生失败");
                log.error("批量删除学生失败，IDs: {}", ids);
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量删除学生失败: " + e.getMessage());
            log.error("批量删除学生异常，IDs: {}, 错误: {}", ids, e.getMessage(), e);
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据班级获取学生列表
     */
    @GetMapping("/class/{className}")
    public ResponseEntity<Map<String, Object>> getStudentsByClass(@PathVariable String className) {
        log.info("根据班级获取学生列表请求，班级: {}", className);
        try {
            List<Student> students = studentService.getStudentsByClass(className);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", students);
            response.put("message", "获取班级学生列表成功");
            log.info("成功获取班级学生列表，班级: {}, 共{}条记录", className, students.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取班级学生列表失败，班级: {}, 错误: {}", className, e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取班级学生列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取学生统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStudentStatistics() {
        log.info("获取学生统计信息请求");
        try {
            Map<String, Object> statistics = studentService.getStudentStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", statistics);
            response.put("message", "获取学生统计信息成功");
            log.info("成功获取学生统计信息");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取学生统计信息失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取学生统计信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}