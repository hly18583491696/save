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
    @Select("SELECT * FROM student WHERE student_number = #{studentNumber}")
    Student findByStudentNumber(String studentNumber);
    
    /**
     * 根据状态查找学生
     */
    @Select("SELECT * FROM student WHERE status = #{status}")
    List<Student> findByStatus(String status);
    
    /**
     * 查询所有有效学生
     */
    @Select("SELECT * FROM student ORDER BY id")
    List<Student> findAllActive();
    
    /**
     * 根据关键词搜索学生（姓名、学号、手机号）
     */
    @Select("SELECT * FROM student WHERE (student_name LIKE CONCAT('%', #{keyword}, '%') OR student_number LIKE CONCAT('%', #{keyword}, '%') OR phone LIKE CONCAT('%', #{keyword}, '%'))")
    List<Student> searchStudents(String keyword);
    
    /**
     * 根据班级查找学生
     */
    @Select("SELECT * FROM student WHERE class_name = #{className} ORDER BY student_number")
    List<Student> findByClassName(String className);
    
    /**
     * 统计学生总数
     */
    @Select("SELECT COUNT(*) FROM student")
    int countActiveStudents();
    
    /**
     * 根据性别统计学生数量
     */
    @Select("SELECT COUNT(*) FROM student WHERE gender = #{gender}")
    int countByGender(String gender);
    
    /**
     * 统计各班级学生数量
     */
    @Select("SELECT class_name, COUNT(*) as count FROM student GROUP BY class_name ORDER BY class_name")
    List<java.util.Map<String, Object>> countByClass();
}