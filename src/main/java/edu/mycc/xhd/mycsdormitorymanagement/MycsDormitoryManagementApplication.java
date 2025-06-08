package edu.mycc.xhd.mycsdormitorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
// @RestController // 禁用以避免与CleanApp冲突
public class MycsDormitoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MycsDormitoryManagementApplication.class, args);
    }
    
    // @GetMapping("/")
    // public String home() {
    //     return "Dormitory Management System is running!";
    // }
    
    // @GetMapping("/health")
    // public String health() {
    //     return "OK";
    // }
}
