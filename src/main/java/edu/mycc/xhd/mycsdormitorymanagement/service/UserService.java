package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.User;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.UserMapper;
import edu.mycc.xhd.mycsdormitorymanagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

/**
 * 用户服务类
 */
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    // @Autowired
    // private JwtUtils jwtUtils;
    
    /**
     * 用户登录
     */
    public String login(String username, String password) {
        // 根据用户名查询用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码（使用明文密码直接比较）
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }
        
        // 检查用户角色权限（只允许ADMIN和TEACHER登录）
        if (!"ADMIN".equals(user.getRole()) && !"TEACHER".equals(user.getRole())) {
            throw new RuntimeException("权限不足，只允许管理员和教师登录系统");
        }
        
        // 生成JWT令牌 (暂时简化)
        return "login_success_" + user.getUsername();
    }
    
    /**
     * 用户注册
     */
    public boolean register(String username, String password, String realName, String email, String phone) {
        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建新用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // 明文密码存储
        newUser.setRealName(realName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setRole("USER"); // 默认角色为普通用户（注意：普通用户无法登录系统）
        newUser.setStatus(0); // 默认状态为禁用，需要管理员激活
        newUser.setCreateTime(LocalDateTime.now());
        newUser.setUpdateTime(LocalDateTime.now());
        newUser.setDeleted(0); // 默认未删除
        
        // 保存到数据库
        int result = userMapper.insert(newUser);
        return result > 0;
    }
    
    /**
     * 根据用户名获取用户
     */
    public User getUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
        }
        return user;
    }
    
    /**
     * 根据角色获取用户列表
     */
    public java.util.List<User> getUsersByRole(String role) {
        java.util.List<User> users = userMapper.findByRole(role);
        // 不返回密码
        users.forEach(user -> user.setPassword(null));
        return users;
    }
}