package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.service.DataConsistencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据一致性管理控制器
 * 提供数据一致性检查和修复的API接口
 */
@RestController
@RequestMapping("/api/data-consistency")
@CrossOrigin(origins = "*")
public class DataConsistencyController {
    
    private static final Logger log = LoggerFactory.getLogger(DataConsistencyController.class);
    
    @Autowired
    private DataConsistencyService dataConsistencyService;
    
    /**
     * 检查数据一致性
     */
    @GetMapping("/check")
    public Map<String, Object> checkDataConsistency() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("接收到数据一致性检查请求");
            
            List<DataConsistencyService.InconsistentRecord> inconsistentRecords = 
                    dataConsistencyService.checkDataConsistency();
            
            response.put("success", true);
            response.put("message", "数据一致性检查完成");
            response.put("data", Map.of(
                    "inconsistentCount", inconsistentRecords.size(),
                    "inconsistentRecords", inconsistentRecords,
                    "isConsistent", inconsistentRecords.isEmpty()
            ));
            
            log.info("数据一致性检查完成，发现{}条不一致记录", inconsistentRecords.size());
            
        } catch (Exception e) {
            log.error("数据一致性检查失败", e);
            response.put("success", false);
            response.put("message", "数据一致性检查失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 修复数据不一致问题
     */
    @PostMapping("/fix")
    public Map<String, Object> fixDataInconsistency() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("接收到数据不一致修复请求");
            
            int fixedCount = dataConsistencyService.fixDataInconsistency();
            
            response.put("success", true);
            response.put("message", "数据不一致修复完成");
            response.put("data", Map.of(
                    "fixedCount", fixedCount,
                    "message", fixedCount > 0 ? 
                            String.format("成功修复%d条不一致记录", fixedCount) : 
                            "没有发现需要修复的数据不一致问题"
            ));
            
            log.info("数据不一致修复完成，共修复{}条记录", fixedCount);
            
        } catch (Exception e) {
            log.error("数据不一致修复失败", e);
            response.put("success", false);
            response.put("message", "数据不一致修复失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取数据一致性报告
     */
    @GetMapping("/report")
    public Map<String, Object> getConsistencyReport() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("接收到数据一致性报告请求");
            
            String report = dataConsistencyService.getConsistencyReport();
            
            response.put("success", true);
            response.put("message", "数据一致性报告生成成功");
            response.put("data", Map.of("report", report));
            
        } catch (Exception e) {
            log.error("生成数据一致性报告失败", e);
            response.put("success", false);
            response.put("message", "生成数据一致性报告失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 一键检查并修复数据不一致问题
     */
    @PostMapping("/check-and-fix")
    public Map<String, Object> checkAndFixDataInconsistency() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("接收到一键检查并修复数据不一致请求");
            
            // 先检查
            List<DataConsistencyService.InconsistentRecord> inconsistentRecords = 
                    dataConsistencyService.checkDataConsistency();
            
            if (inconsistentRecords.isEmpty()) {
                response.put("success", true);
                response.put("message", "数据一致性良好，无需修复");
                response.put("data", Map.of(
                        "inconsistentCount", 0,
                        "fixedCount", 0,
                        "message", "数据一致性检查通过，没有发现不一致问题"
                ));
            } else {
                // 有不一致数据，进行修复
                int fixedCount = dataConsistencyService.fixDataInconsistency();
                
                response.put("success", true);
                response.put("message", "数据不一致检查和修复完成");
                response.put("data", Map.of(
                        "inconsistentCount", inconsistentRecords.size(),
                        "fixedCount", fixedCount,
                        "message", String.format("发现%d条不一致记录，成功修复%d条", 
                                inconsistentRecords.size(), fixedCount)
                ));
            }
            
            log.info("一键检查并修复完成");
            
        } catch (Exception e) {
            log.error("一键检查并修复失败", e);
            response.put("success", false);
            response.put("message", "一键检查并修复失败: " + e.getMessage());
        }
        
        return response;
    }
}