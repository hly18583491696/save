package edu.mycc.xhd.mycsdormitorymanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单测试控制器 - 最小可用版本
 */
// @RestController // 暂时禁用以避免与StandaloneApp冲突
public class SimpleTestController {

    @GetMapping("/")
    public String root() {
        return "Dormitory Management System - Running";
    }

    @GetMapping("/test")
    public String test() {
        return "Test endpoint - OK";
    }

    @GetMapping("/api/test/health")
    public String health() {
        return "OK - Service is running";
    }

    @GetMapping("/api/test/status")
    public String status() {
        return "Status: Active";
    }

    @GetMapping("/api/test/hello")
    public String hello() {
        return "Hello from Dormitory Management System!";
    }
}