package edu.mycc.xhd.mycsdormitorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    JpaRepositoriesAutoConfiguration.class,
    SecurityAutoConfiguration.class,
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
public class StandaloneApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StandaloneApp.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.SERVLET);
        app.run(args);
        System.out.println("Standalone Application started successfully on port 8081");
    }
}

// @RestController // 禁用以避免与CleanApp冲突
class StandaloneController {
    
    @GetMapping("/")
    public String home() {
        return "Dormitory Management System - Standalone Version is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK - System is healthy";
    }
    
    @GetMapping("/api/test")
    public String test() {
        return "Test endpoint is working perfectly!";
    }
    
    @GetMapping("/status")
    public String status() {
        return "Application Status: Running on port 8081";
    }
}