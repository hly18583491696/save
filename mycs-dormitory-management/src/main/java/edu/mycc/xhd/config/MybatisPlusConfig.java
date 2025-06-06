package edu.mycc.xhd.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus配置类
 * 
 * @author xhd
 */
@Configuration
public class MybatisPlusConfig {
    
    /**
     * MyBatis-Plus拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        
        return interceptor;
    }
    
    /**
     * 元数据处理器
     */
    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {
        
        @Override
        public void insertFill(MetaObject metaObject) {
            // 创建时间
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            // 更新时间
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            // 逻辑删除标志
            this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
            
            // TODO: 从当前登录用户获取用户ID
            Long currentUserId = getCurrentUserId();
            if (currentUserId != null) {
                // 创建人
                this.strictInsertFill(metaObject, "createBy", Long.class, currentUserId);
                // 更新人
                this.strictInsertFill(metaObject, "updateBy", Long.class, currentUserId);
            }
        }
        
        @Override
        public void updateFill(MetaObject metaObject) {
            // 更新时间
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            
            // TODO: 从当前登录用户获取用户ID
            Long currentUserId = getCurrentUserId();
            if (currentUserId != null) {
                // 更新人
                this.strictUpdateFill(metaObject, "updateBy", Long.class, currentUserId);
            }
        }
        
        /**
         * 获取当前登录用户ID
         * TODO: 实现从SecurityContext或ThreadLocal获取当前用户ID
         */
        private Long getCurrentUserId() {
            // 暂时返回null，后续集成JWT认证后实现
            return null;
        }
    }
}