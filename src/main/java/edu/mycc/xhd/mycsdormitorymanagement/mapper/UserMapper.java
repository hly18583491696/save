package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查找用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND deleted = 0")
    User findByUsername(String username);
    
    /**
     * 根据角色查找用户
     */
    @Select("SELECT * FROM sys_user WHERE role = #{role} AND deleted = 0")
    List<User> findByRole(String role);
    
    /**
     * 插入新用户
     */
    @Insert("INSERT INTO sys_user (username, password, real_name, email, phone, role, status, avatar, create_time, update_time, deleted) " +
            "VALUES (#{username}, #{password}, #{realName}, #{email}, #{phone}, #{role}, #{status}, #{avatar}, #{createTime}, #{updateTime}, #{deleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}