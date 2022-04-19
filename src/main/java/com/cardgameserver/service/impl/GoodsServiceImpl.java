package com.cardgameserver.service.impl;


import com.cardgameserver.dao.GoodsDao;
import com.cardgameserver.entity.Goods;
import com.cardgameserver.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@SuppressWarnings("all")
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    @Override
    public int insert(Goods goods) {
        return goodsDao.insert(goods);
    }

    @Override
    public int update(Goods goods) {
        return goodsDao.update(goods);
    }
}
