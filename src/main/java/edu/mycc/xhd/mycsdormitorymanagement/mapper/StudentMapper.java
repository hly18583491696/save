package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 学生数据访问层
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    
    /**
     * 根据学号查找学生
     */
    @Select("SELECT * FROM sys_user WHERE student_number = #{studentNumber} AND deleted = 0")
    Student findByStudentNumber(String studentNumber);
    
    /**
     * 根据性别查找学生
     */
    @Select("SELECT * FROM sys_user WHERE gender = #{gender} AND deleted = 0")
    List<Student> findByGender(String gender);
    
    /**
     * 根据年级查找学生
     */
    @Select("SELECT * FROM sys_user WHERE grade = #{grade} AND deleted = 0")
    List<Student> findByGrade(String grade);
    
    /**
     * 根据专业查找学生
     */
    @Select("SELECT * FROM sys_user WHERE major = #{major} AND deleted = 0")
    List<Student> findByMajor(String major);
    
    /**
     * 根据班级查找学生
     */
    @Select("SELECT * FROM sys_user WHERE class_name = #{className} AND deleted = 0")
    List<Student> findByClassName(String className);
    
    /**
     * 根据状态查找学生
     */
    @Select("SELECT * FROM sys_user WHERE status = #{status} AND deleted = 0")
    List<Student> findByStatus(String status);
    
    /**
     * 查询所有有效学生
     */
    @Select("SELECT * FROM sys_user WHERE deleted = 0 ORDER BY student_number")
    List<Student> findAllActive();
    
    /**
     * 根据关键词搜索学生（姓名、学号、手机号）
     */
    @Select("SELECT * FROM sys_user WHERE (real_name LIKE CONCAT('%', #{keyword}, '%') OR student_number LIKE CONCAT('%', #{keyword}, '%') OR phone LIKE CONCAT('%', #{keyword}, '%')) AND deleted = 0")
    List<Student> searchStudents(String keyword);
}