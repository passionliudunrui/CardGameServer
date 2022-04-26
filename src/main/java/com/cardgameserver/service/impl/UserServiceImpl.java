package com.cardgameserver.service.impl;

import com.cardgameserver.dao.UserDao;
import com.cardgameserver.entity.User;
import com.cardgameserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@SuppressWarnings("all")
public class UserServiceImpl implements UserService {
    private static final String USER_INFO="userInfo:";

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;



    @Override
    public List<User> findAll() {
        System.out.println("aaaaaaaaaaaaaaaaaa ");
        return userDao.findAll();
    }

    /**
     * 使用redis来优化查询
     * 1.先从redis中查找
     * 2.如果没有则去mysql查找
     * 3.mysql查找完成之后更新到redis中
     * @param id
     * @return  user不是空就存在用户  是空不存在用户
     */
    @Override
    public User findById(Long id) {

        ValueOperations valueOperations=redisTemplate.opsForValue();
        User user1 = (User) valueOperations.get(USER_INFO+id);
        if(user1!=null){
            return user1;
        }
        User user=userDao.findById(id);
        System.out.println("查询数据库");
        if(user!=null){
            valueOperations.set(USER_INFO+id,user);
        }

        return user;
    }

    /**
     * 使用redis来优化查询
     * 1.先从redis中查找并且验证
     * 2.如果没有则去mysql查找并且验证
     * 3.mysql查找完成之后更新到redis中  不管有没有都存到redis中 解决缓存击穿问题
     * @param id
     * @return   user不是空就验证成功 是空验证失败
     */
    @Override
    public User check(Long id, String password) {
        ValueOperations valueOperations=redisTemplate.opsForValue();
        User user1 = (User) valueOperations.get(USER_INFO+id);

        if(user1!=null){

            if(password.equals(user1.getPassword())){
                return user1;
            }
            else{
                return null;
            }
        }


        User user=userDao.findById(id);

        //不管有没有全部加入到redis缓存中
        valueOperations.set(USER_INFO+id,user);

        if(user!=null){

            if(password.equals(user.getPassword())){
                return user;
            }
            else{
                return null;
            }
        }

        else{
            return null;
        }

    }

    @Override
    public int insert(User user) {
        User user1=userDao.findById(user.getId());

        if(user1!=null){
            return 0;
        }
        return userDao.insert(user);
    }

    /**
     * 使用延时双删的策略来解决redis和mysql中数据不一致的问题
     * 以及并发数据不一致的问题
     * @param user
     * @return   !=1更新失败   ==1 更新成功
     */
    @Override
    public int update(User user) {
        //1.删除redis中的数据
        ValueOperations valueOperations=redisTemplate.opsForValue();
        redisTemplate.delete(USER_INFO+ user.getId());

        //2.更新mysql中的数据
        int resultNumber=userDao.update(user);
        if(resultNumber!=1){
            return 0;
        }

        //延时
        try{
            Thread.sleep(50);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        //4.第二次删除redis中的数据
        redisTemplate.delete(USER_INFO+ user.getId());

        return resultNumber;
    }
}
