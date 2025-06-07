package edu.mycc.xhd.mycsdormitorymanagement.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 - 简化版本
 */
@Component
public class JwtUtils {
    
    private String secret = "mycs_dormitory_secret_key_2024";
    private int jwtExpiration = 86400; // 24小时
    
    /**
     * 从token中获取用户名 - 简化实现
     */
    public String getUsernameFromToken(String token) {
        try {
            // 简单的Base64解码实现
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(Base64.getDecoder().decode(parts[1]));
                // 简单解析用户名（实际项目中应该使用JSON解析）
                if (payload.contains("username")) {
                    int start = payload.indexOf("username\": \"") + 12;
                    int end = payload.indexOf("\"", start);
                    if (start > 11 && end > start) {
                        return payload.substring(start, end);
                    }
                }
            }
        } catch (Exception e) {
            // 忽略解析错误
        }
        return null;
    }
    
    /**
     * 检查token是否过期 - 简化实现
     */
    private Boolean isTokenExpired(String token) {
        // 简化实现：暂时返回false，实际项目中应该检查过期时间
        return false;
    }
    
    /**
     * 生成token - 简化实现
     */
    public String generateToken(String username) {
        // 简化的JWT实现：header.payload.signature
        String header = Base64.getEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getEncoder().encodeToString(
            ("{\"username\":\"" + username + "\",\"exp\":" + (System.currentTimeMillis() + jwtExpiration * 1000) + "}").getBytes()
        );
        String signature = Base64.getEncoder().encodeToString((header + "." + payload + "." + secret).getBytes());
        return header + "." + payload + "." + signature;
    }
    
    /**
     * 生成token（带用户ID和角色）- 简化实现
     */
    public String generateToken(Long userId, String username, String role) {
        return generateToken(username); // 简化实现，只使用用户名
    }
    
    /**
     * 验证token - 简化实现
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return tokenUsername != null && tokenUsername.equals(username) && !isTokenExpired(token);
    }
}