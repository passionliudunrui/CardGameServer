package com.cardgameserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cardgameserver.dao")
public class CardGameServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardGameServerApplication.class, args);
    }

}
