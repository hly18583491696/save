package edu.mycc.xhd.controller;

// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.mycc.xhd.common.Result;
import edu.mycc.xhd.entity.User;
import edu.mycc.xhd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 用户控制器
 * 
 * @author xhd
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        return userService.login(request.getUsername(), request.getPassword());
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody User user) {
        return userService.register(user);
    }
    
    // /**
    //  * 分页查询用户
    //  */
    // @GetMapping("/page")
    // public Result<Page<User>> pageUsers(
    //         @RequestParam(defaultValue = "1") Integer page,
    //         @RequestParam(defaultValue = "10") Integer size,
    //         @RequestParam(required = false) String username,
    //         @RequestParam(required = false) String realName,
    //         @RequestParam(required = false) Integer userType) {
    //     return userService.pageUsers(page, size, username, realName, userType);
    // }
    
    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/info/{username}")
    public Result<User> getUserInfo(@PathVariable String username) {
        return userService.getUserInfo(username);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping
    public Result<Void> updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
    
    // /**
    //  * 重置密码
    //  */
    // @PostMapping("/{id}/reset-password")
    // public Result<Void> resetPassword(@PathVariable Long id, @RequestBody ResetPasswordRequest request) {
    //     return userService.resetPassword(id, request.getNewPassword());
    // }
    
    // /**
    //  * 修改密码
    //  */
    // @PostMapping("/{id}/password")
    // public Result<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
    //     return userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
    // }
    
    // /**
    //  * 启用/禁用用户
    //  */
    // @PostMapping("/{id}/status")
    // public Result<Void> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
    //     return userService.updateStatus(id, request.getStatus());
    // }
    
    /**
     * 登录请求
     */
    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    /**
     * 重置密码请求
     */
    public static class ResetPasswordRequest {
        private String newPassword;
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
    
    /**
     * 修改密码请求
     */
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
        
        public String getOldPassword() {
            return oldPassword;
        }
        
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
    
    /**
     * 更新状态请求
     */
    public static class UpdateStatusRequest {
        private Integer status;
        
        public Integer getStatus() {
            return status;
        }
        
        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}