package com.cardgameserver;

import com.cardgameserver.entity.User;
import com.cardgameserver.rabbitmq.MQReceiver;
import com.cardgameserver.rabbitmq.MQSender;
import com.cardgameserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class CardGameServerApplicationTests {

    @Autowired
    private MQSender mqSender;

    @Autowired
    private MQReceiver mqReceiver;


    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRabbitMQ(){
        mqSender.send("hello world");

    }



    @Test
    public void test(){
        Boolean delete = redisTemplate.delete("userInfo:12");
        System.out.println(delete);


    }




}
