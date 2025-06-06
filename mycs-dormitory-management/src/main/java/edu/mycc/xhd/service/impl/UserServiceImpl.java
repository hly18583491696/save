package edu.mycc.xhd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.mycc.xhd.common.Result;
import edu.mycc.xhd.entity.User;
import edu.mycc.xhd.mapper.UserMapper;
import edu.mycc.xhd.service.UserService;
import edu.mycc.xhd.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 * 
 * @author xhd
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Override
    public Result<String> login(String username, String password) {
        // 查询用户
        User user = baseMapper.selectByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            return Result.error("用户已被禁用");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error("密码错误");
        }
        
        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getUserType());
        
        return Result.success("登录成功", token);
    }
    
    @Override
    public Result<Void> register(User user) {
        // 检查用户名是否存在
        if (baseMapper.checkUsernameExists(user.getUsername(), null) > 0) {
            return Result.error("用户名已存在");
        }
        
        // 检查学号/工号是否存在
        if (StringUtils.hasText(user.getUserCode()) && 
            baseMapper.checkUserCodeExists(user.getUserCode(), null) > 0) {
            return Result.error("学号/工号已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getUserType() == null) {
            user.setUserType(3); // 默认学生
        }
        
        // 保存用户
        save(user);
        
        return Result.success("注册成功");
    }
    
    @Override
    public Result<Page<User>> pageUsers(Integer page, Integer size, String username, String realName, Integer userType) {
        Page<User> pageParam = new Page<>(page, size);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), User::getUsername, username)
               .like(StringUtils.hasText(realName), User::getRealName, realName)
               .eq(userType != null, User::getUserType, userType)
               .orderByDesc(User::getCreateTime);
        
        Page<User> result = page(pageParam, wrapper);
        
        // 清除密码字段
        result.getRecords().forEach(user -> user.setPassword(null));
        
        return Result.success(result);
    }
    
    @Override
    public Result<User> getUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 清除密码字段
        user.setPassword(null);
        
        return Result.success(user);
    }
    
    @Override
    public Result<Void> updateUser(User user) {
        // 检查用户是否存在
        User existUser = getById(user.getId());
        if (existUser == null) {
            return Result.error("用户不存在");
        }
        
        // 检查用户名是否重复
        if (StringUtils.hasText(user.getUsername()) && 
            baseMapper.checkUsernameExists(user.getUsername(), user.getId()) > 0) {
            return Result.error("用户名已存在");
        }
        
        // 检查学号/工号是否重复
        if (StringUtils.hasText(user.getUserCode()) && 
            baseMapper.checkUserCodeExists(user.getUserCode(), user.getId()) > 0) {
            return Result.error("学号/工号已存在");
        }
        
        // 不允许修改密码
        user.setPassword(null);
        
        updateById(user);
        
        return Result.success("更新成功");
    }
    
    @Override
    public Result<Void> deleteUser(Long id) {
        if (!removeById(id)) {
            return Result.error("删除失败");
        }
        
        return Result.success("删除成功");
    }
    
    @Override
    public Result<Void> resetPassword(Long id, String newPassword) {
        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        
        if (!updateById(user)) {
            return Result.error("重置密码失败");
        }
        
        return Result.success("重置密码成功");
    }
    
    @Override
    public Result<Void> changePassword(Long id, String oldPassword, String newPassword) {
        User user = getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.error("旧密码错误");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
        
        return Result.success("修改密码成功");
    }
    
    @Override
    public Result<Void> updateStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        
        if (!updateById(user)) {
            return Result.error("操作失败");
        }
        
        return Result.success(status == 1 ? "启用成功" : "禁用成功");
    }
}