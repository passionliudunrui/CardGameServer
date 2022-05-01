package com.cardgameserver.service.impl;

import com.cardgameserver.dao.AccountDao;
import com.cardgameserver.dao.OrderDao;
import com.cardgameserver.entity.Account;
import com.cardgameserver.entity.Order;
import com.cardgameserver.entity.Seckillgoods;
import com.cardgameserver.netty.MyServer;
import com.cardgameserver.netty.MyServerHandlerBuy;
import com.cardgameserver.netty.MyServerHandlerInfo;
import com.cardgameserver.netty.MyServerHandlerPlay;
import com.cardgameserver.service.OrderService;
import com.cardgameserver.service.SeckillgoodsService;
import com.cardgameserver.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private SeckillgoodsService seckillgoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int insert(Order order) {

        return orderDao.insert(order);
    }

    /**
     * 执行数据库的秒杀的逻辑
     * @param userVo
     * @param goods
     * @return
     *
     *
     */
    @Override
    public Order secKill(UserVo userVo, Seckillgoods goods) {
        //1.减去数据库的库存
        int ans=seckillgoodsService.update(goods.getId());
        if(ans==0){
            //竞争失败
            return null;
        }
        Seckillgoods seckillgoods = seckillgoodsService.select(goods.getId());
        if(seckillgoods.getStock()<1){
            redisTemplate.opsForValue().set("isStockEmpty:"+goods.getId(),"0");
        }

        //2.生成订单插入到mysql中的订单表中
        Order order=new Order(userVo.getId(),goods.getId(),10,100,"抢购欢乐豆");

        //3.更改用户的数据  userVo的balance和happybean


        System.out.println("test");
        //4.订单存储到redis中
        redisTemplate.opsForValue().set("order:"+userVo.getId()+":"+goods.getId(),order);
        System.out.println("test");

        return order;


    }

    @Override
    public Order select(Long userId, Integer goodsId) {
        return orderDao.select( userId, goodsId);
    }
}
