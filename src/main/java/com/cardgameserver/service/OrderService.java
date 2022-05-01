package com.cardgameserver.service;

import com.cardgameserver.entity.Order;
import com.cardgameserver.entity.Seckillgoods;
import com.cardgameserver.vo.UserVo;

public interface OrderService {
    int insert(Order order);
    Order secKill(UserVo userVo, Seckillgoods goods);
    Order select(Long userId,Integer goodsId);
}
