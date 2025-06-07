package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND deleted = 0")
    User findByUsername(String username);
    
    /**
     * 根据学号查询用户
     */
    @Select("SELECT * FROM sys_user WHERE student_id = #{studentId} AND deleted = 0")
    User findByStudentId(String studentId);
    
    /**
     * 根据角色查询用户列表
     */
    @Select("SELECT * FROM sys_user WHERE role = #{role} AND deleted = 0")
    List<User> findByRole(String role);
    
    /**
     * 根据状态查询用户列表
     */
    @Select("SELECT * FROM sys_user WHERE status = #{status} AND deleted = 0")
    List<User> findByStatus(Integer status);
}