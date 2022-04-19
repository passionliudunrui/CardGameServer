package com.cardgameserver.service;

import com.cardgameserver.entity.Seckillgoods;

public interface SeckillgoodsService {
    int insert(Seckillgoods seckillgoods);
    int update(Integer id);
    int delete(Integer id);

}
