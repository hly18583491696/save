package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 系统配置数据访问层
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
    
    /**
     * 根据配置键查找配置
     */
    @Select("SELECT * FROM system_config WHERE config_key = #{configKey} AND status = 1")
    SystemConfig findByConfigKey(String configKey);
    
    /**
     * 根据配置类型查找配置
     */
    @Select("SELECT * FROM system_config WHERE config_type = #{configType} AND status = 1 ORDER BY config_key")
    List<SystemConfig> findByConfigType(String configType);
    
    /**
     * 查询所有启用的配置
     */
    @Select("SELECT * FROM system_config WHERE status = 1 ORDER BY config_type, config_key")
    List<SystemConfig> findAllActive();
    
    /**
     * 批量更新配置值
     */
    @Update("UPDATE system_config SET config_value = #{configValue}, update_time = NOW() WHERE config_key = #{configKey}")
    int updateConfigValue(String configKey, String configValue);
    
    /**
     * 根据配置类型统计数量
     */
    @Select("SELECT COUNT(*) FROM system_config WHERE config_type = #{configType} AND status = 1")
    int countByConfigType(String configType);
    
    /**
     * 查询系统配置（不可删除的配置）
     */
    @Select("SELECT * FROM system_config WHERE is_system = 1 AND status = 1 ORDER BY config_type, config_key")
    List<SystemConfig> findSystemConfigs();
    
    /**
     * 查询用户配置（可删除的配置）
     */
    @Select("SELECT * FROM system_config WHERE is_system = 0 AND status = 1 ORDER BY config_type, config_key")
    List<SystemConfig> findUserConfigs();
}