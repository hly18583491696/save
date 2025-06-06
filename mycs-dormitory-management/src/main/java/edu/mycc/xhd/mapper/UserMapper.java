package edu.mycc.xhd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 * 
 * @author xhd
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);
    
    /**
     * 根据学号/工号查询用户
     * 
     * @param userCode 学号/工号
     * @return 用户信息
     */
    User selectByUserCode(@Param("userCode") String userCode);
    
    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @param id 用户ID（排除自己）
     * @return 数量
     */
    int checkUsernameExists(@Param("username") String username, @Param("id") Long id);
    
    /**
     * 检查学号/工号是否存在
     * 
     * @param userCode 学号/工号
     * @param id 用户ID（排除自己）
     * @return 数量
     */
    int checkUserCodeExists(@Param("userCode") String userCode, @Param("id") Long id);
}