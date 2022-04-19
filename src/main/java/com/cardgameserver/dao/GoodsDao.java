package com.cardgameserver.dao;

import com.cardgameserver.entity.Goods;

public interface GoodsDao {
    int insert(Goods goods);
    int update(Goods goods);


}
