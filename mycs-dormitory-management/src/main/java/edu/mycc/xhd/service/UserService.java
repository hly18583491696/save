package edu.mycc.xhd.service;

// import com.baomidou.mybatisplus.extension.service.IService;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.mycc.xhd.entity.User;
import edu.mycc.xhd.common.Result;

/**
 * 用户服务接口
 * 
 * @author xhd
 */
public interface UserService {
    
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
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return 用户信息
     */
    Result<User> getUserInfo(String username);
    
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
}