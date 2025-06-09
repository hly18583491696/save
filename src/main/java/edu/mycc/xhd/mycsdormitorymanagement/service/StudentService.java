package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生服务类
 */
@Slf4j
@Service
public class StudentService {
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private DormAccommodationService dormAccommodationService;
    
    // private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 获取所有学生
     */
    public List<Student> getAllStudents() {
        return studentMapper.findAllActive();
    }
    
    /**
     * 根据ID获取学生
     */
    public Student getStudentById(Long id) {
        return studentMapper.selectById(id);
    }
    
    /**
     * 根据学号获取学生
     */
    public Student getStudentByNumber(String studentNumber) {
        return studentMapper.findByStudentNumber(studentNumber);
    }
    

    
    /**
     * 根据班级获取学生
     */
    public List<Student> getStudentsByClassName(String className) {
        // TODO: 实现根据班级查询学生的功能
        return new ArrayList<>();
    }
    
    /**
     * 根据状态获取学生
     */
    public List<Student> getStudentsByStatus(String status) {
        return studentMapper.findByStatus(status);
    }
    
    /**
     * 根据性别获取学生
     */
    public List<Student> getStudentsByGender(String gender) {
        // TODO: 实现根据性别查询学生的功能
        return new ArrayList<>();
    }
    
    /**
     * 搜索学生
     */
    public List<Student> searchStudents(String keyword) {
        return studentMapper.searchStudents(keyword);
    }
    
    /**
     * 添加学生     */
    public boolean addStudent(Student student) {
        // 添加调试日志
        System.out.println("接收到的学生数据: " + student);
        System.out.println("学号: " + student.getStudentNumber());
        
        // 检查学号是否已存在
        Student existing = studentMapper.findByStudentNumber(student.getStudentNumber());
        if (existing != null) {
            throw new RuntimeException("学号已存在");
        }
        
        // 检查学号不能为空
        if (student.getStudentNumber() == null || student.getStudentNumber().isEmpty()) {
            throw new RuntimeException("学号不能为空");
        }
        
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        
        // 设置默认状态为在校
        if (student.getStatus() == null) {
            student.setStatus(1); // 1表示在校状态
        }
        
        return studentMapper.insert(student) > 0;
    }
    
    /**
     * 更新学生
     */
    @Transactional
    public boolean updateStudent(Student student) {
        // 更新学生信息
        student.setUpdateTime(LocalDateTime.now());
        boolean studentUpdated = studentMapper.updateById(student) > 0;
        
        if (studentUpdated) {
            // 同步更新住宿记录表中的学生信息
            log.info("开始同步更新住宿记录中的学生信息，学生ID: {}", student.getId());
            boolean accommodationUpdated = dormAccommodationService.updateStudentInfoInAccommodations(student);
            if (!accommodationUpdated) {
                log.warn("同步更新住宿记录失败，但学生信息已更新，学生ID: {}", student.getId());
            }
        }
        
        return studentUpdated;
    }
    
    /**
     * 删除学生（物理删除）
     */
    @Transactional
    public boolean deleteStudent(Long id) {
        log.info("开始删除学生，ID: {}", id);
        
        // 先检查学生是否存在
        Student student = studentMapper.selectById(id);
        if (student == null) {
            log.error("删除失败：学生不存在，ID: {}", id);
            throw new RuntimeException("学生不存在");
        }
        log.info("找到学生记录：{} ({})", student.getStudentName(), student.getStudentNumber());
        
        // 先删除该学生的所有住宿记录
        log.info("开始删除学生住宿记录，学生ID: {}", id);
        boolean accommodationDeleted = dormAccommodationService.deleteAccommodationsByStudentId(id);
        if (!accommodationDeleted) {
            log.error("删除学生住宿记录失败，学生ID: {}", id);
            throw new RuntimeException("删除学生住宿记录失败");
        }
        log.info("成功删除学生住宿记录，学生ID: {}", id);
        
        // 物理删除学生记录
        log.info("开始物理删除学生记录，ID: {}", id);
        int deleteResult = studentMapper.deleteById(id);
        log.info("数据库删除结果：受影响行数 = {}", deleteResult);
        
        boolean success = deleteResult > 0;
        if (success) {
            log.info("成功删除学生，ID: {}", id);
        } else {
            log.error("删除学生失败，数据库操作未影响任何行，ID: {}", id);
        }
        
        return success;
    }
    
    /**
     * 批量删除学生
     */
    public boolean deleteStudents(List<Long> ids) {
        for (Long id : ids) {
            if (!deleteStudent(id)) {
                return false;
            }
        }
        return true;
    }
}