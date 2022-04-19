package com.cardgameserver.dao;

import com.cardgameserver.entity.User;

import java.util.List;

public interface UserDao {
    //查询全部用户
    List<User> findAll();

    //根据账号密码验证登录
    User check(Long id,String password);

    //根据id找到User
    User findById(Long id);

    //根据账号和密码注册 (判断账号是否存在)
    int insert(User user);

    //修改密码
    int update(User user);

}
