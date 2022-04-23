package com.cardgameserver.service.impl;


import com.cardgameserver.dao.AccountDao;
import com.cardgameserver.entity.Account;
import com.cardgameserver.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@SuppressWarnings("all")
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountDao accountDao;

    @Override
    public int insert(Long id) {
        int insert = accountDao.insert(id);
        return insert;
    }

    @Override
    public int update(Account account) {
        int update = accountDao.update(account);
        return update;
    }

    @Override
    public Account findById(Long id) {
        Account account = accountDao.selectById(id);
        return account;
    }
}
