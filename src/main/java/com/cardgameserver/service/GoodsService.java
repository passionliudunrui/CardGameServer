package com.cardgameserver.service;

import com.cardgameserver.entity.Goods;

public interface GoodsService {
    int insert(Goods goods);
    int update(Goods goods);

}
