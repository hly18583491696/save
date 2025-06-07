package edu.mycc.xhd.mycsdormitorymanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.mycc.xhd.mycsdormitorymanagement.entity.User;
import edu.mycc.xhd.mycsdormitorymanagement.common.PageRequest;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    String login(String username, String password);
    
    /**
     * 用户注册
     */
    User register(User user);
    
    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);
    
    /**
     * 根据用户名获取用户信息
     */
    User getUserByUsername(String username);
    
    /**
     * 根据学号获取用户信息
     */
    User getUserByStudentId(String studentId);
    
    /**
     * 分页查询用户列表
     */
    Page<User> getUserList(PageRequest pageRequest);
    
    /**
     * 根据角色查询用户列表
     */
    List<User> getUsersByRole(String role);
    
    /**
     * 创建用户
     */
    User createUser(User user);
    
    /**
     * 更新用户信息
     */
    User updateUser(User user);
    
    /**
     * 删除用户
     */
    boolean deleteUser(Long id);
    
    /**
     * 批量删除用户
     */
    boolean deleteUsers(List<Long> ids);
    
    /**
     * 启用/禁用用户
     */
    boolean updateUserStatus(Long id, Integer status);
    
    /**
     * 重置用户密码
     */
    boolean resetPassword(Long id, String newPassword);
    
    /**
     * 修改密码
     */
    boolean changePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查学号是否存在
     */
    boolean existsByStudentId(String studentId);
}