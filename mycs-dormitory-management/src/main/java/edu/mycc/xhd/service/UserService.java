package edu.mycc.xhd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.mycc.xhd.entity.User;
import edu.mycc.xhd.common.Result;

/**
 * 用户服务接口
 * 
 * @author xhd
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    Result<String> login(String username, String password);
    
    /**
     * 用户注册
     * 
     * @param user 用户信息
     * @return 注册结果
     */
    Result<Void> register(User user);
    
    /**
     * 分页查询用户
     * 
     * @param page 页码
     * @param size 每页大小
     * @param username 用户名（可选）
     * @param realName 真实姓名（可选）
     * @param userType 用户类型（可选）
     * @return 分页结果
     */
    Result<Page<User>> pageUsers(Integer page, Integer size, String username, String realName, Integer userType);
    
    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    Result<User> getUserById(Long id);
    
    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新结果
     */
    Result<Void> updateUser(User user);
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    Result<Void> deleteUser(Long id);
    
    /**
     * 重置密码
     * 
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    Result<Void> resetPassword(Long id, String newPassword);
    
    /**
     * 修改密码
     * 
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    Result<Void> changePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 启用/禁用用户
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 操作结果
     */
    Result<Void> updateStatus(Long id, Integer status);
}