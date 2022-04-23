package com.cardgameserver.dao;

import com.cardgameserver.entity.Account;

public interface AccountDao {
    int insert(Long id);
    int update(Account account);
    Account selectById(Long id);

}
