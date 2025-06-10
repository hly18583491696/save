package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.common.Result;
import edu.mycc.xhd.mycsdormitorymanagement.dto.LoginRequest;
import edu.mycc.xhd.mycsdormitorymanagement.dto.RegisterRequest;
import edu.mycc.xhd.mycsdormitorymanagement.entity.User;
import edu.mycc.xhd.mycsdormitorymanagement.service.UserService;
import edu.mycc.xhd.mycsdormitorymanagement.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        log.info("用户登录尝试: username={}", loginRequest.getUsername());
        try {
            String token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            
            // 获取用户信息
            User user = userService.getUserByUsername(loginRequest.getUsername());
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("realName", user.getRealName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole());
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", userData);
            
            log.info("用户登录成功: username={}", loginRequest.getUsername());
            return Result.success(result, "登录成功");
        } catch (Exception e) {
            log.warn("用户登录失败: username={}, error={}", loginRequest.getUsername(), e.getMessage());
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        log.info("用户注册尝试: username={}, email={}", registerRequest.getUsername(), registerRequest.getEmail());
        try {
            boolean success = userService.register(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getRealName(),
                registerRequest.getEmail(),
                registerRequest.getPhone()
            );
            if (success) {
                log.info("用户注册成功: username={}", registerRequest.getUsername());
                return ResponseEntity.ok(Map.of("message", "注册成功"));
            } else {
                log.warn("用户注册失败: username={}", registerRequest.getUsername());
                return ResponseEntity.badRequest().body(Map.of("message", "注册失败"));
            }
        } catch (Exception e) {
            log.error("用户注册异常: username={}, error={}", registerRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String token) {
        log.debug("获取当前用户信息请求");
        try {
            // 验证token
            if (token == null || !token.startsWith("Bearer ")) {
                log.warn("无效的token格式: {}", token);
                return Result.unauthorized("无效的token格式");
            }
            
            // 从token中提取用户名（简化实现，实际应该解析JWT）
            String tokenValue = token.substring(7); // 移除"Bearer "
            String username = null;
            if (tokenValue.startsWith("login_success_")) {
                username = tokenValue.substring("login_success_".length());
            }
            
            if (username == null) {
                log.warn("无法从token中提取用户名: {}", token);
                return Result.unauthorized("无效的token");
            }
            
            // 根据用户名获取用户信息
            User user = userService.getUserByUsername(username);
            if (user == null) {
                log.warn("用户不存在: {}", username);
                return Result.unauthorized("用户不存在");
            }
            
            // 检查用户角色权限（只允许ADMIN和TEACHER登录）
            if (!"ADMIN".equals(user.getRole()) && !"TEACHER".equals(user.getRole())) {
                log.warn("用户角色权限不足: username={}, role={}", username, user.getRole());
                return Result.unauthorized("权限不足，只允许管理员和教师登录");
            }
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("realName", user.getRealName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole());
            userData.put("status", user.getStatus());
            
            log.info("成功获取用户信息: username={}, role={}", username, user.getRole());
            return Result.success(userData);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.error("获取用户信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result logout() {
        log.info("用户登出操作");
        return Result.success(null, "登出成功");
    }
}