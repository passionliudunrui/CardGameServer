package com.cardgameserver;

import com.cardgameserver.netty.MyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cardgameserver.dao")
public class CardGameServerApplication implements CommandLineRunner {
    @Autowired
    private MyServer myServer;


    public static void main(String[] args) {
        SpringApplication.run(CardGameServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        myServer.start();

        Runtime.getRuntime().addShutdownHook(
                new Thread(()->myServer.destory())
        );
    }
}
