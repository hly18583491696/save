package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.StudentMapper;
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
@Service
public class StudentService {
    
    @Autowired
    private StudentMapper studentMapper;
    
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
        System.out.println("用户名: " + student.getUsername());
        
        // 检查学号是否已存在
        Student existing = studentMapper.findByStudentNumber(student.getStudentNumber());
        if (existing != null) {
            throw new RuntimeException("学号已存在");
        }
        
        // 设置默认用户名为学号
        if (student.getUsername() == null || student.getUsername().isEmpty()) {
            if (student.getStudentNumber() != null && !student.getStudentNumber().isEmpty()) {
                student.setUsername(student.getStudentNumber());
            } else {
                throw new RuntimeException("学号不能为空");
            }
        }
        
        // 设置默认密码（加密后的学号）
        if (student.getPassword() == null || student.getPassword().isEmpty()) {
            if (student.getStudentNumber() != null && !student.getStudentNumber().isEmpty()) {
                student.setPassword(student.getStudentNumber()); // 简化密码处理，实际项目中应该加密
            } else {
                student.setPassword("123456"); // 默认密码
            }
        } else {
            student.setPassword(student.getPassword()); // 简化密码处理，实际项目中应该加密
        }
        
        // 设置默认角色为USER
        if (student.getRole() == null || student.getRole().isEmpty()) {
            student.setRole("USER");
        }
        
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        student.setDeleted(0);
        
        // 设置默认状态为在校
        if (student.getStatus() == null) {
            student.setStatus(1); // 1表示在校状态
        }
        
        return studentMapper.insert(student) > 0;
    }
    
    /**
     * 更新学生
     */
    public boolean updateStudent(Student student) {
        // 如果密码不为空，则加密
        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            student.setPassword(student.getPassword()); // 简化密码处理，实际项目中应该加密
        } else {
            // 如果密码为空，则不更新密码字段
            Student existingStudent = studentMapper.selectById(student.getId());
            if (existingStudent != null) {
                student.setPassword(existingStudent.getPassword());
            }
        }
        
        student.setUpdateTime(LocalDateTime.now());
        return studentMapper.updateById(student) > 0;
    }
    
    /**
     * 删除学生（逻辑删除）
     */
    @Transactional
    public boolean deleteStudent(Long id) {
        // 先检查学生是否存在
        Student student = studentMapper.selectById(id);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        // 使用MyBatis-Plus的removeById方法，会自动处理逻辑删除
        return studentMapper.deleteById(id) > 0;
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