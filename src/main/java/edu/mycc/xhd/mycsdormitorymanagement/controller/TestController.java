package edu.mycc.xhd.mycsdormitorymanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController // 暂时禁用以避免与StandaloneApp冲突
// @RequestMapping("/api")
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "Hello, World! Server is running on port 8081";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}