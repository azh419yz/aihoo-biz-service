package com.aihoo.api.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.aihoo.api.admin", "com.aihoo.domain", "com.aihoo"})
@MapperScan("com.aihoo.domain.*.model.mapper")
public class AdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
    }
}
