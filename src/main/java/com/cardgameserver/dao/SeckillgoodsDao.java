package com.cardgameserver.dao;

import com.cardgameserver.entity.Seckillgoods;

public interface SeckillgoodsDao {
    int insert(Seckillgoods seckillgoods);
    int update(Integer id);
    int delete(Integer id);

    Seckillgoods select(Integer id);

}
