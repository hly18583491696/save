package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * 根据性别获取学生
     */
    public List<Student> getStudentsByGender(String gender) {
        return studentMapper.findByGender(gender);
    }
    
    /**
     * 根据年级获取学生
     */
    public List<Student> getStudentsByGrade(String grade) {
        return studentMapper.findByGrade(grade);
    }
    
    /**
     * 根据专业获取学生
     */
    public List<Student> getStudentsByMajor(String major) {
        return studentMapper.findByMajor(major);
    }
    
    /**
     * 根据班级获取学生
     */
    public List<Student> getStudentsByClassName(String className) {
        return studentMapper.findByClassName(className);
    }
    
    /**
     * 根据状态获取学生
     */
    public List<Student> getStudentsByStatus(String status) {
        return studentMapper.findByStatus(status);
    }
    
    /**
     * 搜索学生
     */
    public List<Student> searchStudents(String keyword) {
        return studentMapper.searchStudents(keyword);
    }
    
    /**
     * 添加学生
     */
    public boolean addStudent(Student student) {
        // 检查学号是否已存在
        Student existing = studentMapper.findByStudentNumber(student.getStudentNumber());
        if (existing != null) {
            throw new RuntimeException("学号已存在");
        }
        
        // 设置默认用户名为学号
        if (student.getUsername() == null || student.getUsername().isEmpty()) {
            student.setUsername(student.getStudentNumber());
        }
        
        // 设置默认密码（加密后的学号）
        if (student.getPassword() == null || student.getPassword().isEmpty()) {
            student.setPassword(student.getStudentNumber()); // 简化密码处理，实际项目中应该加密
        } else {
            student.setPassword(student.getPassword()); // 简化密码处理，实际项目中应该加密
        }
        
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        student.setDeleted(0);
        
        // 设置默认状态为在校
        if (student.getStatus() == null || student.getStatus().isEmpty()) {
            student.setStatus("ACTIVE");
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
    public boolean deleteStudent(Long id) {
        Student student = new Student();
        student.setId(id);
        student.setDeleted(1);
        student.setUpdateTime(LocalDateTime.now());
        return studentMapper.updateById(student) > 0;
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