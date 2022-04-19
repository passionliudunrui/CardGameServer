package com.cardgameserver.dao;

import com.cardgameserver.entity.Account;

public interface AccountDao {
    int insert(Integer id);
    int update(Account account);

}
