package edu.mycc.xhd.mycsdormitorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
// @RestController // 禁用以避免与CleanApp冲突
public class MinimalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
    }
    
    // @GetMapping("/")
    // public String home() {
    //     return "Dormitory Management System is running!";
    // }
    
    // @GetMapping("/health")
    public String health() {
        return "OK";
    }
    
    @GetMapping("/api/test")
    public String test() {
        return "Test endpoint is working!";
    }
}