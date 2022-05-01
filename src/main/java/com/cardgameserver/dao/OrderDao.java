package com.cardgameserver.dao;

import com.cardgameserver.entity.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderDao {
    int insert(Order order);

    Order select(@Param("userId") Long userId,@Param("goodsId") Integer goodsId);
}
