package edu.mycc.xhd.mycsdormitorymanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.mycc.xhd.mycsdormitorymanagement.entity.User;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.UserMapper;
import edu.mycc.xhd.mycsdormitorymanagement.service.UserService;
import edu.mycc.xhd.mycsdormitorymanagement.utils.JwtUtils;
import edu.mycc.xhd.mycsdormitorymanagement.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        return jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
    }
    
    @Override
    public User register(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (StringUtils.hasText(user.getStudentId()) && existsByStudentId(user.getStudentId())) {
            throw new RuntimeException("学号已存在");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1); // 默认启用
        if (!StringUtils.hasText(user.getRole())) {
            user.setRole("STUDENT"); // 默认学生角色
        }
        
        userMapper.insert(user);
        return user;
    }
    
    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    @Override
    public User getUserByStudentId(String studentId) {
        return userMapper.findByStudentId(studentId);
    }
    
    @Override
    public Page<User> getUserList(PageRequest pageRequest) {
        Page<User> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(pageRequest.getKeyword())) {
            queryWrapper.like("username", pageRequest.getKeyword())
                       .or().like("real_name", pageRequest.getKeyword())
                       .or().like("student_id", pageRequest.getKeyword());
        }
        
        if (StringUtils.hasText(pageRequest.getSortField())) {
            if ("asc".equalsIgnoreCase(pageRequest.getSortOrder())) {
                queryWrapper.orderByAsc(pageRequest.getSortField());
            } else {
                queryWrapper.orderByDesc(pageRequest.getSortField());
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        
        return userMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public List<User> getUsersByRole(String role) {
        return userMapper.findByRole(role);
    }
    
    @Override
    public User createUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (StringUtils.hasText(user.getStudentId()) && existsByStudentId(user.getStudentId())) {
            throw new RuntimeException("学号已存在");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }
    
    @Override
    public User updateUser(User user) {
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查用户名是否重复（排除自己）
        User userWithSameUsername = userMapper.findByUsername(user.getUsername());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(user.getId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查学号是否重复（排除自己）
        if (StringUtils.hasText(user.getStudentId())) {
            User userWithSameStudentId = userMapper.findByStudentId(user.getStudentId());
            if (userWithSameStudentId != null && !userWithSameStudentId.getId().equals(user.getId())) {
                throw new RuntimeException("学号已存在");
            }
        }
        
        // 如果密码为空，保持原密码
        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        userMapper.updateById(user);
        return user;
    }
    
    @Override
    public boolean deleteUser(Long id) {
        return userMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean deleteUsers(List<Long> ids) {
        return userMapper.deleteBatchIds(ids) > 0;
    }
    
    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        return userMapper.updateById(user) > 0;
    }
    
    @Override
    public boolean resetPassword(Long id, String newPassword) {
        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.updateById(user) > 0;
    }
    
    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.updateById(user) > 0;
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userMapper.findByUsername(username) != null;
    }
    
    @Override
    public boolean existsByStudentId(String studentId) {
        return userMapper.findByStudentId(studentId) != null;
    }
}