package com.cardgameserver.service;

import com.cardgameserver.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User check(Long id,String password);
    int insert(User user);
    int update(User user);
}
