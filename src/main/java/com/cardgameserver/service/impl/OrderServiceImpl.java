package com.cardgameserver.service.impl;

import com.cardgameserver.dao.OrderDao;
import com.cardgameserver.entity.Order;
import com.cardgameserver.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public int insert(Order order) {
        return orderDao.insert(order);
    }
}
