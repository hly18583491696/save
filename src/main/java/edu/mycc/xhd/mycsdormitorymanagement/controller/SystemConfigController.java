package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.entity.SystemConfig;
import edu.mycc.xhd.mycsdormitorymanagement.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置管理控制器
 * 负责处理系统配置的增删改查操作
 */
@RestController
@RequestMapping("/api/system-config")
@CrossOrigin(origins = "*")
@Slf4j
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 获取所有配置
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllConfigs() {
        log.info("获取所有系统配置请求");
        try {
            List<SystemConfig> configs = systemConfigService.getAllConfigs();
            log.info("成功获取系统配置，共{}条记录", configs.size());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", configs);
            response.put("message", "获取系统配置成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取系统配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取系统配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取分组配置
     */
    @GetMapping("/grouped")
    public ResponseEntity<Map<String, Object>> getGroupedConfigs() {
        log.info("获取分组系统配置请求");
        try {
            Map<String, Map<String, String>> groupedConfigs = systemConfigService.getAllConfigsGrouped();
            log.info("成功获取分组系统配置");
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", groupedConfigs);
            response.put("message", "获取分组配置成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取分组配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取分组配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据配置键获取配置
     */
    @GetMapping("/key/{configKey}")
    public ResponseEntity<Map<String, Object>> getConfigByKey(@PathVariable String configKey) {
        log.info("获取配置请求，配置键: {}", configKey);
        try {
            SystemConfig config = systemConfigService.getConfigByKey(configKey);
            Map<String, Object> response = new HashMap<>();
            if (config != null) {
                response.put("success", true);
                response.put("data", config);
                response.put("message", "获取配置成功");
                log.info("成功获取配置: {}", configKey);
            } else {
                response.put("success", false);
                response.put("message", "配置不存在");
                log.warn("配置不存在: {}", configKey);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 根据配置类型获取配置列表
     */
    @GetMapping("/type/{configType}")
    public ResponseEntity<Map<String, Object>> getConfigsByType(@PathVariable String configType) {
        log.info("获取配置类型请求，类型: {}", configType);
        try {
            List<SystemConfig> configs = systemConfigService.getConfigsByType(configType);
            log.info("成功获取配置类型: {}，共{}条记录", configType, configs.size());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", configs);
            response.put("message", "获取配置类型成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取配置类型失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取配置类型失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 更新单个配置
     */
    @PutMapping("/key/{configKey}")
    public ResponseEntity<Map<String, Object>> updateConfig(
            @PathVariable String configKey, 
            @RequestBody Map<String, String> request) {
        log.info("更新配置请求，配置键: {}", configKey);
        try {
            String configValue = request.get("configValue");
            if (configValue == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "配置值不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean success = systemConfigService.updateConfig(configKey, configValue);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "配置更新成功");
                log.info("配置更新成功: {} = {}", configKey, configValue);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "配置更新失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            log.error("更新配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 批量更新配置
     */
    @PutMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchUpdateConfigs(@RequestBody Map<String, String> configs) {
        log.info("批量更新配置请求，配置数量: {}", configs.size());
        try {
            boolean success = systemConfigService.batchUpdateConfigs(configs);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "批量更新配置成功");
                log.info("批量更新配置成功，更新了{}个配置", configs.size());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量更新配置失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            log.error("批量更新配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量更新配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 添加新配置
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addConfig(@RequestBody SystemConfig config) {
        log.info("添加配置请求，配置键: {}", config.getConfigKey());
        try {
            boolean success = systemConfigService.addConfig(config);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "添加配置成功");
                log.info("添加配置成功: {}", config.getConfigKey());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "添加配置失败，配置键可能已存在");
                return ResponseEntity.status(400).body(response);
            }
        } catch (Exception e) {
            log.error("添加配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "添加配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/key/{configKey}")
    public ResponseEntity<Map<String, Object>> deleteConfig(@PathVariable String configKey) {
        log.info("删除配置请求，配置键: {}", configKey);
        try {
            boolean success = systemConfigService.deleteConfig(configKey);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "删除配置成功");
                log.info("删除配置成功: {}", configKey);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除配置失败，配置可能不存在或为系统配置");
                return ResponseEntity.status(400).body(response);
            }
        } catch (Exception e) {
            log.error("删除配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 初始化默认配置
     */
    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initDefaultConfigs() {
        log.info("初始化默认配置请求");
        try {
            systemConfigService.initDefaultConfigs();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "初始化默认配置成功");
            log.info("初始化默认配置成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("初始化默认配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "初始化默认配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 重置配置为默认值
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetToDefault() {
        log.info("重置配置为默认值请求");
        try {
            boolean success = systemConfigService.resetToDefault();
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "重置配置成功");
                log.info("重置配置成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "重置配置失败");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            log.error("重置配置失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "重置配置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取系统信息（用于前端显示）
     */
    @GetMapping("/system-info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        log.info("获取系统信息请求");
        try {
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("version", "v1.0.0");
            systemInfo.put("databaseVersion", "MySQL 8.0");
            systemInfo.put("serverTime", java.time.LocalDateTime.now().toString());
            systemInfo.put("onlineUsers", 23); // 模拟数据，实际应从缓存或数据库获取
            systemInfo.put("totalStudents", 1250); // 可以调用其他服务获取
            systemInfo.put("totalRooms", 320); // 可以调用其他服务获取
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", systemInfo);
            response.put("message", "获取系统信息成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取系统信息失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取系统信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}