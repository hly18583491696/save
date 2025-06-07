package edu.mycc.xhd.mycsdormitorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.mybatis.spring.annotation.MapperScan;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@MapperScan("edu.mycc.xhd.mycsdormitorymanagement.mapper")
public class CleanApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CleanApp.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.SERVLET);
        app.run(args);
        System.out.println("Clean Application started successfully on port 8081");
    }
    
    @RestController
    public static class CleanController {
        
        @GetMapping("/")
        public String home() {
            return "Clean Dormitory Management System is running!";
        }
        
        @GetMapping("/health")
        public String health() {
            return "OK - Clean System is healthy";
        }
        
        @GetMapping("/api/test")
        public String test() {
            return "Clean test endpoint is working!";
        }
        
        @GetMapping("/status")
        public String status() {
            return "Clean Application Status: Running on port 8081";
        }
    }
}