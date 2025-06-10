package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.SystemConfig;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.SystemConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置服务类
 */
@Slf4j
@Service
public class SystemConfigService {
    
    @Autowired
    private SystemConfigMapper systemConfigMapper;
    
    /**
     * 获取所有配置
     */
    public List<SystemConfig> getAllConfigs() {
        return systemConfigMapper.findAllActive();
    }
    
    /**
     * 根据配置键获取配置值
     */
    public String getConfigValue(String configKey) {
        SystemConfig config = systemConfigMapper.findByConfigKey(configKey);
        return config != null ? config.getConfigValue() : null;
    }
    
    /**
     * 根据配置键获取配置
     */
    public SystemConfig getConfigByKey(String configKey) {
        return systemConfigMapper.findByConfigKey(configKey);
    }
    
    /**
     * 根据配置类型获取配置列表
     */
    public List<SystemConfig> getConfigsByType(String configType) {
        return systemConfigMapper.findByConfigType(configType);
    }
    
    /**
     * 获取所有配置并按类型分组
     */
    public Map<String, Map<String, String>> getAllConfigsGrouped() {
        List<SystemConfig> configs = systemConfigMapper.findAllActive();
        Map<String, Map<String, String>> groupedConfigs = new HashMap<>();
        
        for (SystemConfig config : configs) {
            String type = config.getConfigType();
            if (!groupedConfigs.containsKey(type)) {
                groupedConfigs.put(type, new HashMap<>());
            }
            groupedConfigs.get(type).put(config.getConfigKey(), config.getConfigValue());
        }
        
        return groupedConfigs;
    }
    
    /**
     * 更新配置值
     */
    @Transactional
    public boolean updateConfig(String configKey, String configValue) {
        try {
            SystemConfig existingConfig = systemConfigMapper.findByConfigKey(configKey);
            if (existingConfig != null) {
                existingConfig.setConfigValue(configValue);
                existingConfig.setUpdateTime(LocalDateTime.now());
                return systemConfigMapper.updateById(existingConfig) > 0;
            } else {
                log.warn("配置键不存在: {}", configKey);
                return false;
            }
        } catch (Exception e) {
            log.error("更新配置失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 批量更新配置
     */
    @Transactional
    public boolean batchUpdateConfigs(Map<String, String> configs) {
        try {
            for (Map.Entry<String, String> entry : configs.entrySet()) {
                updateConfig(entry.getKey(), entry.getValue());
            }
            return true;
        } catch (Exception e) {
            log.error("批量更新配置失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 添加新配置
     */
    @Transactional
    public boolean addConfig(SystemConfig config) {
        try {
            // 检查配置键是否已存在
            SystemConfig existingConfig = systemConfigMapper.findByConfigKey(config.getConfigKey());
            if (existingConfig != null) {
                log.warn("配置键已存在: {}", config.getConfigKey());
                return false;
            }
            
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());
            config.setStatus(1); // 默认启用
            
            return systemConfigMapper.insert(config) > 0;
        } catch (Exception e) {
            log.error("添加配置失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 删除配置（仅限非系统配置）
     */
    @Transactional
    public boolean deleteConfig(String configKey) {
        try {
            SystemConfig config = systemConfigMapper.findByConfigKey(configKey);
            if (config == null) {
                log.warn("配置不存在: {}", configKey);
                return false;
            }
            
            if (config.getIsSystem() == 1) {
                log.warn("系统配置不允许删除: {}", configKey);
                return false;
            }
            
            return systemConfigMapper.deleteById(config.getId()) > 0;
        } catch (Exception e) {
            log.error("删除配置失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 初始化默认配置
     */
    @Transactional
    public void initDefaultConfigs() {
        try {
            // 基本设置
            addDefaultConfig("system_name", "学生宿舍管理系统", "basic", "系统名称", 1);
            addDefaultConfig("school_name", "某某大学", "basic", "学校名称", 1);
            addDefaultConfig("admin_email", "admin@university.edu", "basic", "管理员邮箱", 1);
            addDefaultConfig("contact_phone", "400-123-4567", "basic", "联系电话", 1);
            
            // 宿舍设置
            addDefaultConfig("default_room_type", "quad", "dormitory", "默认房间类型", 1);
            addDefaultConfig("max_occupancy", "4", "dormitory", "最大入住人数", 1);
            addDefaultConfig("allow_mixed_gender", "false", "dormitory", "允许性别混住", 1);
            addDefaultConfig("auto_assignment", "true", "dormitory", "自动分配宿舍", 1);
            
            // 通知设置
            addDefaultConfig("email_notification", "true", "notification", "邮件通知", 1);
            addDefaultConfig("sms_notification", "false", "notification", "短信通知", 1);
            addDefaultConfig("system_announcement", "true", "notification", "系统公告", 1);
            addDefaultConfig("maintenance_reminder", "true", "notification", "维护提醒", 1);
            
            // 安全设置
            addDefaultConfig("min_password_length", "8", "security", "密码最小长度", 1);
            addDefaultConfig("max_login_attempts", "5", "security", "登录失败锁定次数", 1);
            addDefaultConfig("session_timeout", "120", "security", "会话超时时间（分钟）", 1);
            addDefaultConfig("enforce_password_complexity", "true", "security", "强制密码复杂度", 1);
            
            // 备份设置
            addDefaultConfig("auto_backup", "true", "backup", "自动备份", 1);
            addDefaultConfig("backup_frequency", "weekly", "backup", "备份频率", 1);
            
            log.info("默认配置初始化完成");
        } catch (Exception e) {
            log.error("初始化默认配置失败: {}", e.getMessage());
        }
    }
    
    /**
     * 添加默认配置（如果不存在）
     */
    private void addDefaultConfig(String key, String value, String type, String description, Integer isSystem) {
        SystemConfig existingConfig = systemConfigMapper.findByConfigKey(key);
        if (existingConfig == null) {
            SystemConfig config = new SystemConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            config.setConfigType(type);
            config.setConfigDescription(description);
            config.setIsSystem(isSystem);
            config.setStatus(1);
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());
            systemConfigMapper.insert(config);
        }
    }
    
    /**
     * 重置配置为默认值
     */
    @Transactional
    public boolean resetToDefault() {
        try {
            initDefaultConfigs();
            return true;
        } catch (Exception e) {
            log.error("重置配置失败: {}", e.getMessage());
            return false;
        }
    }
}