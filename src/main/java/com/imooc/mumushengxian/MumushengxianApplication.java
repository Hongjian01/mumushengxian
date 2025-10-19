package com.imooc.mumushengxian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.imooc.mumushengxian.model.dao")
@EnableCaching
public class MumushengxianApplication {

    public static void main(String[] args) {
        SpringApplication.run(MumushengxianApplication.class, args);
    }

}
