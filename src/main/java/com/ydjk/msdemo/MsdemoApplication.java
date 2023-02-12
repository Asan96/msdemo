package com.ydjk.msdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.ydjk.msdemo.mapper")
@SpringBootApplication
public class MsdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsdemoApplication.class, args);
    }

}
