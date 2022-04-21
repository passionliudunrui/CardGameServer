package com.cardgameserver;

import com.cardgameserver.entity.User;
import com.cardgameserver.rabbitmq.MQReceiver;
import com.cardgameserver.rabbitmq.MQSender;
import com.cardgameserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CardGameServerApplicationTests {

    @Autowired
    private MQSender mqSender;

    @Autowired
    private MQReceiver mqReceiver;


    @Autowired
    private UserService userService;

    @Test
    public void testRabbitMQ(){
        mqSender.send("hello world");

    }



    @Test
    public void test(){
        User user=new User(12L,"132","21");
        int insert = userService.insert(user);
        System.out.println(insert);
    }




}
