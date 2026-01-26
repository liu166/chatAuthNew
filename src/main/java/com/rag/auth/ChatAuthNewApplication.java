package com.rag.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rag.auth.dao")
public class ChatAuthNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAuthNewApplication.class, args);
    }

}
