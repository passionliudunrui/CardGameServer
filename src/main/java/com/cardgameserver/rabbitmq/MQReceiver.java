package com.cardgameserver.rabbitmq;

import ch.qos.logback.core.util.InvocationGate;
import com.cardgameserver.entity.Order;
import com.cardgameserver.entity.Seckillgoods;
import com.cardgameserver.service.GoodsService;
import com.cardgameserver.service.OrderService;
import com.cardgameserver.service.SeckillgoodsService;
import com.cardgameserver.utils.JsonUtils;
import com.cardgameserver.vo.SeckillMessage;
import com.cardgameserver.vo.UserVo;
import com.fasterxml.jackson.core.JsonToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillgoodsService seckillgoodsService;

    @RabbitListener(queues = "seckillQueue")

    public void receive(String msg) throws Exception {
        System.out.println("-----------------------------------11111111111111-------------------------------------------");
        log.info("接收用户秒杀消息"+msg);
        SeckillMessage seckillMessage = JsonUtils.objectFromJsonStr(msg, SeckillMessage.class);
        UserVo userVo=seckillMessage.getUserVo();
        Integer goodsId=seckillMessage.getGoodsId();

        //从数据库判断是否还有库存
        Seckillgoods goods = seckillgoodsService.select(goodsId);
        if(goods.getStock()<1){
            return;
        }
        //从redis中判断是否重复购买
         Order order = (Order) redisTemplate.opsForValue().get("order:" + userVo.getId() + ":" + goodsId);
        if(order!=null){
            return;
        }

        orderService.secKill(userVo,goods);

    }


}
