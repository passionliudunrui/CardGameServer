package com.cardgameserver.thread;

import com.cardgameserver.entity.Order;
import com.cardgameserver.entity.Seckillgoods;
import com.cardgameserver.service.SeckillgoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;


@Configuration
@EnableScheduling

/**
 * 定时任务 用来执行每周六周天 特价商品的上架
 * 将数据库和redis中的商品都更新库存
 *
 * 提前执行
 * 用户在秒杀之前 要判断时间是不是大于本周六晚的八点
 */

public class ScheduleTask {
    @Autowired
    private SeckillgoodsService seckillgoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    private Integer goodsId;

    @Scheduled(cron = "0 15 10 ? * *")  //每天10.15触发一次
    @Scheduled(cron = "0 0 20 ? * SAT-SUN")
    private void run(){
        //创建一个商品存到mysql中
        //时间的设置
        Date date=new Date();
        long time = date.getTime();

        Date endDate=new Date();
//        Seckillgoods seckillgoods=new Seckillgoods(10,100,10,1,"特惠商品",date);



    }

}
