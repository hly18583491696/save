package edu.mycc.xhd;

// import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 绵阳城市学院学生宿舍管理系统主启动类
 * 
 * @author xhd
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
// @MapperScan("edu.mycc.xhd.mapper")
public class MycsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MycsApplication.class, args);
        System.out.println("\n===========================================\n" +
                "  绵阳城市学院学生宿舍管理系统启动成功！\n" +
                "  访问地址：http://localhost:8080\n" +
                "===========================================\n");
    }

}