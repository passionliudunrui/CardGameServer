package com.cardgameserver.dao;

import com.cardgameserver.entity.Account;

import java.util.List;

public interface AccountDao {
    int insert(Long id);
    int update(Account account);
    Account selectById(Long id);
    List<Account> selectAllAccount();

    int transfer(Long id1, Long id2, Double happybean);
}
