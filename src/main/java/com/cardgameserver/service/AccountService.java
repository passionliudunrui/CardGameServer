package com.cardgameserver.service;

import com.cardgameserver.entity.Account;

public interface AccountService {
    int insert(Long id);
    int update(Account account);
    Account findById(Long id);
}
