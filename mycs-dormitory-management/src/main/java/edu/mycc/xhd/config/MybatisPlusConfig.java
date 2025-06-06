package edu.mycc.xhd.config;

// import com.baomidou.mybatisplus.annotation.DbType;
// import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
// import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
// import org.apache.ibatis.reflection.MetaObject;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.stereotype.Component;

// import java.time.LocalDateTime;

/**
 * MyBatis-Plus配置类 - 暂时禁用
 * 
 * @author xhd
 */
// @Configuration
public class MybatisPlusConfig {
    
    // 暂时注释掉所有MyBatis-Plus相关配置
    
    // /**
    //  * MyBatis-Plus拦截器
    //  */
    // @Bean
    // public MybatisPlusInterceptor mybatisPlusInterceptor() {
    //     MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    //     
    //     // 分页插件
    //     interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    //     
    //     return interceptor;
    // }
    
    // /**
    //  * 元数据处理器
    //  */
    // @Component
    // public static class MyMetaObjectHandler implements MetaObjectHandler {
    //     
    //     @Override
    //     public void insertFill(MetaObject metaObject) {
    //         // 创建时间
    //         this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    //         // 更新时间
    //         this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    //         // 逻辑删除标志
    //         this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
    //         
    //         // TODO: 从当前登录用户获取用户ID
    //         // 创建人
    //         // this.strictInsertFill(metaObject, "createBy", Long.class, getCurrentUserId());
    //         // 更新人
    //         // this.strictInsertFill(metaObject, "updateBy", Long.class, getCurrentUserId());
    //     }
    //     
    //     @Override
    //     public void updateFill(MetaObject metaObject) {
    //         // 更新时间
    //         this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    //         
    //         // TODO: 从当前登录用户获取用户ID
    //         // 更新人
    //         // this.strictUpdateFill(metaObject, "updateBy", Long.class, getCurrentUserId());
    //     }
    //     
    //     /**
    //      * 获取当前登录用户ID
    //      * TODO: 实现获取当前登录用户的逻辑
    //      */
    //     // private Long getCurrentUserId() {
    //     //     // 这里应该从SecurityContext或其他地方获取当前登录用户的ID
    //     //     return 1L; // 临时返回固定值
    //     // }
    // }
}