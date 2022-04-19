package com.cardgameserver.service;

import com.cardgameserver.entity.Account;

public interface AccountService {
    int insert(Integer id);
    int update(Account account);
}
