package com.cardgameserver.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置交换机模式的MQ队列
 * 匹配模式  一个交换机可以绑定很多的队列 只要满足匹配的条件就将信息发送到队列中去
 */
@Configuration
public class RabbitMQTopicConfig {

    private static final String QUEUE="seckillQueue";
    private static final String EXCHANGE="seckillExchange";


    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");

    }


}
