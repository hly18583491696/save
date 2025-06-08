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
    @Select("SELECT * FROM student WHERE student_number = #{studentNumber} AND status = 1")
    Student findByStudentNumber(String studentNumber);
    
    /**
     * 根据状态查找学生
     */
    @Select("SELECT * FROM student WHERE status = #{status}")
    List<Student> findByStatus(String status);
    
    /**
     * 查询所有有效学生
     */
    @Select("SELECT * FROM student WHERE status = 1 ORDER BY id")
    List<Student> findAllActive();
    
    /**
     * 根据关键词搜索学生（姓名、学号、手机号）
     */
    @Select("SELECT * FROM student WHERE (student_name LIKE CONCAT('%', #{keyword}, '%') OR student_number LIKE CONCAT('%', #{keyword}, '%') OR phone LIKE CONCAT('%', #{keyword}, '%')) AND status = 1")
    List<Student> searchStudents(String keyword);
}