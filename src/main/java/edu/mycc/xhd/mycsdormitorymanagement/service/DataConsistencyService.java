package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormAccommodation;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Student;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormAccommodationMapper;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据一致性检查和修复服务
 * 定期校验和修复学生表与住宿记录表之间的数据不一致问题
 */
@Service
public class DataConsistencyService {
    
    private static final Logger log = LoggerFactory.getLogger(DataConsistencyService.class);
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private DormAccommodationMapper accommodationMapper;
    
    /**
     * 数据不一致记录
     */
    public static class InconsistentRecord {
        private Long accommodationId;
        private Long studentId;
        private String field;
        private String accommodationValue;
        private String studentValue;
        
        public InconsistentRecord(Long accommodationId, Long studentId, String field, 
                                String accommodationValue, String studentValue) {
            this.accommodationId = accommodationId;
            this.studentId = studentId;
            this.field = field;
            this.accommodationValue = accommodationValue;
            this.studentValue = studentValue;
        }
        
        // Getters
        public Long getAccommodationId() { return accommodationId; }
        public Long getStudentId() { return studentId; }
        public String getField() { return field; }
        public String getAccommodationValue() { return accommodationValue; }
        public String getStudentValue() { return studentValue; }
        
        @Override
        public String toString() {
            return String.format("住宿记录ID:%d, 学生ID:%d, 字段:%s, 住宿记录值:'%s', 学生表值:'%s'", 
                    accommodationId, studentId, field, accommodationValue, studentValue);
        }
    }
    
    /**
     * 检查数据一致性
     * @return 不一致的记录列表
     */
    public List<InconsistentRecord> checkDataConsistency() {
        log.info("开始检查学生表与住宿记录表的数据一致性");
        
        List<InconsistentRecord> inconsistentRecords = new ArrayList<>();
        
        try {
            // 获取所有有效的住宿记录
            List<DormAccommodation> accommodations = accommodationMapper.findAllActive();
            
            for (DormAccommodation accommodation : accommodations) {
                // 获取对应的学生信息
                Student student = studentMapper.selectById(accommodation.getStudentId());
                
                if (student == null) {
                    log.warn("住宿记录中的学生不存在，住宿记录ID: {}, 学生ID: {}", 
                            accommodation.getId(), accommodation.getStudentId());
                    continue;
                }
                
                // 检查学生姓名
                if (!isEqual(accommodation.getStudentName(), student.getStudentName())) {
                    inconsistentRecords.add(new InconsistentRecord(
                            accommodation.getId(), student.getId(), "student_name",
                            accommodation.getStudentName(), student.getStudentName()));
                }
                
                // 检查学号
                if (!isEqual(accommodation.getStudentNumber(), student.getStudentNumber())) {
                    inconsistentRecords.add(new InconsistentRecord(
                            accommodation.getId(), student.getId(), "student_number",
                            accommodation.getStudentNumber(), student.getStudentNumber()));
                }
                
                // 检查班级
                if (!isEqual(accommodation.getClassName(), student.getClassName())) {
                    inconsistentRecords.add(new InconsistentRecord(
                            accommodation.getId(), student.getId(), "class_name",
                            accommodation.getClassName(), student.getClassName()));
                }
                
                // 检查身份证号
                if (!isEqual(accommodation.getIdCard(), student.getIdCard())) {
                    inconsistentRecords.add(new InconsistentRecord(
                            accommodation.getId(), student.getId(), "id_card",
                            accommodation.getIdCard(), student.getIdCard()));
                }
            }
            
            log.info("数据一致性检查完成，发现{}条不一致记录", inconsistentRecords.size());
            
        } catch (Exception e) {
            log.error("数据一致性检查失败", e);
        }
        
        return inconsistentRecords;
    }
    
    /**
     * 修复数据不一致问题
     * @return 修复的记录数量
     */
    @Transactional
    public int fixDataInconsistency() {
        log.info("开始修复数据不一致问题");
        
        List<InconsistentRecord> inconsistentRecords = checkDataConsistency();
        
        if (inconsistentRecords.isEmpty()) {
            log.info("没有发现数据不一致问题，无需修复");
            return 0;
        }
        
        int fixedCount = 0;
        
        try {
            // 按住宿记录ID分组处理
            for (InconsistentRecord record : inconsistentRecords) {
                DormAccommodation accommodation = accommodationMapper.selectById(record.getAccommodationId());
                Student student = studentMapper.selectById(record.getStudentId());
                
                if (accommodation != null && student != null) {
                    // 以学生表的数据为准，更新住宿记录
                    accommodation.setStudentName(student.getStudentName());
                    accommodation.setStudentNumber(student.getStudentNumber());
                    accommodation.setClassName(student.getClassName());
                    accommodation.setIdCard(student.getIdCard());
                    accommodation.setUpdateTime(LocalDateTime.now());
                    
                    int updateResult = accommodationMapper.updateById(accommodation);
                    if (updateResult > 0) {
                        fixedCount++;
                        log.info("修复住宿记录数据不一致，住宿记录ID: {}, 学生ID: {}", 
                                accommodation.getId(), student.getId());
                    }
                }
            }
            
            log.info("数据不一致修复完成，共修复{}条记录", fixedCount);
            
        } catch (Exception e) {
            log.error("修复数据不一致失败", e);
            throw new RuntimeException("修复数据不一致失败: " + e.getMessage());
        }
        
        return fixedCount;
    }
    
    /**
     * 定时任务：每天凌晨2点执行数据一致性检查
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledDataConsistencyCheck() {
        log.info("开始执行定时数据一致性检查任务");
        
        List<InconsistentRecord> inconsistentRecords = checkDataConsistency();
        
        if (!inconsistentRecords.isEmpty()) {
            log.warn("发现{}条数据不一致记录，建议及时修复", inconsistentRecords.size());
            
            // 记录详细的不一致信息
            for (InconsistentRecord record : inconsistentRecords) {
                log.warn("数据不一致: {}", record.toString());
            }
            
            // 可以选择自动修复或发送告警
            // fixDataInconsistency();
        } else {
            log.info("定时数据一致性检查完成，数据一致性良好");
        }
    }
    
    /**
     * 比较两个字符串是否相等（处理null值）
     */
    private boolean isEqual(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }
    
    /**
     * 获取数据一致性报告
     */
    public String getConsistencyReport() {
        List<InconsistentRecord> inconsistentRecords = checkDataConsistency();
        
        if (inconsistentRecords.isEmpty()) {
            return "数据一致性检查通过，没有发现不一致问题。";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("数据一致性检查报告\n");
        report.append("检查时间: ").append(LocalDateTime.now()).append("\n");
        report.append("发现不一致记录数量: ").append(inconsistentRecords.size()).append("\n\n");
        
        for (InconsistentRecord record : inconsistentRecords) {
            report.append(record.toString()).append("\n");
        }
        
        return report.toString();
    }
}