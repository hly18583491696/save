package edu.mycc.xhd.mycsdormitorymanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.mycc.xhd.mycsdormitorymanagement.common.Result;
import edu.mycc.xhd.mycsdormitorymanagement.common.PageRequest;
import edu.mycc.xhd.mycsdormitorymanagement.entity.User;
import edu.mycc.xhd.mycsdormitorymanagement.service.UserService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
// @Tag(name = "用户管理", description = "用户相关接口")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    // @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody @Valid User user) {
        try {
            String username = user.getUsername();
            String password = user.getPassword();
            String token = userService.login(username, password);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return Result.success("登录成功", data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/register")
    // @Operation(summary = "用户注册")
    public Result<User> register(@RequestBody @Valid User user) {
        try {
            User newUser = userService.register(user);
            // 清除密码信息
            newUser.setPassword(null);
            return Result.success("注册成功", newUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    // @Operation(summary = "根据ID获取用户信息")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null); // 清除密码信息
            return Result.success(user);
        }
        return Result.notFound("用户不存在");
    }
    
    @GetMapping("/username/{username}")
    // @Operation(summary = "根据用户名获取用户信息")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.notFound("用户不存在");
    }
    
    @PostMapping("/list")
    // @Operation(summary = "分页查询用户列表")
    public Result<Page<User>> getUsers(PageRequest pageRequest) {
        Page<User> userPage = userService.getUserList(pageRequest);
        // 清除密码信息
        userPage.getRecords().forEach(user -> user.setPassword(null));
        return Result.success(userPage);
    }
    
    @GetMapping("/role/{role}")
    // @Operation(summary = "根据角色查询用户列表")
    public Result<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role);
        users.forEach(user -> user.setPassword(null));
        return Result.success(users);
    }
    
    @PostMapping
    // @Operation(summary = "创建用户")
    public Result<User> createUser(@RequestBody @Valid User user) {
        try {
            User newUser = userService.createUser(user);
            newUser.setPassword(null);
            return Result.success("用户创建成功", newUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    // @Operation(summary = "更新用户信息")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody @Valid User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            updatedUser.setPassword(null);
            return Result.success("用户更新成功", updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    // @Operation(summary = "删除用户")
    public Result<String> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        return success ? Result.success("用户删除成功") : Result.error("用户删除失败");
    }
    
    @DeleteMapping("/batch")
    // @Operation(summary = "批量删除用户")
    public Result<String> deleteUsers(@RequestBody List<Long> ids) {
        boolean success = userService.deleteUsers(ids);
        return success ? Result.success("用户批量删除成功") : Result.error("用户批量删除失败");
    }
    
    @PutMapping("/{id}/status")
    // @Operation(summary = "启用/禁用用户")
    public Result<String> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusData) {
        Integer status = statusData.get("status");
        boolean success = userService.updateUserStatus(id, status);
        return success ? Result.success("用户状态更新成功") : Result.error("用户状态更新失败");
    }
    
    @PutMapping("/{id}/reset-password")
    // @Operation(summary = "重置用户密码")
    public Result<String> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> passwordData) {
        String newPassword = passwordData.get("newPassword");
        boolean success = userService.resetPassword(id, newPassword);
        return success ? Result.success("密码重置成功") : Result.error("密码重置失败");
    }
    
    @PutMapping("/{id}/change-password")
    // @Operation(summary = "修改密码")
    public Result<String> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwordData) {
        try {
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            boolean success = userService.changePassword(id, oldPassword, newPassword);
            return success ? Result.success("密码修改成功") : Result.error("密码修改失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/check-username/{username}")
    // @Operation(summary = "检查用户名是否存在")
    public Result<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }
    
    @GetMapping("/check-student-id/{studentId}")
    // @Operation(summary = "检查学号是否存在")
    public Result<Boolean> checkStudentId(@PathVariable String studentId) {
        boolean exists = userService.existsByStudentId(studentId);
        return Result.success(exists);
    }
}