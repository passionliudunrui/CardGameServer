package com.cardgameserver;

import com.cardgameserver.rabbitmq.MQReceiver;
import com.cardgameserver.rabbitmq.MQSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CardGameServerApplicationTests {

    @Autowired
    private MQSender mqSender;

    @Autowired
    private MQReceiver mqReceiver;

    @Test
    public void testRabbitMQ(){
        mqSender.send("hello world");

    }

}
