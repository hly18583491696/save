package edu.mycc.xhd.service.impl;

// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.mycc.xhd.common.Result;
import edu.mycc.xhd.entity.User;
import edu.mycc.xhd.mapper.UserMapper;
import edu.mycc.xhd.service.UserService;
import edu.mycc.xhd.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder; // 暂时禁用
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 * 
 * @author xhd
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    // private final PasswordEncoder passwordEncoder; // 暂时禁用
    private final JwtUtils jwtUtils;
    // private final UserMapper userMapper; // 暂时禁用
    
    @Override
    public Result<String> login(String username, String password) {
        // 简化实现，暂时返回成功
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            String token = jwtUtils.generateToken(1L, username, 3);
            return Result.success(token);
        }
        return Result.error("用户名或密码不能为空");
    }
    
    @Override
    public Result<Void> register(User user) {
        // 简化实现，暂时返回成功
        return Result.success();
    }
    
    @Override
    public Result<User> getUserInfo(String username) {
        // 简化实现，返回一个默认用户
        User user = new User();
        user.setUsername(username);
        return Result.success(user);
    }
    
    @Override
    public Result<Void> updateUser(User user) {
        // 简化实现，暂时返回成功
        return Result.success();
    }
    
    @Override
    public Result<Void> deleteUser(Long id) {
        // 简化实现，暂时返回成功
        return Result.success();
    }
    
    // @Override
    // public Result<Page<User>> getUserList(int page, int size, String keyword) {
    //     // 简化实现，暂时返回空页面
    //     return Result.success(new Page<>());
    // }
}